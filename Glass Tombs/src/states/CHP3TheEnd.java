package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import engine.Main;
import util.Textures;

public class CHP3TheEnd extends TextRoll {

	private String[] introText = {
			"7ADVENTURE CATALOG:",
			"7- new entry -",
			"",
			"",
			"",
			"",
			"7========",
			"7The unthinkable was achieved. The most",
			"7difficult dungeon was completed... and",
			"7not by someone us town folks expected.",
			"",
			"",
			"",
			"7This adventurer is different. Aside from",
			"7being inexperienced, they decided to credit",
			"7everyone who has helped them along the way.",
			"",
			"",
			"",
			"7Here is everyone he says he couldn't have",
			"7beaten the dungeon without:",
			"3		- Ghost #1 (name: Flat)",
			"3		- Ghost #2 (name: Sharp)",
			"3		- Ghost #3 (name: Paladin) ",
			"",
			"7He credited a bunch of fallen heros from",
			"7ages ago and we don't know why he credited them,",
			"7but he seemed really happy to do so.",
			"",
			"",
			"",
			"",
			"",
			"",
			"0THE END",
			"",
			"7\"And then the adventurer just started",
			"7thanking people we never heard of...\"",
			"",
			"",
			"",
			"0CREDITS:",
			"",
			"",
			"0Game by:",
			"0		ANGELO SAEZ",
			"",
			"",
			"0Music by:",
			"0 		YZABEL BREBONERIA",
			"0		ANGELO SAEZ",		
			"",
			"",
			"0Engine by:",
			"0		ANGELO SAEZ",
			"",
			"",
			"0Art by:",
			"0		ANGELO SAEZ",
			"",
			"",
			"0Testing by:",
			"0		SEAN ST. CLAIR",	
			"0		KIERAN CHOI-SLATTERY",
			"",
			"",
			"",
			"",
			"3 These people supported me the most this month:",
			"",
			"0Special thanks:",
			"0		YZABEL BREBONERIA",
			"0		RICKY SAEZ",
			"0		SEAN ST. CLAIR",
			"0		KIERAN CHOI-SLATTERY",
			"0		CHARLES PORR",
			"0		RAI BINDRA",
			"0		DAVID MARTINEZ",
			"0		STEVENS GAME DEV. CLUB",
			"0		SIMON",
			"",
			"0 THANK YOU FOR PLAYING",
			"",
			"",
			"",
			"7The adventurer will be remembered as a hero.",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"7FOOTNOTE:",
			"7No one in town knows who any of",
			"7those people were. Also, we saw",
			"7the adventurer walk in and walk out",
			"7of the dungeon alone...",
			"",
			"7We are pretty sure he's insane",
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	
	public CHP3TheEnd(Main main) {
		super(main);
		textArray = introText;
	}
	
	protected void scrollText(Graphics2D g) {
		double scrollLimit = -textMargin - spacingHeight * textArray.length;
		scrollOffset -= scrollStep;
		g.translate(0, scrollOffset);
		// Render title image
		Dimension titleImageSize = new Dimension(Main.width - tileRes, (int)((Main.width - tileRes) * 0.10766));
		g.drawImage(Textures.titleImage, Main.width / 2 - titleImageSize.width / 2,
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
		Main.currentSong.stop();
		Main.gsm.introStates.clear();
		Main.gsm.introStates.add(new AGlassTitleScreen(main));
		Main.gsm.currentState = Main.gsm.introStates.get(0);
	}
	
	
}
