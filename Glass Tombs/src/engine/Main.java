package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;

import states.AGlassTitleScreen;
import states.Intro;
import states.Overworld;
import states.SplashScreen;
import util.GameStateManager;
import util.Style;
import util.Textures;
import utilmisc.BranchData;

public class Main extends JFrame implements KeyListener, MouseListener, MouseWheelListener {

	//Display
	public static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	
	// Tiles
	public static int tileRes = 32;
	
	// Gui bar size
	public static int guiBarSize = 1;
	
	// Local map size
	public static Dimension localMapSize = new Dimension(16, 12);
	
	// Frame variables
	public static int width = tileRes * localMapSize.width;
	public static int height = tileRes * localMapSize.height + guiBarSize * tileRes;

	// Run variables
	private static boolean isRunning;
	private double FPS;
	private boolean debugMode = false;

	// GameState
	public static GameStateManager gsm = new GameStateManager();
	
	// Gameplay
	public static int storyStage = 0;
	public static int cryptsCompleted = 0;
	public static int glassDungeonMarker;
	public static BranchData glassDungeonRoom;
	public static int maxPlayerHealth = 8;
	public static int playerHealth = 8;
	public static int money = 0;
	
	// Music
	public static Clip crypt;
	public static Clip ghost;
	public static Clip house;
	public static Clip outsideCrypt;
	public static Clip overworld;
	public static Clip town;
	public static Clip currentSong;
	public static Clip intro;
	public static Clip glassDungeon;
	public static Clip flowers;
	public static double masterVolume = 0.4;
	
	public Main() {
		// Listener setup
		addKeyListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);

		// Window setup
		setSize(screen.width, screen.height);
		setLocation(0, 0);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		// Textures
		Textures.initializeLists();

		// Style
		Style.initializeStyle();

		// Music
		try {
			InputStream audioSrc;
			InputStream bufferedIn;
			AudioInputStream audioStream;
			
            audioSrc = getClass().getResourceAsStream("/sounds/Crypt.wav");
            bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            crypt = AudioSystem.getClip();
            crypt.open(audioStream);
            
            audioSrc = getClass().getResourceAsStream("/sounds/Ghost.wav");
            bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            ghost = AudioSystem.getClip();
            ghost.open(audioStream);
            
            audioSrc = getClass().getResourceAsStream("/sounds/House.wav");
            bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            house = AudioSystem.getClip();
            house.open(audioStream);
            
            audioSrc = getClass().getResourceAsStream("/sounds/OutsideCrypt.wav");
            bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            outsideCrypt = AudioSystem.getClip();
            outsideCrypt.open(audioStream);

            audioSrc = getClass().getResourceAsStream("/sounds/Overworld.wav");
            bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            overworld = AudioSystem.getClip();
            overworld.open(audioStream);
            
            audioSrc = getClass().getResourceAsStream("/sounds/Town.wav");
            bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            town = AudioSystem.getClip();
            town.open(audioStream);
            
            audioSrc = getClass().getResourceAsStream("/sounds/OutsideCrypt.wav");
            bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            intro = AudioSystem.getClip();
            intro.open(audioStream);
            
            audioSrc = getClass().getResourceAsStream("/sounds/GlassTomb.wav");
            bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            glassDungeon = AudioSystem.getClip();
            glassDungeon.open(audioStream);
            
            audioSrc = getClass().getResourceAsStream("/sounds/Flowers.wav");
            bufferedIn = new BufferedInputStream(audioSrc);
            audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            flowers = AudioSystem.getClip();
            flowers.open(audioStream);
            
		} catch (Exception e) {
        	System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
		
		// GameState
		gsm.currentState = new SplashScreen(this);
		
		// Run setup
		isRunning = true;
		FPS = 40;
		run();
	}
	
	public void run() {
		// Time between each frame in nanoseconds
		double delta = 1000000000 / FPS;

		// Variable setup for recording elapsed time
		long lastTime = System.nanoTime();
		long now;
		long elapsedTime = 0;

		// Render variable
		boolean canRender;

		while (isRunning) {
			// Identifies the elapsed time since last update
			now = System.nanoTime();
			elapsedTime += (now - lastTime);
			lastTime = now;
			canRender = false;
			// Makes updates happen every 'delta' amount of time has passed
			if (elapsedTime >= delta) {
				elapsedTime -= delta;
				canRender = true;
				// Update
				update();
			}
			// Render
			if (canRender) {
				render();
			}
		}
	}

	public void update() {
		//// GameState
		gsm.update();
	}

	public void render() {
		
		// Creates 2-buffer buffer strategy
		BufferStrategy bs = getBufferStrategy();
		if (getBufferStrategy() == null) {
			createBufferStrategy(2);
			return;
		}

		// Gg
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		// Draws to hidden buffer, clears previous image
		g.setBackground(Color.getHSBColor(0.741f, 0.39f, 0.33f));
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.clearRect(0, 0, screen.width, screen.height);
		// Centers the game
		double scaleRatio = (double)(screen.height) / (double)(Main.height);
		double toCenter = screen.width * 0.5 - (Main.width * scaleRatio * 0.5) ;
		g.translate(toCenter, 0);
		g.scale(scaleRatio, scaleRatio);
		
		// GameState
		gsm.render(g);
		
		// Disposes graphics object and shows hidden buffer
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		// Main
		new Main();
		System.exit(0);
	}

	//// KeyListener
	@Override
	public void keyPressed(KeyEvent e) {
		// GameState
		gsm.keyPressed(e);

		// Entire Game key commands
		switch (e.getKeyCode()) {
		case KeyEvent.VK_BACK_SPACE:
			System.out.print("Exit message!");
			System.exit(1);
			break;
			
		case KeyEvent.VK_BACK_QUOTE:
			debugMode = !debugMode;
			if (debugMode) {
				System.out.println("[Debugmode on]");
			} else {
				System.out.println("[Debugmode off]");
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// GameState
		gsm.keyReleased(e);
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



}
