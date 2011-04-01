package br.ufrgs.f180.elements;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import br.ufrgs.f180.elements.Wall.CollisionSide;
import br.ufrgs.f180.math.MathUtils;
import br.ufrgs.f180.math.Point;
import br.ufrgs.f180.math.Vector;
import br.ufrgs.f180.resources.GameProperties;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * 
 * @author Gabriel Detoni
 * 
 */
public class Robot extends MovingElement {
	private static Logger logger = Logger.getLogger(Robot.class);

	private static final double ROBOT_MAX_VELOCITY = (double) GameProperties
			.getDoubleValue("robot.max.velocity") * 100;

	// FIXME: This multiplier is empiric.. need to do precise calculations to
	// know the real force of the robot
	private static final double ROBOT_MAX_FORCE = (double) GameProperties
			.getDoubleValue("robot.max.power") * 10;

	private static final double SPOT_SIZE = 2.5;
	private double radius;
	private Vector front;
	private String id;

	/**
	 * This is the Velocity to be achieved when a setVelocity command is
	 * received. The robot will put maximum force towards it in a way the delay
	 * will be minimal.
	 */
	private Vector targetVelocity = new Vector(0, 0);

	// dribbler and kicker
	private boolean dribbling;
	private double kicking;

	/**
	 * True means the force will be displayed as an arrow inside the robot
	 */
	private boolean displayForce = false;
	/**
	 * True means the colored balls will be displayed inside the robot
	 */
	private boolean displayMarks = true;
	/**
	 * True means the colored balls will be displayed inside the robot
	 */
	private boolean displayName = false;

	public String getId() {
		return id;
	}

	public enum Team {
		A("A"), B("B");
		private String value;

		Team(String str) {
			value = str;
		}

		public String toString() {
			return value;
		}
	}

	private Team team;

	public Robot(Point position, Team team) {
		this.setAngle(0);
		this.setRotationForce(0);
		this.setRotationVelocity(0);
		this.setMass(5);
		this.setForce(new Vector(0, 0));
		this.setVelocity(new Vector(0, 0));
		this.setPosition(position);
		// this.setId(id);
		// this.setFrente(getPosition()+, y);
		this.team = team;

	}

	public Robot(double x, double y, Team team, double mass, double radius,
			String id) {
		this(new Point(x, y), team);
		this.setMass(mass);
		this.setRadius(radius);
		this.setId(id);

	}

	@Override
	public void draw(GC gc) {
		Color old = gc.getBackground();
		Color backgroundColor = SWTResourceManager.getColor(0, 0, 0);
		gc.setBackground(backgroundColor);
		gc.fillOval(realx(position.getX() - radius), realy(position.getY()
				+ radius), scalex(radius * 2), scaley(radius * 2));
		drawMarks(gc);
		drawDribbler(gc);
		drawKicker(gc);

		// UI status stuff
		drawName(gc);
		drawForce(gc);
		drawSelection(gc);

		gc.setBackground(old);

	}

	private Point dribblerPosition1() {
		Point dribblerPosition1 = new Point(radius, -3).rotate(-this.angle)
				.sum(this.getPosition());

		return dribblerPosition1;
	}

	private Point dribblerPosition2() {
		Point dribblerPosition2 = new Point(radius, 3).rotate(-this.angle).sum(
				this.getPosition());
		return dribblerPosition2;
	}

	/**
	 * Draws a red line that represents the dribbler turned on
	 * 
	 * @param gc
	 */
	private void drawDribbler(GC gc) {
		if (isDribbling()) {
			Point dribblerPosition1 = dribblerPosition1();
			Point dribblerPosition2 = dribblerPosition2();

			Color old = gc.getForeground();
			Color color = SWTResourceManager.getColor(255, 0, 0);
			gc.setForeground(color);
			gc.drawLine(realx(dribblerPosition1.getX()),
					realy(dribblerPosition1.getY()), realx(dribblerPosition2
							.getX()), realy(dribblerPosition2.getY()));
			gc.setForeground(old);
		}
	}

	private Point kickerPosition1() {
		Point kickerPosition1 = new Point(radius, -3).rotate(-this.angle).sum(
				this.getPosition());
		return kickerPosition1;
	}

	private Point kickerPosition2() {
		Point kickerPosition2 = new Point(radius, 3).rotate(-this.angle).sum(
				this.getPosition());
		return kickerPosition2;
	}

	/**
	 * Draws a Blue line that represents the dribbler turned on
	 * 
	 * @param gc
	 */
	private void drawKicker(GC gc) {
		if (getKicking() > 0) {
			Point kickerPosition1 = kickerPosition1();
			Point kickerPosition2 = kickerPosition2();

			Color old = gc.getForeground();
			Color color = SWTResourceManager.getColor(0, 0, 255);
			gc.setForeground(color);
			gc.drawLine(realx(kickerPosition1.getX()), realy(kickerPosition1
					.getY()), realx(kickerPosition2.getX()),
					realy(kickerPosition2.getY()));
			gc.setForeground(old);
		}
	}

	/**
	 * Draws yellow halo around object
	 * 
	 * @param gc
	 */
	private void drawSelection(GC gc) {
		if (isSelected()) {
			Color old = gc.getForeground();
			Color color = SWTResourceManager.getColor(200, 200, 0);
			gc.setForeground(color);
			gc.drawOval(realx(position.getX() - radius), realy(position.getY()
					+ radius), scalex(radius * 2), scaley(radius * 2));
			gc.setForeground(old);
		}
	}

	private void drawForce(GC gc) {
		if (isDisplayForce()) {
			double x1 = getPosition().getX();
			double y1 = getPosition().getY();
			double x2 = getPosition().getX() + getVelocity().getX();
			double y2 = getPosition().getY() + getVelocity().getY();
			Color old = gc.getForeground();
			Color foregroundColor = SWTResourceManager.getColor(0, 255, 55);
			gc.setForeground(foregroundColor);
			gc.drawLine(realx(x1), realy(y1), realx(x2), realy(y2));
			gc.setForeground(old);
		}

	}

	private void drawName(GC gc) {
		if (isDisplayName()) {
			gc.drawString(getId(), realx(getPosition().getX() - getRadius()),
					realy(getPosition().getY() - getRadius()), true);
		}
	}

	private void drawMarks(GC gc) {
		if (isDisplayMarks()) {
			Color centerColor = Team.A.equals(team) ? SWTResourceManager
					.getColor(0, 0, 200) : SWTResourceManager.getColor(200,
					200, 0);
			drawSpot(gc, new Vector(0, 0), centerColor);

			if (Team.A.equals(team)) {
				Color foregroundColor = SWTResourceManager
						.getColor(255, 0, 128);
				drawSpot(gc, new Vector(5.5, 0), foregroundColor);

				Color cian = SWTResourceManager.getColor(128, 255, 255);
				Color lt_green = SWTResourceManager.getColor(128, 255, 0);

				switch (getIndex()) {
				case 1:
					drawSpot(gc, new Vector(-5.5, 0), cian);
					break;
				case 2:
					drawSpot(gc, new Vector(-5.5, 0), lt_green);
					break;
				case 3:
					drawSpot(gc, new Vector(0, -5.5), cian);
					break;
				case 4:
					drawSpot(gc, new Vector(0, -5.5), lt_green);
					break;
				}

			}
		}
	}

	private void drawSpot(GC gc, Vector spotPositionAbsolute, Color color) {

		// Rotate the spot to the actual angle
		Vector spotPosition = spotPositionAbsolute.rotate(this.angle);

		Color old = gc.getBackground();
		gc.setBackground(color);
		gc.fillOval(realx(position.getX() + spotPosition.getX() - SPOT_SIZE),
				realy(position.getY() - spotPosition.getY() + SPOT_SIZE),
				scalex(SPOT_SIZE * 2), scaley(SPOT_SIZE * 2));
		gc.setBackground(old);
	}

	@Override
	public int scalex(double x) {
		return field.scalex(x);
	}

	@Override
	public int scaley(double y) {
		return field.scaley(y);
	}

	@Override
	public int realx(double x) {
		return field.realx(x);
	}

	@Override
	public int realy(double y) {
		return field.realy(y);
	}

	@Override
	public double getRadius() {
		return radius;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setFront(int x, int y) {
		this.front.setX(x);
		this.front.setY(y);
	}

	public Vector getFront() {
		return this.front;

	}

	// seta índice do robo
	public void setId(String newId) {
		this.id = newId;
	}

	// retorna índice do robô

	public int getIndex() {

		return id.charAt(id.length() - 1) - '0';
	}

	public boolean isDisplayForce() {
		return displayForce;
	}

	public void setDisplayForce(boolean displayForce) {
		this.displayForce = displayForce;
	}

	public boolean isDisplayMarks() {
		return displayMarks;
	}

	public void setDisplayMarks(boolean displayMarks) {
		this.displayMarks = displayMarks;
	}

	public boolean isDisplayName() {
		return displayName;
	}

	public void setDisplayName(boolean displayName) {
		this.displayName = displayName;
	}

	public void setTargetVelocity(Vector targetVelocity) {
		if (targetVelocity.module() > ROBOT_MAX_VELOCITY) {
			targetVelocity = targetVelocity.normalize().multiply(ROBOT_MAX_VELOCITY);
		}
		this.targetVelocity = targetVelocity;
	}

	public void setDribbling(boolean dribbling) {
		this.dribbling = dribbling;
	}

	public boolean isDribbling() {
		return dribbling;
	}

	public void setKicking(double strength) {
		this.kicking = strength <= 1d ? strength : 1d;
		this.kicking = kicking >= 0d ? kicking : 0d;  
	}

	public double getKicking() {
		return kicking;
	}

	/**
	 * If the dribbler is on, retain the ball
	 * 
	 * @param movingElement
	 */
	public void dribbleBall(Ball ball) {
		if (isDribbling() && getKicking() == 0d) {
			Point dribblerPosition1 = dribblerPosition1();
			Point dribblerPosition2 = dribblerPosition2();
			Wall w = new Wall(dribblerPosition1.getX(), dribblerPosition1
					.getY(), dribblerPosition2.getX(),
					dribblerPosition2.getY(), CollisionSide.NORMAL);

			// Verify the ball is within the bounds of the dribbler
			Point projection = w.perpendicularProjection(ball.position);
			Point p1 = new Point(w.getX0(), w.getY0());
			Point p2 = new Point(w.getX1(), w.getY1());
			double dp1 = projection.distanceFrom(p1);
			double dp2 = projection.distanceFrom(p2);
			double len = p2.distanceFrom(p1);
			if (dp1 < len && dp2 < len) {
				double distanceFromBall = projection.subtract(ball.position)
						.module();
				distanceFromBall -= ball.getRadius();
				//Set a spin effect to the ball
				if (distanceFromBall > 0.8 && distanceFromBall < 3) {
					Vector direction = new Vector(ball.position, projection);
					double spinForce = 0d;
					try {
						//If the ball is in contact with the robot it will just spin in false.
						//On the other hand, if it is far, it will speed up in the direction of the robot.
						spinForce = MathUtils.normalize(.8, 3, distanceFromBall);
					} catch (Exception e) {
						logger.error(e);
					}
					direction = direction.normalize().multiply(7d * spinForce);
					ball.getVelocity().sum(direction);
				}				
			}

		}
	}

	/**
	 * Trigger the kicker. Once it triggers it returns to resting position
	 * 
	 * @param movingElement
	 */
	public void kickBall(Ball ball) {
		if (getKicking() > 0) {
			Point kickerPosition1 = kickerPosition1();
			Point kickerPosition2 = kickerPosition2();
			Wall w = new Wall(kickerPosition1.getX(), kickerPosition1.getY(),
					kickerPosition2.getX(), kickerPosition2.getY(),
					CollisionSide.NORMAL);

			// Verify the ball is within the bounds of the dribbler
			// Verify the ball is within the bounds of the dribbler
			Point projection = w.perpendicularProjection(ball.position);
			Point p1 = new Point(w.getX0(), w.getY0());
			Point p2 = new Point(w.getX1(), w.getY1());
			double dp1 = projection.distanceFrom(p1);
			double dp2 = projection.distanceFrom(p2);
			double len = p2.distanceFrom(p1);
			if (dp1 < len && dp2 < len) {
				double distanceFromBall = projection.subtract(ball.position)
						.module();
				if (distanceFromBall < 5) {
					Vector direction = new Vector(projection, ball.position);
					direction = direction.normalize().multiply(GameProperties.getIntValue("robot.kicker.velocity.max") * 100d * getKicking());
					ball.setVelocity(ball.velocity.sum(direction));
					logger.debug("Kicking");
				}
			}

			kicking = 0d;
		}
	}

	/**
	 * This is the robot specific calculation for the position. It is different
	 * because it needs to handle the robot commands for velocity. The key
	 * variable here is the targetVelocity.
	 */
	@Override
	public void calculatePosition(double timeElapsed) {

		// Initializes force to 0
		force = new Vector(0, 0);

		// If the desired velocity is still not achieved, increase the motor
		// power.
		// The inertia of previous commands will be dissipated thru the friction
		// force.
		if (velocity.getX() > targetVelocity.getX())
			force.setX(-ROBOT_MAX_FORCE);
		if (velocity.getX() < targetVelocity.getX())
			force.setX(ROBOT_MAX_FORCE);
		if (velocity.getY() > targetVelocity.getY())
			force.setY(-ROBOT_MAX_FORCE);
		if (velocity.getY() < targetVelocity.getY())
			force.setY(ROBOT_MAX_FORCE);

		super.calculatePosition(timeElapsed);
	}
}
