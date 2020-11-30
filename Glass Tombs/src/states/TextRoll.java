package states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import engine.Main;
import util.GameState;
import util.Style;

public abstract class TextRoll extends GameState {
	
	public String[] textArray;
	
	public int textMargin;
	public int scrollOffset;
	public int scrollStep;
	public double spacingHeight;
	
	public TextRoll(Main main) {
		super(main);
		textMargin = Main.height;
		scrollOffset = 0;
		scrollStep = 1;
	}
	
	public void render(Graphics2D g) {
		scrollText(g);
		renderText(g);
	}
	
	protected void renderText(Graphics2D g) { 
		// Font setup
		double textSize = (tileRes * 0.5);
		spacingHeight = (tileRes * 0.9);
		g.setFont(Style.righteous);
		g.setFont(g.getFont().deriveFont((float)textSize));
		g.setColor(Style.white);
		for (int i = 0; i < textArray.length; i++) {
			String str = textArray[i];
			if (str.length() > 0) {
				int colorIndex = Integer.parseInt(str.substring(0, 1));
				g.setColor(Style.textColors[colorIndex]);
				g.drawString(str.substring(1), tileRes, (int)(i * spacingHeight + textMargin));
			}
			
		}
		// Render skip hint
		g.translate(0, -scrollOffset);
		g.setColor(Style.gray);
		String skipHint = "Press [Z] to skip...";
		int skipHintWidth = g.getFontMetrics().stringWidth(skipHint);
		g.drawString(skipHint, Main.width - skipHintWidth, Main.height - tileRes / 4);
	}
	
	protected void scrollText(Graphics2D g) {
		double scrollLimit = -textMargin - spacingHeight * textArray.length;
		scrollOffset -= scrollStep;
		g.translate(0, scrollOffset);
		if (scrollOffset < scrollLimit) {
			triggerTransition();
		}
	}
	
	protected void triggerTransition() {
		
	}
	
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Z:
			scrollStep = 64;
			break;
		}
	}
	
}
