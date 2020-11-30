package states;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.sound.sampled.FloatControl;

import engine.Main;
import util.MapGenerator;
import util.Style;
import util.Textures;

public abstract class DialogueHall extends Level {

	protected String[][] textArrays;
	
	public ArrayList<Integer> roomCentersX = new ArrayList<Integer>();
	
	BufferedImage inhabitantTexture;
	int inhabitantWidth;
	boolean delayedMusicStart = false;
	boolean musicStarted = false;
	boolean hideUntilEnd = false;
	
	public DialogueHall(Main main, Dimension roomMapSize, String[][] textArrays, boolean musicContinues) {
		super(main, roomMapSize);
		this.textArrays = textArrays;
		// Camera
		initializeCamera();
		roomTextIsGui = false;
		// Create roommap
		int centerY = roomMap.length / 2;
		for (int x = 0; x < textArrays.length; x++) {
			roomMap[centerY][x] = 1;
			if (x == 0) {
				doorwayMap[centerY][x] = 2;
			} else if (x == textArrays.length - 1) {
				doorwayMap[centerY][x] = 8;
			} else {
				doorwayMap[centerY][x] = 10;
			}
		}
		// Derivate tilemap from roommap
		MapGenerator.applyToTileMap(3, roomMapSize, roomMap, doorwayMap, overallWallMap, overallFloorMap,
				overallSpawnMap);
		
		// Spawned objects
		initializeSpawnedObjects();
		// Player
		initializePlayer();
		// Ghost texture
		initializeInhabitantTexture();
		// Music
		if (!musicContinues) {
			Main.currentSong.stop();
			Main.currentSong = Main.ghost;
			FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
	        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
	        volume.setValue(dB);
			Main.currentSong.setMicrosecondPosition(0);
			Main.currentSong.start();
			Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
		}
	}
	
	protected void initializeInhabitantTexture() {
		inhabitantTexture = Textures.ghost0;
		inhabitantWidth = tileRes;
	}
	
	protected void repositionPlayer() {
		player.bounds.x = tileRes * 3;
	}
	
	protected void renderRoomText(Graphics2D g) {
		double textSize = (tileRes * 0.6);
		double spacingHeight = (tileRes * 0.6);
		g.setFont(Style.righteous);
		g.setFont(g.getFont().deriveFont((float)textSize));
		g.setColor(Style.white);
		
		for (int room = 0; room < textArrays.length; room++) {
			int drawX = -1;
			int textWidth = -1;
			for (int line = 0; line < textArrays[room].length; line++) {
				String text = textArrays[room][line];
				textWidth = g.getFontMetrics().stringWidth(text);
				int spacingOffset = (int) (spacingHeight * line);
				drawX  = Main.width/2 - textWidth/2 + (Main.width * room);
				int drawY = (int)(tileRes * (tileMapSize.height / 2) + tileRes * (localMapSize.height / 2) + spacingOffset);
				g.drawString(text, drawX, drawY);
			}
			roomCentersX.add(drawX + textWidth / 2);
		}
	}
	
	protected void renderRoomInhabitant(Graphics2D g) {
		// I want to make the ghost seem to appear and reappear
		int renderRange = tileRes * 5;
		boolean playerInRenderRange = false;
		for (int n : roomCentersX) {
			int deltaX = n - player.bounds.x - tileRes / 2;
			if (Math.abs(deltaX) < renderRange) {
				playerInRenderRange = true;
			}
		}

		
		// Renders if in range
		if (playerInRenderRange) {
			Point pos = new Point(Main.width / 2 - tileRes / 2, (int)(tileRes * (tileMapSize.height / 2) + tileRes * (localMapSize.height / 2) - tileRes * 2));
			for (int i = 0; i < textArrays.length; i++) {
				// Skips the render if hiding until end
				if (hideUntilEnd) { 
					if (i >= textArrays.length - 3) {
						g.drawImage(inhabitantTexture, pos.x + Main.width * i - inhabitantWidth / 2, pos.y, inhabitantWidth, tileRes, main);
					}
				} else {
					g.drawImage(inhabitantTexture, pos.x + Main.width * i - inhabitantWidth / 2, pos.y, inhabitantWidth, tileRes, main);
				}
			}
		}
		
		// Delayed start final song
		if (hideUntilEnd) {
			if (player.bounds.x > Main.width * 5) {
				if (!musicStarted) {
					Main.currentSong.stop();
					Main.currentSong = Main.glassDungeon;
					FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
			        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
			        volume.setValue(dB);
					Main.currentSong.setMicrosecondPosition(0);
					Main.currentSong.start();
					Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
					musicStarted = true;
				}
			}
		}
		
	}
	
	

}
