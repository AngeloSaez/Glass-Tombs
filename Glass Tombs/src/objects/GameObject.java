package objects;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.Main;
import util.Textures;
import utilmisc.Vect2D;

public class GameObject {

	// Main
	public Main main;
	
	// SpawnMapID, if is a spawned object
	public int spawnMapID = -1;
	
	// Movement
	public Vect2D vel;
	public Vect2D acc;
	public Vect2D velOverflow;
	public Vect2D terminalVel;
	
	// Bounds
	public Rectangle bounds;
	public Dimension hitbox;
	public Point hitboxOffset;
	public Dimension hurtbox;
	
	// Appearance
	public BufferedImage texture;
	public BufferedImage[] north;
	public BufferedImage[] south;
	public BufferedImage[] east;
	public BufferedImage[] west;
	public String directionFacing = "south";
	public BufferedImage attackTexture;
	public BufferedImage[] attackNorth;
	public BufferedImage[] attackSouth;
	public BufferedImage[] attackEast;
	public BufferedImage[] attackWest;
	public long animationStartTime = -1;
	public long animationDelayMillis = 50; // How many millis until the next animation frame
	public boolean shotOnceDuringInterval = true;
	 
	// Gameplay
	public boolean killable = false;
	public int maxHealth = 1;
	public int health = 1;
	public boolean hasAttack = false;
	public boolean attackActive = false;
	public long attackStartTime = -1;
	public long attackLengthMillis = 1000;
	public long lastAttackEndTime = -1;
	public long subsequentAttackDelayMillis = 250;
	public int lootTableVarient = 0;
	public int lootType = -1;
	public int animationStage;
	public double randomTrait = Math.random();
	public int runSpeed = 1;
	public int shootDelayMillis = 1000;
	public int shootStage;
	public long birthTime = -1;
	public long lifeSpan = 1000;
	public boolean ignoresCollisions = false;
	
	// Init
	public GameObject(Main main) {
		this.main = main;
		this.vel = new Vect2D();
		this.velOverflow = new Vect2D();
		this.acc = new Vect2D();
		this.terminalVel = new Vect2D();
		this.bounds = new Rectangle();
		this.bounds.width = Main.tileRes;
		this.bounds.height = Main.tileRes;
		this.hitbox = new Dimension();
	}
	
	// Hitbox
	public Rectangle getHitbox() {
		return new Rectangle(bounds.x + hitboxOffset.x, bounds.y + hitboxOffset.y, hitbox.width, hitbox.height);
	}
	
	// Hurtbux
	public Rectangle getHurtbox() {
		Rectangle hitBox = getHitbox();
		switch (directionFacing) {
		case "north":
			return new Rectangle(hitBox.x - hurtbox.height / 2 + hitBox.width / 2, hitBox.y - hurtbox.width, hurtbox.height, hurtbox.width);
		case "south":
			return new Rectangle(hitBox.x - hurtbox.height / 2 + hitBox.width / 2, hitBox.y + hitBox.height, hurtbox.height, hurtbox.width);
		case "east":
			return new Rectangle(hitBox.x + hitBox.width, hitBox.y - hurtbox.height / 2 + hitBox.height / 2, hurtbox.width, hurtbox.height);
		case "west":
			return new Rectangle(hitBox.x - hurtbox.width, hitBox.y - hurtbox.height / 2 + hitBox.height / 2, hurtbox.width, hurtbox.height);
		}
		// Else
		return new Rectangle(bounds.x, bounds.y, hurtbox.width, hurtbox.height);
	}
	
	// Attack
	public void updateAttack() {
		if (attackActive) {
			// Ends attack if its been held on for too long
			long elapsedTime = System.currentTimeMillis() - attackStartTime;
			if (elapsedTime > attackLengthMillis) {
				attackActive = false;
			}
		}
	}
	
	public void updateShooting(GameObject player, ArrayList<GameObject> objectsForAdding, ArrayList<GameObject> dynamic, ArrayList<GameObject> animated) {
		// Timing
		long modifiedCurrentTime = System.currentTimeMillis() + (long)(this.randomTrait * shootDelayMillis);
		shootStage =(int)((modifiedCurrentTime / shootDelayMillis) % 2);
		if (shootStage == 0) {
			if (!shotOnceDuringInterval) {
				// Get the projectile type
				GameObject projectile;
				if (spawnMapID == 10) {
					// Plant
					projectile = getPlantBubble();
					// Determine velocity
					int plantProjSpeed = 2;
					Point playerCenter = new Point(player.bounds.x + player.bounds.width / 2, player.bounds.y + player.bounds.height / 2);
					double hDist = (playerCenter.x - (projectile.bounds.x + projectile.bounds.width / 2));
					double vDist = (playerCenter.y - (projectile.bounds.y + projectile.bounds.height / 2));
					double theta = Math.atan(Math.abs(vDist) / Math.abs(hDist));
					projectile.vel.i = Math.cos(theta) * plantProjSpeed;
					if (hDist < 0) projectile.vel.i *= -1;
					projectile.vel.j = Math.sin(theta) * plantProjSpeed;
					if (vDist < 0) projectile.vel.j *= -1;
				} else {
					// Critter
					projectile = getCritterBubble();
					// Determine velocity
					int critterProjSpeed = 4;
					int hDist = player.bounds.x - projectile.bounds.x;
					int vDist = player.bounds.y - projectile.bounds.y;
					if (Math.abs(hDist) > Math.abs(vDist)) {
						if (player.bounds.x > bounds.x) {
							projectile.vel.i = critterProjSpeed;
						} 
						else if (player.bounds.x < bounds.x) {
							projectile.vel.i = -critterProjSpeed;
						}
						else if (player.bounds.y > bounds.y) {
							projectile.vel.j = critterProjSpeed;
						}
						else {
							projectile.vel.j = -critterProjSpeed;
						}
					} else {
						if (player.bounds.y > bounds.y) {
							projectile.vel.j = critterProjSpeed;
						}
						else if (player.bounds.y < bounds.y) {
							projectile.vel.j = -critterProjSpeed;
						}
						else if (player.bounds.x > bounds.x) {
							projectile.vel.i = critterProjSpeed;
						} 
						else {
							projectile.vel.i = -critterProjSpeed;
						}
					}
				}
				// Add the projectile to the lists and add texture
				objectsForAdding.add(projectile);
				dynamic.add(projectile);
				animated.add(projectile);
				projectile.texture = Textures.spawnMapObjects.get(projectile.spawnMapID);
				// Spam protection
				projectile.birthTime = System.currentTimeMillis();
				shotOnceDuringInterval = true;
			}
		} else {
			shotOnceDuringInterval = false;
		}
	}
	
	public GameObject getPlantBubble() {
		GameObject bubble = new GameObject(main);
		bubble.spawnMapID = 18;
		bubble.bounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
		bubble.hitbox = new Dimension(this.bounds.width, this.bounds.width);
		bubble.hitboxOffset = new Point(0, 0);
		bubble.ignoresCollisions = true;
		bubble.lifeSpan = 5000;
		// Appearance
		bubble.north = Textures.plantsAttackThough;
		bubble.east = Textures.plantsAttackThough;
		bubble.south = Textures.plantsAttackThough;
		bubble.west = Textures.plantsAttackThough;
		bubble.attackNorth = Textures.plantsAttackThough;
		bubble.attackEast = Textures.plantsAttackThough;
		bubble.attackSouth = Textures.plantsAttackThough;
		bubble.attackWest = Textures.plantsAttackThough;
		bubble.animationDelayMillis = 250;
		return bubble;
	}
	
	public GameObject getCritterBubble() {
		GameObject bubble = new GameObject(main);
		bubble.spawnMapID = 19;
		bubble.bounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
		bubble.hitbox = new Dimension(this.bounds.width, this.bounds.width);
		bubble.hitboxOffset = new Point(0, 0);
		bubble.ignoresCollisions = true;
		// Appearance
		bubble.north = Textures.bugAttack;
		bubble.east = Textures.bugAttack;
		bubble.south = Textures.bugAttack;
		bubble.west = Textures.bugAttack;
		bubble.attackNorth = Textures.bugAttack;
		bubble.attackEast = Textures.bugAttack;
		bubble.attackSouth = Textures.bugAttack;
		bubble.attackWest = Textures.bugAttack;
		bubble.animationDelayMillis = 500;
		return bubble;
	}
	
	// Tranaslate
	public void translateHorizontal() {
		double velToInt = (int)vel.i;
		bounds.x += velToInt;
		velOverflow.i += vel.i - velToInt;
		if (vel.i > 0) {
			// Handle overflow
			if (velOverflow.i > 0.5) {
				velOverflow.i -= 0.5;
				bounds.x += 1;
			}
		} else {
			// Handle overflow
			if (velOverflow.i < -0.5) {
				velOverflow.i += 0.5;
				bounds.x -= 1;
			}
		}
	}
	
	public void translateVertical() {
		double velToInt = (int)vel.j;
		bounds.y += velToInt;
		velOverflow.j += vel.j - velToInt;
		if (vel.j > 0) {
			// Handle overflow
			if (velOverflow.j > 0.5) {
				velOverflow.j -= 0.5;
				bounds.y += 1;
			}
		} else {
			// Handle overflow
			if (velOverflow.j < -0.5) {
				velOverflow.j += 0.5;
				bounds.y -= 1;
			}
		}
	}
	
	// Collisions
	public void horizontalCollisions(ArrayList<Rectangle> solidWalls, ArrayList<GameObject> solidObjects) {
		if (ignoresCollisions) return;
		Rectangle hitbox = getHitbox();
		// Solid Walls
		for (Rectangle wall : solidWalls) {
			if (hitbox.intersects(wall)) {
				if (vel.i > 0) {
					bounds.x = wall.x - hitbox.width - hitboxOffset.x;
				} else if (vel.i < 0)  {
					bounds.x = wall.x + hitbox.width + hitboxOffset.x;
				}
				vel.i = 0;
			}
		}
		// Solid Objects
		for (GameObject obj : solidObjects) {
			Rectangle objHitbox = obj.getHitbox();
			if (obj.spawnMapID != spawnMapID) {
				if (hitbox.intersects(objHitbox)) {
					if (vel.i > 0) {
						bounds.x = objHitbox.x - hitbox.width - hitboxOffset.x;
					} else if (vel.i < 0) {
						bounds.x = objHitbox.x + hitbox.width + hitboxOffset.x;
					}
					vel.i = 0;
				}
			}
		}
	}
	
	public void verticalCollisions(ArrayList<Rectangle> solidWalls, ArrayList<GameObject> solidObjects) {
		if (ignoresCollisions) return;
		Rectangle hitbox = getHitbox();
		// Solid Walls
		for (Rectangle wall : solidWalls) {
			if (hitbox.intersects(wall)) {
				if (vel.j > 0) {
					bounds.y = wall.y - hitbox.height - hitboxOffset.y;
				} else if (vel.j < 0)  {
					bounds.y = wall.y + hitbox.height + hitboxOffset.y;
				}
				vel.j = 0;
			}
		}
		// Solid Objects
		for (GameObject obj : solidObjects) {
			Rectangle objHitbox = obj.getHitbox();
			if (obj.spawnMapID != spawnMapID) {
				if (hitbox.intersects(objHitbox)) {
					if (vel.j > 0) {
						bounds.y = objHitbox.y - hitbox.height - hitboxOffset.y;
					} else if (vel.j < 0)  {
						bounds.y = objHitbox.y + hitbox.height + hitboxOffset.y;
					}
					vel.j = 0;
				}
			}
		}
	}
	
	public void animate() {
		// Determine direction and if in motion
		boolean inMotion;
		// East
		if (vel.i > 0) {
			directionFacing = "east";
			inMotion = true;
		}
		// Wests
		else if (vel.i < 0) {
			directionFacing = "west";
			inMotion = true;
		}
		// South
		else if (vel.j > 0) {
			directionFacing = "south";
			inMotion = true;
		}
		// North
		else if (vel.j < 0) {
			directionFacing = "north";
			inMotion = true;
		}
		// Idle
		else {
			inMotion = false;
		}
		
		// texture
		animationStage = 0;
		if (inMotion) {
			if (animationStartTime == -1) {
				animationStartTime = System.currentTimeMillis();
			} else {
				int elapsedTime = (int)(System.currentTimeMillis() - animationStartTime);
				animationStage = (int)((System.currentTimeMillis() / animationDelayMillis) % 4);
			}
		}
		
		// If attacking sets to the attacking animation
		Point[] offsets = {new Point(0, -bounds.height), new Point(bounds.width, 0), new Point(0, bounds.height), new Point(-bounds.width, 0)};
		if (attackActive) {
			switch (directionFacing) {
			case "north":
				texture = north[4];
				attackTexture = attackNorth[0];
				break;
			case "south":
				texture = south[4];
				attackTexture = attackSouth[0];
				break;
			case "east":
				texture = east[4];
				attackTexture = attackEast[0];
				break;
			case "west":
				texture = west[4];
				attackTexture = attackWest[0];
				break;
			}
		}
		
		// Or just does the walking animation
		else {
			switch (directionFacing) {
			case "north":
				texture = north[animationStage];
				attackTexture = attackNorth[0];
				break;
			case "south":
				texture = south[animationStage];
				attackTexture = attackSouth[0];
				break;
			case "east":
				texture = east[animationStage];
				attackTexture = attackEast[0];
				break;
			case "west":
				texture = west[animationStage];
				attackTexture = attackWest[0];
				break;
			}
		}
	}
	
	public void accelerate(boolean north, boolean east, boolean south, boolean west) {
		// Horizontal
		if (east) {
			if (vel.i < terminalVel.i) {
				vel.i += acc.i;
			}
		} else if (vel.i > 0) {
			vel.i = 0;
		}
		if (west) {
			if (vel.i > -terminalVel.i) {
				vel.i -= acc.i;
			}
		} else if (vel.i < 0) {
			vel.i = 0;
		}
		
		// Vertical
		if (north) {
			if (vel.j > -terminalVel.j) {
				vel.j -= acc.j;
			}
		} else if (vel.j < 0) {
			vel.j = 0;
		}
		if (south) {
			if (vel.j < terminalVel.j) {
				vel.j += acc.j;
			}
		} else if (vel.j > 0) {
			vel.j = 0;
		}
	}
	
	// Render
	public void render(Graphics2D g) {
		
	}
	
	public void draw(Graphics2D g) {
		 g.drawImage(texture, (int)(bounds.x), (int)(bounds.y), bounds.width, bounds.height, main);		
		 if (hasAttack && attackActive) {
			 Rectangle hurtbox = getHurtbox();
			 g.drawImage(attackTexture, (int)(hurtbox.x) , (int)(hurtbox.y), bounds.width, bounds.height, main);	
		 }
	}
	
	
	
	
	
}

