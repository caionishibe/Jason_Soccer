
import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;
import br.ufrgs.f180.api.Player;
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
    private static final Literal COM_BOLA = Literal.parseLiteral("com(bola)");
    private static final Literal MAIS_PERTO_BOLA = Literal.parseLiteral("maisPerto(bola)");
    private static final Literal PERTO_GOL = Literal.parseLiteral("perto(gol)");
    /* Ações */
    private static final String CREATE_PLAYER = "createPlayer";
    private static final String ROTACIONE_PARA_BOLA = "rotacioneParaBola";
    private static final String IR_LINHA_RETA = "irLinhaReta";
    private static final String DEFENDER_GOL = "defender";
    private static final Literal CHUTAR = Literal.parseLiteral("chutar");
    private static final Literal POSICAO_CHUTE = Literal.parseLiteral("posicaoChute");
    private static final Literal POSICIONA_ATAQUE = Literal.parseLiteral("posicionaAtaque");
    /*Jogadores*/
    private static final Jogador goleiro = new Jogador(0, 0);
    private static final Jogador atacanteMeio = new Jogador(0, 0);
    private static final Jogador atacanteDireita = new Jogador(0, 0);
    private static final Jogador atacanteEsquerda = new Jogador(0, 0);
    /*Outras constantes*/
    public static final int posXGoleiro = 510;

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

        if (!(action.getFunctor().equals(CREATE_PLAYER))) {
            try {
                this.updateAgPercept(agName);
            } catch (Exception ex) {
                Logger.getLogger(SoccerEnv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {


            //logger.info(agName + " doing: " + action);

            if (action.getFunctor().equals(CREATE_PLAYER)) {
                this.createPlayer(agName, action);
                //atualiza as percepcoes dos agentes criados
                this.updateAgPercept(agName);

            }
            if (action.getFunctor().equals(ROTACIONE_PARA_BOLA)) {
                Point posBola = new Point(Double.parseDouble(action.getTerm(0).toString()),
                        Double.parseDouble(action.getTerm(1).toString()));
                MetodosAuxiliares.rotacionarLentamenteParaPonto(posBola, clientProxy, agName);
            }
            if (action.getFunctor().equals(IR_LINHA_RETA)) {
                Point posBola = new Point(Double.parseDouble(action.getTerm(0).toString()),
                        Double.parseDouble(action.getTerm(1).toString()));

                this.getJogadorByName(agName).setPosicaoDesejada(posBola);
                MetodosAuxiliares.irLinhaReta(clientProxy, this.getJogadorByName(agName));

            }
            if (action.getFunctor().equals(DEFENDER_GOL)) {

                this.defender(agName, action);
            }
            if (action.equals(CHUTAR)) {
                clientProxy.setPlayerKick(agName, Double.MAX_VALUE);
            }
            if (action.equals(POSICAO_CHUTE)) {

                this.melhorPosChute(agName, action);
            }
            if(action.equals(POSICIONA_ATAQUE))
            {
                this.posicionaAtaque(agName,action);
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
    private void updateAgPercept(String agName) throws Exception {
        this.clearPercepts(agName);

        //atualiza posicao da bola
        Point ballPosition = clientProxy.getBallInformation().getPosition();
       

        Literal p = ASSyntax.createLiteral("posBola",
                ASSyntax.createNumber(ballPosition.getX()),
                ASSyntax.createNumber(ballPosition.getY()));
        addPercept(agName, p);



        //atualiza a posicao do agente
        Point agentPosition = clientProxy.getPlayerInformation(agName).getPosition();
        
        Literal pa = ASSyntax.createLiteral("posicao",
                ASSyntax.createNumber(agentPosition.getX()),
                ASSyntax.createNumber(agentPosition.getY()));

        addPercept(agName, pa);
        

        //se agente com bola
        if (true) {

            addPercept(agName, COM_BOLA);
        }

        //se bola nao dominada
        if (false) {

            addPercept(agName, NAO_DOMINADA);

        }

        //se próximo ao gol
        if (agentPosition.getX() >= 400.0) {
            addPercept(agName, PERTO_GOL);

        }


        //adiciona percepcao ao jogador mais proximo da bola
        String jogadorMaisProximoDaBola = this.verificaJogadorMaisProximoDaBola(agName);
        if (agName.equals(jogadorMaisProximoDaBola)) {
            addPercept(agName, MAIS_PERTO_BOLA);
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
        Point position = new Point(Double.parseDouble(action.getTerm(0).toString()), Double.parseDouble(action.getTerm(1).toString()));
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

    /**
     * Método privado que modela a ação de defesa do goleiro
     * @param agName Nome do agente
     * @param action Acao
     * @throws Exception
     */
    private synchronized void defender(String agName, Structure action) throws Exception {

        Point posBola = new Point(Double.parseDouble(action.getTerm(0).toString()),
                Double.parseDouble(action.getTerm(1).toString()));
        Point posGole = new Point(Double.parseDouble(action.getTerm(2).toString()),
                Double.parseDouble(action.getTerm(3).toString()));

        double xIniGoleiro = (SoccerEnv.posXGoleiro);

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

    private synchronized void melhorPosChute(String agName, Structure action) throws Exception {
        Fuzzy f = new Fuzzy();
        //posicao do jogador
        Point posJog = clientProxy.getPlayerInformation(agName).getPosition();
        //posicao do goleiro
        Point posGole = clientProxy.getPlayerInformation(SoccerEnv.goleiro.getNome()).getPosition();

        //calculaChute o y de chute
        double yChute = f.getPosChute(posGole.getY(), posJog.getY());
        //chute
        MetodosAuxiliares.rotacionarParaPonto(new Point(posGole.getX(), yChute), clientProxy, agName);
        //clientProxy.setPlayerKick(agName, 50.0);



    }

    private synchronized void posicionaAtaque(String agName, Structure action) throws Exception {

       int xAtaque = 17 + (int)(Math.random() * ((24 - 17) + 1));
       int yAtaque = (int)(Math.random()*16);


       MetodosAuxiliares.irLinhaReta(clientProxy, this.getJogadorByName(agName));

    }

    /**
     * Método privado que retorna a instancia do jogador através do nome
     * @param name Nome do jogador
     * @return Instância correspondente ao jogador
     */
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

    /**
     * Método privado que verifica qual dos jogadores está mais próximo da bola
     * @return <code>String</code> contendo o nome do jogador mais próximo da bola
     */
    private synchronized String verificaJogadorMaisProximoDaBola(String agName) {
        String nomeJogador = null;

        Point posBola = clientProxy.getBallInformation().getPosition();
        Point posGoleiro = clientProxy.getPlayerInformation(SoccerEnv.goleiro.getNome()).getPosition();
        Point posAtacanteM = clientProxy.getPlayerInformation(SoccerEnv.atacanteMeio.getNome()).getPosition();
        Point posAtacanteD = clientProxy.getPlayerInformation(SoccerEnv.atacanteDireita.getNome()).getPosition();
        Point posAtacanteE = clientProxy.getPlayerInformation(SoccerEnv.atacanteEsquerda.getNome()).getPosition();



        double distGoleiro = posGoleiro.distanceFrom(posBola);


        double distAtacanteM = posAtacanteM.distanceFrom(posBola);


        double distAtacanteD = posAtacanteD.distanceFrom(posBola);


        double distAtacanteE = posAtacanteE.distanceFrom(posBola);



        double distancias[] = {distGoleiro, distAtacanteM, distAtacanteD, distAtacanteE};
        Arrays.sort(distancias);

        if (!(agName.equals(SoccerEnv.goleiro.getNome())) && distancias[0] == distGoleiro) {
            distancias[0] = distancias[1];
        }


        if (distancias[0] == distGoleiro) {
            nomeJogador = SoccerEnv.goleiro.getNome();


        } else if (distancias[0] == distAtacanteM) {
            nomeJogador = SoccerEnv.atacanteMeio.getNome();


        } else if (distancias[0] == distAtacanteD) {
            nomeJogador = SoccerEnv.atacanteDireita.getNome();


        } else if (distancias[0] == distAtacanteE) {
            nomeJogador = SoccerEnv.atacanteEsquerda.getNome();


        }

        return nomeJogador;

    }
}
