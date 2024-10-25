package views;

import java.awt.BorderLayout;

import javax.swing.JProgressBar;
import javax.swing.JWindow;

import presenters.GamePresenter;

public class JWWindowProgress extends JWindow implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar jprogressBar;
	private JPProgress jPProgres;
	private GamePresenter gamePresenter; 
	private Thread thread;
	public JWWindowProgress(GamePresenter gamePresenter) {
		this.gamePresenter=gamePresenter;
		setSize(400, 250);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		init();
	}
	private void init() {
		jprogressBar = new JProgressBar();
		jprogressBar.setIndeterminate(true);
		this.add(jprogressBar,BorderLayout.SOUTH);
		
		jPProgres = new JPProgress();
		this.add(jPProgres,BorderLayout.CENTER);	
		
		thread = new Thread(this);
		thread.start();
	}
	@Override
	public void run() {
		this.setVisible(true);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.dispose();
		gamePresenter.setvisibleFrame();	
	}
	
	
}
