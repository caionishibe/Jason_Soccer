package br.ufrgs.f180.api.model;

import br.ufrgs.f180.math.Point;
import br.ufrgs.f180.math.Vector;

/**
 * This class contains the published information about game elements. All moving elements extends from it and
 * allow the client to obtain some physical data from the elements relative to the game field. 
 * @author Gabriel
 *
 */
public class ElementInformation {
	protected Double timestamp;
	protected Point position;
	protected Double angle;
	protected Vector velocity;
	protected Double radius;

	/**
	 * @return the radius of the bounding circle of the element.
	 */
	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}


	/**
	 * @return the velocity of the element within the game field.
	 */
	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return the position of the element within the game field.
	 */
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * @return the relative angle of the element within the game field. Where 0 means it is aligned with the horizontal axis. 
	 * Angle increases counterclockwise. 
	 */
	public Double getAngle() {
		return angle;
	}

	public void setAngle(Double angle) {
		this.angle = angle;
	}

	public Double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Double timestamp) {
		this.timestamp = timestamp;
	}
}
