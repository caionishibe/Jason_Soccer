package br.ufrgs.f180.math;

/**
 * A representation of a circle.
 * 
 * @author Gabriel
 *
 */
public class Circle {

	private Point center;
	private double radius;
	
	public Circle(Point center, double radius) {
		this.center = center; 
		this.radius = radius; 
	}

	public Circle(double x, double y, double radius) {
		this(new Point(x, y), radius); 
	}

	public Circle() {
		this(0, 0, 1); 
	}

	public Point getCenter() {
		return center;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setCenter(Point center) {
		this.center = center;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
}
