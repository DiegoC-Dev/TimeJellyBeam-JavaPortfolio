package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

public class Constants {
	public static final Dimension SIZE= new Dimension(500, 500);
	public static final Dimension SCREEN_SIZE= Toolkit.getDefaultToolkit().getScreenSize();

	public static final Color PRIMARY_COLOR = Color.decode("#00a78e");
	public static final Color SECUNDARY_COLOR= Color.decode("#cc0066");
	public static final Color  TERTIARY_COLOR= Color.decode("#000000");
	
	public static final Font MAIN_FONT = new Font("Dyuthi", Font.BOLD, 40);
	public static final Color FONT_COLOR = Color.decode("#ef3081");
}
