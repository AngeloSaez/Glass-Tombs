package states;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import engine.Main;
import util.MapGenerator;
import util.Style;
import util.Textures;

public class House extends Level {

	private String[][] textArrays = { 
		{
			"I have no magic",
			"for you."
		},
		{
			"I don't think you will",
			"beat the Glass Dungeon",
			"by yourself."
		},
		{
			"Who even are you again?",
			"Are you even an",
			"adventurer?"
		},
		{
			"The dark creepy buildings",
			"around are crypts. They",
			"are infested, we think."
		},
		{
			"Crypts were built to",
			"honor the greatest heroes",
			"from this town."
		},
		{
			"No hero has ever",
			"completed the Glass",
			"Dungeon."
		},
		{
			"Those who can't fight,",
			"farm. You should farm.",
		},
		{
			"Visit a crypt to pay",
			"respects to the great",
			"heroes of the past.",
		},
		{
			"The forest can be",
			"dangerous. You don't have",
			"to fight everything.",
		},
		{
			"Some enemies run",
			"towards you relentlessly",
		},
		{
			"Stationary enemies can",
			"be killed too."
		},
		{
			"Some enemies wander",
			"aimlessly."
		},
		{
			"The adventure log is",
			"our town's legacy.",
		},
		{
			"When Paladin died in the",
			"Glass Dungeon, we knew it",
			"must be impossible",
		},
		{
			"What are those purple",
			"things anyway?",
			"Check the source code.",
		},
		{
			"You're a long was from",
			"Kakariko Village.",
		},
		{
			"You don't have to fight",
			"all the monsters.",
			"Just walk past them.",
		},
		{
			"Wise adventurers pick",
			"their battles. Don't forget",
			"you can run away.",
		},
		{
			"Hey get out of",
			"my house.",
		},
		{
			"I heard this world was",
			"made in a month.",
		},
		{
			"Green is the color of",
			"envy and greed.",
		},
		{
			"Purple contrasts the",
			"good and the bad of Green.",
		},
		{
			"Whats better?",
			"Good actions + bad motives?",
			"Bad actions + good motives?",
		},
		{
			"No checkpoints.",
			"Use pots wisely.",
		},
		{
			"You have " + Main.maxPlayerHealth + " max health.",
			"Break open pots to get hearts.",
		},
		{
			"You'd be surprised how",
			"easy it is to avoid enemies.",
			"They're pretty slow.",
		},
		{
			"Enemies are slow enough to",
			"avoid. Just go around.",
		},
		{
			"Take your time and",
			"avoid enemies when",
			"you need to.",
		},
		{
			"Four-way intersections",
			"are as rare as they are",
			"dangerous.",
		},
		{
			"You like root beer?",
			"Yea, me neither."
		},
		{
			"Is money even important?",
		},
		{
			"You adventurers are too",
			"obsessed with money."
		},
	};
	
	public int textIndex;
	public int inhabitantIndex;

	public House(Main main, Dimension roomMapSize) {
		super(main, roomMapSize);
		// Camera
		initializeCamera();
		// Tile map generation
		MapGenerator og = new MapGenerator(2, roomMapSize, tileMapSize);
		roomMap = og.roomMap;
		doorwayMap = og.doorwayMap;
		overallWallMap = og.overallWallMap;
		overallFloorMap = og.overallFloorMap;
		overallSpawnMap = og.overallSpawnMap;
		// Spawned objects
		initializeSpawnedObjects();
		// Player
		initializePlayer();
		// Initialize building text
		textIndex = (int)(Math.random() * textArrays.length);
		inhabitantIndex = (int)(Math.random() * Textures.inhabitantTextures.size());
	}

	protected void renderRoomText(Graphics2D g) {
		double textSize = (tileRes);
		double spacingHeight = (tileRes * 0.9);
		g.setFont(Style.righteous);
		g.setFont(g.getFont().deriveFont((float)textSize));
		g.setColor(Style.white);
		for (int i = 0; i < textArrays[textIndex].length; i++) {
			String text = textArrays[textIndex][i];
			int textWidth = g.getFontMetrics().stringWidth(text);
			g.drawString(text, Main.width/2 - textWidth/2, (int)(i * spacingHeight + Main.height/2));
		}
	}
	
	protected void renderRoomInhabitant(Graphics2D g) {
		Point pos = new Point(Main.width / 2 - tileRes / 2, tileRes * 4);
		BufferedImage inhabitantTexture = Textures.inhabitantTextures.get(inhabitantIndex);
		g.drawImage(inhabitantTexture, pos.x, pos.y, tileRes, tileRes, main);
	}
	
	// Unique player initialization
	protected void repositionPlayer() {
		player.bounds.setLocation(tileRes * (tileMapSize.width / 2) + tileRes * (localMapSize.width / 2) - tileRes,
				tileRes * (tileMapSize.height / 2) + tileRes * (localMapSize.height - 2));
	}

}
