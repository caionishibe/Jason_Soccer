
import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;
import br.ufrgs.f180.api.Player;
import br.ufrgs.f180.api.model.RobotInformation;
import br.ufrgs.f180.math.Point;
import java.net.URL;
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
    private static final Term termGire = Literal.parseLiteral("gire");
    private static final Term termRotacioneBola = Literal.parseLiteral("rotacioneParaBola");

    /*constantes dos controladores*/
    private static final double KP_ROTACIONAR = 0.05;

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

        addPercept(Literal.parseLiteral("percept(demo)"));

        //pega informacao da posicao da bola
        Point posBola = clientProxy.getBallInformation().getPosition();
        int newPosBola[] = FieldModel.toJasonPosition(posBola);
        //instancia o modelo do campo
        modelo = new FieldModel(newPosBola[0], newPosBola[1], NUMBER_OF_AGENTS);

    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        try {
            logger.info(agName + " doing: " + action);

            if (action.getFunctor().equals("createPlayer")) {
                this.createPlayer(agName, action);
            }

            if (action.equals(termGire)) {
                clientProxy.setPlayerRotation(agName, new Double(10));
            }

            if(action.equals(this.termRotacioneBola))
            {
                Point posBola = clientProxy.getBallInformation().getPosition();
                this.rotacionarParaPonto(posBola, clientProxy, agName);
            }

            //atualiza a posicao da bola
            Point posBola = clientProxy.getBallInformation().getPosition();
            int newPosBola[] = FieldModel.toJasonPosition(posBola);
            modelo.updateBallLocation(newPosBola[0], newPosBola[1]);
            logger.info("Ball position: ( " + newPosBola[0] + "," + newPosBola[1] + ")");

            Thread.sleep(2000);

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
        if (action.getTerm(2).toString().equals("TEAM_A")) {
            clientProxy.setPlayer(teamAIdentifier, agName, position.getX(), position.getY());

            //se o jogador for do time B
        } else {
            clientProxy.setPlayer(teamBIdentifier, agName, position.getX(), position.getY());
        }

        clientProxy.setPlayerDribble(agName, Boolean.TRUE);
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


}
