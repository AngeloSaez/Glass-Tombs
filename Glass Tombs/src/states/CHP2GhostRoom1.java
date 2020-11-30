package states;

import java.awt.Dimension;

import engine.Main;

public class CHP2GhostRoom1 extends DialogueHall {
	
	protected static String[][] ta = {
			{
				"Are you lost?",
				"Your shirt looks like the ones we",
				"used to have in town."
			},
			{
				"I was the talk of the town",
				"but now I'm stuck here alone.",
				"Do they mention me much anymore?",
			},
			{
				"Heed my warning: the",
				"Glass Dungeon is unlike any other.",
			},
			{
				"I have crawled dungeons many a time.",
				"That place feels nothing like one.",
			},
			{
				"And I'm not just suspicious",
				"because I died in it.",
			},
			{
				"I'm dead but I can't rest. Did they",
				"still talk about me after dragging my",
				"body out of there?"
			},
			{
				"...Nevermind. I guess whatevers",
				"happening out there shouldn't matter as",
				"much anymore...",
			},
			{
				"Hey,",
				"Good luck out there."
			},
	};

	public CHP2GhostRoom1(Main main, Dimension roomMapSize) {
		super(main, roomMapSize, ta, false);
		repositionPlayer();
		Main.cryptsCompleted = 1;
	}

}
