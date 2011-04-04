
import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;
import br.ufrgs.f180.api.Player;
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
    private static final String TEAM_B_NAME = "Professores";
    private static Player clientProxy = null;
    private static String teamAIdentifier;
    private static String teamBIdentifier;
    private static FieldModel modelo = null;
    /* Ações */
    private static final Literal termGire = Literal.parseLiteral("gire");

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
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            this.stop();
        }

        addPercept(Literal.parseLiteral("percept(demo)"));

        double posXBola = clientProxy.getBallInformation().getPosition().getX();
        double posYBola = clientProxy.getBallInformation().getPosition().getY();
        posXBola = (posXBola / FieldModel.CELL_WIDTH) - 1;
        posYBola = (posYBola / FieldModel.CELL_HEIGHT) - 1;

        modelo = new FieldModel((int) posXBola, (int) posYBola, NUMBER_OF_AGENTS);

    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        try {
            logger.info(agName + " doing: " + action);

            if (action.getFunctor().equals("createPlayer")) {

                Point position = modelo.toTewntaPosition(Integer.parseInt(action.getTerm(0).toString()), Integer.parseInt(action.getTerm(1).toString()));
                logger.info("PosRobo " + position.getX() + " " + position.getY());
                clientProxy.setPlayer(teamAIdentifier, agName, position.getX(), position.getY());
            }

            if (action.equals(termGire)) {
                clientProxy.setPlayerRotation(agName, new Double(10));
            }

            Point posBola = clientProxy.getBallInformation().getPosition();
            int newPosBola[] = modelo.toJasonPosition(posBola);

            modelo.updateBallLocation(newPosBola[0], newPosBola[1]);
            logger.info("Ball position: ( " + newPosBola[0] + "," +newPosBola[1] + ")");
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
}
