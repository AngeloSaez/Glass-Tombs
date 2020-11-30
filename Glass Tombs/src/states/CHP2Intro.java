package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class CHP2Intro extends TextRoll {

	private String[] introText = {
		"7CHAPTER 2",
		"0Search crypts for answers.",
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	
	public CHP2Intro(Main main) {
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
		g.drawImage(Textures.dungeonImage, Main.width / 2 - titleImageSize.width / 2, (int)(textArray.length * spacingHeight + textMargin), titleImageSize.width, titleImageSize.height, main);
		
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
