package states;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.sound.sampled.FloatControl;

import engine.Main;
import objects.GameObject;
import util.MapGenerator;
import util.Textures;
import utilmisc.Vect2D;

public class BasicDungeon extends Level {
	
	public BasicDungeon(Main main, Dimension roomMapSize) {
		super(main, roomMapSize);
		// Camera
		initializeCamera();
		// Tile map generation
		MapGenerator og = new MapGenerator(1, roomMapSize, tileMapSize);
		roomMap = og.roomMap;
		doorwayMap = og.doorwayMap;
		overallWallMap = og.overallWallMap;
		overallFloorMap = og.overallFloorMap;
		overallSpawnMap = og.overallSpawnMap;
		// Player
		initializePlayer();
		// Spawned objects
		initializeSpawnedObjects();
		// Music
		Main.currentSong.stop();
		Main.currentSong = Main.crypt;
		FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
        volume.setValue(dB);
		Main.currentSong.setMicrosecondPosition(0);
		Main.currentSong.start();
		Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
	}
}
