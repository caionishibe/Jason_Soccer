package robos;

import br.ufrgs.f180.api.Player;
import br.ufrgs.f180.api.model.RobotInformation;
import br.ufrgs.f180.math.Point;

/**
 * Classe estática que implementa os métodos auxiliares necessário para movimentação básica
 * @author Caio Nishibe
 * @author Renato Gasoto
 */
public class MetodosAux {

    /**
     * Intervalo de tempo
     */
    public static final double dt = 0.1;
    /*Caio*/
    /**
     * Ganho proporcional
     */
    public static final double kP = 4.5;
    /**
     * Ganho integral
     */
    public static final double kI = 0; //1.75;//1.75;
    /**
     * Ganho derivativo
     */
    public static final double kD = 0; //1.4;//1.4;
    /**
     * Erro mínimo
     */
    public static final double ERRO_MIN = 0.05;
    /**
     * Objeto que implementa os controladores fuzzy
     */
    public static Fuzzy f = new Fuzzy();
    /**
     * Booleano que indica se o alinhamento em Y está ok
     */
    public static boolean yOk = false;
    /**
     * Objeto que implementa as redes neurais
     */
    public static RedeNeural nn = new RedeNeural();


    /* Renato */
    /*
    static final double kP = 2.0;
    static final double kI = 0.75;//1.75;
    static final double kD = 0.4;//1.4;
    static final double ERRO_MIN = 0.05;
     */
    /**
     * Método que faz com que o robo vá para uma posição desejada
     * @param cliente Cliente do simulador
     * @param j jogador
     * @throws Exception
     */
    public static void irLinhaReta(Player cliente, Jogador j) throws Exception {

        RobotInformation p1 = cliente.getPlayerInformation(j.getNome());
        //posicao do jogador
        Point posJog = p1.getPosition();
        //posição desejada
        Point p = j.getPosicaoDesejada();

        System.out.println("Posição: " + posJog.getY() + " - " + p.getY());

        //se na estiver no ponto desejado
        if (posJog != p) {

            //controle PID

            //diferença entre a posicao desejada e a atual
            Double rX = p.getX() - posJog.getX();
            Double rY = p.getY() - posJog.getY();

            //zera os erros mínimos
            if (Math.abs(rX) < ERRO_MIN) {
                rX = 0.0;
            }
            if (Math.abs(rY) < ERRO_MIN) {
                rY = 0.0;
            }

            //Integral
            double iX = dt * kI * ((rX + j.getXErro()) / 2 + j.getiXAnt());
            double iY = dt * kI * ((rY + j.getYErro()) / 2 + j.getiYAnt());

            //Proporcional
            double pX = rX * kP;
            double pY = rY * kP;

            //Derivativo
            double dX = kD * (rX - j.getXErro()) / dt;
            double dY = kD * (rY - j.getYErro()) / dt;

            //velocidade em x
            double vX = pX + iX + dX;
            //velocidade em y
            double vY = pY + iY + dY;

            //redefine os atributos do jogador
            j.setvXAnterior(vX);
            j.setvYAnterior(vY);

            j.setiYAnt((rY + j.getYErro()) / 2 + j.getiYAnt());
            j.setiXAnt((rX + j.getXErro()) / 2 + j.getiXAnt());

            j.setXErro(rX);
            j.setYErro(rY);

            //define os vetores velocidade do jogador
            cliente.setPlayerVelocity(j.getNome(), vX, vY);
            System.out.println("Vx: " + vX);
            System.out.println("Vy: " + vY);
        }
    }

    /**
     * Método que faz o robo rotacionar para determinado angulo
     * @param anguloDesejado angulo desejado
     * @param cliente cliente do simulador
     * @param id identificador do jogador
     * @throws Exception
     */
    public static void rotacionar(double anguloDesejado, Player cliente, String id) throws Exception {

        RobotInformation p1 = cliente.getPlayerInformation(id);

        double atual = p1.getAngle() / Math.PI * 180;
        double totalAvirarHorario = anguloDesejado - atual;
        double totalAvirarAntiHorario = 360 - totalAvirarHorario;
        double totalAvirar = Math.abs(totalAvirarAntiHorario) < Math.abs(totalAvirarHorario) ? totalAvirarAntiHorario : totalAvirarHorario;

        if (Math.abs(totalAvirar) >= 0.05) {
            cliente.setPlayerRotationVelocity(id, totalAvirar * 0.12);
            atual = p1.getAngle();
        } else {
            cliente.setPlayerRotationVelocity(id, 0.0);
        }
    }

    /**
     * Método que faz o robô rotacionar para determinado ponto
     * @param p
     * @param cliente
     * @param id
     * @return
     * @throws Exception
     */
    public static double rotacionarParaPonto(Point p, Player cliente, String id) throws Exception {

        RobotInformation p1 = cliente.getPlayerInformation(id);
        Point posJog = p1.getPosition();

        Double rX = p.getX() - posJog.getX();
        Double rY = p.getY() - posJog.getY();

        double ang = (Math.atan2(rY, rX) / Math.PI * 180);
        double angB = p1.getAngle() / Math.PI * 180;
        System.out.println(ang + " - " + angB);
        rotacionar(ang, cliente, id);

        return ang;

    }

    /**
     * Método que faz o robô contornar a bola e se preparar para o chute
     * @param cliente
     * @param jogador
     * @throws Exception
     */
    public static void contornaBola(Player cliente, Jogador jogador) throws Exception {

        nn.setSavedNeural(RedeNeural.CHUTE);

        Point posJog = cliente.getPlayerInformation(jogador.getNome()).getPosition();
        Point posBola = cliente.getBallInformation().getPosition();
        Point posGole = cliente.getPlayerInformation("Goleiro").getPosition();
        // Point posGole = cliente.getPlayerInformation("RenanRibeiro").getPosition();


        //se a bola está acima da linha do gol
        if (posBola.getY() >= 230.0) {

            //move-se na direção y enquanto nao estiver próximo da bola por cima
            if (posJog.getY() > posBola.getY() + 30 && posJog.getY() < posBola.getY() + 45) {
                yOk = true;
            } else {
                jogador.setPosicaoDesejada(new Point(posJog.getX(), posBola.getY() + 40));
                irLinhaReta(cliente, jogador);

            }

            // se a bola está abaixo da linha da trave superior
        } else if (posBola.getY() < 230) {

            //move-se na direção y enquanto não estiver proximo da bola por baixo
            if (posJog.getY() < posBola.getY() - 30 && posJog.getY() > posBola.getY() - 45) {
                yOk = true;
            } else {
                jogador.setPosicaoDesejada(new Point(posJog.getX(), posBola.getY() - 40));
                irLinhaReta(cliente, jogador);
            }
        }

        //se está alinhado na componente Y
        if (yOk) {

            //calculaChute o y de chute
            double yChute = f.getPosChute(posGole.getY(), posJog.getY());

            //se a bola estiver alinha na componente X
            if (posJog.getX() >= posBola.getX() && posJog.getX() <= posBola.getX() + 40) {

                //se a bola está acima da trave do gol
                if (posBola.getY() > 230.0) {

                    //mova-se para perto da bola
                    jogador.setPosicaoDesejada(new Point(posBola.getX() + 10, posBola.getY() + 5));
                    irLinhaReta(cliente, jogador);

                    //se a bola estiver antes do meio de campo
                    if (nn.calculaChute(posJog.getX(), posJog.getY()) > 0.5) {
                        //chute
                        rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                        cliente.setPlayerKick(jogador.getNome(), 50.0);

                        //caso contrário
                    } else {
                        //rotaciona e continua levando a bola
                        rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                        jogador.setPosicaoDesejada(new Point(posBola.getX() + 2, posBola.getY()));
                        irLinhaReta(cliente, jogador);

                    }
                } else if (posBola.getY() < 160.0) {


                    jogador.setPosicaoDesejada(new Point(posBola.getX() + 10, posBola.getY() - 5));
                    irLinhaReta(cliente, jogador);
                    rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());


                    if (nn.calculaChute(posJog.getX(), posJog.getY()) > 0.5) {
                        rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                        cliente.setPlayerKick(jogador.getNome(), 50.0);

                    } else {
                        jogador.setPosicaoDesejada(new Point(posBola.getX() + 2, posBola.getY()));
                        irLinhaReta(cliente, jogador);
                        rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                    }
                } else {

                    if (posBola.getY() > 197.0) {
                        jogador.setPosicaoDesejada(new Point(posBola.getX() + 10, posBola.getY() + 3));
                        irLinhaReta(cliente, jogador);
                        rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());



                        if (nn.calculaChute(posJog.getX(), posJog.getY()) > 0.5) {
                            rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                            cliente.setPlayerKick(jogador.getNome(), 50.0);

                        } else {
                            jogador.setPosicaoDesejada(new Point(posBola.getX() + 2, posBola.getY()));
                            irLinhaReta(cliente, jogador);
                            rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                        }
                    } else if (posBola.getY() < 197.0) {

                        jogador.setPosicaoDesejada(new Point(posBola.getX() + 5, posBola.getY() - 3));
                        irLinhaReta(cliente, jogador);
                        rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());



                        if (nn.calculaChute(posJog.getX(), posJog.getY()) > 0.5) {
                            rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                            cliente.setPlayerKick(jogador.getNome(), 50.0);

                        } else {
                            jogador.setPosicaoDesejada(new Point(posBola.getX() + 2, posBola.getY()));
                            irLinhaReta(cliente, jogador);
                            rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                        }
                    } else {
                        jogador.setPosicaoDesejada(new Point(posBola.getX() + 5, posBola.getY()));
                        irLinhaReta(cliente, jogador);
                        rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());



                        if (nn.calculaChute(posJog.getX(), posJog.getY()) > 0.5) {
                            rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                            cliente.setPlayerKick(jogador.getNome(), 50.0);

                        } else {
                            jogador.setPosicaoDesejada(new Point(posBola.getX() + 2, posBola.getY()));
                            irLinhaReta(cliente, jogador);
                            rotacionarParaPonto(new Point(posGole.getX(), yChute), cliente, jogador.getNome());
                        }
                    }

                }


            } else {

                //posiciona robo nas proximidades x da bola
                jogador.setPosicaoDesejada(new Point(posBola.getX() + 30, posJog.getY()));
                irLinhaReta(cliente, jogador);
                yOk = false;


            }



        }
    }
}
