package robos;

import br.ufrgs.f180.api.Player;
import br.ufrgs.f180.api.model.RobotInformation;
import br.ufrgs.f180.math.Point;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * Classe principal que se conecta com o simulador
 * @author Caio Nishibe
 * @author Renato Gasoto
 */
public class Main {

    /**
     * Cliente do simulador. Através deste pode-se acessar as informações do jogo
     */
    public static Player cliente;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, Exception {
        Fuzzy f = new Fuzzy();

        //nn.treinamento();

        System.out.println("Iniciando Simulação");
        System.out.println("\tConectando com o servidor");
        URL wsdlURL = new URL("http://localhost:9000/player?wsdl");
        QName SERVICE_NAME = new QName("http://api.f180.ufrgs.br/", "Player");
        Service service = Service.create(wsdlURL, SERVICE_NAME);
        cliente = service.getPort(Player.class);

        System.out.println("Definindo Atacante");



        //System.out.println("Defininfo Goleiro");
        String timeA = cliente.login("Atacante");

        cliente.setPlayer(timeA, "Atacante", new Double(145), new Double(200));
        //goleiro implementado para testes
        String timeB = cliente.login("Goleiro");
        cliente.setPlayer(timeB, "Goleiro", new Double(40), new Double(200));


        //goleiro dado pelos professores
        //String teamB = cliente.login("Atletico-MG");
        //cliente.setPlayer(teamB, "RenanRibeiro", new Double(40), new Double(200));





        //thread para monitoramento de Gol
        MonitoraGol gol = new MonitoraGol(cliente);
        gol.start();

        //loop principal
        while (true) {

            //funções do goleiro
            goleiro();

            //trydefend(teamB);
            //funções do atacante
            atacante();

            //se ocorreu um gol
            //if (gol.isGol()) {
            //imprima
            //    System.out.println("GOOOOLLL!!!!");
            //     gol.setGolFalse();
            //}


            System.out.println("");
            Thread.sleep(100);



        }
    }

    /**
     * Método que modela as atitudes do goleiro
     * @throws Exception
     */
    private static void goleiro() throws Exception {

        cliente.setPlayerDribble("Goleiro", Boolean.TRUE);
        //cliente.setPlayerKick("Goleiro", Double.MAX_VALUE);
        //posicao da bola
        Point posBola = cliente.getBallInformation().getPosition();
        //posicao do goleiro
        Point posGole = cliente.getPlayerInformation("Goleiro").getPosition();

        //cria um objeto jogador com os atributos de goleiro
        Jogador goleiro = new Jogador(posGole.getX(), posGole.getY());
        goleiro.setNome("Goleiro");
        goleiro.setPosicaoDesejada(new Point((Double) posGole.getX(), (Double) posGole.getY()));

        //se a bola estiver acima da posicao do goleiro
        if (posBola.getY() > posGole.getY()) {
            //se a bola estiver acima da trave do gol
            if (posBola.getY() > 230) {
                //goleiro fica na trave
                goleiro.setPosicaoDesejada(new Point(40.0, 229.0));
                MetodosAux.irLinhaReta(cliente, goleiro);
                //se a bola estiver dentro dos limites do gol
            } else {
                //goleiro se posiciona conforme bola
                goleiro.setPosicaoDesejada(new Point(40.0, posBola.getY()));
                MetodosAux.irLinhaReta(cliente, goleiro);
            }

            //se a bola estiver abaixo da posicao do goleiro
        } else if (posBola.getY() <= posGole.getY()) {
            //se a bola estiver abaixo da trave do gol
            if (posBola.getY() < 170) {
                //goleiro fica na trave
                goleiro.setPosicaoDesejada(new Point(40.0, 171.0));
                MetodosAux.irLinhaReta(cliente, goleiro);
                //se a bola estiver dentro dos limites do gol
            } else {
                //goleiro se posiciona conforme bola
                goleiro.setPosicaoDesejada(new Point(40.0, posBola.getY()));
                MetodosAux.irLinhaReta(cliente, goleiro);
            }
        }

    }

    /**
     * Métodos que modelam as atitudes do atacante
     * @throws Exception
     */
    private static void atacante() throws Exception {

        Fuzzy f = new Fuzzy();
        //posicao do jogador
        Point posJog = cliente.getPlayerInformation("Atacante").getPosition();
        //posicao da bola
        Point posBola = cliente.getBallInformation().getPosition();


        //cria um objeto jogador com os atributos de atacante
        Jogador atacante = new Jogador(posJog.getX(), posJog.getY());
        atacante.setNome("Atacante");


        //ativa o drible
        cliente.setPlayerDribble("Atacante", Boolean.TRUE);


        System.out.println("Distancia da bola: " + posJog.distanceFrom(posBola));

        //se próximo da bola
        if (posJog.distanceFrom(posBola) < 120) {
            //contorna a bola e se prepara para o chute
            MetodosAux.contornaBola(cliente, atacante);
            //caso contrário
        } else {
            //se posiciona utilizando o controle fuzzy
            atacante.setPosicaoDesejada(new Point(f.getPosX(posBola.getX(), posJog.getX()), f.getPosY(posBola.getY(), posJog.getY())));
            MetodosAux.irLinhaReta(cliente, atacante);
        }


    }

    private static void trydefend(String TeamB) throws Exception {




        // ball y coordinate
        double yb = cliente.getBallInformation().getPosition().getY();
        RobotInformation atacante = cliente.getRobotsFromOpponentTeam(TeamB).get(0);
        Point pAt = atacante.getPosition();
        Point pBol = cliente.getBallInformation().getPosition();
        double alpha = (pAt.getY() - pBol.getY()) / (pAt.getX() - pBol.getX());
        double b = pAt.getY() - alpha * pAt.getX();


        Point posGole = cliente.getPlayerInformation("RenanRibeiro").getPosition();
        Jogador goleiro = new Jogador(posGole.getX(), posGole.getY());
        goleiro.setNome("RenanRibeiro");
        
        double yg = alpha * (40.0) + b;



        // goalkeeper y coordinate
        //double yg = cliente.getPlayerInformation("RenanRibeiro").getPosition().getY();

        if (yg > posGole.getY()) {
            //se a bola estiver acima da trave do gol
            if (yg > 230) {
                //goleiro fica na trave
                goleiro.setPosicaoDesejada(new Point(40.0, 229.0));
                MetodosAux.irLinhaReta(cliente, goleiro);
                //se a bola estiver dentro dos limites do gol
            } else {
                //goleiro se posiciona conforme bola
                goleiro.setPosicaoDesejada(new Point(40.0, yg));
                MetodosAux.irLinhaReta(cliente, goleiro);
            }

            //se a bola estiver abaixo da posicao do goleiro
        } else if (yg <= posGole.getY()) {
            //se a bola estiver abaixo da trave do gol
            if (yg < 170) {
                //goleiro fica na trave
                goleiro.setPosicaoDesejada(new Point(40.0, 171.0));
                MetodosAux.irLinhaReta(cliente, goleiro);
                //se a bola estiver dentro dos limites do gol
            } else {
                //goleiro se posiciona conforme bola
                goleiro.setPosicaoDesejada(new Point(40.0, yg));
                MetodosAux.irLinhaReta(cliente, goleiro);
            }
        }



    }
}
