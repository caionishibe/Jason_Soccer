
import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;
import br.ufrgs.f180.api.Player;
import br.ufrgs.f180.api.model.RobotInformation;
import br.ufrgs.f180.math.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * Classe que modela o ambiente, além de ser a camada de interpretação entre Jason e Tewnta
 * @author Caio Nishibe
 * @author Luís Mendes
 *
 */
public class SoccerEnv extends Environment {

    private Logger logger = Logger.getLogger("tewntajason.mas2j." + SoccerEnv.class.getName());
    /**
     * Número de agentes no ambiente
     */
    private static final int NUMBER_OF_AGENTS = 4;
    /**
     * Nome do time A
     */
    private static final String TEAM_A_NAME = "Computeiros";
    /**
     * Nome do time B
     */
    private static final String TEAM_B_NAME = "Professores";
    /**
     * Proxy que se comunica com o webservice
     */
    private static Player clientProxy = null;
    /**
     * Identificador do time A
     */
    private static String teamAIdentifier;
    /**
     * Identificador do time B
     */
    private static String teamBIdentifier;
    /**
     * Modelo do campo
     */
    private static FieldModel modelo = null;
    /* Ações */
    private static final Term TERM_GIRE = Literal.parseLiteral("gire");
    private static final String FUNCTOR_CREATE_PLAYER = "createPlayer";
    private static final String FUNCTOR_ROTACIONE_PARA_BOLA = "rotacioneParaBola";
    private static final String FUNCTOR_IR_LINHA_RETA = "irLinhaReta";
    /*constantes dos controladores*/
    /**
     * KP - CAIO
     */
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
    /*Vetor contendo os jogadores*/
    private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);

        try {
            // Conecta no simulador Tewnta
            URL wsdlURL = new URL("http://localhost:9000/player?wsdl");
            QName SERVICE_NAME = new QName("http://api.f180.ufrgs.br/", "Player");
            Service service = Service.create(wsdlURL, SERVICE_NAME);
            clientProxy = service.getPort(Player.class);
            teamAIdentifier = clientProxy.login(TEAM_A_NAME);
            teamBIdentifier = clientProxy.login(TEAM_B_NAME);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            this.stop();
        }

        //instancia o modelo do campo
        modelo = new FieldModel(NUMBER_OF_AGENTS);



    }

    @Override
    public boolean executeAction(String agName, Structure action) {

        try {

            if (!(action.getFunctor().equals(FUNCTOR_CREATE_PLAYER))) {
                this.updateAgPercept(agName);
            }

            logger.info(agName + " doing: " + action);

            if (action.getFunctor().equals(FUNCTOR_CREATE_PLAYER)) {
                this.createPlayer(agName, action);
                //atualiza as percepcoes dos agentes criados
                this.updateAgPercept(agName);

            }
            if (action.getFunctor().equals(FUNCTOR_ROTACIONE_PARA_BOLA)) {

                Point posBola = FieldModel.toTewntaPosition(Integer.parseInt(action.getTerm(0).toString()),
                        Integer.parseInt(action.getTerm(1).toString()));
                this.rotacionarParaPonto(posBola, clientProxy, agName);
            }
            if (action.getFunctor().equals(FUNCTOR_IR_LINHA_RETA)) {
                Point posBola = FieldModel.toTewntaPosition(Integer.parseInt(action.getTerm(0).toString()),
                        Integer.parseInt(action.getTerm(1).toString()));

                Jogador jogadorAtual = null;

                synchronized (jogadores) {
                    Iterator<Jogador> jogadoresIterator = jogadores.iterator();

                    while (jogadoresIterator.hasNext()) {
                        Jogador j = jogadoresIterator.next();
                        if (j.getNome().equals(agName)) {
                            jogadorAtual = j;
                        }
                    }
                }


                jogadorAtual.setPosicaoDesejada(posBola);
                SoccerEnv.irLinhaReta(clientProxy, jogadorAtual);

            }

            if (action.equals(TERM_GIRE)) {
                clientProxy.setPlayerRotation(agName, new Double(10));
            }

            //logger.info("Ball position: ( " + newPosBola[0] + "," + newPosBola[1] + ")");

            Thread.sleep(100);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return true;
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }

    /**
     * Método privado que modela a criacao de um jogador
     * @param agName Nome do agente
     * @param action acao
     * @throws Exception
     */
    private void createPlayer(String agName, Structure action) throws Exception {
        //converte a posicao do grid para posicao do tewnta
        Point position = FieldModel.toTewntaPosition(Integer.parseInt(action.getTerm(0).toString()), Integer.parseInt(action.getTerm(1).toString()));
        //se o jogador for do time A
        if (action.getTerm(2).toString().equals("team_a")) {
            clientProxy.setPlayer(teamAIdentifier, agName, position.getX(), position.getY());

            //se o jogador for do time B
        } else {
            clientProxy.setPlayer(teamBIdentifier, agName, position.getX(), position.getY());
        }

        clientProxy.setPlayerDribble(agName, Boolean.TRUE);

        //cria um objeto jogador com os atributos de goleiro
        Jogador novoJogador = new Jogador(position.getX(), position.getY());
        novoJogador.setNome(agName);
        novoJogador.setPosicaoDesejada(position);

        synchronized (jogadores) {
            jogadores.add(novoJogador);
        }


    }

    /**
     * Método que faz o robo rotacionar para determinado angulo
     * @param anguloDesejado angulo desejado
     * @param cliente cliente do simulador
     * @param id identificador do jogador
     * @throws Exception
     */
    private void rotacionar(double anguloDesejado, Player cliente, String id) throws Exception {

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
     * Método que faz o robô rotacionar para determinado ponto
     * @param p
     * @param cliente
     * @param id
     * @return
     * @throws Exception
     */
    private double rotacionarParaPonto(Point p, Player cliente, String id) throws Exception {

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
     * Atualiza as percepções do agente passado como parâmetro
     * @param agName nome do agente a atualizar as percepções
     */
    private void updateAgPercept(String agName) {
        clearPercepts(agName);

        //atualiza posicao da bola
        Point ballPosition = clientProxy.getBallInformation().getPosition();
        int ballGridPosition[] = FieldModel.toJasonPosition(ballPosition);

        Literal p = ASSyntax.createLiteral("posBola",
                ASSyntax.createNumber(ballGridPosition[0]),
                ASSyntax.createNumber(ballGridPosition[1]));
        addPercept(agName, p);

        //atualiza a posicao do agente
        Point agentPosition = clientProxy.getPlayerInformation(agName).getPosition();
        int agentGridPosition[] = FieldModel.toJasonPosition(agentPosition);
        Literal pa = ASSyntax.createLiteral("posicao",
                ASSyntax.createNumber(agentGridPosition[0]),
                ASSyntax.createNumber(agentGridPosition[1]));
        addPercept(agName, pa);
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




            } //Integral
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
}
