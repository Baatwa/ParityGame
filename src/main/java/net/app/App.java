package net.app;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App implements RenderListener, MouseWheelListener, MouseListener, MouseMotionListener{
	
	private JFrame frame;
	private Renderer renderer;
	private float zoom = 1;
	private Point camera;
	private Point previous;
	private Point mouseInModel;
	private Node selected;
	
	private Graph graph;
	
	
	
	public App() {
		
	}
	
	
	public void init() {
		frame = new JFrame("ParityGame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		renderer = new Renderer();
		renderer.addRenderListener(this);
		frame.add(renderer);
		renderer.addMouseWheelListener(this);
		renderer.addMouseListener(this);
		renderer.addMouseMotionListener(this);
		camera = new Point(0,0);
		
		Random r = new Random();
		
		graph = new Graph();
		for(int i = 0; i<6; i++) {
			graph.addNode(new Node(r.nextInt(500) - 250, r.nextInt(500) - 250, i+"", false));
		}
		
		for(int i = 0; i<6; i++) {
			for(int j = 0; j<6; j++) {
				if(i!=j) {
					graph.addLink(graph.getNodes().get(i), graph.getNodes().get(j));
				}
			}
		}
		
		Region region = new Region(new Color(255,0,0,30), new Color(255,0,0,60), "rouge");
		Region region2 = new Region(new Color(0,0,255,30), new Color(0,0,255,60), "bleu");
		region.addNode(graph.getNodes().get(0));
		region.addNode(graph.getNodes().get(1));
		region.addNode(graph.getNodes().get(2));
		graph.addRegion(region);
		region2.addNode(graph.getNodes().get(3));
		region2.addNode(graph.getNodes().get(4));
		region2.addNode(graph.getNodes().get(5));
		graph.addRegion(region2);
		
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		renderer.init();
		renderer.start();
	}
	
	public Point fromScreenToModel(Point p) {
		return p.sub(new Point(renderer.getWidth()/2, renderer.getHeight()/2)).scale(1.0f/zoom).add(camera);
		
	}
	
	@Override
	public void render(Renderer renderer) {
		renderer.translate(renderer.getWidth()/2, renderer.getHeight()/2);
		renderer.scale(zoom);
		renderer.translate(-camera.x, -camera.y);
		graph.render(renderer, selected);
		
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point temp = fromScreenToModel(new Point(e.getX(),e.getY()));
		zoom *= Math.pow(1.05, -e.getWheelRotation());
		camera = camera.sub(fromScreenToModel(new Point(e.getX(),e.getY())).sub(temp));
	}

	
	public void updateMousePos(int x, int y) {
		mouseInModel = fromScreenToModel(new Point(x, y));
		selected = null;
		for(Node node:graph.getNodes()) {
			if(node.contains(mouseInModel)) {
				selected = node;
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e) && selected == null) {
			camera = camera.sub(fromScreenToModel(new Point(e.getX(),e.getY())).sub(previous));
			
		}
		if(SwingUtilities.isLeftMouseButton(e) && selected != null) {
			selected.setPos(previous.add(mouseInModel));
		}
		updateMousePos(e.getX(), e.getY());
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		updateMousePos(e.getX(), e.getY());
		
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		if(selected != null) {
			previous = selected.getPos().sub(mouseInModel);
		}
		else {
			previous = fromScreenToModel(new Point(e.getX(),e.getY()));
		}
		
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
