package net.app;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Node {
	private Point pos;
	private String name;
	private boolean isSquare;
	private Set<Link> inputs, outputs;


	
	public Node(int x, int y, String name, boolean isSquare) {
		this.pos = new Point(x, y);
		this.name = name;
		this.isSquare = isSquare;
		inputs = new HashSet<Link>();
		outputs = new HashSet<Link>();
		
	}
	
	public void render(Renderer renderer, Node selected) {
		if(isSquare) {
			renderer.fillRect((int) pos.x - 15, (int) pos.y - 15, 30, 30, selected==this? Color.GREEN: Color.RED);
			renderer.fillRect((int) pos.x - 14, (int) pos.y - 14, 28, 28, Color.DARK_GRAY);
		}
		else {
			renderer.fillCircle((int) pos.x, (int) pos.y, 30, selected==this? Color.GREEN: Color.RED);
			renderer.fillCircle((int) pos.x, (int) pos.y, 28, Color.DARK_GRAY);
		}
		renderer.drawCenteredText(name, (int) pos.x, (int) pos.y, Color.WHITE);
		
	}
	
	public boolean contains(Point p) {
		if(isSquare) {
			return pos.x - 15 <= p.x && pos.x + 15 >= p.x && pos.y - 15 <= p.y && pos.y + 15 >= p.y;
		}
		else {
			return Math.pow(p.x - pos.x, 2) + Math.pow(p.y - pos.y, 2) <= 225;
		}
		
	}
	
	public Point getPos() {
		return pos;
	}
	
	public void setPos(Point p) {
		this.pos = p;
	}
	
	
	@Override
	public String toString() {
		return name;
	}
	
	public Set<Link> getInputs(){
		return inputs;
	}
	
	public Set<Link> getOutputs(){
		return outputs;
	}
	
	
	
	
}
