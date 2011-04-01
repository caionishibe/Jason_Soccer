package br.ufrgs.f180.math;

public class Point {
	protected Double x;
	protected Double y;

	public Point() {
		this.x = Double.valueOf(0);
		this.y = Double.valueOf(0);
	}
	
	public Point(Point p){
		this.x = Double.isNaN(p.getX()) ? 0 : p.getX();
		this.y = Double.isNaN(p.getY()) ? 0 : p.getY();
	}
	
	public Point(double x, double y){
		this.x = Double.isNaN(x) ? 0 : x;
		this.y = Double.isNaN(y) ? 0 : y;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public Point sum(Point p){
		return new Point(p.getX() + x, p.getY() + y);
	}

	public Point rotate(double angle){

		Matrix rot = new Matrix(Math.cos(angle), -Math.sin(angle), Math.sin(angle), Math.cos(angle));
		
		return rot.multiply(this);
		
	}

	protected double hipotenuse(){
		return Math.sqrt((x*x) + (y*y));
	}

	public Vector subtract(Point vector) {
		return new Vector(x - vector.getX(), y - vector.getY());
	}
	
	/**
	 * Calculates the 2D distance between a point and a line
	 * @param x
	 * @param y
	 * @return
	 */
	public double distanceFrom(Point point) {
		return point.subtract(this).module();
	}
	
	@Override
	public String toString() {
		return "(" + String.format("%.1f", x) + ", " + String.format("%.1f", y) + ")";
	}
}
