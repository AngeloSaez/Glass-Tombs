package states;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.sound.sampled.FloatControl;

import engine.Main;
import objects.GameObject;
import objects.ObjectSpawnManager;
import util.GameState;
import util.Style;
import util.Textures;
import utilmisc.BranchData;
import utilmisc.Vect2D;

public abstract class Level extends GameState {
	
	// Control booleans 
	public boolean north, east, west, south;
	public boolean primaryButton;
	
	// Player menu
	public boolean playerMenu = false;
	
	// Debug control booleans
	public boolean mapMenuToggled = false;
	public boolean doorwayMapMenuToggled = false;
	public boolean hitBoxViewToggled = false;
	
	// Essential objects
	public GameObject camera;
	public GameObject player;
	public boolean roomTextIsGui = true;
	
	// Smooth camera
	public boolean cameraInitialized = false;
	public boolean cameraTransitioning = false;
	public Point newCameraBounds;
	public int transitionDirection = -1;
	
	public int transitionStepMax = 60;
	public int transitionStep = 0;
	
	// Maps
	public Dimension roomMapSize;
	public int[][] roomMap;
	public int[][] doorwayMap;
	public Dimension tileMapSize;
	public int[][] overallWallMap;
	public int[][] overallFloorMap;
	public int[][] overallSpawnMap;
	
	// Lists
	public ArrayList<Point> playerTraveledRooms = new ArrayList<Point>();
	public ArrayList<Rectangle> solidWalls = new ArrayList<Rectangle>();
	public ArrayList<GameObject> solidObjects = new ArrayList<GameObject>();
	public ArrayList<GameObject> dynamic = new ArrayList<GameObject>();
	public ArrayList<GameObject> attacking = new ArrayList<GameObject>();
	public ArrayList<GameObject> animated = new ArrayList<GameObject>();
	public ArrayList<GameObject> spawnedObjects = new ArrayList<GameObject>();
	public ArrayList<GameObject> hurtObjects = new ArrayList<GameObject>();
	public ArrayList<GameObject> dungeonEntrances = new ArrayList<GameObject>();
	public ArrayList<GameObject> houseEntrances = new ArrayList<GameObject>();
	public ArrayList<GameObject> specialtyEntrances = new ArrayList<GameObject>();
	public ArrayList<GameObject> objectsForAdding = new ArrayList<GameObject>();
	public ArrayList<BufferedImage> foregroundWallTextures = new ArrayList<BufferedImage>();
	public ArrayList<Rectangle> foregroundWallBounds = new ArrayList<Rectangle>();
	public ArrayList<GameObject> foregroundSpawnedObjects = new ArrayList<GameObject>();
	
	// Current song varition
	public String songVariation = "null";

	
	// Initialization
	public Level(Main main, Dimension roomMapSize) {
		super(main);
		this.roomMapSize = roomMapSize;
		roomMap = new int[roomMapSize.height][roomMapSize.width];
		doorwayMap = new int[localMapSize.height * roomMapSize.height][localMapSize.width * roomMapSize.width];
		tileMapSize = new Dimension(localMapSize.width * roomMapSize.width, localMapSize.height * roomMapSize.height);
		overallWallMap = new int[tileMapSize.height][tileMapSize.width];
		overallFloorMap = new int[tileMapSize.height][tileMapSize.width];
		overallSpawnMap = new int[tileMapSize.height][tileMapSize.width];
	}
	
	// Player initialization
	protected void initializePlayer() {
		player = new GameObject(main);
		player.bounds.width = tileRes;
		player.bounds.height = tileRes;
		player.bounds.setLocation(tileRes * (tileMapSize.width / 2) + tileRes * (localMapSize.width / 2),
				tileRes * (tileMapSize.height / 2) + tileRes * (localMapSize.height / 2));
		player.hitbox = new Dimension(player.bounds.width / 2, player.bounds.height / 2);
		player.hitboxOffset = new Point(player.bounds.width / 4, player.bounds.height / 4);
		player.hurtbox = new Dimension(tileRes, tileRes);
		player.acc = new Vect2D(1); // 1
		player.terminalVel = new Vect2D(8); // 8
		// Textures
		player.texture = Textures.playerSouth[0];
		player.north = Textures.playerNorth;
		player.south = Textures.playerSouth;
		player.east = Textures.playerEast;
		player.west = Textures.playerWest;
		player.attackNorth = Textures.playerAttackNorth;
		player.attackEast = Textures.playerAttackEast;
		player.attackSouth = Textures.playerAttackSouth;
		player.attackWest = Textures.playerAttackWest;
		// Add to the list of dynamic objects
		dynamic.add(player);
		animated.add(player);
		attacking.add(player);
		// Gameplay
		player.maxHealth = Main.maxPlayerHealth;
		player.health = Main.playerHealth;
		player.hasAttack = true;
		// Reposition playcer if needed
		repositionPlayer();
	}
	
	protected void repositionPlayer() {
		// Abstract function
	}
	
	// Misc initialization
	protected void initializeSpawnedObjects() {
		ObjectSpawnManager osm = new ObjectSpawnManager(main);
		for (int y = 0; y < tileMapSize.height; y++) {
			for (int x = 0; x < tileMapSize.width; x++) {
				int spawnMapID = overallSpawnMap[y][x];
				if (spawnMapID != 0) {
					GameObject object = osm.getSpawnType(spawnMapID);
					object.bounds.setLocation(new Point(x * tileRes, y * tileRes));
					// Adds objects to appropiate lists
					switch (object.spawnMapID) {
					case 10: // Terror plant
						animated.add(object);
						break;
					case 11: // Mad Dog
						dynamic.add(object);
						animated.add(object);
						break;
					case 12: // Critter
						dynamic.add(object);
						animated.add(object);
						break;
					case 4: 
						solidObjects.add(object);
						break;
					case 15: 
						solidObjects.add(object);
						break;
					}
					// Add to the list of spawned objects
					spawnedObjects.add(object);
				}
			}
		}
	}
	
	protected void initializeCamera() {
		camera = new GameObject(main);
		camera.bounds.setBounds(0, 0, Main.width, Main.height);
	}
	
	// Render
	public void render(Graphics2D g) {
		// Camera
		translateCamera(g);
		
		// Render the floors
		renderFloors(g);
		
		// Render rooms
		if (roomTextIsGui) {
			revertCamera(g);
			renderRoomText(g);
			renderRoomInhabitant(g);
			translateCamera(g);
		} else {
			renderRoomInhabitant(g);
			renderRoomText(g);
		}
		
		// Render the walls
		renderWalls(g);

		// Render player
		player.draw(g);
		
		// Render foreground walls and objects
		renderForegroundWalls(g);
		
		// Render spawned objects
		for (GameObject obj : spawnedObjects) {
			if (inRenderBounds(obj.bounds.getLocation())) {
				obj.draw(g);
				if (hitBoxViewToggled) {
					g.setColor(Color.red);
					Rectangle hurt = obj.getHitbox();
					g.drawRect(hurt.x, hurt.y, hurt.width, hurt.height);
				}
			}
		}
		
		// Render player hitbox
		if (hitBoxViewToggled) {
			g.setColor(Color.green);
			Rectangle h = player.getHitbox();
			g.drawRect(h.x, h.y, h.width, h.height);
			g.setColor(Color.red);
			Rectangle hurt = player.getHurtbox();
			g.drawRect(hurt.x, hurt.y, hurt.width, hurt.height);
		}
		
		// Reverts camera
		revertCamera(g);
		 
		// HUD
		renderHud(g);
		
		// Player menu
		renderPlayerMenu(g);
		
		// Debug Menus
		renderMapMenu(g); 
		renderDoorwayMapMenu(g);
	}
	
	protected boolean inRenderBounds(Point p) {
		int marginSize = 1;
		Rectangle renderBoundsCameraBased = new Rectangle(-camera.bounds.x - (tileRes * marginSize), -camera.bounds.y + guiBarSize * tileRes - (tileRes * marginSize), Main.width + (tileRes * marginSize * 2), Main.height - guiBarSize * tileRes + (tileRes * marginSize * 2));
		return renderBoundsCameraBased.contains(p);
	}
	
	protected void renderFloors(Graphics2D g) {
		for (int y = 0; y < tileMapSize.height; y++) {
			for (int x = 0; x < tileMapSize.width; x++) {
				int textureIndex = overallFloorMap[y][x];
				if (inRenderBounds(new Point(x * tileRes, y * tileRes))) {
					g.drawImage(Textures.floors.get(textureIndex), x * tileRes, y * tileRes, tileRes, tileRes, main);
				}
			}
		}
	}
	
	protected void renderWalls(Graphics2D g) {
		// Clear list of solid tiles
		solidWalls.clear();
		// Clear the list of foreground walls
		foregroundWallBounds.clear();
		foregroundWallTextures.clear();
		// Search tilemap 
		for (int y = 0; y < tileMapSize.height; y++) {
			for (int x = 0; x < tileMapSize.width; x++) {
				int textureIndex = overallWallMap[y][x];
				if (textureIndex != 0) {
					Point p = new Point(x * tileRes, y * tileRes);
					// Checks if in the bounds of rendering
					if (inRenderBounds(p)) {
						// Perspective: check if rendering needs to happen later
						if (p.y > player.bounds.y) {
							foregroundWallBounds.add(new Rectangle(x * tileRes, y * tileRes, tileRes, tileRes));
							foregroundWallTextures.add(Textures.walls.get(textureIndex));
						}
						// Or render normally
						else {
							g.drawImage(Textures.walls.get(textureIndex), x * tileRes, y * tileRes, tileRes, tileRes, main);
						}
						solidWalls.add(new Rectangle(x * tileRes, y * tileRes, tileRes, tileRes));
					}
				}
			}
		}
	}
	
	protected void renderMapMenu(Graphics2D g) {
		// Map menu must be toggled
		if (!mapMenuToggled) {
			return;
		}
		// Style
		int mapMenuRes = ((Main.height - tileRes * 2) / roomMapSize.height) / 4;
		Point mapOffset = new Point(tileRes / 2, Main.height - mapMenuRes * roomMapSize.height - tileRes /2);
		// Outline of the overworlds roommap
		for (int y = 0; y < roomMapSize.height; y++) {
			for (int x = 0; x < roomMapSize.width; x++) {
				int roomMarker = roomMap[y][x];
				if (roomMarker != 0) {
					// Color the map tiles
					Point p = new Point(x, y);
					if (playerTraveledRooms.contains(p)) {
						// Glass dungeon
						BranchData glassDungeonRoom = Main.glassDungeonRoom;
						if (y == glassDungeonRoom.ry && x == glassDungeonRoom.rx && this instanceof Overworld) {
							g.setColor(Style.lightCyan);
						} else if (roomMarker == 1) {
							g.setColor(Style.lightOrange);
						} else {
							g.setColor(Style.lightGreen);
						}
					} else {
						g.setColor(Style.darkGray);
					}
					g.fillRect(mapOffset.x + x * mapMenuRes, mapOffset.y + y * mapMenuRes, mapMenuRes, mapMenuRes);
					// Player room is white
					Point playerRoom = getPlayerRoom();
					if (x == playerRoom.x && y == playerRoom.y && System.currentTimeMillis() / 250 % 2 == 0) {
						g.setColor(Style.white);
						g.fillRect(mapOffset.x + x * mapMenuRes, mapOffset.y + y * mapMenuRes, mapMenuRes, mapMenuRes);
					}
					// Outline
					g.setColor(Style.deepPurple);
					g.drawRect(mapOffset.x + x * mapMenuRes, mapOffset.y + y * mapMenuRes, mapMenuRes, mapMenuRes);
				} else {
					g.setColor(Style.deepPurple);
					g.fillRect(mapOffset.x + x * mapMenuRes, mapOffset.y + y * mapMenuRes, mapMenuRes, mapMenuRes);
				}
			}
		}
	}
	
	protected void renderDoorwayMapMenu(Graphics2D g) {
		// Map menu must be toggled
		if (!doorwayMapMenuToggled) {
			return;
		}

		// Style
		int mapMenuRes = 32;
		g.setColor(Color.pink);
		for (int y = 0; y < roomMapSize.height; y++) {
			for (int x = 0; x < roomMapSize.width; x++) {
				// Outline of the overworlds roommap
				g.drawRect(-300 + x * mapMenuRes, -200 + y * mapMenuRes, mapMenuRes, mapMenuRes);
				g.drawString("" + doorwayMap[y][x], -300 + x * mapMenuRes, -200 + y * mapMenuRes);
			}
		}
	}
	
	protected void renderHud(Graphics2D g) {
		// GuiBar (Background)
		g.setColor(Style.deepPurple);
		int barWidth = tileRes * localMapSize.width;
		g.fillRect(0, 0, barWidth, tileRes * Main.guiBarSize);
		
		// Health
		g.setFont(Style.righteous);
		g.setFont(g.getFont().deriveFont((float)(tileRes * 0.5)));
		g.setColor(Style.lightPink);
		g.drawString("HP: ", tileRes / 2, (int)(tileRes * 0.75));
		for (int i = 1; i <= player.health; i++) {
			g.drawImage(Textures.guiImages.get(0), tileRes / 2 * i + tileRes, (int)(tileRes * 0.25), (int)(tileRes * 0.5), (int)(tileRes * 0.5), main);
		}
		
		// Money
		g.setColor(Style.lightGreen);
		g.drawString(": " + Main.money, tileRes / 2 * (player.maxHealth + 3) + tileRes, (int)(tileRes * 0.75));
		g.drawImage(Textures.guiImages.get(6), tileRes / 2 * (player.maxHealth + 2) + tileRes, (int)(tileRes * 0.25), (int)(tileRes * 0.5), (int)(tileRes * 0.5), main);

		
		// Menu Label
		g.setColor(Style.white);
		String menuLabel = "MAP: [X]";
		int menuLabelWidth = g.getFontMetrics().stringWidth(menuLabel);
		int menuLabelDrawX = barWidth - tileRes / 2 - menuLabelWidth;
		g.drawString(menuLabel, menuLabelDrawX, (int)(tileRes * 0.75));
		
		// Hand Label
		int handLabelDrawX = menuLabelDrawX - tileRes - tileRes / 2;
		int handImageDrawX = handLabelDrawX - (int)(tileRes * 0.5);
//		g.drawString(": [X]", handLabelDrawX, (int)(tileRes * 0.75));
//		g.drawImage(Textures.guiImages.get(2), handImageDrawX, (int)(tileRes * 0.25), (int)(tileRes * 0.5), (int)(tileRes * 0.5), main);
//		
		// Sword Label
		if (player.attackActive) g.setColor(Style.gray);
		int swordLabelDrawX = handImageDrawX - (int)(tileRes * 1.5);
		g.drawString(": [Z]", handLabelDrawX, (int)(tileRes * 0.75));
		if (player.attackActive) {
			g.drawImage(Textures.guiImages.get(2), handImageDrawX, (int)(tileRes * 0.25), (int)(tileRes * 0.5), (int)(tileRes * 0.5), main);
		} else {
			g.drawImage(Textures.guiImages.get(1), handImageDrawX, (int)(tileRes * 0.25), (int)(tileRes * 0.5), (int)(tileRes * 0.5), main);
		}
//		g.drawString(": [Z]", swordLabelDrawX, (int)(tileRes * 0.75));
//		g.drawImage(Textures.guiImages.get(1), swordLabelDrawX - (int)(tileRes * 0.5), (int)(tileRes * 0.25), (int)(tileRes * 0.5), (int)(tileRes * 0.5), main);

	}
	
	protected void renderPlayerMenu(Graphics2D g) {
		if (!playerMenu) return;
		g.drawImage(Textures.guiImages.get(5), 0, tileRes * Main.guiBarSize,  tileRes * localMapSize.width, tileRes * localMapSize.height, main);
		g.drawImage(Textures.guiImages.get(4), 0, tileRes * Main.guiBarSize,  tileRes * localMapSize.width, tileRes * localMapSize.height, main);
		// Outline of the overworlds roommap
		int mapLength = tileRes * 6;
		int mapMenuRes = (mapLength / roomMapSize.height);
		Point mapOffset = new Point(tileRes * 5, tileRes * 4);
		for (int y = 0; y < roomMapSize.height; y++) {
			for (int x = 0; x < roomMapSize.width; x++) {
				int roomMarker = roomMap[y][x];
				// Leave out rooms not in map
				if (roomMarker != 0) {
					// Color the map tiles
					Point p = new Point(x, y);
					if (playerTraveledRooms.contains(p)) {
						// Glass dungeon
						BranchData glassDungeonRoom = Main.glassDungeonRoom;
						if (y == glassDungeonRoom.ry && x == glassDungeonRoom.rx && this instanceof Overworld) {
							g.setColor(Style.lightCyan);
						} else if (roomMarker == 1) {
							g.setColor(Style.lightOrange);
						} else {
							g.setColor(Style.lightGreen);
						}
					} else {
						g.setColor(Style.gray);
					}
					g.fillRect(mapOffset.x + x * mapMenuRes, mapOffset.y + y * mapMenuRes, mapMenuRes, mapMenuRes);
					// Outline
					g.setColor(Style.white);
					g.drawRect(mapOffset.x + x * mapMenuRes, mapOffset.y + y * mapMenuRes, mapMenuRes, mapMenuRes);
				}
			}
		}
		// Player room is white
		Point playerRoom = getPlayerRoom();
		if (System.currentTimeMillis() / 250 % 2 == 0) {
			g.setColor(Style.darkGray);
			g.drawRect(mapOffset.x + playerRoom.x * mapMenuRes, mapOffset.y + playerRoom.y * mapMenuRes, mapMenuRes, mapMenuRes);
		} 
		
		// Mini map hint
		g.setColor(Style.white);
		g.drawString("Toggle mini map: [M]", tileRes / 2, Main.height - tileRes);
		g.drawString("Exit game: [Backspace]", tileRes / 2, Main.height - tileRes / 2);
		
	}
	
	protected void renderForegroundWalls(Graphics2D g) {
		for (int i = 0; i < foregroundWallBounds.size(); i++) {
			Rectangle r = foregroundWallBounds.get(i);
			g.drawImage(foregroundWallTextures.get(i), r.x, r.y, r.width, r.height, main);
		}
	}
	
	// House / Specialty render
	protected void renderRoomText(Graphics2D g) {
		
	}
 
	protected void renderRoomInhabitant(Graphics2D g) {
		
	}
	
	// Update
	public void update() {
		// Player in menu
		if (playerMenu) {
			
		} 
		// Not in player menu
		else {
			if (!cameraTransitioning) {
				// Normal game update
				player.accelerate(north, east, south, west);
				
				spawnedObjectUpdate();
				
				checkSpawnedObjectCollisions();
				
				translateDynamicObjects();
				
				animateAnimatedObjects();
				
				//cpoc() was here
				
				removeSpawnedObjects();
				
				addSpawnedObjects();
				
				updatePlayerTraveledRooms();
			}
			
			followCamera(player);
		}
	}
	
	protected void translateDynamicObjects() {
		for (GameObject obj : dynamic) {
			// Returns if a spawned mob and not on screen
			if (obj.spawnMapID != -1) {
				if (!inRenderBounds(obj.bounds.getLocation())) {
					continue;
				}
			}
			// Translation
			obj.translateHorizontal();
			obj.horizontalCollisions(solidWalls, solidObjects);
			obj.translateVertical();
			obj.verticalCollisions(solidWalls, solidObjects);
		}
	}
	
	protected void animateAnimatedObjects() {
		for (GameObject obj : animated) {
			obj.animate();
		}
	}
	
	protected void checkSpawnedObjectCollisions() {
		hurtObjects = new ArrayList<GameObject>();
		
		for (GameObject obj : spawnedObjects) {
			// Player and Obj hitbox collisions
			if (player.getHitbox().intersects(obj.getHitbox())) {
				spawnedObjectCollisionTrigger(obj);
			}
			// Obj touches Player hurtbox collisions while attacking
			if (player.getHurtbox().intersects(obj.getHitbox()) && player.attackActive) {
				// Register the object as hurt
				obj.health--;
				hurtObjects.add(obj);
				// Knockback
				Point playerCenter = new Point(player.bounds.x + player.bounds.width / 2, player.bounds.y + player.bounds.height / 2);
				double hDist = (playerCenter.x - (obj.bounds.x + obj.bounds.width / 2));
				double vDist = (playerCenter.y - (obj.bounds.y + obj.bounds.height / 2));
				double theta = Math.atan(Math.abs(vDist) / Math.abs(hDist));
				double kbi = Math.cos(theta) * (int)(tileRes);
				if (hDist > 0) kbi *= -1;
				obj.vel.i += kbi;
				double kbj = Math.sin(theta) * (int)(tileRes);
				if (vDist > 0) kbj *= -1;
				obj.vel.j += kbj;
				// End the players attack
				player.attackActive = false;
				player.attackStartTime = -1;
			}
		}
		
	}
	
	protected void spawnedObjectUpdate() {
		for (GameObject obj : spawnedObjects) {
			// Returns if not in the render
			if (!inRenderBounds(obj.bounds.getLocation())) continue;
			// Performs an ID-specific update per object
			switch (obj.spawnMapID) {
			case 10:
				// Shooting
				obj.updateShooting(player, objectsForAdding, dynamic, animated);
				break;
			case 11: // Mad Dog
				int variableFollowOffset = obj.runSpeed + (int)(2 * tileRes * obj.randomTrait);
				// Horizontal follow player
				int hDist = player.bounds.x - obj.bounds.x;
				if (Math.abs(hDist) > obj.runSpeed + variableFollowOffset) {
					if (hDist > 0) {
						obj.vel.i = obj.runSpeed;
					} else if (hDist < 0) {
						obj.vel.i = -obj.runSpeed; 
					}
				}
				// Vertical follow player
				int vDist = player.bounds.y - obj.bounds.y;
				if (Math.abs(vDist) > obj.runSpeed + variableFollowOffset) {
					if (vDist > 0) {
						obj.vel.j = obj.runSpeed;
					} else if (vDist < 0) {
						obj.vel.j = -obj.runSpeed;
					}
				}
				break;
			case 12: // Critter
				// Random depending on time
				int crawlDir = (int)((System.currentTimeMillis() / 750) % 4);
				// Add the random trait to offset direction
				crawlDir += (int) (obj.randomTrait * 4);
				// More random
				int crawlModifier2 = (int)((System.currentTimeMillis() / 750) % 8);
				if (crawlModifier2 == 7) {
					crawlDir -=2;
				}
				// Make it conform to range(4)
				crawlDir %= 4;
				
				switch(crawlDir) {
				case 0:
					obj.vel.i = 0;
					obj.vel.j = -obj.runSpeed;
					break;
				case 3:
					obj.vel.i = obj.runSpeed;
					obj.vel.j = 0;
					break;
				case 2:
					obj.vel.i = 0;
					obj.vel.j = obj.runSpeed;
					break;
				case 1:
					obj.vel.i = -obj.runSpeed;
					obj.vel.j = 0;
					break;
				}
				// Shoot
				obj.updateShooting(player, objectsForAdding, dynamic, animated);
				break;
			}
			// Normal attacking
			obj.updateAttack();
		}
	}
	
	protected void removeSpawnedObjects() {
		// Remove projectiles
		for (GameObject obj : dynamic) {
			if (obj.birthTime != -1) {
				long elapsedTime = System.currentTimeMillis() - obj.birthTime;
				if (elapsedTime > obj.lifeSpan) {
					obj.health = 0;
					obj.killable = true;
					hurtObjects.add(obj);
				}
			}
		}
		// Remove objects with no health left
		for (GameObject obj : hurtObjects) {
			if (obj.health <= 0 && obj.killable) {
				spawnedObjects.remove(obj);
				// Replaces it with loot
				if (obj.lootTableVarient != 0) {
					ObjectSpawnManager osm = new ObjectSpawnManager(main);
					spawnedObjects.add(osm.getLootObject(obj));
				}
				// Removes from the solid
				if (solidObjects.contains(obj)) {
					solidObjects.remove(obj);
				}
			}
		}
	}
	
	protected void updatePlayerTraveledRooms() {
		Point p = getPlayerRoom();
		if (!playerTraveledRooms.contains(p)) {
			playerTraveledRooms.add(p);
		}
	}
	
	protected void updateMusic() {
		Point p = getPlayerRoom();
		// Update music
		if (this instanceof Overworld) {
			// Initially set to town
			if (songVariation.equals("null")) {
				Main.currentSong.stop();
				Main.currentSong = Main.town;
				FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
		        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
		        volume.setValue(dB);
				Main.currentSong.setMicrosecondPosition(0);
				Main.currentSong.start();
				Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
				songVariation = "town";
			}
			// End the music if its a glass dungeon path
			if (roomMap[p.y][p.x] == Main.glassDungeonMarker && !songVariation.equals("flowers")) {
				Main.currentSong.stop();
				Main.currentSong = Main.flowers;
				FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
		        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
		        volume.setValue(dB);
				Main.currentSong.setMicrosecondPosition(0);
				Main.currentSong.start();
				Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
				songVariation = "flowers";
			}
			// Anywhere to overworld
			else if (roomMap[p.y][p.x] != 1 && !songVariation.equals("overworld") && roomMap[p.y][p.x] != Main.glassDungeonMarker) {
				Main.currentSong.stop();
				Main.currentSong = Main.overworld;
				FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
		        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
		        volume.setValue(dB);
				Main.currentSong.setMicrosecondPosition(0);
				Main.currentSong.start();
				Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
				songVariation = "overworld";
			}
			// Anywhere to town
			else if (roomMap[p.y][p.x] == 1 && !songVariation.equals("town")) {
				Main.currentSong.stop();
				Main.currentSong = Main.town;
				FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
		        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
		        volume.setValue(dB);
				Main.currentSong.setMicrosecondPosition(0);
				Main.currentSong.start();
				Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
				songVariation = "town";
			}
			// Overworld to outside a crypt
			else if (roomMap[p.y][p.x] != 1 && (doorwayMap[p.y][p.x] == 1 || doorwayMap[p.y][p.x] == 2
					|| doorwayMap[p.y][p.x] == 4 || doorwayMap[p.y][p.x] == 8) && roomMap[p.y][p.x] != Main.glassDungeonMarker) {
				long oldMicroPosition = Main.currentSong.getMicrosecondPosition();
				Main.currentSong.stop();
				Main.currentSong = Main.outsideCrypt;
				FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
		        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
		        volume.setValue(dB);
				Main.currentSong.setMicrosecondPosition(oldMicroPosition);
				Main.currentSong.start();
				Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
				songVariation = "outsideCrypt";
			}
		}
	}
	
	protected void addSpawnedObjects() {
		// In effort to avoid concurrent modification
		// Adds the temp list that stores objects to be added
		for (GameObject obj : objectsForAdding) {
			spawnedObjects.add(obj);
		}
		// Clears the temp list
		objectsForAdding.clear();
	}
	
	// Object Collisions
	protected void spawnedObjectCollisionTrigger(GameObject obj) {
		// Transitions
		switch (obj.spawnMapID) {
		case 13: // Overworld -> Glass Dungeon
			resetPlayerForDungeonTransition(obj);
			if (Main.storyStage == 0) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP1Realization(main));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			} else if (Main.storyStage == 1 || Main.storyStage == 2 || Main.storyStage == 3) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP2TaskReminder(main));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			} else if (Main.storyStage == 4) {
				// Determine dungeon index before transitioning to dungeon feeling
				int dungeonIndex;
				boolean isNewDungeon = !dungeonEntrances.contains(obj);
				if (isNewDungeon) {
					resetPlayerForDungeonTransition(obj);
					Main.gsm.dungeonStates.add(new GlassDungeon(main, new Dimension(8, 8)));
					dungeonIndex = Main.gsm.dungeonStates.size() - 1;
					dungeonEntrances.add(obj);
					// Transition to dungeon feeling
					// Move this block of code after the else statement to allow for reentering dungeons
					Main.gsm.introStates.clear();
					Main.gsm.introStates.add(new CHP3DungeonFeeling1(main, dungeonIndex));
					Main.gsm.currentState = Main.gsm.introStates.get(0);
				}
			}
			break;
		case 14: // Overworld -> BasicDungeon
			if (Main.storyStage == 0) {
				resetPlayerForDungeonTransition(obj);
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP1TaskReminder(main));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			} else if (Main.storyStage == 1) {
				// Determine dungeon index before transitioning to dungeon feeling
				int dungeonIndex;
				boolean isNewDungeon = !dungeonEntrances.contains(obj);
				if (isNewDungeon) {
					resetPlayerForDungeonTransition(obj);
					Main.gsm.dungeonStates.add(new BasicDungeon(main, new Dimension(8, 8)));
					dungeonIndex = Main.gsm.dungeonStates.size() - 1;
					dungeonEntrances.add(obj);
					// Transition to dungeon feeling
					// Move this block of code after the else statement to allow for reentering dungeons
					Main.gsm.introStates.clear();
					Main.gsm.introStates.add(new CHP2DungeonFeeling1(main, dungeonIndex));
					Main.gsm.currentState = Main.gsm.introStates.get(0);
				} else {
					dungeonIndex = dungeonEntrances.indexOf(obj);
				}
			} else if (Main.storyStage == 2) {
				// Determine dungeon index before transitioning to dungeon feeling
				int dungeonIndex;
				boolean isNewDungeon = !dungeonEntrances.contains(obj);
				if (isNewDungeon) {
					resetPlayerForDungeonTransition(obj);
					Main.gsm.dungeonStates.add(new BasicDungeon(main, new Dimension(8, 8)));
					dungeonIndex = Main.gsm.dungeonStates.size() - 1;
					dungeonEntrances.add(obj);
					// Transition to dungeon feeling
					// Move this block of code after the else statement to allow for reentering dungeons
					Main.gsm.currentState = Main.gsm.dungeonStates.get(dungeonIndex);;
				}
			} else if (Main.storyStage == 3) {
				// Determine dungeon index before transitioning to dungeon feeling
				int dungeonIndex;
				boolean isNewDungeon = !dungeonEntrances.contains(obj);
				if (isNewDungeon) {
					resetPlayerForDungeonTransition(obj);
					Main.gsm.dungeonStates.add(new BasicDungeon(main, new Dimension(8, 8)));
					dungeonIndex = Main.gsm.dungeonStates.size() - 1;
					dungeonEntrances.add(obj);
					// Transition to dungeon feeling
					// Move this block of code after the else statement to allow for reentering dungeons
					Main.gsm.currentState = Main.gsm.dungeonStates.get(dungeonIndex);;
				}
			}
			
			break;
		case 2: // Dungeon -> Ghost room
			if (Main.storyStage == 1) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP2GhostRoom1(main, new Dimension(8, 8)));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			if (Main.storyStage == 2) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP2GhostRoom2(main, new Dimension(8, 8)));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			if (Main.storyStage == 3) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP2GhostRoom3(main, new Dimension(8, 8)));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			if (Main.storyStage == 4) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP3GhostRoom1(main, new Dimension(8, 8)));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			break;
		case 3: // BasicDungeon/GhostRoom -> Overworld
			// After beating the first ghost room
			if (Main.storyStage == 1) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP2Realization1(main));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			if (Main.storyStage == 2) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP2Realization2(main));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			if (Main.storyStage == 3) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP3Intro(main));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			if (Main.storyStage == 5) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP3GhostRoom2(main, new Dimension(8, 8)));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			else if (Main.storyStage == 6) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP3GhostRoom3(main, new Dimension(8, 8)));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			else if (Main.storyStage == 7) {
				Main.gsm.introStates.clear();
				Main.gsm.introStates.add(new CHP3TheEnd(main));
				Main.gsm.currentState = Main.gsm.introStates.get(0);
			}
			// Music
			if (Main.storyStage < 5) {
				songVariation = "outsideCrypt";
				Main.currentSong.stop();
				Main.currentSong = Main.outsideCrypt;
				FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
		        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
		        volume.setValue(dB);
				Main.currentSong.setMicrosecondPosition(0);
				Main.currentSong.start();
				Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
			}
			break;
		case 7: // Overworld -> House
			resetPlayerForToHouseTransition(obj);
			boolean isNewHouse = !houseEntrances.contains(obj);
			if (isNewHouse) {
				Main.gsm.houseStates.add(new House(main, new Dimension(8, 8)));
				Main.gsm.currentState = Main.gsm.houseStates.get(Main.gsm.houseStates.size() - 1);
				houseEntrances.add(obj);
			} else {
				Main.gsm.currentState = Main.gsm.houseStates.get(houseEntrances.indexOf(obj)); // obj  + 1?
			}
			// Music
			long oldMicroposition = Main.currentSong.getMicrosecondPosition();
			Main.currentSong.stop();
			Main.currentSong = Main.house;
			FloatControl volume = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
	        float dB = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
	        volume.setValue(dB);
			Main.currentSong.setMicrosecondPosition(oldMicroposition);
			Main.currentSong.start();
			Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
			break;
		case 8: // Overworld -> Specialty
			resetPlayerForToHouseTransition(obj);
			boolean isNewSpecialty = !specialtyEntrances.contains(obj);
			if (isNewSpecialty) {
				Main.gsm.specialtyStates.add(new House(main, new Dimension(8, 8)));
				Main.gsm.currentState = Main.gsm.specialtyStates.get(Main.gsm.specialtyStates.size() - 1);
				specialtyEntrances.add(obj);
			} else {
				Main.gsm.currentState = Main.gsm.specialtyStates.get(specialtyEntrances.indexOf(obj)); // obj  + 1?
			}
			// Music
			long oldMicroposition2 = Main.currentSong.getMicrosecondPosition();
			Main.currentSong.stop();
			Main.currentSong = Main.house;
			FloatControl volume2 = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
	        float dB2 = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
	        volume2.setValue(dB2);
			Main.currentSong.setMicrosecondPosition(oldMicroposition2);
			Main.currentSong.start();
			Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
			break;	
		case 9: // House -> Overworld
			resetPlayerForFromHouseTransition(obj);
			Main.gsm.currentState = Main.gsm.mainStates.get(0);
			// Music
			long oldMicroposition3 = Main.currentSong.getMicrosecondPosition();
			Main.currentSong.stop();
			Main.currentSong = Main.town;
			FloatControl volume3 = (FloatControl) Main.currentSong.getControl(FloatControl.Type.MASTER_GAIN);
	        float dB3 = (float) (Math.log(Main.masterVolume) / Math.log(10.0) * 20.0);
	        volume3.setValue(dB3);
			Main.currentSong.setMicrosecondPosition(oldMicroposition3);
			Main.currentSong.start();
			Main.currentSong.loop(Main.currentSong.LOOP_CONTINUOUSLY);
			break;
		case 18: // Plant bubble
			damagePlayer(obj);
			obj.birthTime -= obj.lifeSpan;
			break;
		case 19: // Critter bubble
			damagePlayer(obj);
			obj.birthTime -= obj.lifeSpan;
			break;
		case 11: // Mad dog
			damagePlayer(obj);
			break;
		case 12: // Critter
			damagePlayer(obj);
			break;
		}
		// Loot
		if (obj.lootType != -1) {
			switch(obj.lootType + 1) {
			case 1: // Heart
				// Update both of the healths because only the one saved in Main is transfered and its easy this way
				int increment = player.health + 1;
				if (increment <= Main.maxPlayerHealth) {
					player.health = increment;
					Main.playerHealth = increment;
				}
				break;
			case 2: // Green money
				Main.money += 1;
				break;
			case 3: // Blue money
				Main.money += 5;
				break;
			case 4: // Red money
				Main.money += 20;
				break;
			}
			obj.lootType = -1;
			obj.health = 0;
			obj.killable = true;
			hurtObjects.add(obj);
		}
		
	}
	
	// Damage player
	protected void damagePlayer(GameObject obj) {
		// Decrement health
		int decrement = player.health - 1;
		if (decrement >= 0) {
			Main.playerHealth = decrement;
			player.health = decrement;
		} else {
			Main.gsm.introStates.clear();
			Main.gsm.introStates.add(new GameOver(main));
			Main.gsm.currentState = Main.gsm.introStates.get(0);
		}
		// Knockback
		Point playerCenter = new Point(player.bounds.x + player.bounds.width / 2, player.bounds.y + player.bounds.height / 2);
		double hDist = (playerCenter.x - (obj.bounds.x + obj.bounds.width / 2));
		double vDist = (playerCenter.y - (obj.bounds.y + obj.bounds.height / 2));
		double theta = Math.atan(Math.abs(vDist) / Math.abs(hDist));
		double kbi = Math.cos(theta) * (int)(tileRes / 2);
		if (hDist < 0) kbi *= -1;
		player.vel.i += kbi;
		double kbj = Math.sin(theta) * (int)(tileRes / 2);
		if (vDist < 0) kbj *= -1;
		player.vel.j += kbj;
	}
		
	// Transitions
	protected void resetPlayerForDungeonTransition(GameObject door) {
		// Gets the player ready to switch gameStates
		player.bounds.y = door.bounds.y + tileRes;
		player.vel.j = 0;
		north = false;
		east = false;
		south = false;
		west = false;
	}
	
	protected void resetPlayerForToHouseTransition(GameObject door) {
		// Gets the player ready to switch gameStates
		player.bounds.y = door.bounds.y + tileRes;
		player.directionFacing = "south";
		player.vel.j = 0;
		north = false;
		east = false;
		south = false;
		west = false;
	}
	
	protected void resetPlayerForFromHouseTransition(GameObject door) {
		// Gets the player ready to switch gameStates
		player.bounds.x = door.bounds.x - tileRes;
		north = false;
		east = false;
		south = false;
		west = false;
	}
	
	// Camera
	protected void followCamera(GameObject obj) {
		// If camera in the transitioning process
		if (cameraTransitioning) {
			int cameraStep = 8;
			switch(transitionDirection) {
			case 1:
				camera.bounds.y -= cameraStep;
				if (camera.bounds.y < newCameraBounds.y) cameraTransitioning = false;
				break;
			case 2:
				camera.bounds.x += cameraStep;
				if (camera.bounds.x > newCameraBounds.x) cameraTransitioning = false;
				break;
			case 4:
				camera.bounds.y += cameraStep;
				if (camera.bounds.y > newCameraBounds.y) cameraTransitioning = false;
				break;
			case 8:
				camera.bounds.x -= cameraStep;
				if (camera.bounds.x < newCameraBounds.x) cameraTransitioning = false;
				break;
			}
			// Set the camera position if transition complete
			if (!cameraTransitioning) {
				camera.bounds.setLocation(newCameraBounds);
			}
		}
		// Checking if the camera needs to transition
		else {
			int roomWidth = localMapSize.width * tileRes;
			int roomHeight = localMapSize.height * tileRes;
			double newBoundsX = ((int)(obj.bounds.x + obj.bounds.width / 2) / roomWidth) * roomWidth;
			double newBoundsY = ((int)(obj.bounds.y + obj.bounds.height / 2) / roomHeight) * roomHeight - guiBarSize * tileRes;
			Point newCameraBounds = new Point (-(int)newBoundsX, -(int)newBoundsY);
			// Setting camera to transition
			if (!camera.bounds.getLocation().equals(newCameraBounds)) {
				// Update music
				updateMusic();
				// Subsequent camera corrections
				if (cameraInitialized) { 
					this.newCameraBounds = newCameraBounds;
					cameraTransitioning = true;
					// Determine camera transition direction
					if (newCameraBounds.y < camera.bounds.y) {
						transitionDirection = 1;
					} else if (newCameraBounds.y > camera.bounds.y) {
						transitionDirection = 4;
					} else if (newCameraBounds.x > camera.bounds.x) {
						transitionDirection = 2;
					} else {
						transitionDirection = 8;
					}
				} 
				// The first camera correction
				else {
					camera.bounds.setLocation(newCameraBounds);
					cameraInitialized = true;
				}
			}
		}
	}
	
	protected void translateCamera(Graphics2D g) {
		g.translate((camera.bounds.x), (camera.bounds.y));
	}
	
	protected void revertCamera(Graphics2D g) {
		g.translate(-1 * (camera.bounds.x), -1 * (camera.bounds.y));
	}
	
	// Misc
	protected Point getPlayerRoom() {
		int roomWidth = localMapSize.width * tileRes;
		int roomHeight = localMapSize.height * tileRes;
		int x = (int)((int)(player.bounds.x + player.bounds.width/2) / roomWidth);
		int y = (int)((int)(player.bounds.y + player.bounds.height/2) / roomHeight);
		return new Point(x, y);
	}
	
	// Control
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		// Player attacking
		case KeyEvent.VK_Z:
			player.attackActive = true;
			if (player.attackStartTime == -1) {
				player.attackStartTime = System.currentTimeMillis();
			}
			break;
		// Player movement
		case KeyEvent.VK_UP:
			north = true;
			break;
		case KeyEvent.VK_RIGHT:
			east = true;
			break;
		case KeyEvent.VK_DOWN:
			south = true;
			break;
		case KeyEvent.VK_LEFT:
			west = true;
			break;
		// Menu controls
		case KeyEvent.VK_M:
			mapMenuToggled = !mapMenuToggled;
			break;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		// Menus
		case KeyEvent.VK_X:
			playerMenu = !playerMenu;
			break;
		case KeyEvent.VK_Z:
			player.attackActive = false;
			player.attackStartTime = -1;
			break;
		// Player controls
		case KeyEvent.VK_UP:
			north = false;
			break;
		case KeyEvent.VK_RIGHT:
			east = false;
			break;
		case KeyEvent.VK_DOWN:
			south = false;
			break;
		case KeyEvent.VK_LEFT:
			west = false;
			break;
		}
	}
	
}