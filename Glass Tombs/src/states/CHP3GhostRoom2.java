package states;

import java.awt.Dimension;

import engine.Main;
import util.Textures;

public class CHP3GhostRoom2 extends DialogueHall {
	
	protected static String[][] ta = {
			{
				"Welcome to the last test",
				"This test ended the journey",
				"of countless heroes."
			},
			{
				"These heroes all came here alone",
				"clawing at the notion to be the one",
				"who completed this dungeon on their own."
			},
			{
				"But this chamber is locked.",
				"And the only way to leave would be to",
				"have at least two people to simultaneously",
				"activate the unlock switches."
			},
			{
				"A simple obstacle right?",
				"Yet, no adventurer comes here with help.",
				"That way the spotlight can be all theirs.",
				"It's so silly."
			},
			{
				"As more people died over the centuries",
				"trying, history forgot that this was my",
				"tomb and the term Glass Dungeon",
				"was coined."
			},
			{
				"Only those not blinded by their desire for",
				"spotlight by willingly sharing the glory,",
				"would be worthy of completing my dungeon."
			},
			{
				"But it appears that you came here.",
				"alone too. Sealing your fate."
			},
			{
				"Your're just another adventurer",
				"dying a failure."
			},
			
	};

	public CHP3GhostRoom2(Main main, Dimension roomMapSize) {
		super(main, roomMapSize, ta, true);
		repositionPlayer();
		System.out.println("transitioned 2 to 3");
		Main.storyStage = 6;
	}

	protected void initializeInhabitantTexture() {
		inhabitantTexture = Textures.ghost1;
		inhabitantWidth = tileRes;
	}
	
}
