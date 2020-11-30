package states;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.sound.sampled.FloatControl;

import engine.Main;
import util.GameState;
import util.Style;
import util.Textures;

public class AGlassTitleScreen extends GameState{

	public AGlassTitleScreen(Main main) {
		super(main);
		// Reset main variables
		Main.storyStage = 0;
		Main.cryptsCompleted = 0;
		Main.playerHealth = Main.maxPlayerHealth;
		Main.money = 0;
		
		// Music 
		Main.currentSong = Main.overworld;
		FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
        volume.setValue(dB);
		Main.currentSong.setMicrosecondPosition(0);
		Main.currentSong.start();
		Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
	}
	
	public String[] menuOptions = {
			"Play", "Exit Game"
	};
	
	public String[] controlHints = {
			"Menu controls:",
			"  - Navigate: [ARROW KEYS]",
			"  - Select:   [Z]",
			"Game controls:",
			"  - Move:     [ARROW KEYS]",
			"  - Attack:   [Z]",
	};
	
	public int selection = 0;
	
	public void render(Graphics2D g) {
		// Background
		g.drawImage(Textures.titleScreen, 0, 0, Main.width, Main.height, main);

		// Title
		g.setColor(Style.darkGray);
		g.setFont(Style.righteous);
		g.setFont(g.getFont().deriveFont((float)(tileRes * 2)));
		int titleWidth = g.getFontMetrics().stringWidth("Glass Tombs");
		g.drawString("Glass Tombs", Main.width / 2 - titleWidth / 2, (int)(tileRes * 3.5));
	
		// Menu options
		g.setFont(Style.russo);
		g.setFont(g.getFont().deriveFont((float)(tileRes * 0.8)));
		for (int i = 0; i < menuOptions.length; i++) {
			int stringWidth = g.getFontMetrics().stringWidth(menuOptions[i]);
			if (i == selection) {
				g.setColor(Style.darkGray);
				stringWidth = g.getFontMetrics().stringWidth("> " + menuOptions[i] + " <");
				g.drawString("> " + menuOptions[i] + " <", Main.width / 2 - stringWidth / 2, Main.height - tileRes * 3 + tileRes * i);
			} else {
				g.setColor(Style.gray);
				g.drawString(menuOptions[i], Main.width / 2 - stringWidth / 2, Main.height - tileRes * 3 + tileRes * i);

			}
		}
		
		// Control hints
		g.setColor(Style.darkGray);
		g.setFont(g.getFont().deriveFont((float)(tileRes * 0.3)));
		int spacing = (int) (tileRes * 0.4);
		for (int i = 0; i < controlHints.length; i++) {
			g.drawString(controlHints[i], tileRes / 4, Main.height + tileRes * -2 + spacing * i);
		}
		
	}
	
	public void makeSelection() {
		switch(selection) {
		case 0:
			startNewGame();
			break;
		case 1:
			System.exit(0);
			break;
		}
	}
	
	public void startNewGame() {
		Main.gsm.introStates.clear();
		Main.gsm.introStates.add(new Intro(main));
		Main.gsm.currentState = Main.gsm.introStates.get(0);
	}
	
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		// Menus
		case KeyEvent.VK_Z:
			makeSelection();
			break;
		// Player controls
		case KeyEvent.VK_UP:
			if (selection < menuOptions.length - 1) {
				selection++;
			} else {
				selection = 0;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (selection >= 1) {
				selection--;
			} else {
				selection = menuOptions.length - 1;
			}
			break;
		}
	}
	
}
