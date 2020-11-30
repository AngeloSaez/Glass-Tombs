package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class CHP2Realization1 extends TextRoll {

	private String[] introText = {
		"3That wasn't bad at all. I wanna",
		"3keep asking other ghosts.",
		"",
		"",
		"",
		"3No ones thought of this before so",
		"3it is worth a shot.",
		"",
		"",
		"",
		"3Fame feels within reach!"
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	
	public CHP2Realization1(Main main) {
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
		Main.storyStage = 2;
	}
	
	
}
