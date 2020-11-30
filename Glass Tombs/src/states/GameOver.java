package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.sound.sampled.FloatControl;

import engine.Main;
import util.Textures;

public class GameOver extends TextRoll {

	private String[] introText = {
		"0GAMEOVER",
		"3GAMEOVER",
		"4GAMEOVER",
		"",
		"",
		"",
		"",
		"",
		"",
		"7========",
		"7The Adventurer",
		"",
		"7Deceased,",
		"7Remembered for not much longer.",
		"7========",
		"",
		"",
		"0As tradition dictates, your loved ones planted",
		"0flowers by where you died.",
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	public int dungeonIndex;
	
	public GameOver(Main main) {
		super(main);
		textArray = introText;
		// Music 
		Main.currentSong.stop();
		Main.currentSong = Main.ghost;
		FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
        volume.setValue(dB);
		Main.currentSong.setMicrosecondPosition(0);
		Main.currentSong.start();
		Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
	}
	
	protected void scrollText(Graphics2D g) {
		double scrollLimit = -textMargin - spacingHeight * textArray.length;
		scrollOffset -= scrollStep;
		g.translate(0, scrollOffset);
		
		if (scrollOffset < scrollLimit) {
			if (scrollEndTime == -1) {
				scrollEndTime = System.currentTimeMillis();
			} else {
				long elapsedTime = System.currentTimeMillis() - scrollEndTime;
				// (int)(spacingHeight * textArray.length
				
				// Finally, transition
				if (elapsedTime > titleImageDisplayMillis) {
					triggerTransition();
				}
			}
			
		}
	}
	
	protected void triggerTransition() {
		Main.currentSong.stop();
		Main.gsm.introStates.clear();
		Main.gsm.introStates.add(new AGlassTitleScreen(main));
		Main.gsm.currentState = Main.gsm.introStates.get(0);
	}
	
	
}
