package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class CHP3Intro extends TextRoll {

	private String[] introText = {
		"7CHAPTER 3",
		"0Take on the glass dungeon.",
		"",
		"",
		"0pg. 1201",
		"0ADVENTURE CATALOG:",
		"7\"When a hero dies, the town builds",
		"7a crypt for them. But its tradition",
		"7that their families plant a flower",
		"7nearby where they died.\""
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	
	public CHP3Intro(Main main) {
		super(main);
		textArray = introText;
		Main.storyStage = 1;
	}
	
	protected void scrollText(Graphics2D g) {
		double scrollLimit = -textMargin - spacingHeight * textArray.length;
		scrollOffset -= scrollStep;
		g.translate(0, scrollOffset);
		// Render title image
		Dimension titleImageSize = new Dimension(tileRes * 3, tileRes * 3);
		g.drawImage(Textures.glassDungeonImage, Main.width / 2 - titleImageSize.width / 2, (int)(textArray.length * spacingHeight + textMargin), titleImageSize.width, titleImageSize.height, main);
		
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
		Main.gsm.currentState = Main.gsm.mainStates.get(0);
		Main.storyStage = 4;
	}
	
	
}
