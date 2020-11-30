package states;

import java.awt.Dimension;
import java.awt.Point;

import engine.Main;
import objects.GameObject;
import util.MapGenerator;
import util.Textures;
import utilmisc.Vect2D;

public class Specialty extends Level {
	
	public Specialty(Main main, Dimension roomMapSize) {
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
		// Player
		initializePlayer();
		// Spawned objects
		initializeSpawnedObjects();
	}
	
	// Player initialization
	protected void repositionPlayer() {
		player.bounds.setLocation(tileRes * (tileMapSize.width / 2) + tileRes * (localMapSize.width / 2) - tileRes,
				tileRes * (tileMapSize.height / 2) + tileRes * (localMapSize.height - 2));
	}

}
