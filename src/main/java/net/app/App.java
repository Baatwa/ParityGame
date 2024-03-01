package net.app;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App implements RenderListener, MouseWheelListener, MouseListener, MouseMotionListener, KeyListener{
	
	private JFrame frame;
	private Renderer renderer;
	private float zoom = 1;
	private Point camera;
	private Point previous;
	private Point mouseInModel;
	private Node selected;
	private Link selectedLink;
	
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
		renderer.addKeyListener(this);
		camera = new Point(0,0);
		
		Random r = new Random();
		int node_nbr = r.nextInt(10) + 3;
//		int node_nbr = 8;
//		int closest_sq = (int) Math.pow(Math.floor(Math.sqrt(node_nbr)), 2);
		
		//nodes creation
		graph = new Graph();
		for(int i = 0; i< node_nbr/2; i++) {
			graph.addNode(new Node(r.nextInt(500) - 250, r.nextInt(500) - 250, (2*i)+"", false));
			graph.addNode(new Node(r.nextInt(500) - 250, r.nextInt(500) - 250, (2*i+1)+"", true));
		}
		if(node_nbr%2 != 0) {
			graph.addNode(new Node(r.nextInt(500) - 250, r.nextInt(500) - 250, (node_nbr-1)+"", false));
		}		
		
		//links creation
		
		for(int i = 0; i<node_nbr;i++) {
			int target = r.nextInt(node_nbr-1);
			if(target < i) {
				graph.addLink(graph.getNodes().get(i), graph.getNodes().get(target));
			}
			else {
				graph.addLink(graph.getNodes().get(i), graph.getNodes().get(target+1));
			}
		}
		
		int temp = r.nextInt(node_nbr*2);
		for(int i = 0; i<temp; i++) {
			int a = r.nextInt(node_nbr), b = r.nextInt(node_nbr);
			if(a!=b) {
				graph.addLink(graph.getNodes().get(a), graph.getNodes().get(b));
			}
		}
		
		
		//regions creation
		Region region = new Region(new Color(255,0,0,30), new Color(255,0,0,60), "rouge");
		Region region2 = new Region(new Color(0,0,255,30), new Color(0,0,255,60), "bleu");
		for(int i = 0; i<node_nbr/2; i++) {
			region.addNode(graph.getNodes().get(i));
			region2.addNode(graph.getNodes().get(i+node_nbr/2));
		}
		if(node_nbr%2 != 0) {
			region2.addNode(graph.getNodes().get(node_nbr-1));
		}
		graph.addRegion(region);
		graph.addRegion(region2);		
		
		//nodes traversal
		selectedLink = getRandom(getRandom(graph.getNodes()).getOutputs());
		
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
		renderer.setStroke(2);
		graph.render(renderer, selected, selectedLink);
		
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point temp = fromScreenToModel(new Point(e.getX(),e.getY()));
		zoom *= Math.pow(1.05, -e.getWheelRotation());
		camera = camera.sub(fromScreenToModel(new Point(e.getX(),e.getY())).sub(temp));
	}

	
	public void updateMousePos(int x, int y, boolean skip) {
		mouseInModel = fromScreenToModel(new Point(x, y));
		if(skip) {
			return;
		}
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
		updateMousePos(e.getX(), e.getY(), SwingUtilities.isLeftMouseButton(e) && selected != null);
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		updateMousePos(e.getX(), e.getY(),false);
		
		
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
	
	public static <T> T getRandom(Collection<? extends T> c) {
		Random r = new Random();
		return new ArrayList<T>(c).get(r.nextInt(c.size()));
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			selectedLink = getRandom(selectedLink.getEnd().getOutputs());
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
