package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPProgress extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JPProgress() {
//		setBackground(Color.black);
		setOpaque(false);
	}
	@Override
	public void paint(Graphics g) {
		Dimension dimension = this.getSize();
		ImageIcon icon = new ImageIcon(getClass().getResource("/img/logoSplash.png"));
		g.drawImage(icon.getImage(), 0, 0, dimension.width, dimension.height, null);
		super.paint(g);
	
	}
}
