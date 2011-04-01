package br.ufrgs.f180.math;


public class Vector extends Point implements Cloneable{

	public Vector(Point p) {
		super(p);
	}
	
	public Vector() {
		super();
	}

	public Vector(double x, double y) {
		super(x, y);
	}

	public Vector divide(double value){
		return new Vector(x / value, y / value);
	}
	
	public double getSinDirection(){
		return y / hipotenuse();
	}

	public double getCosDirection(){
		return x / hipotenuse();
	}

	public Vector multiply(Vector vector) {
		return new Vector(this.getX() * vector.getX(), this.getY() * vector.getY());
	}

	public Vector multiply(double value) {
		return new Vector(this.getX() * value, this.getY() * value);
	}
	
	/**
	 * Calculates the normal vector considering that this vector is tangent to the circle
	 * @param center
	 * @return
	 */
	public Vector(Point point1, Point point2){
		super(point2.getX() - point1.getX(), point2.getY() - point1.getY());
	}
	
	public double module(){
		return hipotenuse();
	}
	
	public Vector normalize(){
		return new Vector(getCosDirection(), getSinDirection());
	}
	
	public static double cos(Vector v1, Vector v2){
		double scalarProduct = v1.getX() * v2.getX() + v1.getY() * v2.getY(); 
		double cos = scalarProduct / (v1.module() * v2.module());
		return cos != Double.NaN ? cos : 0;
	}

	public static double sin(double cos){
		double sin = Math.sqrt(1 - cos * cos);
		return sin;
	}
	
	public static double collisionAngle(Vector collision, Vector v1) {
		double sum = Math.PI / 2;
		if(collision.getX() > 0 && collision.getY() > 0){
			//sum *= -1;
		}
		if(collision.getX() < 0 && collision.getY() > 0){
			//sum *= -1;
		}
		
		double angle = Math.acos(Vector.cos(collision, v1));
		return angle + sum;
	}

	public Vector rotate(double angle){

		Matrix rot = new Matrix(Math.cos(angle), -Math.sin(angle), Math.sin(angle), Math.cos(angle));
		
		return rot.multiply(this);
		
	}
	
	public Vector clone(){
		return new Vector(this.getX(), this.getY());
	}

	public Vector subtract(Vector vector) {
		return new Vector(x - vector.getX(), y - vector.getY());
	}

	public Vector sum(Vector p) {
		return new Vector(p.getX() + x, p.getY() + y);
	}
	
	/**
	 * Returns the angle of a vector.
	 * @return
	 */
	public double getAngle(){
		
		double angle = Math.acos(getCosDirection());
		if(getSinDirection() < 0) {
				angle = 2 * Math.PI - angle;
		}
		return angle;
	}

	public Vector inverse(){
		return new Vector(-x, -y);
	}
}
