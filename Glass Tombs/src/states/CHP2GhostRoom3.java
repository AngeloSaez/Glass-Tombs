package states;

import java.awt.Dimension;

import engine.Main;

public class CHP2GhostRoom3 extends DialogueHall {
	
	protected static String[][] ta = {
			{
				"Welcome to my tomb, mortal.",
				"I was called Paladin when I",
				"was alive.",
			},
			{
				"You have definitely read about me.",
				"I was the greatest there ever was.",
			},
			{
				"I know that it isn't a dungeon.",
				"Its a place of rest. Just like this one.",
			},
			{
				"But the spirit who occupies it is",
				"a hateful one."
			},
			{
				"The spirit hated adventurers so he",
				"designed the crypt to be impossible",
				"to complete."
			},
			{
				"As more and more died trying, the",
				"allure of completing it grew",
				"even more. The perfect storm.",
			},
			{
				"That place will be your doom.",
				"If I couldn't survive, what makes",
				"you think you can?"
			},
			{
				"But then again, you're nothing",
				"like me. I would never stoop down",
				"to asking someone for help.",
				"I never needed to."
			},
	};

	public CHP2GhostRoom3(Main main, Dimension roomMapSize) {
		super(main, roomMapSize, ta, false);
		Main.cryptsCompleted = 2;
	}

}
