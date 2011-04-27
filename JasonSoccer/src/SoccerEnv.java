
import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;
import br.ufrgs.f180.api.Player;
import br.ufrgs.f180.api.model.RobotInformation;
import br.ufrgs.f180.math.Point;
import java.net.URL;
import java.util.Arrays;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * Classe que modela o ambiente, além de ser a camada de interpretação entre Jason e Tewnta
 * @author Caio Nishibe
 * @author Luís Mendes
 *
 */
public class SoccerEnv extends Environment {

    public static final Logger logger = Logger.getLogger("tewntajason.mas2j." + SoccerEnv.class.getName());
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

    /*Percepcoes*/
    private static final Literal NAO_DOMINADA = Literal.parseLiteral("naoDominada(bola)");
    private static final String COM_BOLA = "comBola";
    private static final String MAIS_PERTO_BOLA = "maisPertoBola";
    private static final Literal PERTO_GOL = Literal.parseLiteral("perto(gol)");
    private static final Literal PRONTO_CHUTAR = Literal.parseLiteral("pronto(chutar)");

    /* Ações */
    private static final String CREATE_PLAYER = "createPlayer";
    private static final String ROTACIONE_PARA = "rotacionePara";
    private static final String IR_LINHA_RETA = "irLinhaReta";
    private static final String DEFENDER_GOL = "defender";
    private static final Literal CHUTAR = Literal.parseLiteral("chutar");
    private static final Literal POSICAO_CHUTE = Literal.parseLiteral("posicaoChute");
    private static final String POSICIONA_ATAQUE = "posicionaAtaque";
    private static final String OLHAR_COMPANHEIRO = "olharCompanheiro";
    private static final Literal PASSAR = Literal.parseLiteral("passar");
    /*Jogadores*/
    private static final Jogador goleiroTeamA = new Jogador(0, 0);
    private static final Jogador atacanteMeio = new Jogador(0, 0);
    private static final Jogador atacanteDireita = new Jogador(0, 0);
    private static final Jogador atacanteEsquerda = new Jogador(0, 0);
    private static final Jogador goleiroTeamB = new Jogador(0, 0);
    /*Outras constantes*/
    public static final int posXGoleiroTeamA = 40;
    public static final int posXGoleiroTeamB = 505;

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


    }

    @Override
    public boolean executeAction(String agName, Structure action) {


        try {

            logger.info(agName + " doing: " + action);

            if (action.getFunctor().equals(CREATE_PLAYER)) {
                this.createPlayer(agName, action);
            } else if (action.getFunctor().equals(ROTACIONE_PARA)) {
                Point posBola = new Point(Double.parseDouble(action.getTerm(0).toString()),
                        Double.parseDouble(action.getTerm(1).toString()));
                MetodosAuxiliares.rotacionarParaPonto(posBola, clientProxy, agName);
            } else if (action.getFunctor().equals(IR_LINHA_RETA)) {
                Point posBola = new Point(Double.parseDouble(action.getTerm(0).toString()),
                        Double.parseDouble(action.getTerm(1).toString()));

                this.getJogadorByName(agName).setPosicaoDesejada(posBola);
                MetodosAuxiliares.irLinhaReta(clientProxy, this.getJogadorByName(agName));
                MetodosAuxiliares.rotacionarParaPonto(posBola, clientProxy, agName);

            } else if (action.getFunctor().equals(DEFENDER_GOL)) {
                this.defender(agName, action);
            } else if (action.equals(CHUTAR)) {

                clientProxy.setPlayerKick(agName, Double.MAX_VALUE);

            } else if (action.equals(POSICAO_CHUTE)) {
                this.melhorPosChute(agName, action);
            } else if (action.getFunctor().equals(POSICIONA_ATAQUE)) {
                this.posicionaAtaque(agName, action, (action.getTerm(0).toString()), Double.parseDouble(action.getTerm(1).toString()));
            } else if (action.getFunctor().equals(OLHAR_COMPANHEIRO)) {
                double x = Double.parseDouble(action.getTerm(0).toString());
                double y = Double.parseDouble(action.getTerm(1).toString());

                MetodosAuxiliares.rotacionarParaPonto(new Point(x, y), clientProxy, agName);
            } else if (action.equals(PASSAR)) {
                this.passar(agName, 0.2);
            }


            this.updateAgPercept(agName);

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
    protected void updateAgPercept(String agName) throws Exception {
        this.clearPercepts(agName);

        //atualiza posicao da bola -> posBola(X,Y)
        Point ballPosition = clientProxy.getBallInformation().getPosition();

        Literal p = ASSyntax.createLiteral("posBola",
                ASSyntax.createNumber(ballPosition.getX()),
                ASSyntax.createNumber(ballPosition.getY()));
        addPercept(agName, p);



        //atualiza a posicao do agente -> posicao(X,Y)
        Point agentPosition = clientProxy.getPlayerInformation(agName).getPosition();
        double agentAngle = clientProxy.getPlayerInformation(agName).getAngle();


        Literal pa = ASSyntax.createLiteral("posicao",
                ASSyntax.createNumber(agentPosition.getX()),
                ASSyntax.createNumber(agentPosition.getY()));

        addPercept(agName, pa);

        //atualiza os companheiros do agente -> companheiro(AgentName)
        if (this.getTeamByPlayerId(agName).equals(SoccerEnv.teamAIdentifier)) {

            for (RobotInformation ri : clientProxy.getRobotsFromTeam(SoccerEnv.teamAIdentifier)) {
                if (ri.getId().equals(agName) == false) {
                    addPercept(agName, Literal.parseLiteral("companheiro(" + ri.getId() + ")"));
                }
            }
        } else if (this.getTeamByPlayerId(agName).equals(SoccerEnv.teamBIdentifier)) {
            for (RobotInformation ri : clientProxy.getRobotsFromTeam(SoccerEnv.teamBIdentifier)) {
                if (ri.getId().equals(agName) == false) {
                    addPercept(agName, Literal.parseLiteral("companheiro(" + ri.getId() + ")"));
                }
            }
        }


        //verifica quem esta com a bola
        String jogadorComBola = null;
        String teamID = this.getTeamByPlayerId(agName);

        //verifica no proprio time
        for (RobotInformation ri : clientProxy.getRobotsFromTeam(teamID)) {
            Point head = ri.getPosition().sum(new Point(10 * Math.cos(agentAngle), 10 * Math.sin(agentAngle)));
            //se agente com bola
            if (head.distanceFrom(ballPosition) < 15) {
                jogadorComBola = ri.getId();
                clientProxy.setPlayerDribble(ri.getId(), Boolean.TRUE);

            } else {
                clientProxy.setPlayerDribble(ri.getId(), Boolean.FALSE);
            }

        }
        try {
            //verifica no time adversario
            for (RobotInformation ri : clientProxy.getRobotsFromOpponentTeam(teamID)) {
                Point head = ri.getPosition().sum(new Point(10 * Math.cos(agentAngle), 10 * Math.sin(agentAngle)));
                if (head.distanceFrom(ballPosition) < 15) {
                    jogadorComBola = ri.getId();
                    clientProxy.setPlayerDribble(ri.getId(), Boolean.TRUE);

                } else {
                    clientProxy.setPlayerDribble(ri.getId(), Boolean.FALSE);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SoccerEnv.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (jogadorComBola != null) {
            addPercept(agName, Literal.parseLiteral("comBola(" + jogadorComBola + ")"));
        }




        //adiciona percepcao ao jogador mais proximo da bola
        String jogadorMaisProximoDaBola = this.verificaJogadorMaisProximoDaBola(agName);
        addPercept(agName, Literal.parseLiteral(MAIS_PERTO_BOLA + "(" + jogadorMaisProximoDaBola + ")"));



        //se próximo ao gol
        if (agentPosition.getX() >= 350.0) {
            addPercept(agName, PERTO_GOL);
            clientProxy.setPlayerDribble(agName, Boolean.TRUE);

        }

        /*
        //define o jogador mais proximo
        Point proximo = verificaJogadorMaisProximo(agName);
        Literal proxA = ASSyntax.createLiteral("companheiroMaisProximo",
        ASSyntax.createNumber(proximo.getX()),
        ASSyntax.createNumber(proximo.getY()));
        addPercept(agName, proxA);
         */

        /*
        //se existir ameaça
        if (containsPercept(agName, COM_BOLA)) {

        this.verificaAmeaca(agName);
        }
         */




    }

    /**
     * Método privado que modela a criacao de um jogador
     * @param agName Nome do agente
     * @param action acao
     * @throws Exception
     */
    private void createPlayer(String agName, Structure action) throws Exception {
        //converte a posicao do grid para posicao do tewnta
        Point position = new Point(Double.parseDouble(action.getTerm(0).toString()), Double.parseDouble(action.getTerm(1).toString()));
        //se o jogador for do time A
        if (action.getTerm(2).toString().equals("team_a")) {
            clientProxy.setPlayer(teamAIdentifier, agName, position.getX(), position.getY());

            //se o jogador for do time B
        } else {
            clientProxy.setPlayer(teamBIdentifier, agName, position.getX(), position.getY());
        }

        clientProxy.setPlayerDribble(agName, Boolean.FALSE);

        //cria um objeto jogador
        this.getJogadorByName(agName).setNome(agName);
        this.getJogadorByName(agName).setPosicaoDesejada(position);
        this.updateAgPercept(agName);
    }

    /**
     * Método privado que modela a ação de defesa do goleiro
     * @param agName Nome do agente
     * @param action Acao
     * @throws Exception
     */
    private void defender(String agName, Structure action) throws Exception {

        Point posBola = new Point(Double.parseDouble(action.getTerm(0).toString()),
                Double.parseDouble(action.getTerm(1).toString()));
        Point posGole = new Point(Double.parseDouble(action.getTerm(2).toString()),
                Double.parseDouble(action.getTerm(3).toString()));

        double xIniGoleiro = 0;

        if (this.getTeamByPlayerId(agName).equals(SoccerEnv.teamAIdentifier)) {
            xIniGoleiro = (SoccerEnv.posXGoleiroTeamA);

        } else {
            xIniGoleiro = (SoccerEnv.posXGoleiroTeamB);
        }



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

        //rotaciona para bola
        MetodosAuxiliares.rotacionarParaPonto(posBola, clientProxy, agName);
        //habilita drible
        clientProxy.setPlayerDribble(agName, Boolean.TRUE);
    }

    private synchronized void melhorPosChute(String agName, Structure action) throws Exception {
        Fuzzy f = new Fuzzy();
        //posicao do jogador
        Point posJog = clientProxy.getPlayerInformation(agName).getPosition();
        Point posGole = null;
        //posicao do goleiro
        if (this.getTeamByPlayerId(agName).equals(SoccerEnv.teamAIdentifier)) {
            posGole = clientProxy.getPlayerInformation(SoccerEnv.goleiroTeamB.getNome()).getPosition();
        } else {
            posGole = clientProxy.getPlayerInformation(SoccerEnv.goleiroTeamA.getNome()).getPosition();
        }


        //calculaChute o y de chute
        double yChute = f.getPosChute(posGole.getY(), posJog.getY());
        //chute
        double totalVirar = MetodosAuxiliares.rotacionarLentamenteParaPonto(new Point(posGole.getX(), yChute), clientProxy, agName);

        if (totalVirar <= 0.05) {

            clientProxy.setPlayerKick(agName, Double.MAX_VALUE);

        }

        //clientProxy.setPlayerKick(agName, 50.0);



    }

    private synchronized void posicionaAtaque(String agName, Structure action, String agComBola, double y) throws Exception {

        double x = 420;
        double xTemp = clientProxy.getPlayerInformation(agComBola).getPosition().getX();
        if (xTemp + 20 < 505) {
            x = xTemp;
        }

        this.getJogadorByName(agName).setPosicaoDesejada(new Point(x, y));
        MetodosAuxiliares.irLinhaReta(clientProxy, this.getJogadorByName(agName));

    }

    /**
     * Método privado que retorna a instancia do jogador através do nome
     * @param name Nome do jogador
     * @return Instância correspondente ao jogador
     */
    private Jogador getJogadorByName(String name) {
        if (name.equals("goleiroTeamA")) {
            return SoccerEnv.goleiroTeamA;


        } else if (name.equals("goleiroTeamB")) {
            return SoccerEnv.goleiroTeamB;
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

    /**
     * Método privado que verifica qual dos jogadores está mais próximo da bola
     * @return <code>String</code> contendo o nome do jogador mais próximo da bola
     */
    private String verificaJogadorMaisProximoDaBola(String agName) {
        String nomeJogadorMaisProximo = null;
        String teamIdentifier = this.getTeamByPlayerId(agName);
        Point ballPosition = clientProxy.getBallInformation().getPosition();
        double menorDistancia = Double.MAX_VALUE;

        for (RobotInformation ri : clientProxy.getRobotsFromTeam(teamIdentifier)) {
            double distancia = ri.getPosition().distanceFrom(ballPosition);

            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                nomeJogadorMaisProximo = ri.getId();
            }

        }

        return nomeJogadorMaisProximo;

    }

    private static Point verificaJogadorMaisProximo(String agName) {
        Point maisProximo = null;

        Point posJog = clientProxy.getPlayerInformation(agName).getPosition();
        Point posAtacanteM = clientProxy.getPlayerInformation(SoccerEnv.atacanteMeio.getNome()).getPosition();
        Point posAtacanteD = clientProxy.getPlayerInformation(SoccerEnv.atacanteDireita.getNome()).getPosition();
        Point posAtacanteE = clientProxy.getPlayerInformation(SoccerEnv.atacanteEsquerda.getNome()).getPosition();



        double distAtacanteM = posAtacanteM.distanceFrom(posJog);


        double distAtacanteD = posAtacanteD.distanceFrom(posJog);


        double distAtacanteE = posAtacanteE.distanceFrom(posJog);



        double distancias[] = {distAtacanteM, distAtacanteD, distAtacanteE};
        Arrays.sort(distancias);


        if (distancias[0] == distAtacanteM) {
            maisProximo = posAtacanteM;


        } else if (distancias[0] == distAtacanteD) {
            maisProximo = posAtacanteD;


        } else if (distancias[0] == distAtacanteE) {
            maisProximo = posAtacanteE;


        }

        return maisProximo;

    }

    private synchronized void passar(String agName, double velocidade) throws Exception {
        clientProxy.setPlayerDribble(agName, Boolean.FALSE);
        clientProxy.setPlayerKick(agName, velocidade);
    }

    /**
     * Método privado que verifica a existencia de adversários próximos
     * @param agName
     */
    private void verificaAmeaca(String agName) {
        String teamIdentifier = this.getTeamByPlayerId(agName);
        Point ballPosition = clientProxy.getBallInformation().getPosition();
        try {
            for (RobotInformation ri : clientProxy.getRobotsFromOpponentTeam(teamIdentifier)) {
                if (ri.getPosition().distanceFrom(ballPosition) < 50.0) {
                    addPercept(agName, Literal.parseLiteral("cuidado(ameaca)"));
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SoccerEnv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo privado que retorna as informacoes do jogador passado como parametro
     * @param playerId Identificador do jogador
     * @return <code>RobotInformation</code> do jogador passado como parametro
     */
    private static RobotInformation getRobotInformationByPlayerId(String playerId) {
        //percorre os jogadores do time a
        for (RobotInformation ri : clientProxy.getRobotsFromTeam(SoccerEnv.teamAIdentifier)) {
            if (ri.getId().equals(playerId)) {
                return ri;
            }
        }

        //percorre os jogadores do time b
        for (RobotInformation ri : clientProxy.getRobotsFromTeam(SoccerEnv.teamBIdentifier)) {
            if (ri.getId().equals(playerId)) {
                return ri;
            }
        }

        return null;
    }

    /**
     * Método privado que retorna a posicao do jogador passado como parametro
     * @param playerId Identificador do jogador
     * @return <code>Point</code> com a posicao do jogador passado como parâmetro
     */
    private Point getPositionById(String playerId) {

        return this.getRobotInformationByPlayerId(playerId).getPosition();
    }

    private String getTeamByPlayerId(String playerId) {

        //percorre os jogadores do time a
        for (RobotInformation ri : clientProxy.getRobotsFromTeam(SoccerEnv.teamAIdentifier)) {
            if (ri.getId().equals(playerId)) {
                return SoccerEnv.teamAIdentifier;
            }
        }

        //percorre os jogadores do time b
        for (RobotInformation ri : clientProxy.getRobotsFromTeam(SoccerEnv.teamBIdentifier)) {
            if (ri.getId().equals(playerId)) {
                return SoccerEnv.teamBIdentifier;
            }
        }


        return null;
    }
}
