package net.app;

import java.awt.Color;
import java.util.Objects;

public class Link {
	private Node start, end;
	private boolean hasTwin = false;
	
	public Link(Node start, Node end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Link l && start == l.start && end == l.end;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(start, end);
	}
	
	public void setHasTwin(boolean hasTwin) {
		this.hasTwin = hasTwin;
	}
	
	public void render(Renderer renderer) {
		Point vect = end.getPos().sub(start.getPos()).normalize();
		Point vectOrtho = vect.ortho();
		Point lineEnd = end.getPos().sub(vect.scale(30));
		Point lineStart = start.getPos().add(vect.scale(30));
		
		
//		if(hasTwin) {
//			lineEnd = lineEnd.add(vectOrtho.scale(10));
//			lineStart = lineStart.add(vectOrtho.scale(10));
//		}
		renderer.drawLine(lineStart, lineEnd, Color.BLACK);
		renderer.drawLine(lineEnd, lineEnd.sub(vect.add(vectOrtho).scale(10)), Color.BLACK);
		renderer.drawLine(lineEnd, lineEnd.sub(vect.sub(vectOrtho).scale(10)), Color.BLACK);
	}
}
