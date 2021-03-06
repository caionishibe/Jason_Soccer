
import br.ufrgs.f180.api.Player;
import br.ufrgs.f180.api.model.RobotInformation;
import br.ufrgs.f180.math.Point;

/**
 * Classe estática que implementa os métodos auxiliares necessário para movimentação básica
 * @author Caio Nishibe
 * @author Renato Gasoto
 */
public class MetodosAuxiliares {

    /*constantes dos controladores*/
    private static final double KP_ROTACIONAR = 0.12;
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
            cliente.setPlayerRotationVelocity(id, totalAvirar * KP_ROTACIONAR);
            atual = p1.getAngle();
        } else {
            cliente.setPlayerRotationVelocity(id, 0.0);
        }
    }

    /**
     * Método que faz o robo rotacionar Lentamente para determinado angulo
     * @param anguloDesejado angulo desejado
     * @param cliente cliente do simulador
     * @param id identificador do jogador
     * @throws Exception
     */
    public static double rotacionarLentamente(double anguloDesejado, Player cliente, String id) throws Exception {

        RobotInformation p1 = cliente.getPlayerInformation(id);

        double atual = p1.getAngle() / Math.PI * 180;
        double totalAvirarHorario = anguloDesejado - atual;
        double totalAvirarAntiHorario = 360 - totalAvirarHorario;
        double totalAvirar = Math.abs(totalAvirarAntiHorario) < Math.abs(totalAvirarHorario) ? totalAvirarAntiHorario : totalAvirarHorario;

        if (Math.abs(totalAvirar) >= 0.05) {
            cliente.setPlayerRotationVelocity(id, totalAvirar * (KP_ROTACIONAR/5));
            atual = p1.getAngle();
        } else {
            cliente.setPlayerRotationVelocity(id, 0.0);
        }

        return Math.abs(totalAvirar);
    }


        /**
     * Método que faz o robô rotacionar para determinado ponto
     * @param p
     * @param cliente
     * @param id
     * @return
     * @throws Exception
     */
    public static double rotacionarLentamenteParaPonto(Point p, Player cliente, String id) throws Exception {

        RobotInformation p1 = cliente.getPlayerInformation(id);
        Point posJog = p1.getPosition();

        Double rX = p.getX() - posJog.getX();
        Double rY = p.getY() - posJog.getY();

        double ang = (Math.atan2(rY, rX) / Math.PI * 180);
        double angB = p1.getAngle() / Math.PI * 180;
        //System.out.println(ang + " - " + angB);
        rotacionarLentamente(ang, cliente, id);

        return ang;

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
       // System.out.println(ang + " - " + angB);
        rotacionar(ang, cliente, id);

        return ang;

    }

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

        //System.out.println("Posição: " + posJog.getY() + " - " + p.getY());

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
            //System.out.println("Vx: " + vX);
            //System.out.println("Vy: " + vY);


        }
    }
}
