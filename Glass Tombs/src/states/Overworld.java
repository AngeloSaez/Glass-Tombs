package states;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;

import engine.Main;
import objects.GameObject;
import util.MapGenerator;
import util.Textures;
import utilmisc.Vect2D;

public class Overworld extends Level {
	
	public Overworld(Main main, Dimension roomMapSize) {
		super(main, roomMapSize);
		// Camera
		initializeCamera();
		// Tile map generation
		MapGenerator og = new MapGenerator(0, roomMapSize, tileMapSize);
		roomMap = og.roomMap;
		doorwayMap = og.doorwayMap;
		overallWallMap = og.overallWallMap;
		overallFloorMap = og.overallFloorMap;
		overallSpawnMap = og.overallSpawnMap;
		// Player
		initializePlayer();
		// Spawned objects
		initializeSpawnedObjects();
		// Add glass dungeon to the map
		playerTraveledRooms.add(new Point(Main.glassDungeonRoom.rx, Main.glassDungeonRoom.ry));
	}
	
	protected void repositionPlayer() {
		// Positions the player 3 in front a door
		Point pRoom = getPlayerRoom();
		int doorwayClass = doorwayMap[pRoom.x][pRoom.y];
		if (doorwayClass % 2 != 0) {
			player.bounds.y -= tileRes * 4;
		} else if (doorwayClass >= 8) {
			player.bounds.x -= tileRes * 6;
		} else if (doorwayClass >= 4) {
			player.bounds.y += tileRes * 3;
		} else {
			player.bounds.x += tileRes * 5;
		}
	}

}
