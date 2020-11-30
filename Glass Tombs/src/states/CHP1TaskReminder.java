package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class CHP1TaskReminder extends TextRoll {

	private String[] introText = {
		"3This is a crypt. It was build to honor heroes",
		"3from the past.",
		"",
		"",
		"",
		"3Somewhere deep in the center of the crypt lies",
		"3the final resting place of a legendary hero.",
		"",
		"",
		"",
		"3Enough wandering. I should find my way to the",
		"3Glass Dungeon.",
		"0It's marked with blue on my map."
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	
	public CHP1TaskReminder(Main main) {
		super(main);
		textArray = introText;
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
	}
	
	
}
