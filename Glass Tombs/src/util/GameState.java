package util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import engine.Main;

public class GameState {

	public Main main;
	
	// Dimensions
	public int tileRes = Main.tileRes;
	public int guiBarSize = Main.guiBarSize;
	public static Dimension localMapSize = Main.localMapSize;

	public GameState(Main main) {
		this.main = main;
	}

	public void update() {

	}

	public void render(Graphics2D g) {
		
	}

	//// KeyListener
	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

	//// MouseListener
	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	//// MouseWheelListener
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

}
