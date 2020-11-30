package states;

import java.awt.Dimension;

import engine.Main;

public class CHP2GhostRoom2 extends DialogueHall {
	
	protected static String[][] ta = {
			{
				"FINALLY! A mortal!",
				"So? What'd you think?",
				"I designed this place myself."
			},
			{
				"This place is a showcase of my",
				"rare priceless antique vases and",
				"other riches that I collected."
			},
			{
				"Most humans don't realize it but us",
				"ghosts design our own crypts by",
				"possessing the builders."
			},
			{
				"When you die you can design.",
				"your own crypt.",
				"In fact, why don't you try...",
			},
			{
				"JUST KIDDING JUST KIDDING!",
				"I'm a jovial and spirited spirit.",
				"I just loved money a bit too much.",
			},
			{
				"Wouldn't even hurt a fly...",
				"That's probably why I didn't last",
				"long in the Glass Dungeon."
			},
			{
				"Thanks for paying your respects.",
				"Feel free to stop by again",
				"\"mi casa su casa\" as they say.",
			},
			{
				"Just be warned,",
				"Other ghosts might not be",
				"as hospitable."
			},
	};

	public CHP2GhostRoom2(Main main, Dimension roomMapSize) {
		super(main, roomMapSize, ta, false);
		Main.cryptsCompleted = 2;
	}

}
