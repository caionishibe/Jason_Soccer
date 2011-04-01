package br.ufrgs.f180.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class is responsible for loading the physical parameters of the simulation.
 * 
 * @author Gabriel
 *
 */
public class GameProperties extends Properties {
	private static Logger logger = Logger.getLogger(GameProperties.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static GameProperties instance;

	private GameProperties(){
		try {
			InputStream f = ClassLoader.getSystemResourceAsStream("game.properties");
			load(f);
		} catch (FileNotFoundException e) {
			logger.error("ERROR: ", e);;
		} catch (IOException e) {
			logger.error("ERROR: ", e);;
		}
	}
	
	public static GameProperties getInstance(){
		if(instance == null){
			instance = new GameProperties();
		}
		return instance;
	}
	
	/**
	 * Loads properties as double values
	 * @param key
	 * @return
	 */
	public static double getDoubleValue(String key){
		return Double.valueOf(getInstance().getProperty(key));
	}

	/**
	 * Loads properties as integer values
	 * @param key
	 * @return
	 */
	public static int getIntValue(String key){
		return Integer.valueOf(getInstance().getProperty(key));
	}
}
