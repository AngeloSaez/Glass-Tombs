package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Style {

	
	public static Font righteous;
	public static Font russo;
	
	public static Color white = Color.getHSBColor(0.225f, 0.1f, 0.8f);
	public static Color gray = Color.getHSBColor(0.225f, 0.02f, 0.6f);			// 1
	public static Color darkGray = Color.getHSBColor(0.755f, 0.13f, 0.46f);	
	
	public static Color deepPurple = Color.getHSBColor(0.741f, 0.39f, 0.33f);	
	public static Color deepRed = Color.getHSBColor(1.0f, 0.49f, 0.6f);			
	
	public static Color lightPink = Color.getHSBColor(0.977f, 0.3f, 0.76f);		// 2
	public static Color lightGreen = Color.getHSBColor(0.356f, 0.35f, 0.67f);	// 3
	public static Color lightBlue = Color.getHSBColor(0.575f, 0.35f, 0.75f);	// 4
	public static Color lightCyan = Color.getHSBColor(0.491f, 0.4f, 0.67f);		// 5
	public static Color lightPurple = Color.getHSBColor(0.833f, 0.11f, 0.65f);	// 6
	public static Color lightOrange = Color.getHSBColor(0.052f, 0.4f, 0.76f);	// 7


	public static Color[] textColors = {
		white,
		gray,
		lightPink,
		lightGreen,
		lightBlue,
		lightCyan,
		lightPurple,
		lightOrange,
	};
	
	public static void initializeStyle() {
		initializeFonts();
	}
	
	private static void initializeFonts() {
		try {
			String fontName = "Righteous-Regular.ttf"; // if using eclipse put this inside the quptes: src/
			InputStream myStream = new FileInputStream("" + fontName);
			righteous = Font.createFont(Font.TRUETYPE_FONT, myStream);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String fontName = "RussoOne-Regular.ttf";
			InputStream myStream = new FileInputStream("" + fontName);
			russo = Font.createFont(Font.TRUETYPE_FONT, myStream);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
