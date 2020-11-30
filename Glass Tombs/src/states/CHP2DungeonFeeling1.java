package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class CHP2DungeonFeeling1 extends TextRoll {

	private String[] introText = {
		"0The heavy dungeon doors slowly open, inciting the",
		"0sound of several distant footsteps whose owners",
		"0are hidden by the shadows.",
		"",
		"",
		"",
		"3This crypt might be more dangerous than I",
		"3anticipated. Heres to securing my lasting memory.",
		"",
		"",
		"3My name will be put in a spotlight above all others.",
		"3It'll be all worth it soon."
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	public int dungeonIndex;
	
	public CHP2DungeonFeeling1(Main main, int dungeonIndex) {
		super(main);
		textArray = introText;
		this.dungeonIndex = dungeonIndex;
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
		Main.gsm.currentState = Main.gsm.dungeonStates.get(dungeonIndex);
	}
	
	
}
