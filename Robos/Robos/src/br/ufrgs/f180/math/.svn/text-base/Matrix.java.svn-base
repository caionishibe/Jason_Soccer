package br.ufrgs.f180.math;

public class Matrix {
	private double d00;
	private double d01;
	private double d10;
	private double d11;

	public Matrix(double d00, double d01, double d10, double d11) {
		this.d00 = d00;
		this.d01 = d01;
		this.d10 = d10;
		this.d11 = d11;
	}
	
	public Vector multiply(Point v){
		double x = v.getX();
		double y = v.getY();
		
	    double x1 = x * d00 + y * d10;
	    double y1 = x * d01 + y * d11;
	    return new Vector(x1, y1);
	}
}
