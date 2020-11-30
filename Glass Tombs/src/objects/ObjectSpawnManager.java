package objects;

import java.awt.Dimension;
import java.awt.Point;

import engine.Main;
import util.LootTable;
import util.Textures;

public class ObjectSpawnManager {

	private Main main;
	private int tileRes;
	
	public ObjectSpawnManager(Main main) {
		this.main = main;
		this.tileRes = Main.tileRes;
		
	}
	
	public GameObject getSpawnType(int spawnMapID) {
		GameObject result = new GameObject(main);
		switch (spawnMapID) {
		// Mole man
		case 1:
			result = getMoleMan();
			break;
		// Cave entrance
		case 2:
			result = getStairsDown();
			break;
		// Cave exit
		case 3:
			result = getStairsUp();
			break;
		// Town pot
		case 4:
			result = getTownPot();
			break;
		// Flowers
		case 5:
			result = getFlowers();
			break;
		// grasshole
		case 6:
			result = getGrasshole();
			break;
		// bluedoor
		case 7:
			result = getHouseDoor();
			break;
		// cyandoor
		case 8:
			result = getHouseDoor();
			break;
		// purpledoor
		case 9:
			result = getHouseDoor();
			break;
		// pinkdoor
		case 10:
			result = getHouseDoor();
			break;
		// specialtydoor
		case 11:
			result = getSpecialtyDoor();
			break;
		// house exit mat
		case 12:
			result = getHouseExitMat();
			break;
		// terror plant
		case 13:
			result = getTerrorPlant();
			break;
		// mad dog
		case 14:
			result = getMadDog();
			break;
		// critter
		case 15:
			result = getCritter();
			break;
		// glass dungeon door
		case 16:
			result = getGlassDoors();
			break;
		// dungeon door
		case 17:
			result = getDungeonDoors();
			break;
		case 20:
			result = getDungeonPot();
			break;
		}
		
		// Apply texture
		result.texture = Textures.spawnMapObjects.get(spawnMapID);
		return result;
	}
	
	// Spawn object types
	public GameObject getMoleMan() {
		GameObject moleMan = new GameObject(main);
		moleMan.spawnMapID = 1;
		moleMan.hitbox = new Dimension(tileRes, tileRes);
		moleMan.hitboxOffset = new Point(0, 0);
		return moleMan;
	}
	
	public GameObject getStairsDown() {
		GameObject caveEntrance = new GameObject(main);
		caveEntrance.spawnMapID = 2;
		caveEntrance.hitbox = new Dimension(tileRes, tileRes);
		caveEntrance.hitboxOffset = new Point(0, 0);
		return caveEntrance;
	}
	
	public GameObject getStairsUp() {
		GameObject caveExit = new GameObject(main);
		caveExit.spawnMapID = 3;
		caveExit.hitbox = new Dimension(tileRes, tileRes);
		caveExit.hitboxOffset = new Point(0, 0);
		return caveExit;
	}
	
	public GameObject getTownPot() {
		GameObject townPot = new GameObject(main);
		townPot.spawnMapID = 4;
		townPot.hitbox = new Dimension(tileRes, tileRes);
		townPot.hitboxOffset = new Point(0, 0);
		townPot.killable = true;
		townPot.lootTableVarient = 1;
		return townPot;
	}
	
	public GameObject getFlowers() {
		GameObject flowers = new GameObject(main);
		flowers.spawnMapID = 5;
		flowers.hitbox = new Dimension(tileRes, tileRes);
		flowers.hitboxOffset = new Point(0, 0);
		return flowers;
	}
	
	public GameObject getGrasshole() {
		GameObject grasshole = new GameObject(main);
		grasshole.spawnMapID = 6;
		grasshole.hitbox = new Dimension(tileRes, tileRes);
		grasshole.hitboxOffset = new Point(0, 0);
		return grasshole;
	}
	
	public GameObject getHouseDoor() {
		GameObject houseDoor = new GameObject(main);
		houseDoor.spawnMapID = 7;
		houseDoor.hitbox = new Dimension(tileRes, tileRes);
		houseDoor.hitboxOffset = new Point(0, 0);
		return houseDoor;
	}
	
	public GameObject getSpecialtyDoor() {
		GameObject specialDoor = new GameObject(main);
		specialDoor.spawnMapID = 8;
		specialDoor.hitbox = new Dimension(tileRes, tileRes);
		specialDoor.hitboxOffset = new Point(0, 0);
		return specialDoor;
	}
	
	public GameObject getHouseExitMat() {
		GameObject exitMat = new GameObject(main);
		exitMat.spawnMapID = 9;
		exitMat.hitbox = new Dimension(tileRes, tileRes);
		exitMat.hitboxOffset = new Point(0, 0);
		return exitMat;
	}
	
	public GameObject getLootObject(GameObject object) {
		GameObject loot = new GameObject(main);
		int[] lootTable = LootTable.lootTables[object.lootTableVarient];
		loot.lootType = lootTable[(int)(Math.random() * lootTable.length)];
		loot.texture = Textures.lootTextures.get(loot.lootType);
		loot.bounds.setLocation(object.bounds.getLocation());
		loot.hitbox = new Dimension(tileRes / 2, tileRes / 2);
		loot.hitboxOffset = new Point(tileRes / 4, tileRes / 4);
		return loot;
	}
	
	public GameObject getTerrorPlant() {
		GameObject plant = new GameObject(main);
		plant.spawnMapID = 10;
		plant.hitbox = new Dimension(tileRes, tileRes);
		plant.hitboxOffset = new Point(0, 0);
		// Gameplay
		plant.hasAttack = true;
		plant.shootDelayMillis = 500;
		plant.killable = true;
		plant.lootTableVarient = 1;
		// Appearance
		plant.north = Textures.plantsDontWalk;
		plant.east = Textures.plantsDontWalk;
		plant.south = Textures.plantsDontWalk;
		plant.west = Textures.plantsDontWalk;
		plant.attackNorth = Textures.plantsDontWalk;
		plant.attackEast = Textures.plantsDontWalk;
		plant.attackSouth = Textures.plantsDontWalk;
		plant.attackWest = Textures.plantsDontWalk;
		plant.animationDelayMillis = 1000;
		// The plant is only animated if its vel != 0; but I dont want it to move
		// So the plant will be given a velocity but not added to the list of obj
		// that translate. 
		plant.vel.i = 1;
		return plant;
	}
	
	public GameObject getMadDog() {
		GameObject dog = new GameObject(main);
		dog.spawnMapID = 11;
		dog.hitbox = new Dimension(tileRes, tileRes);
		dog.hitboxOffset = new Point(0, 0);
		// Gameplay
		dog.killable = true;
		dog.health = 3;
		dog.lootTableVarient = 1;
		// Appearance
		dog.north = Textures.dogWalk;
		dog.east = Textures.dogWalk;
		dog.south = Textures.dogWalk;
		dog.west = Textures.dogWalk;
		dog.attackNorth = Textures.dogWalk;
		dog.attackEast = Textures.dogWalk;
		dog.attackSouth = Textures.dogWalk;
		dog.attackWest = Textures.dogWalk;
		dog.animationDelayMillis = 250;
		return dog;
	}
	
	public GameObject getCritter() {
		GameObject bug = new GameObject(main);
		bug.spawnMapID = 12;
		bug.hitbox = new Dimension(tileRes, tileRes);
		bug.hitboxOffset = new Point(0, 0);
		// Gameplay
		bug.runSpeed = 2;
		bug.hasAttack = true;
		bug.killable = true;
		bug.health = 1;
		bug.lootTableVarient = 1;
		// Appearance
		bug.north = Textures.bugWalk;
		bug.east = Textures.bugWalk;
		bug.south = Textures.bugWalk;
		bug.west = Textures.bugWalk;
		bug.attackNorth = Textures.bugWalk;
		bug.attackEast = Textures.bugWalk;
		bug.attackSouth = Textures.bugWalk;
		bug.attackWest = Textures.bugWalk;
		return bug;
	}
	
	public GameObject getGlassDoors() {
		GameObject glassDoor = new GameObject(main);
		glassDoor.spawnMapID = 13;
		glassDoor.hitbox = new Dimension(tileRes, tileRes);
		glassDoor.hitboxOffset = new Point(0, 0);
		return glassDoor;
	}
	
	public GameObject getDungeonDoors() {
		GameObject glassDoor = new GameObject(main);
		glassDoor.spawnMapID = 14;
		glassDoor.hitbox = new Dimension(tileRes, tileRes);
		glassDoor.hitboxOffset = new Point(0, 0);
		return glassDoor;
	}
	
	public GameObject getDungeonPot() {
		GameObject dungeonPot = new GameObject(main);
		dungeonPot.spawnMapID = 15;
		dungeonPot.hitbox = new Dimension(tileRes, tileRes);
		dungeonPot.hitboxOffset = new Point(0, 0);
		dungeonPot.killable = true;
		dungeonPot.lootTableVarient = 1;
		return dungeonPot;
	}
	
}
