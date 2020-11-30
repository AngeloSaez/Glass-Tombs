package states;

import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.sound.sampled.FloatControl;

import engine.Main;
import util.Textures;

public class Intro extends TextRoll {

	private String[] introText = {
		"7pg. 128",
		"7ADVENTURE CATALOG:",
		"7          \"forgot the heroes you should remember?",
		"7          \"well, here they are.\"",
		"",
		"",
		"",
		"",
		"7========",
		"7PALADIN",
		"",
		"7Completed the most dungeons and slain the",
		"7most monsters.",
		"",
		"7Deceased,",
		"7Remembered as the greatest there ever was.",
		"7========",
		"7SIMON THE GIANT",
		"",
		"7The biggest of the heros. Had the disposition",
		"7of an elephant.",
		"", 
		"7Deceased,",
		"7Remembered as the biggest and the drunkest.",
		"7========",
		"7CHARLES CLARKE",
		"",
		"7Amassed the most gold compared to anyone else.",
		"",
		"7Deceased,",
		"7Remembered for all that golden furniture.",
		"7========",
		"",
		"",
		"",
		"0Reading about the heroes of the past",
		"0reminded me that I will never lie in those",
		"0pages with those esteemed legends.",
		"",
		"0Instead, my memory will leave with my",
		"0last breath and I will be forgotten",
		"0someday.",
		"",
		"",
		"",
		"0My life has been peaceful overall.",
		"0In contrast, the life of an adventurer",
		"0is full of glory and violence.",
		"0Their lives of fame and wealth almost always",
		"0end in the place:",
		"5the Glass Dungeon.",
		"",
		"",
		"",
		"0After all, history even says",
		"7\"The greatest heroes have all tried and",
		"7sadly failed trying to best the Glass Dungeon.\"",
		"",
		"0The dungeon must be next to impossible.",
		"",
		"",
		"",
		"0Taking on the dungeon might be",
		"3extremely ambitious,",
		"0but it might be the only thing that will make",
		"0me be remembered.",
		"",
		"",
		"",
		"3If I do this I would be greater than the greatest.",
		"3My name would be above all others for entire history.",
		"",
		"",
		"",
		"3It might be the last thing I do,",
		"3but my name will be above all others.",
		"3I will beat the glass dungeon.",
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		"",
	};
	
	public long scrollEndTime = -1;
	public long titleImageDisplayMillis = 500;
	
	public Intro(Main main) {
		super(main);
		textArray = introText;
		// Set song
		long oldPos = Main.currentSong.getMicrosecondPosition();
		Main.currentSong.stop();
		Main.currentSong = Main.intro;
        FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
        volume.setValue(dB);
		Main.currentSong.setMicrosecondPosition(oldPos);
		Main.currentSong.start();
		Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
	}
	
	protected void scrollText(Graphics2D g) {
		double scrollLimit = -textMargin - spacingHeight * textArray.length;
		scrollOffset -= scrollStep;
		g.translate(0, scrollOffset);
		// Render title image
		Dimension titleImageSize = new Dimension(Main.width - tileRes, (int)((Main.width - tileRes) * 0.10766));
		g.drawImage(Textures.titleImage, Main.width / 2 - titleImageSize.width / 2, (int)(textArray.length * spacingHeight + textMargin), titleImageSize.width, titleImageSize.height, main);
		
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
		Main.gsm.mainStates.clear();
		Main.gsm.mainStates.add(new CHP1Intro(main));
		Main.gsm.currentState = Main.gsm.mainStates.get(0);
	}
	
	
}
