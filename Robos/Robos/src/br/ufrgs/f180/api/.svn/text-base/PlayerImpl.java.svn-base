package br.ufrgs.f180.api;

import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import br.ufrgs.f180.api.model.BallInformation;
import br.ufrgs.f180.api.model.GameInformation;
import br.ufrgs.f180.api.model.RobotInformation;
import br.ufrgs.f180.elements.Robot.Team;
import br.ufrgs.f180.server.Game;

/**
 * This is the implementation of the Player APIs. 
 * 
 * Most information is gathered from the Game.java class.
 * 
 * @author Gabriel Detoni
 *
 */
@WebService(endpointInterface = "br.ufrgs.f180.api.Player", serviceName = "Player" )
public class PlayerImpl implements Player {

	private static Logger logger = Logger.getLogger(PlayerImpl.class);

	@Override
	public String echo(String msg) {
		logger.debug("echo: " + msg);
		return msg;
	}

	@Override
	public void setPlayer(String teamId, String id, Double x, Double y) throws Exception {
		Game.getInstance().addPlayer(teamId, id, x, y);
	}

	@Override
	public void setPlayerForce(String id, Double x, Double y) throws Exception {
		Game.getInstance().setPlayerForce(id, x, y);
	}

	@Override
	public String login(String teamName) throws Exception {
		return Game.getInstance().login(teamName);
	}

	@Override
	public void setPlayerRotation(String id, Double force) throws Exception {
		Game.getInstance().setPlayerRotation(id, force);
	}

	@Override
	public void logout(String teamId) throws Exception {
		
		Game.getInstance().logout(teamId);
	}

	@Override
	public BallInformation getBallInformation() {
		return Game.getInstance().getBallInformation();
	}

	@Override
	public List<RobotInformation> getRobotsFromTeam(String teamId) {
		return Game.getInstance().getRobotsFromTeam(teamId);
	}

	@Override
	public RobotInformation getPlayerInformation(String playerId) {
		return Game.getInstance().getPlayerInformation(playerId);
	}

	@Override
	public void setPlayerVelocity(String id, Double x, Double y)
			throws Exception {
		Game.getInstance().setPlayerVelocity(id, x, y);		
	}

	@Override
	public void setPlayerRotationVelocity(String id, Double velocity)
			throws Exception {
		Game.getInstance().setPlayerRotationVelocity(id, velocity);		
	}

	@Override
	public void setPlayerKick(String id, Double strength)
			throws Exception {
		Game.getInstance().setPlayerKick(id, strength);		
	}

	@Override
	public void setPlayerDribble(String id, Boolean dribble) throws Exception {
		Game.getInstance().setPlayerDribble(id, dribble);		
	}

	@Override
	public List<RobotInformation> getRobotsFromOpponentTeam(String teamId) throws Exception {
		return Game.getInstance().getRobotsFromOpponentTeam(teamId);
	}

	@Override
	public GameInformation getGameInformation() throws Exception {
		GameInformation gi = new GameInformation();
		gi.setTeamA(Game.getInstance().getRobotsFromTeam(Team.A.toString()));
		gi.setTeamB(Game.getInstance().getRobotsFromTeam(Team.B.toString()));
		gi.setBall(Game.getInstance().getBallInformation());
		return gi;
	}

}
