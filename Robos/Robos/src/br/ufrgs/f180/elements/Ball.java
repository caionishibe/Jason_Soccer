package br.ufrgs.f180.elements;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;


import br.ufrgs.f180.math.Point;
import br.ufrgs.f180.math.Vector;

import com.cloudgarden.resource.SWTResourceManager;

public class Ball extends MovingElement {
	private double radius;
	
	public Ball(Point position){
		this.setMass(1);
		this.setForce(new Vector(0, 0));
		this.setVelocity(new Vector(0, 0));
		this.setPosition(position);
	}

	public Ball(double x, double y, double mass, double radius){
		this(new Point(x, y));
		this.setMass(mass);
		this.setRadius(radius);
	}

	@Override
	public void draw(GC gc) {
		Color old = gc.getForeground();
		Color c = SWTResourceManager.getColor(255, 128, 0);
		gc.setBackground(c);
		gc.fillOval(realx(position.getX() - radius), realy(position.getY() + radius) , scalex(radius * 2), scaley(radius * 2) );
		gc.setBackground(old);
		//int y1 = realy(position.getY());
		//int y2 = getField().relativeRealy(realy(position.getY() + velocity.getY()));
		//gc.drawLine(realx(position.getX()), y1, realx(position.getX() + velocity.getX()), y2);
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
	public int scalex(double x) {
		return field.scalex(x);
	}

	@Override
	public int scaley(double y) {
		return field.scaley(y);
	}
	
	@Override
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}
