package net.app;

public class Point {
	public float x, y;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Point add(Point p) {
		return new Point(this.x + p.x, this.y + p.y);
	}
	
	public Point sub(Point p) {
		return new Point(this.x - p.x, this.y - p.y);
	}
	
	public Point scale(float s) {
		return new Point(this.x *s, this.y *s);
	}
	
	public float normSQ() {
		return x*x + y*y;
	}
	
	public float norm( ) {
		return (float) Math.sqrt(normSQ());
	}
	
	public Point normalize() {
		return this.scale(1.0f/norm());
	}
	
	public Point ortho() {
		return new Point(-this.y, this.x);
	}
}

