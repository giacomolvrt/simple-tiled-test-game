package game.src;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player {
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private int width;
	private int height;
	
	private boolean left;
	private boolean right;
	private boolean jumping;
	private boolean falling;
	
	private double moveSpeed;
	private double maxSpeed;
	private double maxFallingSpeed;
	private double stopSpeed;
	private double jumpStart;
	private double gravity;
	
	private TileMap tileMap;
	
	private boolean topLeft;
	private boolean topRight;
	private boolean bottomLeft;
	private boolean bottomRight;
	
	public Player(TileMap tm) {
		
		tileMap = tm;
		width = 20;
		height = 20;
		//moveSpeed = 0.6;
		moveSpeed = 0.2;
		//maxSpeed = 4.2;
		maxSpeed = 6;
		maxFallingSpeed = 12;
		stopSpeed = 0.30;
		jumpStart = -11.0;
		gravity = 0.64;
	}
	
	public void setx(int i) { x = i; }
	
	public void sety(int i) { y = i; }
	
	public void setLeft(boolean b) { left = b; }
	
	public void setRight(boolean b) { right = b; }
	
	public void setJumping(boolean b) {
		if(!falling) {
			jumping = true;
		}
	}
	
	public void update() {
		//determine next position
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
			System.out.println("PLAYER:LEFT "+dx);
		}
		else if (right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
			System.out.println("PLAYER:RIGHT "+dx);
		}
		else {                  //se non sto andando a destra e sinistra rallento...
			if(dx > 0) {		//mentre vado a destra...
				dx -= stopSpeed;//rallento...
				if(dx < 0) {	//e se la mia direzione cambia...
					dx = 0;		//mi fermo a zero
				}
				System.out.println("PLAYER STOP1 "+dx);
			}
			else if (dx < 0) {
				dx += stopSpeed;//mentre vado a sinistra...
				if(dx > 0) {	//rallento...
					dx = 0;		//e se la mia direzione cambia...
				}				//mi fermo a zero
				System.out.println("PLAYER STOP2 "+dx);
			}
		}
		
		if(jumping) {
			System.out.println("PLAYER JUMPING");
			dy = jumpStart;
			falling = true;
			jumping = false;
		}
		
		if(falling) {
			System.out.println("PLAYER FALLING");
			dy += gravity;
			if(dy > maxFallingSpeed) {
				dy = maxFallingSpeed;
			}
		}
		else {
			dy = 0;
		}
		
		// check collisions
		int currCol = tileMap.getColTile((int) x);
		int currRow = tileMap.getRowTile((int) y);
		
		double tox = x + dx;
		double toy = y + dy;
		
		double tempx = x;
		double tempy = y;
		
		calculateCorners(x, toy);
		if(dy < 0) {                  //mi sto muovendo in alto (y negative)
			if(topLeft || topRight) { //controllo gli angoli in alto
				dy = 0;               //mi fermo
				tempy = currRow * tileMap.getTileSize() + height / 2;
			}
			else {
				tempy += dy;			//siampo liberi di muoverci nella direzione y
			}
			
		}
		if (dy > 0) {
			if(bottomLeft || bottomRight) { //controllo gli angoli in basso
				dy = 0;                     //mi fermo
				falling = false;
				tempy = (currRow + 1) * tileMap.getTileSize() - height / 2;
			}
			else {
				tempy += dy;			//siampo liberi di muoverci nella direzione y
			}
		}
		
		calculateCorners(tox, y);
		if(dx < 0) {                  //mi sto muovendo a sinistra
			if(topLeft || bottomLeft) { //controllo gli angoli a sinistra
				dx = 0;               //mi fermo
				tempx = currCol * tileMap.getTileSize() + width / 2;
			}
			else {
				tempx += dx;			//siampo liberi di muoverci nella direzione y
			}
			
		}
		if (dx > 0) {
			if(topRight || bottomRight) { //controllo gli angoli a destra
				dx = 0;                     //mi fermo
				tempx = (currCol + 1) * tileMap.getTileSize() - width / 2;
			}
			else {
				tempx += dx;			//siampo liberi di muoverci nella direzione y
			}
		}
		
		if(!falling) {
			calculateCorners(x, y + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
		
		x = tempx;
		y = tempy;
		
		//move the map
		
		tileMap.setx((int)(GamePanel.WIDTH/2 - x));
		tileMap.sety((int)(GamePanel.HEIGHT/2 - y));
	}
	
	private void calculateCorners(double x, double y) {
		
		int leftTile = tileMap.getColTile((int) (x - width / 2));
		int rightTile = tileMap.getColTile((int) (x + width / 2) - 1);
		int topTile = tileMap.getRowTile((int) (y - height / 2));
		int bottomTile = tileMap.getRowTile((int) (y + height / 2) - 1);
		topLeft = tileMap.getTile(topTile, leftTile) == 0;
		topRight = tileMap.getTile(topTile, rightTile) == 0;
		bottomLeft = tileMap.getTile(bottomTile, leftTile) == 0;
		bottomRight = tileMap.getTile(bottomTile, rightTile) == 0;
	}
	
	public void draw(Graphics2D g) {
		
		int tx = tileMap.getx();
		int ty = tileMap.gety();
		
		g.setColor(Color.RED);
		g.fillRect(
				(int) (tx + x - width / 2),
				(int) (ty + y - height /2),
				width,
				height
		);
	}

}
