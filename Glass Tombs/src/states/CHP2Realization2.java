package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class CHP2Realization2 extends TextRoll {

	private String[] introText = {
		"3That ghost was somewhat helpful",
		"3but I think I should try one that",
		"3was a bit more... tough.",
		"",
		"",
		"",
		"3There should be one last crypt",
		"3nearby. ",
		"",
		"",
		"",
		"3I wonder if they will name the",
		"3town after me!",
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	
	public CHP2Realization2(Main main) {
		super(main);
		textArray = introText;
	}
	
	protected void scrollText(Graphics2D g) {
		double scrollLimit = -textMargin - spacingHeight * textArray.length;
		scrollOffset -= scrollStep;
		g.translate(0, scrollOffset);
		// Render title image
		Dimension titleImageSize = new Dimension(tileRes * 3, tileRes * 3);
		g.drawImage(Textures.dungeonImage, Main.width / 2 - titleImageSize.width / 2,
				(int) (textArray.length * spacingHeight + textMargin), titleImageSize.width, titleImageSize.height,
				main);

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
		Main.storyStage = 3;
	}
	
	
}
