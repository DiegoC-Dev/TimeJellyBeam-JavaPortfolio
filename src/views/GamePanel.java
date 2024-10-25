package views;

import models.Enemy;
import models.Game;
import models.GameObject;
import models.Gem;
import models.Hero;
import models.IndexColection;
import models.Sprite;
import models.TimeGem;
import presenters.GamePresenter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class GamePanel extends JPanel {
	private ArrayList<GameObject> gameObjectList;
    private ArrayList<Enemy> enemyList;
    private ArrayList<Gem> gemList;
    private TimeGem gemTime;
	private Hero hero;
	private boolean draw;
	private Image background;
	private int time;
	private int finalTime;
	private boolean initGame;
	private boolean win;
	private ArrayList<Sprite> spriteList;
	private Sprite loadSprite;
	private IndexColection[] indexColectionList;
	private IndexColection[] indexColectionEnemyList;
	private IndexColection[] indexColectionGemList;
	private IndexColection indexBigGem;
	private IndexColection loadIndex;
	private BufferedImage blackBackground;
	private AlphaComposite alphaComposite;
	private Point[] positionImages;
	private Point positionImageLoad;
	private boolean save;
	private boolean screenShot;
	private int initTime;
	private boolean finishLoad;
	private boolean paint;
	public GamePanel(GamePresenter gamePresenter) throws IOException {
		addMouseMotionListener(gamePresenter);
		addMouseListener(gamePresenter);
		indexColectionList = new IndexColection[] {new IndexColection(0, 0)};
		positionImages = new Point[] {new Point(498,226)};
		setBackground(Color.black);
		alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f);
		blackBackground =	ImageIO.read(new File("src/img/blackBackground.png"));
		background =	ImageIO.read(new File("src/img/GameMap.png"));
		spriteList = new ArrayList<Sprite>();
		JButton jButton = new JButton();
		jButton.addKeyListener(gamePresenter);
		add(jButton,BorderLayout.NORTH);
	}
    @Override
    public void paint(Graphics graphics) {
    	super.paint(graphics);
    	if(draw && !initGame) 
    		drawScene(graphics);
    	else if(initGame) 
    		drawGame(graphics);
    	getToolkit().sync();    		
    }

	private void drawScene(Graphics graphics) {
		if(finishLoad)
		drawAnimatedImage(graphics);
		else
			drawAnimatedLoad(graphics);
		if(win) {
			drawWin(graphics);
		}
		setOpacity(graphics);
		drawBlackBackground(graphics);
	}

	private void drawBlackBackground(Graphics graphics) {
		graphics.drawImage(blackBackground,0,0, null);		
	}

	private void setOpacity(Graphics graphics) {
		Graphics2D g2d = (Graphics2D)graphics;
		g2d.setComposite(alphaComposite);
	}
	private void drawAnimatedImage(Graphics graphics) {
		int arrayIndex;
		int imageIndex;
		BufferedImage image;
		for (int i = 0; i < indexColectionList.length; i++) {
			arrayIndex= indexColectionList[i].getArrayIndex();
			imageIndex= indexColectionList[i].getImageIndex();
			image =spriteList.get(arrayIndex).getSprite().get(imageIndex);
			if(arrayIndex!=35 && arrayIndex!=36)
				graphics.drawImage(image,positionImages[i].x,positionImages[i].y, null);
			else if(arrayIndex==35 && save) 
				graphics.drawImage(image,positionImages[i].x,positionImages[i].y, null);
			else if(arrayIndex==36 && screenShot)
				graphics.drawImage(image,positionImages[i].x,positionImages[i].y, null);
		}
	}
	private void drawAnimatedLoad(Graphics graphics) {
		int imageIndex;
		BufferedImage image;
			imageIndex= loadIndex.getImageIndex();
			image =loadSprite.getSprite().get(imageIndex);
			graphics.drawImage(image,positionImageLoad.x,positionImageLoad.y, null);
	}
	private void drawGame(Graphics graphics) {
		    		drawBackground(graphics);
//		    		drawGameObjects(graphics);
//		    		drawGemList(graphics);
//		    		drawGemTime(graphics);
//		    		drawEnemyList(graphics);
//		    		drawHeroGraphics(graphics);
		    		drawHeroSprite(graphics);
		    		drawTime(graphics);

		    		drawEnemyListSprite(graphics);
		    		drawGemListSprite(graphics);
		    		drawGemTimeSprite(graphics);

		    		drawAnimatedImage(graphics);

		    		setOpacity(graphics);
		    		drawBlackBackground(graphics);

	}
	private void drawGemTimeSprite(Graphics graphics) {
		int arrayIndex;
		int imageIndex;
		BufferedImage image;
			arrayIndex= indexBigGem.getArrayIndex();
			imageIndex= indexBigGem.getImageIndex();
			image =spriteList.get(arrayIndex).getSprite().get(imageIndex);
			graphics.drawImage(image,(int)gemTime.getX()-20,(int)gemTime.getY()-20, null);
	}

	private void drawEnemyListSprite(Graphics graphics) {
		int arrayIndex;
		int imageIndex;
		BufferedImage image;
		for (int i = 0; i < enemyList.size(); i++) {
			arrayIndex= indexColectionEnemyList[i].getArrayIndex();
			imageIndex= indexColectionEnemyList[i].getImageIndex();
			image =spriteList.get(enemyList.get(i).isDirection()?arrayIndex:30).getSprite().get(imageIndex);
			graphics.drawImage(image,(int)enemyList.get(i).getX()-30,(int)enemyList.get(i).getY()-60, null);
		}
	}

	private void drawGemListSprite(Graphics graphics) {
		int arrayIndex;
		int imageIndex;
		BufferedImage image;
		for (int i = 0; i < gemList.size(); i++) {
			arrayIndex= indexColectionGemList[0].getArrayIndex();
			imageIndex= indexColectionGemList[0].getImageIndex();
			image =spriteList.get(arrayIndex).getSprite().get(imageIndex);
			graphics.drawImage(image,(int)gemList.get(i).getX()-3,(int)gemList.get(i).getY(), null);
		}
	}

	private void drawTime(Graphics graphics) {
	graphics.setColor(Color.decode("#80044a"));
    	graphics.setFont(Constants.MAIN_FONT);	
    	if(win) {
    		graphics.drawString(""+finalTime,625, 55);
    	}else if(time<0)
    		graphics.drawString("0",625, 55);
    	else
    		graphics.drawString(""+time,625, 55);
    }
    private void drawWin(Graphics graphics) {
    	graphics.setColor(Color.decode("#80044a"));
    	graphics.setFont(Constants.MAIN_FONT);		
    	graphics.drawString(""+finalTime,640, 405);
    }
	private void drawBackground(Graphics graphics) {
		graphics.drawImage(background,0
				,0, null);
	}
    public BufferedImage modifiedCreateResizedCopy() throws IOException{
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	BufferedImage scaledBI = new BufferedImage(screenSize.width, screenSize.height,BufferedImage.TYPE_INT_RGB);
    	Graphics2D g = scaledBI.createGraphics();
    	g.setComposite(AlphaComposite.Src);
		g.drawImage(ImageIO.read(new File("src/img/finalBackground.png")), 0, 0, screenSize.width, screenSize.height, null);
    	g.dispose();
    	return scaledBI;
    }
	private void drawHeroSprite(Graphics graphics) {
		graphics.drawImage(hero.getSubSprite(),(int)(hero.getX()-hero.getDiameter()/2)-5
				,(int)(hero.getY()-hero.getDiameter()/2), null);
	}
    public void updateObjectList(ArrayList<GameObject> gameObjectList) {
    	this.gameObjectList=gameObjectList;
    }
    public void draw() {
    	draw = true;
    	swingWorker();
    }
    public void updateHero(Hero hero) {
    	this.hero = hero;
    }
    public void updateEnemys(ArrayList<Enemy> enemyList) {
    	this.enemyList = enemyList;
    }
    public void updateGems(ArrayList<Gem> gemList) {
    	this.gemList = gemList;
    }
    private void swingWorker(){
		SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>(){
			boolean condition = false;
			int wait = 1000000;
			int cont= 0;
			@Override
			protected Integer doInBackground() throws Exception {
				while(!condition) {
						cont++;
						if(cont%wait==0) {
						repaint();
//							repaint((int)hero.getX()-40,(int) hero.getY()-40, 
//									(int)(hero.getX()+hero.getDiameter())+30,
//									(int)(hero.getX()+hero.getDiameter())+30);
						}
				}
				return 0;
			}
			@Override
			protected void process(List<Integer> chunks) {
			}
			@Override
			protected void done() {
			}
		};worker.execute();
	}
    public void setWin(boolean win) {
    	finalTime = initTime-time;
    	this.win = win;
    }
	public JPanel getPanel() {
		return this;
	}
	public void setInitGame(boolean initGame) {
		this.initGame = initGame;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public void setIndexColectionList(IndexColection[] indexColectionList) {
		this.indexColectionList = indexColectionList;
	}
	public void addSpriteList(Sprite sprite) {
		spriteList.add(sprite);
	}
	public void setPositionImages(Point[] positionImages) {
		this.positionImages = positionImages;
	}
	public void setAlphaComposite(AlphaComposite alphaComposite) {
		this.alphaComposite = alphaComposite;
	}
	public void setAnimatedImage(ArrayList<Sprite> animatedImage) {
		this.spriteList = animatedImage;
	}
	public void setIndexColectionGemList(IndexColection[] indexColectionGemList) {
		this.indexColectionGemList = indexColectionGemList;
	}
	public void setIndexColectionEnemyList(IndexColection[] indexColectionEnemyList) {
		this.indexColectionEnemyList = indexColectionEnemyList;
	}
	public void setIndexBigGem(IndexColection indexBigGem) {
		this.indexBigGem = indexBigGem;
	}
	public void setGemTime(TimeGem gemTime) {
		this.gemTime = gemTime;
	}
	public void setSave(boolean save) {
		this.save = save;
	}
	public void setScreenShot(boolean screenShot) {
		this.screenShot = screenShot;
	}
	public void setInitTime(int initTime) {
		this.initTime = initTime;
	}
	public void setLoadSprite(Sprite loadSprite) {
		this.loadSprite = loadSprite;
	}
	public void setPositionImageLoad(Point positionImageLoad) {
		this.positionImageLoad = positionImageLoad;
	}
	public void setIndexLoad(IndexColection indexLoad) {
		this.loadIndex =indexLoad;
	}
	public void setFinishLoad(boolean finishLoad) {
		this.finishLoad = finishLoad;
	}
}