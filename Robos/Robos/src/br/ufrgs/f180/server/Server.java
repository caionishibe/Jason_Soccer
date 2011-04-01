package br.ufrgs.f180.server;

import java.net.InetAddress;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import br.ufrgs.f180.api.PlayerImpl;

public class Server {
	private static Logger logger = Logger.getLogger(Server.class);

	private static final String PROTOCOL = "http://";
	private static final String PORT = ":9000";
	private static final String SERVICE = "/player";
	
	private Endpoint endpoint;
	private PlayerImpl implementor;
	
	public void startServer() throws Exception {
		// START SNIPPET: publish
		logger.debug("Starting Server");
		
		String ip = InetAddress.getLocalHost().getHostAddress();
		
		implementor = new PlayerImpl();
		endpoint = Endpoint.create(implementor);
		endpoint.publish(PROTOCOL + ip + PORT + SERVICE);
		// END SNIPPET: publish
	}

	public void stopServer() throws Exception {
		// START SNIPPET: publish
		logger.debug("Stopping Server");
		endpoint.stop();
	}
	
	public static void main(String args[]) throws Exception {
		Server s = new Server();
		s.startServer();
		logger.debug("Server ready...");

		Thread.sleep(60 * 1000);
		logger.debug("Server exiting");
		s.stopServer();
		System.exit(0);
	}

	public boolean isStarted() {
		return endpoint != null && endpoint.isPublished();
	}

}
