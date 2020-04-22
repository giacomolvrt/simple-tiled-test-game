package game.src;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;

	private Thread thread;
	private boolean running;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private int FPS = 30;
	private int targetTime = 1000 / FPS;
	
	private TileMap tileMap;
	private Player player;
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}

	public void run() {
		
		init();
		
		long startTime;
		long urdTime;
		long waitTime;
		
		while(running) {
			startTime = System.nanoTime();
			
			update();
			render();
			draw();
			
			urdTime = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - urdTime;

			try {
				Thread.sleep(waitTime);
			}
			catch(Exception e) {
				System.out.println("Eccezione!");
			}
		}
	}	

	private void init() {
		
		running = true;
		
		image = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		tileMap = new TileMap("testmap.txt", 32);
		player = new Player(tileMap);
		player.setx(50);
		player.sety(50);
	}
	
	////////////////////////////////////////////////
	
	private void update() {

		tileMap.update();
		player.update();
	}
	
	private void render() {

		tileMap.draw(g);
		player.draw(g);
		
	}
	
	private void draw() {
		Graphics g2 = getGraphics();
		g2.drawImage(image,  0, 0, null);
		g2.dispose();		
	}

	public void keyTyped(KeyEvent key) { }

	public void keyPressed(KeyEvent key) {
		
		int code = key.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT) {
			player.setLeft(true);
			System.out.println("LEFT");
		}
		if(code == KeyEvent.VK_RIGHT) {
			player.setRight(true);
			System.out.println("RIGHT");
		}
		if(code == KeyEvent.VK_W) {
			player.setJumping(true);
			System.out.println("JUMP");
		}
		
	}

	public void keyReleased(KeyEvent key) {
		
		int code = key.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT) {
			player.setLeft(false);
			System.out.println("RILASCIO_LEFT");
		}
		if(code == KeyEvent.VK_RIGHT) {
			player.setRight(false);
			System.out.println("RILASCIO_RIGHT");
		}
	}
	
	
	
	
	
}
