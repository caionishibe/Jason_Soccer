
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
    /*Percepcoes*/
    private static final Literal NAO_DOMINADA = Literal.parseLiteral("naoDominada(bola)");
    private static final Literal COM_BOLA = Literal.parseLiteral("com(bola)");
    /* Ações */
    private static final String CREATE_PLAYER = "createPlayer";
    private static final String ROTACIONE_PARA_BOLA = "rotacioneParaBola";
    private static final String IR_LINHA_RETA = "irLinhaReta";
    private static final String DEFENDER_GOL = "defender";
    private static final Literal CHUTAR = Literal.parseLiteral("chutar");
    /*Jogadores*/
    private static final Jogador goleiro = new Jogador(0, 0);
    private static final Jogador atacanteMeio = new Jogador(0, 0);
    private static final Jogador atacanteDireita = new Jogador(0, 0);
    private static final Jogador atacanteEsquerda = new Jogador(0, 0);

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

        if (!(action.getFunctor().equals(CREATE_PLAYER))) {
            this.updateAgPercept(agName);
        }

        try {


            logger.info(agName + " doing: " + action);

            if (action.getFunctor().equals(CREATE_PLAYER)) {
                this.createPlayer(agName, action);
                //atualiza as percepcoes dos agentes criados
                this.updateAgPercept(agName);

            }
            if (action.getFunctor().equals(ROTACIONE_PARA_BOLA)) {
                Point posBola = FieldModel.toTewntaPosition(Integer.parseInt(action.getTerm(0).toString()),
                        Integer.parseInt(action.getTerm(1).toString()));
                MetodosAuxiliares.rotacionarParaPonto(posBola, clientProxy, agName);
            }
            if (action.getFunctor().equals(IR_LINHA_RETA)) {
                Point posBola = FieldModel.toTewntaPosition(Integer.parseInt(action.getTerm(0).toString()),
                        Integer.parseInt(action.getTerm(1).toString()));

                this.getJogadorByName(agName).setPosicaoDesejada(posBola);
                MetodosAuxiliares.irLinhaReta(clientProxy, this.getJogadorByName(agName));

            }
            if (action.getFunctor().equals(DEFENDER_GOL)) {

                this.defender(agName, action);
            }
            if(action.equals(CHUTAR))
            {
                clientProxy.setPlayerKick(agName, Double.MAX_VALUE);
            }



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
     * Atualiza as percepções do agente passado como parâmetro
     * @param agName nome do agente a atualizar as percepções
     */
    private void updateAgPercept(String agName) {
        this.clearPercepts(agName);

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
        modelo.setAgPosByName(agName, agentGridPosition[0], agentGridPosition[1]);

        //se agente com bola
        if (agentGridPosition[0] == ballGridPosition[0] && agentGridPosition[1] == ballGridPosition[1]) {

            addPercept(agName, COM_BOLA);
        }

        //se bola nao dominada
        if (modelo.isFree(ballGridPosition[0], ballGridPosition[1])) {
            addPercept(NAO_DOMINADA);
        }





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

        //cria um objeto jogador
        this.getJogadorByName(agName).setNome(agName);
        this.getJogadorByName(agName).setPosicaoDesejada(position);
    }

    private void defender(String agName, Structure action) throws Exception {

        Point posBola = FieldModel.toTewntaPosition(Integer.parseInt(action.getTerm(0).toString()),
                Integer.parseInt(action.getTerm(1).toString()));
        Point posGole = FieldModel.toTewntaPosition(Integer.parseInt(action.getTerm(2).toString()),
                Integer.parseInt(action.getTerm(3).toString()));

        double xIniGoleiro = FieldModel.toTewntaPosition(24, 0).getX();

        //se a bola estiver acima da posicao do goleiro
        if (posBola.getY() > posGole.getY()) {
            //se a bola estiver acima da trave do gol
            if (posBola.getY() > 230) {
                //goleiro fica na trave
                this.getJogadorByName(agName).setPosicaoDesejada(new Point(xIniGoleiro, 229.0));
                MetodosAuxiliares.irLinhaReta(clientProxy, this.getJogadorByName(agName));
                //se a bola estiver dentro dos limites do gol
            } else {
                //goleiro se posiciona conforme bola
                this.getJogadorByName(agName).setPosicaoDesejada(new Point(xIniGoleiro, posBola.getY()));
                MetodosAuxiliares.irLinhaReta(clientProxy, this.getJogadorByName(agName));
            }

            //se a bola estiver abaixo da posicao do goleiro
        } else if (posBola.getY() <= posGole.getY()) {
            //se a bola estiver abaixo da trave do gol
            if (posBola.getY() < 170) {
                //goleiro fica na trave
                this.getJogadorByName(agName).setPosicaoDesejada(new Point(xIniGoleiro, 171.0));
                MetodosAuxiliares.irLinhaReta(clientProxy, this.getJogadorByName(agName));
                //se a bola estiver dentro dos limites do gol
            } else {
                //goleiro se posiciona conforme bola
                this.getJogadorByName(agName).setPosicaoDesejada(new Point(xIniGoleiro, posBola.getY()));
                MetodosAuxiliares.irLinhaReta(clientProxy, this.getJogadorByName(agName));
            }
        }

    }

    private Jogador getJogadorByName(String name) {
        if (name.equals("goleiro")) {
            return SoccerEnv.goleiro;
        } else if (name.equals("atacanteMeio")) {
            return SoccerEnv.atacanteMeio;
        } else if (name.equals("atacanteDireita")) {
            return SoccerEnv.atacanteDireita;
        } else if (name.equals("atacanteEsquerda")) {
            return SoccerEnv.atacanteEsquerda;
        } else {
            return null;
        }

    }
}
