package states;

import java.awt.Dimension;

import engine.Main;
import util.Textures;

public class CHP3GhostRoom3 extends DialogueHall {
	
	protected static String[][] ta = {
			{
				"(You are locked here alone)",
			},
			{
				"",
			},
			{
				"",
			},
			{
				"",
			},
			{
				"",
			},
			{
				"You taught us that when you visited",
				"our crypts that all need others to make",
				"life more full."
			},
			{
				"We must help eachother.",
				"We can't do it alone sometimes.",
			},
			{
				"Now, we can rest in peace.",
				"Thank you. You are a real hero."
			},
			
	};

	public CHP3GhostRoom3(Main main, Dimension roomMapSize) {
		super(main, roomMapSize, ta, false);
		Main.currentSong.stop();
		Main.storyStage = 7;
		delayedMusicStart = true;
	}
	
	protected void initializeInhabitantTexture() {
		inhabitantTexture = Textures.ghost2;
		inhabitantWidth = tileRes * 5;
		hideUntilEnd = true;
	}

}
