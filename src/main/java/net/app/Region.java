package net.app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Region {
	private List<Node> nodes;
	private Color back, border;
	private String name;
	
	public Region(Color back, Color border, String name) {
		this.back = back;
		this.border = border;
		this.nodes = new ArrayList<Node>();
		this.name = name;
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void render(Renderer renderer) {
		Node temp = nodes.get(0);
		float xmax, xmin, ymax, ymin;
		xmin = xmax = temp.getPos().x;
		ymin = ymax = temp.getPos().y;
		for(Node node:nodes) {
			xmin = Math.min(xmin, node.getPos().x);
			xmax = Math.max(xmax, node.getPos().x);
			ymin = Math.min(ymin, node.getPos().y);
			ymax = Math.max(ymax, node.getPos().y);
		}
		renderer.fillRect((int) xmin-30, (int) ymin-30, (int) (xmax-xmin)+60, (int) (ymax-ymin)+60, back);
		renderer.drawCenteredText(name, (int) xmax,(int)  ymax + 45, Color.BLACK);
		renderer.drawRect((int) xmin-30, (int) ymin-30, (int) (xmax-xmin)+60, (int) (ymax-ymin)+60, border);
	}
}


