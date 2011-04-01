package br.ufrgs.f180.api;

import java.util.List;

import javax.jws.WebService;

import br.ufrgs.f180.api.model.BallInformation;
import br.ufrgs.f180.api.model.GameInformation;
import br.ufrgs.f180.api.model.RobotInformation;

/**
 * This is the main API provided to players to connect and communicate with the
 * simulator. It's purpose is to provide the same sort of functionality a
 * "vision" and a "motion" components would do in a real robotic soccer game.
 * The APIs are published as a web-service and are available as soon as the
 * system starts up.
 * 
 * A regular game flow requires: 1- Login your team using the login API 2- Add
 * up to 5 players using the setPlayer API 3- Provide motion to your players
 * thru the player APIs 4- Read the players position and information thru the
 * player APIs 5- Disconnect the team thru the logout API
 * 
 * API's are documented individually below.
 * 
 * @author Gabriel Detoni
 * 
 */
@WebService
public interface Player {
	/**
	 * This is a simple echo command to test the communication latency.
	 * 
	 * @param msg
	 * @return the same message
	 */
	String echo(String msg);

	/**
	 * Logs in a team. Notice the team is identified by its name, thus no
	 * duplicate names are allowed. This API will define the team name in the
	 * simulator window as well as provide a logical view of the team.
	 * 
	 * @param teamName
	 *            is the team name to be displayed.
	 * @return the unique team identifier that will be necessary in the
	 *         subsequent API calls.
	 * @throws Exception
	 */
	String login(String teamName) throws Exception;

	/**
	 * Removes a team and all its players from the game.
	 * 
	 * @param teamId
	 * @throws Exception
	 */
	void logout(String teamId) throws Exception;

	/**
	 * Configures a robot. If a robot with the given ID is not present, it will
	 * be created.
	 * 
	 * @param teamId
	 *            the unique identifier for the team, result of a login API
	 *            invocation.
	 * @param id
	 *            the identifier of the robot that shall be configured.
	 * @param x
	 *            the initial horizontal position of the robot in the field. 0
	 *            being the left.
	 * @param y
	 *            the initial vertical position of the robot in the field. 0
	 *            being the bottom.
	 * @throws Exception
	 */
	void setPlayer(String teamId, String id, Double x, Double y)
			throws Exception;

	/**
	 * Sets a force vector that will be used for robot motion. Considering the
	 * robots are omnidirectional it is assumed a single force vector to be
	 * distributed in the 3 electric motors that will produce the expected
	 * resulting force.
	 * 
	 * @param id
	 *            the id of the robot to receive the setting
	 * @param x
	 *            horizontal component of the force vector
	 * @param y
	 *            vertical component of the force vector
	 * @throws Exception
	 */
	void setPlayerForce(String id, Double x, Double y) throws Exception;

	/**
	 * Sets a velocity vector that will be used for robot motion. Considering
	 * the robots are omnidirectional it is assumed a single force vector to be
	 * distributed in the 3 electric motors that will produce the expected
	 * resulting velocity. The robot will use the maximum force until the
	 * desired velocity is reached.
	 * 
	 * @param id
	 *            the id of the robot to receive the setting
	 * @param x
	 *            horizontal component of the velocity vector
	 * @param y
	 *            vertical component of the velocity vector
	 * @throws Exception
	 */
	void setPlayerVelocity(String id, Double x, Double y) throws Exception;

	/**
	 * Sets a rotation force that will be used for robot motion. Considering the
	 * robots are omnidirectional it is assumed a single force vector to be
	 * distributed in the 3 electric motors that will produce the expected
	 * resulting rotation motion.
	 * 
	 * @param id
	 *            the id of the robot to receive the setting.
	 * @param force
	 *            the rotation force.
	 * @throws Exception
	 */
	void setPlayerRotation(String id, Double force) throws Exception;

	/**
	 * Sets a rotation velocity that will be used for robot motion. Considering
	 * the robots are omnidirectional it is assumed a single force vector to be
	 * distributed in the 3 electric motors that will produce the expected
	 * resulting rotation motion.
	 * 
	 * @param id
	 *            the id of the robot to receive the setting.
	 * @param force
	 *            the rotation force.
	 * @throws Exception
	 */
	void setPlayerRotationVelocity(String id, Double velocity) throws Exception;

	/**
	 * Lists all the robots from a given team. Can be used to get the
	 * information of all robots with a single API invocation.
	 * 
	 * @param teamId
	 *            the team that shall return the info.
	 * @return
	 */
	List<RobotInformation> getRobotsFromTeam(String teamId);

	/**
	 * Return the information of a robot, such as its position and velocity.
	 * 
	 * @param playerId
	 *            the robot that shall return the info.
	 * @return
	 */
	RobotInformation getPlayerInformation(String playerId);

	/**
	 * Returns the ball information.
	 * 
	 * @return
	 */
	BallInformation getBallInformation();

	/**
	 * Turn kick on or off
	 * 
	 * @param id
	 * @param strength
	 *            the strength of the kick.
	 * @throws Exception
	 */
	void setPlayerKick(String id, Double strength) throws Exception;

	/**
	 * Turn dribbler on or off
	 * 
	 * @param id
	 * @param dribbler
	 *            state. True means ON.
	 * @throws Exception
	 */
	void setPlayerDribble(String id, Boolean dribble) throws Exception;

	/**
	 * Returns all the information from the opponent team.
	 * 
	 * @param teamId
	 *            the ID of my team. One team is not supposed to know the ID of
	 *            its opponent, only its own.
	 * @return
	 * @throws Exception
	 */
	List<RobotInformation> getRobotsFromOpponentTeam(String teamId)
			throws Exception;

	/**
	 * Read the entire game information. It is assured that the elements will be
	 * returned always in the same index inside the list.
	 * 
	 * @return
	 * @throws Exception
	 */
	GameInformation getGameInformation() throws Exception;

}
