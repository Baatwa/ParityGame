package net.app;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Renderer extends Canvas implements Runnable {
	
	//Thread management
	private Thread thread;
	private boolean running;
	
	//Graphics
	private Graphics2D g;
	private BufferStrategy bs;
	
	
	
	private List<RenderListener> listeners;
	
	public Renderer() {
		super();
		listeners = new ArrayList<RenderListener>();
		
	}
	
	public void addRenderListener(RenderListener listener) {
		listeners.add(listener);
		
		
	}
	
	public void init() {
		createBufferStrategy(3);
	}
	
	private void begin() {
		bs = this.getBufferStrategy();
		g = (Graphics2D) bs.getDrawGraphics();
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}
	
	private void end() {
		bs.show();
		g.dispose();
	}

	@Override
	public void run() {
		init();
		
		double fps = 60.0;
		double period = 1_000_000_000/fps;
		double delta = 0;
		long last = System.nanoTime();
		long now;
		while(running) {
			now = System.nanoTime();
			delta += (now - last)/period;
			if (delta >= 1) {
				delta --;
				begin();
				for(RenderListener listener:listeners) {
					listener.render(this);
				}
				
				end();
			}
			last = now;
			
		}
	}
	
	public synchronized void start() {
		if(running){
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setStroke(float size) {
        g.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 3));
    }
	
	public void translate(float x, float y) {
		g.translate(x, y);
	}
	
	public void scale(float s) {
		g.scale(s, s);
	}
	
	public void fillRect(int x, int y, int width, int height, Color c) {
		g.setColor(c);
		g.fillRect(x, y, width, height);
	}
	
	public void drawRect(int x, int y, int width, int height, Color c) {
		g.setColor(c);
		g.drawRect(x, y, width, height);
	}
	
	public void fillCircle(int x, int y, int size, Color c) {
		g.setColor(c);
		g.fillOval(x - size/2, y - size/2, size, size);
	}
	
	public void drawCenteredText(String s, int x, int y, Color c) {
		FontMetrics m = g.getFontMetrics();
		int width = m.stringWidth(s);
		int height = m.getDescent() + (m.getAscent() + m.getDescent())/2;
		g.setColor(c);
		g.drawString(s, x - width/2, y + height/2);
	}
	
	public void drawLine(Point p1, Point p2, Color c) {
		g.setColor(c);
		g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
	}
	
}
