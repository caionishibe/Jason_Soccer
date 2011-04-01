package br.ufrgs.f180.team;

import java.net.URL;
import java.util.Random;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;

import br.ufrgs.f180.api.Player;

public class DemoTeam {
	private static Logger logger = Logger.getLogger(DemoTeam.class);
	
	static Player client;
	public static void main(String[] args) throws Exception {
		logger.debug("Iniciando simula��o");
		logger.debug("Conectando com servidor");
		URL wsdlURL = new URL("http://localhost:9000/player?wsdl");
		QName SERVICE_NAME = new QName("http://api.f180.ufrgs.br/", "Player");
		Service service = Service.create(wsdlURL, SERVICE_NAME);
		client = service.getPort(Player.class);
		
		logger.debug("Definindo time A");
		String teamA = client.login("Gr�mio");
		client.setPlayer(teamA, "PlayerA1", new Double(120), new Double(120));
		client.setPlayer(teamA, "PlayerA2", new Double(230), new Double(100));
		client.setPlayer(teamA, "PlayerA3", new Double(120), new Double(280));
		client.setPlayer(teamA, "PlayerA4", new Double(230), new Double(300));
		client.setPlayer(teamA, "PlayerA5", new Double(40), new Double(200));

		logger.debug("Definindo time B");
		String teamB = client.login("Inter");
		client.setPlayer(teamB, "PlayerB1", new Double(320), new Double(120));
		client.setPlayer(teamB, "PlayerB2", new Double(430), new Double(100));
		client.setPlayer(teamB, "PlayerB3", new Double(320), new Double(280));
		client.setPlayer(teamB, "PlayerB4", new Double(430), new Double(300));
		client.setPlayer(teamB, "PlayerB5", new Double(440), new Double(200));
		dance();
	}
	
	private static void dance() throws Exception{

		logger.debug("Passando for�as aos jogadores");
		
//		int data = 0;
//		while(data != 'q'){
//			data = System.in.read();
//			switch(data){
//				case 'l': {
//					client.setPlayerForce("PlayerA1", new Double(900), new Double(0));
//				}
//				break;
//				case 'j': {
//					client.setPlayerForce("PlayerA1", new Double(-900), new Double(0));
//				}
//				break;
//				case 'i': {
//					client.setPlayerForce("PlayerA1", new Double(0), new Double(-900));
//				}
//				break;
//				case 'k': {
//					client.setPlayerForce("PlayerA1", new Double(0), new Double(900));
//				}
//				break;
//			}
//		}
		long time = System.currentTimeMillis();
		while(System.currentTimeMillis() - time < 2 * 60 * 1000){
			Random r = new Random();
			int x = r.nextInt(504);
			int y = r.nextInt(504);
			int rot = r.nextInt(50);
			logger.debug("For�a PlayerA1: x " + (x - 252) + ", y " + (y - 252) + " Rota��o: " + (rot - 25));
			client.setPlayerForce("PlayerA1", new Double(x - 252), new Double(y - 252));
			client.setPlayerRotation("PlayerA1", new Double(rot - 25));
			x = r.nextInt(504);
			y = r.nextInt(504);
			rot = r.nextInt(50);
			logger.debug("Força PlayerA2: x " + (x - 252) + ", y " + (y - 252) + " Rota��o: " + (rot - 25));
			client.setPlayerForce("PlayerA2", new Double(x - 252), new Double(y - 252));
			client.setPlayerRotation("PlayerA2", new Double(rot - 25));
			x = r.nextInt(504);
			y = r.nextInt(504);
			logger.debug("Força PlayerA3: x " + (x - 252) + ", y " + (y - 252) + " Rota��o: " + (rot - 25));
			client.setPlayerForce("PlayerA3", new Double(x - 252), new Double(y - 252));
			client.setPlayerRotation("PlayerA3", new Double(rot - 25));
			x = r.nextInt(504);
			y = r.nextInt(504);
			logger.debug("Força PlayerB1: x " + (x - 252) + ", y " + (y - 252));
			client.setPlayerForce("PlayerB1", new Double(x - 252), new Double(y - 252));
			x = r.nextInt(504);
			y = r.nextInt(504);
			logger.debug("Força PlayerB2: x " + (x - 252) + ", y " + (y - 252));
			client.setPlayerForce("PlayerB2", new Double(x - 252), new Double(y - 252));
			x = r.nextInt(504);
			y = r.nextInt(504);
			logger.debug("Força PlayerB3: x " + (x - 252) + ", y " + (y - 252));
			client.setPlayerForce("PlayerB3", new Double(x - 252), new Double(y - 252));
			Thread.sleep(2000);
		}
		
		logger.debug("Programa encerrado.");
		
	}
}
