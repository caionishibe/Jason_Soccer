package br.ufrgs.f180.elements;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.GC;

import br.ufrgs.f180.math.Point;
import br.ufrgs.f180.math.Vector;

public abstract class MovingElement implements VisualElement {
	private static Logger logger = Logger.getLogger(MovingElement.class);

	protected GameField field;
	protected Point position = new Point();
	protected Vector velocity = new Vector();
	protected Vector force = new Vector();
	protected double mass;
	protected double angle;
	protected double rotationVelocity;
	
	protected boolean dragging;
	protected boolean selected;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isDragging() {
		return dragging;
	}

	public double getRotationVelocity() {
		return rotationVelocity;
	}

	public void setRotationVelocity(double rotationVelocity) {
		this.rotationVelocity = rotationVelocity;
	}

	protected double rotationForce;

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getRotationForce() {
		return rotationForce;
	}

	public void setRotationForce(double rotationForce) {
		this.rotationForce = rotationForce;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Vector getVelocity() {
		return velocity;
	}

	protected void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	public Vector getAcceleration() {
		// a = F / m
		Vector v = getField().getFriction(this).sum(force);
		return v.divide(mass);
	}

	public double getRotationAcceleration() {
		// a = F / m
		return rotationForce / mass;
	}

	public void setForce(Vector force) {
		this.force = force;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public GameField getField() {
		return field;
	}

	public void setField(GameField field) {
		this.field = field;
	}

	/**
	 * Calculates the position of the element after an ammount of milliseconds are passed.
	 */
	public void calculatePosition(double timeElapsed) {
		if(isDragging()){
			this.position = field.getMousePosition();
		}
		
		// Calculates the velocity
		velocity.setX(velocity.getX()
				+ (getAcceleration().getX() * timeElapsed));
		velocity.setY(velocity.getY()
				+ (getAcceleration().getY() * timeElapsed));
		
		// updates the position
		position.setX(position.getX() + velocity.getX() * timeElapsed);
		position.setY(position.getY() + velocity.getY() * timeElapsed);
		
		//Calculate the new angle. Assure it is between 0 and 2 PI by discounting the full circles
		rotationVelocity = rotationVelocity + getRotationAcceleration() * timeElapsed;
		angle = angle + rotationVelocity * timeElapsed ;
		double circles = (int)(angle / (2 * Math.PI));
		angle = angle - (circles * 2 * Math.PI);
	}

	/**
	 *   This is one of the most important methods in this class. It calculates the force distribution among 
	 * colliding elements. 
	 * @param element the element that will be checked for collision.
	 */
	public void calculateCollision(MovingElement element) {

		if (this.collide(element)) {
			// Collision detected.. distributing the velocity

			// X
			double m1 = this.getMass();
			Vector v1 = this.getVelocity();
			double m2 = element.getMass();
			Vector v2 = element.getVelocity();
			Vector[] fv1 = calculateFinalVelocity(m1, m2, v1, v2, position, element.position);
			//Make the collision not completelly elastic 
			this.setVelocity(fv1[0].multiply(0.9));
			element.setVelocity(fv1[1].multiply(0.9));

		
			// calcula a distância entre o centros dos 2 elementos
			Vector dist = new Vector(this.getPosition(), element.getPosition());
			// caso a distância seja menor do que a soma dos raios ajusta as posicoes de forma a evitar que 
			// os objetos se soldem 
			double diff = (this.getRadius() + element.getRadius()) - dist.module(); 
			if (diff > 0) {
				//Divides the difference between both elements and translate them
				Vector edist = dist.normalize().multiply(diff / 2);
				element.setPosition(element.getPosition().sum(edist));
				Vector tdist = edist.rotate(Math.PI);
				this.setPosition(this.getPosition().sum(tdist));
			}
		}
	}

	/**
	 *   This method simulates a collision between two bodies and distributes the velocity relative to their mass.
	 * @param m1
	 * @param m2
	 * @param v1
	 * @param v2
	 * @param p1
	 * @param p2
	 * @return
	 */
	private Vector[] calculateFinalVelocity(double m1, double m2, Vector v1,
			Vector v2, Point p1, Point p2) {
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		double vx1 = v1.getX();
		double vy1 = v1.getY();
		double vx2 = v2.getX();
		double vy2 = v2.getY();

		double m21, dvx2, a, x21, y21, vx21, vy21, fy21, sign;

		m21 = m2 / m1; 
		x21 = x2 - x1;
		y21 = y2 - y1;
		vx21 = vx2 - vx1;
		vy21 = vy2 - vy1;

		// *** I have inserted the following statements to avoid a zero divide;
		// (for single precision calculations,
		// 1.0E-12 should be replaced by a larger value). **************

		fy21 = 1.0E-12 * Math.abs(y21);
		if (Math.abs(x21) < fy21) {
			if (x21 < 0) {
				sign = -1;
			} else {
				sign = 1;
			}
			x21 = fy21 * sign;
		}

		// *** update velocities ***
		a = y21 / x21;
		dvx2 = -2 * (vx21 + a * vy21) / ((1 + a * a) * (1 + m21));
		vx2 = vx2 + dvx2;
		vy2 = vy2 + a * dvx2;
		vx1 = vx1 - m21 * dvx2;
		vy1 = vy1 - a * m21 * dvx2;

		return new Vector[] {new Vector(vx1, vy1), new Vector(vx2, vy2)};
	}

	/**
	 * Field collisions simply invert the velocity vector in the axis the
	 * collision occurred
	 * @param walls2 
	 * 
	 * @param field
	 */
	public void calculateCollisionWithWalls(List<Wall> walls) {
		for (Wall wall : walls) {

			//Gets the perpendicular projection of the point into the wall
			Point projection = wall.perpendicularProjection(this.position);
			//Verify if it is possible to collide
			// Verify the ball is within the bounds of the dribbler
			Point p1 = new Point(wall.getX0(), wall.getY0());
			Point p2 = new Point(wall.getX1(), wall.getY1());
			double dp1 = projection.distanceFrom(p1); 
			double dp2 = projection.distanceFrom(p2);
			double len = p2.distanceFrom(p1);
			if (dp1 < len && dp2 < len) {
				WallCollisionPoint e = new WallCollisionPoint(projection);
				calculateCollision(e);				
			}
			
			//Check the corners
			WallCollisionPoint e = new WallCollisionPoint(new Point(wall.getX0(), wall.getY0()));
			calculateCollision(e);				
			e = new WallCollisionPoint(new Point(wall.getX1(), wall.getY1()));
			calculateCollision(e);				
			
		}
	}

	/**
	 * Rotates the element a given angle.
	 * @param angle
	 */
	public void rotate(double angle) {
		double rotationMatrix[][] = { { Math.cos(angle), -Math.sin(angle) },
				{ Math.sin(angle), Math.cos(angle) } };

		double vx = (velocity.getX() * rotationMatrix[0][0])
				+ (velocity.getY() * rotationMatrix[0][1]);
		double vy = (velocity.getX() * rotationMatrix[1][0])
				+ (velocity.getY() * rotationMatrix[1][1]);
		velocity = new Vector(vx, vy);

		double fx = (force.getX() * rotationMatrix[0][0])
				+ (force.getY() * rotationMatrix[0][1]);
		double fy = (force.getX() * rotationMatrix[1][0])
				+ (force.getY() * rotationMatrix[1][1]);
		force = new Vector(fx, fy);
	}

	public abstract void draw(GC gc);

	public abstract double getRadius();

	public Vector getForce() {
		return force;
	}

	/**
	 * Check if two elements collide.
	 * @param element
	 * @return
	 */
	public boolean collide(MovingElement element) {
		double x1 = this.getPosition().getX();
		double y1 = this.getPosition().getY();
		double x2 = element.getPosition().getX();
		double y2 = element.getPosition().getY();
		double distance = this.getRadius() + element.getRadius();
		Vector vdistance = new Vector(x2 - x1, y2 - y1);

		return vdistance.module() < distance;
	}

	/**
	 * Drag and drop behavior.
	 * Dragging will make the element to stop moving.
	 */
	public synchronized void drag() {
		if(Math.abs(field.getMousePosition().getX() - this.getPosition().getX()) <= this.getRadius()){
			if(Math.abs(field.getMousePosition().getY() - this.getPosition().getY()) <= this.getRadius()){
				logger.debug("Dragging.");
				velocity = new Vector(0, 0);
				force = new Vector(0, 0);
				dragging = true;
				rotationVelocity = 0;
				rotationForce = 0;
				position = field.getMousePosition();
			}
		}
	}

	/**
	 * Drag and drop behavior.
	 * Dragging will make the element to stop moving.
	 */
	public synchronized void drop() {
		if(isDragging()) {
			logger.debug("Dropping");
		}
		dragging = false;
	}

	/**
	 * Selects the element displaying a yellow halo around it
	 */
	public void select() {
		selected = false;
		if(Math.abs(field.getMousePosition().getX() - this.getPosition().getX()) <= this.getRadius()){
			if(Math.abs(field.getMousePosition().getY() - this.getPosition().getY()) <= this.getRadius()){
				logger.debug("Selected.");
				selected = true;
			}
		}
	}
	
}
