package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class BrickMap {
	public int map[][];
	public int brickWidth;
	public int brickHeight;
	public int brickPosX;
	public int brickPosY;
	
	public BrickMap(int row, int col, int passedWIDTH, int passedHEIGHT) {
		map = new int[row][col];
		for(int i = 0; i < map.length;i++) {
			for (int j = 0; j< map[0].length;j++ ) {
				map[i][j] = 1;
			}
		}
		brickWidth = passedWIDTH; 
		brickHeight= passedHEIGHT;
	}
	
	public void draw(Graphics2D g) {
		for(int i = 0; i < map.length;i++) {
			for (int j = 0; j< map[0].length;j++ ) {
				if(map[i][j]>0) {
					g.setColor(Color.gray);
					g.fillRect(j*brickWidth+brickWidth, i*brickHeight+brickHeight, brickWidth, brickHeight);
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.white);
					g.drawRect(j*brickWidth+brickWidth, i*brickHeight+brickHeight, brickWidth, brickHeight);
				}
			}
		}
	}
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}
}
