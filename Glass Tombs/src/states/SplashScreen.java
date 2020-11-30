package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class SplashScreen extends TextRoll {

	private String[] introText = {
		"0A game by Angelo Saez for",
		"02020 GitHub Game Off",
		"",
		"0Official soundtrack by",
		"0Yzabel Breboneria and Angelo Saez",
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 250;
	
	public SplashScreen(Main main) {
		super(main);
		textArray = introText;
		scrollStep = 2;
	}
	
	protected void scrollText(Graphics2D g) {
		double scrollLimit = -textMargin - spacingHeight * textArray.length;
		scrollOffset -= scrollStep;
		g.translate(0, scrollOffset);
		// Render title image
		Dimension titleImageSize = new Dimension(tileRes * 8, tileRes * 8);
		g.drawImage(Textures.gameOff, Main.width / 2 - titleImageSize.width / 2, (int)(textArray.length * spacingHeight + textMargin), titleImageSize.width, titleImageSize.height, main);
		
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
		Main.gsm.currentState = new AGlassTitleScreen(main);
	}
	
	
}
