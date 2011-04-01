package br.ufrgs.f180.server;

import java.util.List;

import org.apache.log4j.Logger;

import br.ufrgs.f180.api.model.BallInformation;
import br.ufrgs.f180.api.model.RobotInformation;
import br.ufrgs.f180.elements.Ball;
import br.ufrgs.f180.elements.GameField;
import br.ufrgs.f180.elements.Robot;
import br.ufrgs.f180.elements.Robot.Team;
import br.ufrgs.f180.gui.MainWindow;
import br.ufrgs.f180.math.Vector;
import br.ufrgs.f180.resources.GameProperties;

/**
 * 
 * @author Gabriel Detoni
 * 
 */
public class Game {

	private static Logger logger = Logger.getLogger(Game.class);

	private static Game instance = null;

	/**
	 * Time interval on which the elements state will be updated in ms
	 */
	public static final int GAME_LOOP_INTERVAL = 5;

	/**
	 * This is the visual container of this game.
	 */
	private MainWindow mainWindow;

	/**
	 * Game state variables
	 */
	private boolean gameRunning;
	private String nameTeamA;
	private String nameTeamB;
	private int scoreTeamA;
	private int scoreTeamB;
	private double elapsedTime;

	protected Game() {
	};

	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}

	public void resetGame() {
		gameRunning = false;
		nameTeamA = null;
		nameTeamB = null;
		scoreTeamA = 0;
		scoreTeamB = 0;
		elapsedTime = 0;
		setUpBall();
	}

	public void startGame() {
		gameRunning = true;
	}

	public void pauseGame() {
		gameRunning = false;
	}

	public void setUp(MainWindow window) throws Exception {
		this.mainWindow = window;
		mainWindow.setField(new GameField(window.getFootballFieldCanvas(),
				GameProperties.getDoubleValue("field.width") * 100d,
				GameProperties.getDoubleValue("field.height") * 100d));
		setUpBall();
	}

	public void addPlayer(String teamId, String id, double x, double y)
			throws Exception {
		mainWindow.addRobot(id, x, y, getTeam(teamId), GameProperties
				.getDoubleValue("robot.mass"), GameProperties
				.getDoubleValue("robot.radius") * 100d);
	}

	private Team getTeam(String teamId) {
		return Team.valueOf(teamId);
	}

	public void setPlayerForce(String id, double x, double y) throws Exception {
		if (mainWindow.getField() != null) {
			Robot r = (Robot) mainWindow.getField().getElement(id);
			r.setForce(new Vector(x, y));
		} else {
			throw new Exception(
					"Cannot get element. Configure the mainWindow.getField() first");
		}
	}

	/**
	 * This is the method that updates the game state due a given time in
	 * milliseconds. Usually invoked in the main loop.
	 * 
	 * @param d
	 *            time in ms
	 */
	public void updateState(double d) {

		if (mainWindow.getField() != null) {
			double timeInterval = 0;
			if (gameRunning) {
				timeInterval = d;
			}
			elapsedTime += timeInterval * 1000d;
			mainWindow.getField().updateElementsState(timeInterval);

			ScoredGoal s = goalScored();
			switch (s) {
			case SCORED_LEFT:
				logger.debug("Goal Team A");
				gameRunning = false;
				setUpBall();
				scoreTeamB++;
				break;
			case SCORED_RIGHT:
				logger.debug("Goal Team B");
				gameRunning = false;
				setUpBall();
				scoreTeamA++;
				break;
			}

		}
	}

	/**
	 * Put the Ball back into its initial position
	 */
	private void setUpBall() {
		try {
			mainWindow
					.getField()
					.addElement(
							GameField.BALL_ELEMENT,
							new Ball(
									GameProperties
											.getDoubleValue("field.width") * 100d / 2d,
									GameProperties
											.getDoubleValue("field.height") * 100d / 2d,
									GameProperties.getDoubleValue("ball.mass"),
									GameProperties
											.getDoubleValue("ball.radius") * 100d));
		} catch (Exception e) {
			logger.error("ERROR: ", e);;
		}
	}

	public enum ScoredGoal {
		NONE, SCORED_LEFT, SCORED_RIGHT
	};

	public ScoredGoal goalScored() {

		Ball b = (Ball) mainWindow.getField()
				.getElement(GameField.BALL_ELEMENT);
		double left = b.getPosition().getX() - b.getRadius();
		double right = b.getPosition().getX() + b.getRadius();
		if (b.getPosition().getY() >= mainWindow.getField().getGoalTop()
				&& b.getPosition().getY() <= mainWindow.getField()
						.getGoalDown()) {
			if (left >= mainWindow.getField().getRightBound()) {
				return ScoredGoal.SCORED_RIGHT;
			}
			if (right <= mainWindow.getField().getLeftBound()) {
				return ScoredGoal.SCORED_LEFT;
			}
		}
		return ScoredGoal.NONE;
	}

	public int getScoreTeamA() {
		return scoreTeamA;
	}

	public void setScoreTeamA(int scoreTeamA) {
		this.scoreTeamA = scoreTeamA;
	}

	public int getScoreTeamB() {
		return scoreTeamB;
	}

	public void setScoreTeamB(int scoreTeamB) {
		this.scoreTeamB = scoreTeamB;
	}

	public long getElapsedTime() {
		return (long)elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}

	public boolean getGameRunning() {
		return gameRunning;
	}

	public String login(String teamName) throws Exception {
		if (nameTeamA == null) {
			nameTeamA = teamName;
			return Team.A.toString();
		} else if (nameTeamB == null) {
			nameTeamB = teamName;
			return Team.B.toString();
		} else {
			throw new Exception("All players already defined.");
		}
	}

	public String getNameTeamA() {
		return nameTeamA;
	}

	public String getNameTeamB() {
		return nameTeamB;
	}

	public Object getTeamName(Team team) {
		return Team.A.equals(team) ? nameTeamA : nameTeamB;
	}

	public void setPlayerRotation(String id, Double force) throws Exception {
		if (mainWindow.getField() != null) {
			Robot r = (Robot) mainWindow.getField().getElement(id);
			r.setRotationForce(force);
		} else {
			throw new Exception(
					"Cannot get element. Configure the mainWindow.getField() first");
		}
	}

	public void logout(String teamId) throws Exception {
		switch (getTeam(teamId)) {
		case A:
			nameTeamA = null;
			break;
		case B:
			nameTeamB = null;
			break;
		}
		mainWindow.removeRobotsFromTeam(getTeam(teamId));
	}

	public BallInformation getBallInformation() {
		Ball b = (Ball) mainWindow.getField()
				.getElement(GameField.BALL_ELEMENT);

		BallInformation ball = new BallInformation();
		ball.setTimestamp(((double) this.elapsedTime) / 10000.0);
		ball.setAngle(b.getAngle());
		ball.setPosition(b.getPosition());
		ball.setVelocity(b.getVelocity());
		ball.setRadius(b.getRadius());
		return ball;
	}

	public List<RobotInformation> getRobotsFromTeam(String teamId) {
		List<RobotInformation> l = mainWindow
				.getRobotsFromTeam(getTeam(teamId));
		for (RobotInformation robotInformation : l) {
			robotInformation
					.setTimestamp(((double) this.elapsedTime) / 10000.0);
		}
		return l;
	}

	public RobotInformation getPlayerInformation(String playerId) {
		RobotInformation r = mainWindow.getPlayerInformation(playerId);
		r.setTimestamp(((double) this.elapsedTime) / 10000.0);
		return r;
	}

	/**
	 * Support for CMDragons
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public void setPlayerVelocity(String id, Double x, Double y)
			throws Exception {
		if (mainWindow.getField() != null) {
			Robot r = (Robot) mainWindow.getField().getElement(id);
			r.setForce(new Vector(0, 0));
			r.setTargetVelocity(new Vector(x, y));
		} else {
			throw new Exception(
					"Cannot get element. Configure the mainWindow.getField() first");
		}
	}

	/**
	 * Support for CMDragons
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public void setPlayerRotationVelocity(String id, Double velocity)
			throws Exception {
		if(velocity.isNaN()){
			throw new Exception(
			"Cannot set velocity of " + id + " to NaN.");
		}
		if (mainWindow.getField() != null) {
			Robot r = (Robot) mainWindow.getField().getElement(id);
			r.setRotationForce(0d);
			r.setRotationVelocity(velocity);
		} else {
			throw new Exception(
					"Cannot get element. Configure the mainWindow.getField() first");
		}
	}

	public void setPlayerKick(String id, Double strength) throws Exception {
		if (mainWindow.getField() != null) {
			Robot r = (Robot) mainWindow.getField().getElement(id);
			r.setKicking(strength);
		} else {
			throw new Exception(
					"Cannot get element. Configure the mainWindow.getField() first");
		}
	}

	public void setPlayerDribble(String id, Boolean dribble) throws Exception {
		if (mainWindow.getField() != null) {
			Robot r = (Robot) mainWindow.getField().getElement(id);
			r.setDribbling(dribble);
		} else {
			throw new Exception(
					"Cannot get element. Configure the mainWindow.getField() first");
		}
	}

	public List<RobotInformation> getRobotsFromOpponentTeam(String teamId) throws Exception {
		Team team = Team.valueOf(teamId);

		switch(team){
		case A:
			return getRobotsFromTeam(Team.B.toString());
		case B:
			return getRobotsFromTeam(Team.A.toString());
			default:
				throw new Exception("No team ID matching: " + team.toString());
		}
		
	}
}
