package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class CHP1Realization extends TextRoll {

	private String[] introText = {
		"0You stand at the doors of the Glass Dungeon but",
		"0you are frozen in fear.",
		"",
		"",
		"",
		"3No one's come out of here alive. I'm nowhere near as",
		"3strong as others who have tried and failed...",
		"",
		"",
		"",
		"3No one's come out of here alive. I need to get help.",
		"3Help from someone who has tried the dungeon. But...",
		"",
		"",
		"",
		"3No one's come out of here alive. ",
		"3I'll just have to get help from someone who died trying.",
		"3Search the surrounding crypts. Find answers there.",
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	
	public CHP1Realization(Main main) {
		super(main);
		textArray = introText;
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
		Main.gsm.introStates.clear();
		Main.gsm.introStates.add(new CHP2Intro(main));
		Main.gsm.currentState = Main.gsm.introStates.get(0);
	}
	
	
}
