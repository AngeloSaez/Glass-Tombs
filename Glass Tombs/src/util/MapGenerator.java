package util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.Main;
import states.Level;
import utilmisc.BranchData;
import utilmisc.Prefab;

public class MapGenerator {

	public Dimension roomMapSize;
	public int[][] roomMap;
	public int[][] doorwayMap;
	public Dimension tileMapSize;
	public int[][] overallWallMap;
	public int[][] overallFloorMap;
	public int[][] overallSpawnMap;
	
	/*
	 * Variety:
	 * 0 - Overworld
	 * 1 - Basic dungeon
	 * 2 - End dungeon
	 */
	
	public MapGenerator(int variety, Dimension roomMapSize, Dimension tileMapSize) {
		initializeGeneration(variety, roomMapSize, tileMapSize);
		// Generate tilemap from roommap, doorwaymap, and the prefabs
		applyToTileMap(variety, roomMapSize, roomMap, doorwayMap, overallWallMap, overallFloorMap, overallSpawnMap);
	}
	
	protected void initializeGeneration(int variety, Dimension roomMapSize, Dimension tileMapSize) {
		// Initialize variables and arrays
		this.roomMapSize = roomMapSize;
		this.tileMapSize = tileMapSize;
		roomMap = new int[roomMapSize.height][roomMapSize.width];
		doorwayMap = new int[roomMapSize.height][roomMapSize.width];
		overallWallMap = new int[tileMapSize.height][tileMapSize.width];
		overallFloorMap = new int[tileMapSize.height][tileMapSize.width];
		overallSpawnMap = new int[tileMapSize.height][tileMapSize.width];
		// Variety specific branch generation
		generateBranches(variety);
	}
	
	protected void generateBranches(int variety) {
		switch (variety) {
		case 0: // Over world
			// Add the town branch
			ArrayList<BranchData> townRooms = new ArrayList<BranchData>();
			generateBranch(new BranchData(roomMapSize.height / 2, roomMapSize.width / 2, "null"), 6, townRooms, 1);
			
			// Make a copy of the town rooms. Will be used as the starting positions for the dungeon branches
			ArrayList<BranchData> stemRooms = new ArrayList<BranchData>();
			for (BranchData room : townRooms) {
				stemRooms.add(room);
			}
			
			// Add the dungeon branches
			ArrayList<ArrayList<BranchData>> dungeonPaths = new ArrayList<ArrayList<BranchData>>();
			int dungeonPathLimit = 4;
			for (int i = 0; i < dungeonPathLimit; i++) {
				ArrayList<BranchData> dungeonPathRooms = new ArrayList<BranchData>();
				BranchData chosenStartSpace = stemRooms.get((int) (Math.random() * stemRooms.size()));
				generateBranch(new BranchData(chosenStartSpace.ry, chosenStartSpace.rx, "null"), 6, dungeonPathRooms,
						i + 2);
				// Add the base of the dungeon branch to the pool of possible branch start positions
				if (dungeonPathRooms.size() > 0) {
					dungeonPaths.add(dungeonPathRooms);
					stemRooms.add(dungeonPathRooms.get(0));
				} else {
					i--;
				}
			}
			
			// Add the glass dungeon
			Main.glassDungeonMarker = dungeonPathLimit + 2;
			ArrayList<ArrayList<BranchData>> glassPaths = new ArrayList<ArrayList<BranchData>>();
			int glassPathLimit = 1;
			for (int i = 0; i < glassPathLimit; i++) {
				ArrayList<BranchData> glassPathRooms = new ArrayList<BranchData>();
				// Chooses the dungeon branch to branch off of for the glass dungeon
				ArrayList<BranchData> chosenList = dungeonPaths.get((int) (Math.random() * dungeonPaths.size()));
				// Makes sure the branch is long enough
				while (chosenList.size() <= 2) {
					chosenList = dungeonPaths.get((int) (Math.random() * dungeonPaths.size()));
				}
				// Excludes the room with the dungeon in it
				chosenList.remove(chosenList.size() - 1);
				// Procedes with normal branch generation pipeline
				BranchData chosenStartSpace = chosenList.get((int) (Math.random() * chosenList.size()));
				generateBranch(new BranchData(chosenStartSpace.ry, chosenStartSpace.rx, "null"), 5, glassPathRooms,
						Main.glassDungeonMarker);
				// Add the base of the dungeon branch to the pool of possible branch start positions
				if (glassPathRooms.size() > 0) {
					glassPaths.add(glassPathRooms);
					// Remember the branch data of the glass dungeon
					if (i == glassPathLimit - 1) {
						Main.glassDungeonRoom = glassPathRooms.get(glassPathRooms.size() - 1);
					}
				} else {
					i--;
				}
			}
			
			
			break;
		case 1: // Basic Dungeon
			// Add safe room 
			ArrayList<BranchData> safeRoom = new ArrayList<BranchData>();
			generateBranch(new BranchData(roomMapSize.height / 2, roomMapSize.width / 2, "null"), 1, safeRoom, 1);

			// Add the dungeon branches
			ArrayList<ArrayList<BranchData>> dungeonBranches = new ArrayList<ArrayList<BranchData>>();
			int dungeonBranchLimit = 1;
			for (int i = 0; i < dungeonBranchLimit; i++) {
				ArrayList<BranchData> dungeonBranchRooms = new ArrayList<BranchData>();
				BranchData chosenStartSpace = safeRoom.get((int) (Math.random() * safeRoom.size()));
				generateBranch(new BranchData(chosenStartSpace.ry, chosenStartSpace.rx, "null"), 12, dungeonBranchRooms,
						i + 2);
				dungeonBranches.add(dungeonBranchRooms);
			}
			break;
		case 8: // Glass Dungeon
			// Add safe room
			ArrayList<BranchData> safeRoom2 = new ArrayList<BranchData>();
			generateBranch(new BranchData(roomMapSize.height / 2, roomMapSize.width / 2, "null"), 1, safeRoom2, 1);

			// Add the dungeon branches
			ArrayList<ArrayList<BranchData>> dungeonBranches2 = new ArrayList<ArrayList<BranchData>>();
			int dungeonBranchLimit2 = 1;
			for (int i = 0; i < dungeonBranchLimit2; i++) {
				ArrayList<BranchData> dungeonBranchRooms2 = new ArrayList<BranchData>();
				BranchData chosenStartSpace = safeRoom2.get((int) (Math.random() * safeRoom2.size()));
				generateBranch(new BranchData(chosenStartSpace.ry, chosenStartSpace.rx, "null"), 24, dungeonBranchRooms2,
						i + 2);
				dungeonBranches2.add(dungeonBranchRooms2);
			}
			break;
		case 2: // House
			// Add only room 
			ArrayList<BranchData> onlyRoom = new ArrayList<BranchData>();
			generateBranch(new BranchData(roomMapSize.height / 2, roomMapSize.width / 2, "null"), 1, onlyRoom, 1);		
			break;
		}
	}

	/*
	 * Generates a branch of length 'limit' starting from
	 * roomMap[start.ry][start.rx] and saves all of the final BranchDatas to archive
	 * 
	 * Marker is the value to be stored for every value of the branch. The overworld uses the system of the town branch using all 1's and every subsequent dungeon branch being marked with number n
	 * 
	 * Basic dungeons will have a single entry room and a single long branch.
	 */
	
	protected void generateBranch(BranchData start, int limit, ArrayList<BranchData> archive, int marker) {
		// 'Room Pointer X/Y' are positions of the dungeons generation
		int rpx = start.rx;
		int rpy = start.ry;
		
		// If a branch is chained it means it was started off of another branch. Slight differences come from this
		boolean branchesChained = roomMap[rpy][rpx] != 0;
		
		// Adds the starting grid if an original branch
		if (!branchesChained) {
			roomMap[rpy][rpx] = 1;
			archive.add(start);
		}
		
		// Keeps track of ending loop early if branch reaches problems
		boolean branchTerminated = false;
		int branchCount = 2;
		
		// If branch is chained then the first one is not converted
		if (branchesChained) {
			branchCount = 1;
		}

		// Loops through adding rooms to a branch
		for (; branchCount < limit; branchCount++) {
			// Compiles a list of all available nearby rooms
			ArrayList<BranchData> availableSpaces = new ArrayList<BranchData>();
			// Right
			if (rpx + 1 < roomMapSize.width) {
				if (roomMap[rpy][rpx + 1] == 0) {
					availableSpaces.add(new BranchData(rpy, rpx + 1, "right"));
				}
			}
			// Left
			if (rpx > 0) {
				if (roomMap[rpy][rpx - 1] == 0) {
					availableSpaces.add(new BranchData(rpy, rpx - 1, "left"));
				}
			}
			// Down
			if (rpy + 1 < roomMapSize.width) {
				if (roomMap[rpy + 1][rpx] == 0) {
					availableSpaces.add(new BranchData(rpy + 1, rpx, "down"));
				}
			}
			// Up
			if (rpy > 0) {
				if (roomMap[rpy - 1][rpx] == 0) {
					availableSpaces.add(new BranchData(rpy - 1, rpx, "up"));
				}
			}
			// No rooms available
			if (availableSpaces.size() == 0) {
				branchTerminated = true;
			}
			// Decides on a subsequent room
			else {
				// Increment variable values
				BranchData chosenSpace = availableSpaces.get((int) (Math.random() * availableSpaces.size()));
				archive.add(chosenSpace);
				roomMap[chosenSpace.ry][chosenSpace.rx] = marker;
				rpy = chosenSpace.ry;
				rpx = chosenSpace.rx;

				// Incremennt doorway map
				if (chosenSpace.directionFromPrevious == "right") {
					// Next room
					doorwayMap[chosenSpace.ry][chosenSpace.rx] += 8;
					// Previous room
					doorwayMap[chosenSpace.ry][chosenSpace.rx - 1] += 2;

				}
				if (chosenSpace.directionFromPrevious == "left") {
					// Next room
					doorwayMap[chosenSpace.ry][chosenSpace.rx] += 2;
					// Previous room
					doorwayMap[chosenSpace.ry][chosenSpace.rx + 1] += 8;

				}
				if (chosenSpace.directionFromPrevious == "down") {
					// Next room
					doorwayMap[chosenSpace.ry][chosenSpace.rx] += 1;
					// Previous room
					doorwayMap[chosenSpace.ry - 1][chosenSpace.rx] += 4;

				}
				if (chosenSpace.directionFromPrevious == "up") {
					// Next room
					doorwayMap[chosenSpace.ry][chosenSpace.rx] += 4;
					// Previous
					doorwayMap[chosenSpace.ry + 1][chosenSpace.rx] += 1;
				}
			}
		}
	}
	
	public static void applyToTileMap(int variety, Dimension roomMapSize, int[][] roomMap, int[][] doorwayMap, int[][] overallWallMap, int[][] overallFloorMap, int[][] overallSpawnMap) {
		// Applying to the tilemap
		Dimension localMapSize = Level.localMapSize;
		// RoomX/Y (rx/ry), InteriorX/Y (ix, iy)
		for (int ry = 0; ry < roomMapSize.height; ry++) {
			for (int rx = 0; rx < roomMapSize.width; rx++) {
				// Determine which prefab pool to use
				Prefab[][] pool = new Prefab[][]{};
				int marker = roomMap[ry][rx];
				switch(variety) {
				case 0: // Overworld
					// Town
					if (marker == 1) {
						pool = PrefabPool.townPool;
					} 
					// Glass dungeon branch
					else if (marker == Main.glassDungeonMarker) {
						pool = PrefabPool2.glassPathPool;
					}
					// Dungeon branch
					else {
						pool = PrefabPool.overworldPathPool;
					}
					break;
				case 1: // Basic Dungeon
					// Entrance
					if (marker == 1) {
						pool = PrefabPool.dungeonEntrancePool;
					} else {
						pool = PrefabPool.dungeonBranchPool;
					}
					break;
				case 8: // Basic Dungeon
					// Entrance
					if (marker == 1) {
						pool = PrefabPool2.glassEntrancePool;
					} else {
						pool = PrefabPool3.glassBranchPool;
					}
					break;
				case 2: // House
					pool = PrefabPool.housePool;
					break;
				case 3: // Dungeon Dialogue hall
					pool = PrefabPool2.dialogueHallDungeonhPool;
					break;
				}
				
				// Derive doorway count
				int doorwayClassificaion = doorwayMap[ry][rx];				
				// Select a room from the room pool
				int randomPrefabIndex = (int)(Math.random() * pool[doorwayClassificaion].length);
				Prefab decidedRoom = pool[doorwayClassificaion][randomPrefabIndex];
				for (int iy = 0; iy < localMapSize.height; iy++) {
					for (int ix = 0; ix < localMapSize.width; ix++) {
						// Match the tilemap with the selected room
						int roomOffsetX = rx * localMapSize.width;
						int roomOffsetY = ry * localMapSize.height;
						overallWallMap[roomOffsetY + iy][roomOffsetX + ix] = decidedRoom.wallMap[iy][ix];
						overallFloorMap[roomOffsetY + iy][roomOffsetX + ix] = decidedRoom.floorMap[iy][ix];
						overallSpawnMap[roomOffsetY + iy][roomOffsetX + ix] = decidedRoom.spawnMap[iy][ix];
						// Special floors
						if (variety == 1) { // Basic Dungeon
							if (decidedRoom.floorMap[iy][ix] == 0) {
								overallFloorMap[roomOffsetY + iy][roomOffsetX + ix] = 3;
							}
						}
						if (variety == 2) { // House
							if (decidedRoom.floorMap[iy][ix] == 0) {
								overallFloorMap[roomOffsetY + iy][roomOffsetX + ix] = 2;
							}
						}
						if (variety == 3) { // Dungeon Dialogue hall
							if (decidedRoom.floorMap[iy][ix] == 0) {
								overallFloorMap[roomOffsetY + iy][roomOffsetX + ix] = 3;
							}
						}
						if (variety == 8) { // Dungeon Dialogue hall
							if (decidedRoom.floorMap[iy][ix] == 0) {
								overallFloorMap[roomOffsetY + iy][roomOffsetX + ix] = 3;
							}
						}
					}
				}
			}
		}
		
		// Crawling tiles
		int[] crawlableTiles = new int[] {
			2, 		// brick wall
			18, 	// overworld grass wall
			34,  	// basic dungeon wall
			50,     // water
			117,    // wood
			149, 	// glass
		};
		// Record original map in temp
		int[][] tempMap = new int[overallWallMap.length][overallWallMap[0].length];
		for (int y = 0; y < overallWallMap.length; y++) {
			for (int x = 0; x < overallWallMap[0].length; x++) {
				// Variable setup
				int self = overallWallMap[y][x];
				int tempSelf = self;
				// Determine if a crawling tile
				boolean crawlable = false;
				for (int i : crawlableTiles) {
					if (self == i) crawlable = true;
				}
				
				// Only operates if crawlable
				if (crawlable) {
					// Check north
					if (y > 1) {
						if (overallWallMap[y - 1][x] == self) {
							tempSelf += 1;
						}
					} else {
						tempSelf += 1;
					}
					// Check east
					if (x + 1 < overallWallMap[0].length) {
						if (overallWallMap[y][x + 1] == self) {
							tempSelf += 2;
						}
					} else {
						tempSelf += 2;
					}
					// Check south
					if (y + 1 < overallWallMap.length) {
						if (overallWallMap[y + 1][x] == self) {
							tempSelf += 4;
						}
					} else {
						tempSelf += 4;
					}
					// Check west
					if (x > 1) {
						if (overallWallMap[y][x - 1] == self) {
							tempSelf += 8;
						}
					} else {
						tempSelf += 8;
					}
				}
				// Add the tempSelf to the tempMap
				tempMap[y][x] = tempSelf;
			}
		}
		// Set the real map to be equal to the temp
		for (int y = 0; y < tempMap.length; y++) {
			for (int x = 0; x < tempMap[0].length; x++) {
				overallWallMap[y][x] = tempMap[y][x];
			}
		}
		// Some objects are drawn on the wallmap. Move these to the appropiate maps
		int[][] wallObjects = new int[][] {
			// wallTextureID, objectTextureID, isSolid?
			{67, 4, 1}, // townpot
			{69, 6, 0}, // grasshole
			{77, 7, 0}, // bluedoor
			{86, 8, 0}, // cyandoor
			{95, 9, 0}, // purpledoor
			{104, 10, 0}, // pinkdoor
			{114, 11, 0}, // specialtydoor
			{133, 12, 0}, // house exit mat
			{134, 13, 0}, // terror plant
			{135, 14, 0}, // mad dog
			{136, 15, 0}, // critter
			{137, 16, 0}, // glass dungeon door
			{147, 17, 0}, // dungeon door
			{165, 20, 0}, // dungeon pot
		};
		int[][] wallFloors = new int[][] {
			{68, 1}, // flowers
		};
		
		for (int y = 0; y < overallWallMap.length; y++) {
			for (int x = 0; x < overallWallMap[0].length; x++) {
				int self = overallWallMap[y][x];
				// Check if the wall is actually an object
				boolean isWallObject = false;
				int spawnmapID = -1;
				for (int[] i : wallObjects) {
					if (self == i[0]) {
						isWallObject = true;
						spawnmapID = i[1];
					}
				}
				// Check if wall is actually a floor
				boolean isWallFloor = false;
				int floorID = -1;
				for (int[] i : wallFloors) {
					if (self == i[0]) {
						isWallFloor = true;
						floorID = i[1];
					}
				}
				if (isWallObject) {
					// Remove from wallmap
					overallWallMap[y][x] = 0;
					// Tranfer to spawnmap
					overallSpawnMap[y][x] = spawnmapID;
				}
				if (isWallFloor) {
					// Remove from wallmap
					overallWallMap[y][x] = 0;
					// Tranfer to spawnmap
					overallFloorMap[y][x] = floorID;
				}
			}
		}
		 
	}
	
	

}
