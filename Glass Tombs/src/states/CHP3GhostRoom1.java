package states;

import java.awt.Dimension;

import engine.Main;
import util.Textures;

public class CHP3GhostRoom1 extends DialogueHall {
	
	protected static String[][] ta = {
			{
				"Its not over yet, adventurer.",
				"I'm the spirit of this place.",
			},
			{
				"Except, I prefer to remain nameless.",
				"Unlike you, I don't need my name to",
				"be embedded in history."
			},
			{
				"You adventurers fixate on having your",
				"name in the stars so you are never",
				"down to earth."
			},
			{
				"You all think you are making the world",
				"a better place and you think you are all",
				"good people..."
			},
			{
				"But if your motives are only glory and",
				"gold, can you even call yourself",
				"\"good\"?",
			},
			{
				"I'm here because one day a man chose",
				"to cross the fine line between hero and",
				"murderer. All for fame and fortune."
			},
			{
				"Each and every one of you greedy adventurers",
				"are just like him. You chose to chase this",
				"spotlight. So you deserve whats coming."
			},
			{
				"I'm trying to change the world with",
				"this place. Pose a challenge that calls",
				"for the end of the \"lone hero\"."
			},
			
	};

	public CHP3GhostRoom1(Main main, Dimension roomMapSize) {
		super(main, roomMapSize, ta, false);
		repositionPlayer();
		System.out.println("transitioned 1 to 2");
		Main.storyStage = 5;
	}
	
	protected void initializeInhabitantTexture() {
		inhabitantTexture = Textures.ghost1;
		inhabitantWidth = tileRes;
	}

}
