package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Textures {
	
	// Returns an image, given a local resource path ("/images/_.png")
    public static BufferedImage getImage(String path) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(Textures.class.getResource(path));
        } catch (IOException e) {
            System.out.println("image not found");
        }
        return bi;
    }
    
	private static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public static BufferedImage colorize(BufferedImage original, Color tint) {
		BufferedImage img = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		int redVal = tint.getRed();
		int greenVal = tint.getGreen();
		int blueVal = tint.getBlue();
		
		for (int x = 0; x < original.getWidth(); x++)
			for (int y = 0; y < original.getHeight(); y++) {
				Color pixelVal = new Color(original.getRGB(x, y), true);
				int grayValue = pixelVal.getRed();
				int alpha = pixelVal.getAlpha();
				int newRed = (redVal * (grayValue)) / 255;
				int newGreen = (greenVal * grayValue) / 255;
				int newBlue = (blueVal * grayValue) / 255;
				img.setRGB(x, y, new Color(newRed, newGreen, newBlue, alpha).getRGB());
			}
		return img;
	}

    public static BufferedImage createFlipped(BufferedImage image) {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        return createTransformed(image, at);
    }
    
    // Player sprites
    public static final BufferedImage error = getImage("/images/error.png");
    
    public static final BufferedImage[] playerNorth = {
    		getImage("/images/playerN0.png"),
    		getImage("/images/playerN1.png"),
    		getImage("/images/playerN0.png"),
    		getImage("/images/playerN2.png"),
    		getImage("/images/playerN3.png"),
    };
    
    public static final BufferedImage[] playerSouth = {
    		getImage("/images/playerS0.png"),
    		getImage("/images/playerS1.png"),
    		getImage("/images/playerS0.png"),
    		getImage("/images/playerS2.png"),
    		getImage("/images/playerS3.png"),
    };
    
    public static final BufferedImage[] playerEast = {
    		getImage("/images/playerE0.png"),
    		getImage("/images/playerE1.png"),
    		getImage("/images/playerE0.png"),
    		getImage("/images/playerE2.png"),
    		getImage("/images/playerE3.png"),
    };
    
    public static final BufferedImage[] playerWest = {
    		getImage("/images/playerW0.png"),
    		getImage("/images/playerW1.png"),
    		getImage("/images/playerW0.png"),
    		getImage("/images/playerW2.png"),
    		getImage("/images/playerW3.png"),
    };
    
    public static final BufferedImage[] playerAttackNorth = {
    		getImage("/images/swordNorth.png"),	
    };
	public static final BufferedImage[] playerAttackSouth = {
    		getImage("/images/swordSouth.png"),	
	};
	public static final BufferedImage[] playerAttackEast = {
    		getImage("/images/swordEast.png"),	
	};
	public static final BufferedImage[] playerAttackWest = {
    		getImage("/images/swordWest.png"),	
	};
	
	public static final BufferedImage[] dogWalk = {
    		getImage("/images/madDog0.png"),
    		getImage("/images/madDog1.png"),
    		getImage("/images/madDog0.png"),
    		getImage("/images/madDog1.png"),
    		getImage("/images/madDog0.png"),
    };
	
	public static final BufferedImage[] bugWalk = {
    		getImage("/images/critter0.png"),
    		getImage("/images/critter1.png"),
    		getImage("/images/critter0.png"),
    		getImage("/images/critter1.png"),
    		getImage("/images/critter0.png"),
    };
	
	public static final BufferedImage[] plantsDontWalk = {
    		getImage("/images/terrorPlant0.png"),
    		getImage("/images/terrorPlant1.png"),
    		getImage("/images/terrorPlant0.png"),
    		getImage("/images/terrorPlant1.png"),
    		getImage("/images/terrorPlant0.png"),
    };
	
	public static final BufferedImage[] bugAttack = {
    		getImage("/images/critterBubble0.png"),
    		getImage("/images/critterBubble1.png"),
    		getImage("/images/critterBubble0.png"),
    		getImage("/images/critterBubble1.png"),
    		getImage("/images/critterBubble0.png"),
    };
	
	public static final BufferedImage[] plantsAttackThough = {
    		getImage("/images/plantBubble0.png"),
    		getImage("/images/plantBubble1.png"),
    		getImage("/images/plantBubble0.png"),
    		getImage("/images/plantBubble1.png"),
    		getImage("/images/plantBubble0.png"),
    };
    
    public static final BufferedImage playerIdleR = getImage("/images/playerIdle.png");
    public static final BufferedImage playerIdleL = createFlipped(playerIdleR);
    public static final BufferedImage playerJumpR = getImage("/images/playerJump.png");
    public static final BufferedImage playerJumpL = createFlipped(playerJumpR);
    public static final BufferedImage playerSmushR = getImage("/images/playerSmush.png");
    public static final BufferedImage playerSmushL = createFlipped(playerSmushR);
    public static final BufferedImage playerDeadR = getImage("/images/playerDead.png");
    public static final BufferedImage playerDeadL = createFlipped(playerDeadR);
    
    // Floors
    public static ArrayList<BufferedImage> floors = new ArrayList<BufferedImage>();

    private static void populateFloors() {
    	floors.add(getImage("/images/protoGrass.png"));
    	floors.add(getImage("/images/flowers.png"));
    	floors.add(getImage("/images/houseFloor.png"));
    	floors.add(getImage("/images/dungeonFloor.png"));
    	floors.add(getImage("/images/glassDungeonFloor.png"));
    }
    
    // Walls
    public static ArrayList<BufferedImage> walls = new ArrayList<BufferedImage>();
    
    private static void populateWalls() {
    	walls.add(error);
    	walls.add(getImage("/images/protoRock.png"));
    	walls.add(getImage("/images/brickwall0.png")); // 2
		walls.add(getImage("/images/brickwall1.png"));
		walls.add(getImage("/images/brickwall2.png"));
		walls.add(getImage("/images/brickwall3.png"));
		walls.add(getImage("/images/brickwall4.png"));
		walls.add(getImage("/images/brickwall5.png"));
		walls.add(getImage("/images/brickwall6.png"));
		walls.add(getImage("/images/brickwall7.png"));
		walls.add(getImage("/images/brickwall8.png"));
		walls.add(getImage("/images/brickwall9.png"));
		walls.add(getImage("/images/brickwall10.png"));
		walls.add(getImage("/images/brickwall11.png"));
		walls.add(getImage("/images/brickwall12.png"));
		walls.add(getImage("/images/brickwall13.png"));
		walls.add(getImage("/images/brickwall14.png"));
		walls.add(getImage("/images/brickwall15.png"));
		walls.add(getImage("/images/grass0.png")); // 18
		walls.add(getImage("/images/grass1.png"));
		walls.add(getImage("/images/grass2.png"));
		walls.add(getImage("/images/grass3.png"));
		walls.add(getImage("/images/grass4.png"));
		walls.add(getImage("/images/grass5.png"));
		walls.add(getImage("/images/grass6.png"));
		walls.add(getImage("/images/grass7.png"));
		walls.add(getImage("/images/grass8.png"));
		walls.add(getImage("/images/grass9.png"));
		walls.add(getImage("/images/grass10.png"));
		walls.add(getImage("/images/grass11.png"));
		walls.add(getImage("/images/grass12.png"));
		walls.add(getImage("/images/grass13.png"));
		walls.add(getImage("/images/grass14.png"));
		walls.add(getImage("/images/grass15.png"));
		walls.add(getImage("/images/dungeonbrick0.png")); // 34
		walls.add(getImage("/images/dungeonbrick1.png"));
		walls.add(getImage("/images/dungeonbrick2.png"));
		walls.add(getImage("/images/dungeonbrick3.png"));
		walls.add(getImage("/images/dungeonbrick4.png"));
		walls.add(getImage("/images/dungeonbrick5.png"));
		walls.add(getImage("/images/dungeonbrick6.png"));
		walls.add(getImage("/images/dungeonbrick7.png"));
		walls.add(getImage("/images/dungeonbrick8.png"));
		walls.add(getImage("/images/dungeonbrick9.png"));
		walls.add(getImage("/images/dungeonbrick10.png"));
		walls.add(getImage("/images/dungeonbrick11.png"));
		walls.add(getImage("/images/dungeonbrick12.png"));
		walls.add(getImage("/images/dungeonbrick13.png"));
		walls.add(getImage("/images/dungeonbrick14.png"));
		walls.add(getImage("/images/dungeonbrick15.png"));
		walls.add(getImage("/images/water0.png")); // 50
		walls.add(getImage("/images/water1.png"));
		walls.add(getImage("/images/water2.png"));
		walls.add(getImage("/images/water3.png"));
		walls.add(getImage("/images/water4.png"));
		walls.add(getImage("/images/water5.png"));
		walls.add(getImage("/images/water6.png"));
		walls.add(getImage("/images/water7.png"));
		walls.add(getImage("/images/water8.png"));
		walls.add(getImage("/images/water9.png"));
		walls.add(getImage("/images/water10.png"));
		walls.add(getImage("/images/water11.png"));
		walls.add(getImage("/images/water12.png"));
		walls.add(getImage("/images/water13.png"));
		walls.add(getImage("/images/water14.png"));
		walls.add(getImage("/images/water15.png")); // 65
		walls.add(getImage("/images/fence.png"));
		walls.add(getImage("/images/townPot.png")); // 67
		walls.add(getImage("/images/flowers.png")); // 68
		walls.add(getImage("/images/grassHole.png")); // 69
		walls.add(getImage("/images/tree.png"));
		walls.add(getImage("/images/blueHouse.png"));
		walls.add(getImage("/images/blueHouse-1.png"));
		walls.add(getImage("/images/blueHouse-2.png"));
		walls.add(getImage("/images/blueHouse-3.png"));
		walls.add(getImage("/images/blueHouse-4.png"));
		walls.add(getImage("/images/blueHouse-5.png"));
		walls.add(getImage("/images/blueHouse-6.png")); // 77 DOOR**
		walls.add(getImage("/images/blueHouse-7.png"));
		walls.add(getImage("/images/blueHouse-8.png"));
		walls.add(getImage("/images/cyanHouse.png"));
		walls.add(getImage("/images/cyanHouse-1.png"));
		walls.add(getImage("/images/cyanHouse-2.png"));
		walls.add(getImage("/images/cyanHouse-3.png"));
		walls.add(getImage("/images/cyanHouse-4.png"));
		walls.add(getImage("/images/cyanHouse-5.png"));
		walls.add(getImage("/images/cyanHouse-6.png")); // 86 DOOR**
		walls.add(getImage("/images/cyanHouse-7.png"));
		walls.add(getImage("/images/cyanHouse-8.png"));
		walls.add(getImage("/images/purpleHouse.png")); // 88
		walls.add(getImage("/images/purpleHouse-1.png"));
		walls.add(getImage("/images/purpleHouse-2.png"));
		walls.add(getImage("/images/purpleHouse-3.png"));
		walls.add(getImage("/images/purpleHouse-4.png"));
		walls.add(getImage("/images/purpleHouse-5.png"));
		walls.add(getImage("/images/purpleHouse-6.png")); // 95 DOOR **
		walls.add(getImage("/images/purpleHouse-7.png"));
		walls.add(getImage("/images/purpleHouse-8.png"));
		walls.add(getImage("/images/pinkHouse.png"));
		walls.add(getImage("/images/pinkHouse-1.png"));
		walls.add(getImage("/images/pinkHouse-2.png"));
		walls.add(getImage("/images/pinkHouse-3.png"));
		walls.add(getImage("/images/pinkHouse-4.png"));
		walls.add(getImage("/images/pinkHouse-5.png"));
		walls.add(getImage("/images/pinkHouse-6.png")); // 104 DOOR**
		walls.add(getImage("/images/pinkHouse-7.png"));
		walls.add(getImage("/images/pinkHouse-8.png"));
		walls.add(getImage("/images/specialtyHouse.png"));
		walls.add(getImage("/images/specialtyHouse-1.png"));
		walls.add(getImage("/images/specialtyHouse-2.png"));
		walls.add(getImage("/images/specialtyHouse-3.png"));
		walls.add(getImage("/images/specialtyHouse-4.png"));
		walls.add(getImage("/images/specialtyHouse-5.png"));
		walls.add(getImage("/images/specialtyHouse-6.png"));
		walls.add(getImage("/images/specialtyHouse-7.png")); 
		walls.add(getImage("/images/specialtyHouse-8.png"));
		walls.add(getImage("/images/specialtySign.png"));
		walls.add(getImage("/images/wood-0.png")); // 117
		walls.add(getImage("/images/wood-1.png"));
		walls.add(getImage("/images/wood-2.png"));
		walls.add(getImage("/images/wood-3.png"));
		walls.add(getImage("/images/wood-4.png"));
		walls.add(getImage("/images/wood-5.png"));
		walls.add(getImage("/images/wood-6.png"));
		walls.add(getImage("/images/wood-7.png"));
		walls.add(getImage("/images/wood-8.png"));
		walls.add(getImage("/images/wood-9.png"));
		walls.add(getImage("/images/wood-10.png"));
		walls.add(getImage("/images/wood-11.png"));
		walls.add(getImage("/images/wood-12.png"));
		walls.add(getImage("/images/wood-13.png"));
		walls.add(getImage("/images/wood-14.png"));
		walls.add(getImage("/images/wood-15.png"));
		walls.add(getImage("/images/exitMat.png")); // 133
		walls.add(getImage("/images/terrorPlant0.png")); // 134
		walls.add(getImage("/images/madDog0.png")); // 135
		walls.add(getImage("/images/critter0.png")); //136
		walls.add(getImage("/images/glassDungeonExt.png")); // 137
		walls.add(getImage("/images/glassDungeonExt-1.png"));
		walls.add(getImage("/images/glassDungeonExt-2.png"));
		walls.add(getImage("/images/glassDungeonExt-3.png"));
		walls.add(getImage("/images/glassDungeonExt-4.png"));
		walls.add(getImage("/images/glassDungeonExt-5.png"));
		walls.add(getImage("/images/glassDungeonExt-6.png"));
		walls.add(getImage("/images/glassDungeonExt-7.png"));
		walls.add(getImage("/images/glassDungeonExt-8.png"));
		walls.add(getImage("/images/dungeonExt.png")); 
		walls.add(getImage("/images/dungeonExt-1.png")); // 147
		walls.add(getImage("/images/dungeonExt-2.png"));
		walls.add(getImage("/images/glass0.png")); // 149
		walls.add(getImage("/images/glass1.png"));
		walls.add(getImage("/images/glass2.png"));
		walls.add(getImage("/images/glass3.png"));
		walls.add(getImage("/images/glass4.png"));
		walls.add(getImage("/images/glass5.png"));
		walls.add(getImage("/images/glass6.png"));
		walls.add(getImage("/images/glass7.png"));
		walls.add(getImage("/images/glass8.png"));
		walls.add(getImage("/images/glass9.png"));
		walls.add(getImage("/images/glass10.png"));
		walls.add(getImage("/images/glass11.png"));
		walls.add(getImage("/images/glass12.png"));
		walls.add(getImage("/images/glass13.png"));
		walls.add(getImage("/images/glass14.png"));
		walls.add(getImage("/images/glass15.png"));
		walls.add(getImage("/images/dungeonPot.png")); // 165
    }
    
    // Spawn Map objects
    public static ArrayList<BufferedImage> spawnMapObjects = new ArrayList<BufferedImage>();
    
    private static void populateSpawnMapObjects(){
    	spawnMapObjects.add(error);
    	spawnMapObjects.add(getImage("/images/mole.png"));
    	spawnMapObjects.add(getImage("/images/dungeonStairsDown.png"));
    	spawnMapObjects.add(getImage("/images/dungeonStairsUp.png"));
    	spawnMapObjects.add(getImage("/images/townPot.png"));
    	spawnMapObjects.add(getImage("/images/flowers.png"));
    	spawnMapObjects.add(getImage("/images/grassHole.png"));
    	spawnMapObjects.add(getImage("/images/blueHouse-6.png"));
    	spawnMapObjects.add(getImage("/images/blueHouse-6.png"));
    	spawnMapObjects.add(getImage("/images/blueHouse-6.png"));
    	spawnMapObjects.add(getImage("/images/blueHouse-6.png"));
    	spawnMapObjects.add(getImage("/images/blueHouse-6.png"));
    	spawnMapObjects.add(getImage("/images/exitMat.png"));
    	spawnMapObjects.add(getImage("/images/terrorPlant0.png"));
    	spawnMapObjects.add(getImage("/images/madDog0.png"));
    	spawnMapObjects.add(getImage("/images/critter0.png"));
    	spawnMapObjects.add(getImage("/images/glassDungeonExt.png"));
    	spawnMapObjects.add(getImage("/images/dungeonExt-1.png"));
    	spawnMapObjects.add(getImage("/images/plantBubble0.png"));
    	spawnMapObjects.add(getImage("/images/critterBubble0.png"));
    	spawnMapObjects.add(getImage("/images/dungeonPot.png"));
    }
    
    // Gui textures
    public static ArrayList<BufferedImage> guiImages = new ArrayList<BufferedImage>();

    private static void populateGuiImages() {
    	guiImages.add(getImage("/images/hudHeart.png"));
    	guiImages.add(getImage("/images/hudSword.png"));
    	guiImages.add(getImage("/images/darkenedHudSword.png"));
    	guiImages.add(getImage("/images/hudPotion.png"));
    	guiImages.add(getImage("/images/menuBackground.png"));
    	guiImages.add(getImage("/images/menuTempDarken.png"));
    	guiImages.add(getImage("/images/hudMoney.png"));
    }
    
    // Inhabitants
    public static ArrayList<BufferedImage> inhabitantTextures = new ArrayList<BufferedImage>();

    private static void populateInhabitantTextures() {
    	inhabitantTextures.add(getImage("/images/townsFolk0.png"));
    	inhabitantTextures.add(getImage("/images/townsFolk1.png"));
    	inhabitantTextures.add(getImage("/images/townsFolk2.png"));
    	inhabitantTextures.add(getImage("/images/townsFolk3.png"));
    	inhabitantTextures.add(getImage("/images/townsFolk5.png"));
    }
    
    // Ghosts
    public static BufferedImage ghost0 = getImage("/images/ghost0.png");
    public static BufferedImage ghost1 = getImage("/images/ghost1.png");
    public static BufferedImage ghost2 = getImage("/images/ghost2.png");

    
    // Loot textures
    public static ArrayList<BufferedImage> lootTextures = new ArrayList<BufferedImage>();
    
    private static void populateLootTextures() {
    	spawnMapObjects.add(error);
    	lootTextures.add(getImage("/images/heart.png"));
    	lootTextures.add(getImage("/images/greenMoney.png"));
    	lootTextures.add(getImage("/images/blueMoney.png"));
    	lootTextures.add(getImage("/images/redMoney.png"));
    	lootTextures.add(getImage("/images/purpleMoney.png"));
    	lootTextures.add(getImage("/images/heartContainer.png"));
    }
    
    // Initialize all lists
    public static void initializeLists() {
    	populateFloors();
    	populateWalls();
    	populateSpawnMapObjects();
    	populateGuiImages();
    	populateInhabitantTextures();
    	populateLootTextures();
    }
    
    
    
    // Misc 
    public static BufferedImage titleImage = getImage("/images/titleImage.png");
    public static BufferedImage glassDungeonImage = getImage("/images/glassDungeonImage.png");
    public static BufferedImage dungeonImage = getImage("/images/dungeonImage.png");
    public static BufferedImage titleScreen = getImage("/images/titleScreenImage.png");
    public static BufferedImage gameOff = getImage("/images/gameoff.png");

	
}
