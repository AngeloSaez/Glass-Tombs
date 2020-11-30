package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class CHP3DungeonFeeling1 extends TextRoll {

	private String[] introText = {
		"6You feel different.",
		"",
		"",
		"",
		"6You are think of how much support you",
		"6got. You're not doing this for yourself",
		"6anymore",			
		"",
		"",
		"",
		"6You push open the massive glass doors.",
		"6Doors that no one has walked back out",
		"6out of alive.",
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	public int dungeonIndex;
	
	public CHP3DungeonFeeling1(Main main, int dungeonIndex) {
		super(main);
		textArray = introText;
		this.dungeonIndex = dungeonIndex;
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
		Main.gsm.currentState = Main.gsm.dungeonStates.get(dungeonIndex);
	}
	
	
}
