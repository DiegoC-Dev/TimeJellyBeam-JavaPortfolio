package models;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import persistence.FileManagerJson;

public class Game{
	public static final int COUNT_DOWN = 40;
	public static final int SIZE = 100;
    public static final int HERO_SIZE = 50;
    private ArrayList<GameObject> gameObjectList;
    private ArrayList<Enemy> enemyList;
    private ArrayList<Gem> gemList;
    private TimeGem timeGem;
	private Hero hero;
	private GameData gameData;
//	private int Inittime;
	private int time;
	private boolean pause;
	private JPanel panelScreenShot;
	private Clip clip;
	private long clipTime;
	private Clip clipMenu;
	private long clipTimeMenu;
	private int transitionStarTime;
	private String [][] TextMatrix;
	private int placeOpacity;
	private int endScene;
	private SceneOBjectGroup sceneOBjectGroup;
	private int actualScreen;
	private boolean newScreenshot;
	private int newScreenshotNumber;
	private boolean initScreenShots;
	private boolean restart;
	private boolean win;
	private int currentTimeScene;
	private String currentScene;
	private String changeScene;
	private int spritesNumber;
	private boolean save;
	private boolean screenShot;
	private ArrayList<Integer> spriteSizeList;
	private int loadSize;
	private boolean skipe;
	private int endPrelude;
	private ArrayList<Rectangle2D> virtualButonList;


	public Game() throws IOException {
		menuObjectsScene();
		virtualButonList = new ArrayList<Rectangle2D>();
		createVirtualButtons();
		spriteSizeList = new ArrayList<Integer>();
		actualScreen=spriteSizeList.size()-1;
		endScene=-1;
    	read();
    	autoSaveTimer();
    	screenTimer();
    	clipTime=0;
        enemyList= new ArrayList<Enemy>();
        gemList= new ArrayList<Gem>();
        gameObjectList = new ArrayList<GameObject>();
    	addGameObjects();
    	addHero();
    	addEnemys();
    	addGems();
    	timeGem= new TimeGem(100, 150, 150, Color.black);
//    	playBackgroundSound();
//    	playMenuSound();
    	currentScene = "load";
    	changeScene = "load";
    	time = COUNT_DOWN;
    	currentTimeScene=0;
    	skipe=true;
    }
	public void setEnemyMovements(int cont){
		int adder = 14;
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).setXposition(cont+(adder*i));
		}
	}
	public void modifyFullScreenImage(String path,int scaledWidth,int scaledHeight) throws IOException{
		Image originalImage = ImageIO.read(new File(path));
		int imageType =  BufferedImage.TYPE_INT_RGB ;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
		g.dispose();
		try {
			ImageIO.write(scaledBI, "png", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void createVirtualButtons() {					
		virtualButonList.add(new Rectangle(450, 200, 400, 140));
		virtualButonList.add(new Rectangle(450, 360, 400, 140));
		virtualButonList.add(new Rectangle(450, 520, 400, 140));
	}
    private void addGems() {
    	gemList.add(new Gem(50, 525, 10, Color.pink));
    	gemList.add(new Gem(175, 400, 10, Color.pink));
    	gemList.add(new Gem(800, 630, 10, Color.pink));
    	gemList.add(new Gem(1111, 350, 10, Color.pink));
    	gemList.add(new Gem(800, 100, 10, Color.pink));
    	gemList.add(new Gem(400, 280, 10, Color.pink));
    }
	private void addEnemys() {
		enemyList.add(new Enemy(800, 150, 120, Color.red));
		enemyList.add(new Enemy(800, 445, 120, Color.red));
		enemyList.add(new Enemy(650, 485, 120, Color.red));
		enemyList.add(new Enemy(650, 130, 120, Color.red));
//		enemyList.add(new Enemy(570, 485, 120, Color.red));
//		enemyList.add(new Enemy(570, 130, 120, Color.red));
	}
	public void restartGems(){
		gemList.clear();
		addGems();
	}
	private void read() {
    		gameData = (FileManagerJson.read().get(0));			
    }
    public void writeData() {
    	int index =getHero().getIndex();
    	long x=(long)getHero().getX();
    	long y=(long)getHero().getY();
    	String adhered = ""+getHero().isAdhered();
    	long xBeforeJump = (long)getHero().getXbeforeJump();
    	long yBeforeJump = (long)getHero().getYbeforeJump();
    	String fall = ""+getHero().isFall();
    	String path = (getHero().getSavedImage()!=null)?getHero().getSavedImage():gameData.getImage();/////
    	int[][] spriteCoords = getHero().getSpriteCoords();
    	long xSpriteCoords = (getHero().isAwake())?spriteCoords[index][0]:gameData.getxSpriteCoords();
    	long ySpriteCoords = (getHero().isAwake())?spriteCoords[index][1]:gameData.getySpriteCoords();
    	long widthSpriteCoords = (getHero().isAwake())?spriteCoords[index][2]:gameData.getWidthSpriteCoords();
    	long heightSpriteCoords = (getHero().isAwake())?spriteCoords[index][3]:gameData.getHeightSpriteCoords();
    	GameData list = new GameData(x,y,adhered,xBeforeJump,
    			yBeforeJump,fall,path,
    			xSpriteCoords,ySpriteCoords,widthSpriteCoords,heightSpriteCoords);
    	FileManagerJson.write(list);
    }

	private void addGameObjects() {
		gameObjectList.add(new GameObject(4, new int [] {0,1400,1400,0},//abajo
				new int [] {650,650,800,800}, Color.red));
		
		gameObjectList.add(new GameObject(4, new int [] {0,50,50,0},//izquierda
				new int [] {0,0,800,800}, Color.red));
		
		gameObjectList.add(new GameObject(4, new int [] {0,1400,1400,0},//arriba
				new int [] {0,0,100,100}, Color.red));
		
		gameObjectList.add(new GameObject(4, new int [] {1250,1400,1400,1250},//derecha
				new int [] {0,0,800,800}, Color.red));
		
		gameObjectList.add(new GameObject(4, new int [] {0,1111,1111,0},//centro
				new int [] {300,300,400,400}, Color.red));
    }
	private void addHero() throws IOException {
//		System.out.println("se reinicio heroe");
		hero = new Hero(gameData.getX(), gameData.getY(),
				HERO_SIZE,gameObjectList,Color.BLUE);
		hero.setAdhered(Boolean.parseBoolean(gameData.getAdhered()));
		hero.setXBeforeJump((int)gameData.getxBeforeJump());
		hero.setYBeforeJump((int)gameData.getyBeforeJump());
		hero.setFall(Boolean.parseBoolean(gameData.getFall()));
		BufferedImage img = ImageIO.read(new File("src/img/jelly "+gameData.getImage()+".png"));
		hero.setSubSprite( img.
				getSubimage((int)gameData.getxSpriteCoords(),(int)gameData.getySpriteCoords(),
						(int)gameData.getWidthSpriteCoords(),(int)gameData.getHeightSpriteCoords()));
		hero.setImg(img);
		hero.setImageType(gameData.getImage());
	}
	public ArrayList<Enemy> getEnemyList() {
		return enemyList;
	}
	public void setEnemyList(ArrayList<Enemy> enemyList) {
		this.enemyList = enemyList;
	}
	public ArrayList<Gem> getGemList() {
		return gemList;
	}
	public void setGemList(ArrayList<Gem> gemList) {
		this.gemList = gemList;
	}
	private void autoSaveTimer() {
		Timer time = new Timer(1000, new ActionListener() {
			int second=0;
			int limit = 5;
			int limitImage = 1;
			int ImageSecond=0;
			public void actionPerformed(ActionEvent e) {
				second=(!pause)?second+1:second;
				ImageSecond=(!pause)?ImageSecond+1:ImageSecond;
				if(second==limit) {
					writeData();
					save=true;
					second=0;
					ImageSecond=0;
				}
				if(ImageSecond==limitImage) {
					save=false;
				}
			}
		});
		time.start();
	}
	private void screenTimer() {
		Timer screenLoop = new Timer(1000, new ActionListener(){
			int second=0;
			int limit = 5;
			int numberOfScreenshots;
			int limitImage = 1;
			int ImageSecond=0;
			public void actionPerformed(ActionEvent actionEvent) {
				if(initScreenShots== true) {
				second=(!pause)?second+1:second;
				ImageSecond=(!pause)?ImageSecond+1:ImageSecond;

				if(second==limit && currentScene.contentEquals("game")) {
					numberOfScreenshots++;
					screenShot=true;
					ImageSecond=0;
					try {
						saveSnapShot(numberOfScreenshots);
						newScreenshot= true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(ImageSecond==limitImage) {
					screenShot=false;
				}
				second=(second==limit)?0:second;
				newScreenshot= false;
				}
			}
		});
		screenLoop.start();
	}
	
    public BufferedImage getScreenShot() {
    	Component component =panelScreenShot;
    	BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
    	component.paint(image.getGraphics());
    	return image;
    }
    
    public void saveSnapShot(int numberOfScreenshots) throws Exception {///main
    	BufferedImage img = getScreenShot();
    	ImageIO.write(img, "png", new File("src/screenShoots/screenshot "+numberOfScreenshots+".png"));
    }
	public Hero getHero() {
		return hero;
	}
	public ArrayList<GameObject> getGameObjectList() {
		return gameObjectList;
	}
	public void playBackgroundSound() {
		try {
			clip = AudioSystem.getClip();
//    				AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/Inicio.wav"));
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/game_music .wav"));
			clip.open(inputStream);
			clip.start();
		} catch (LineUnavailableException |UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}
	public void playMenuSound() {
		try {
			clipMenu = AudioSystem.getClip();
    				AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/menu.wav"));
//			AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/game_music .wav"));
    				clipMenu.open(inputStream);
    				clipMenu.start();
//    				clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException |UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void soundBackdgoundPause(int key){
		if(pause) {
			clipTime= clip.getMicrosecondPosition();
			clip.stop();
		}
		else if(key==80 && !pause){
			clip.setMicrosecondPosition(clipTime);
			clip.start();
		}
	}
	public void soundMenuPause(int key){
		if(pause) {
			clipTimeMenu= clipMenu.getMicrosecondPosition();
			clipMenu.stop();
		}
		else if(key==80 && !pause){
			clipMenu.setMicrosecondPosition(clipTimeMenu);
			clipMenu.start();
		}
	}
	public  boolean consumedGem(Hero hero){
		for (int i = 0; i < gemList.size(); i++) {
			if(gemList.get(i).consumedGem(hero)) {
				gemList.remove(i);
				return true;
			}
		}
		return false;
	}
	public  void winCondition(){
		if(timeGem.consumedGem(hero)) {
			win= true;
			changeScene="victory";
		}
	}
	public  void successfulAttack() throws IOException{
		for (int i = 0; i < enemyList.size(); i++) {
			if(enemyList.get(i).successfulAttack(hero)) {
				restartHero();
			}
		}
	}
//	private void recoverHero() throws IOException {
//		hero.setCoordinates(new Point((int)gameData.getX(),(int)hero.getY()));
//	}
	public  void defeatCondition(){
		if(time==0) {
//			defeat= true;
			changeScene="defeat";
		}
	}
	public void restartHero() {
		hero.setCoordinates(new Point(150, 600));
//		win=false;
//		defeat=false;
	}

	public BufferedImage createAnimatedImage(int time,int slowdown,String path) throws IOException {
		int gifTime;
		BufferedImage img = null;
		File gifFolder = new File(path);
		gifTime = (time%slowdown==0)?time/slowdown:(int)(time/slowdown);
		int imageNumber=(gifTime %gifFolder.listFiles().length)+1;
		img = ImageIO.read(new File(path+"/frame-"+imageNumber+".gif"));
		return img;
	}
	public IndexColection createIdexCollection(int time,int slowdown,int indexSpriteList) throws IOException {
		int gifTime;
		BufferedImage img = null;
		gifTime = (time%slowdown==0)?time/slowdown:(int)(time/slowdown);
		int imageNumber=(gifTime %spriteSizeList.get(indexSpriteList));
		return new IndexColection(indexSpriteList, imageNumber);
	}
	public IndexColection createIdexLoad(int time,int slowdown) throws IOException {
		int gifTime;
		BufferedImage img = null;
		gifTime = (time%slowdown==0)?time/slowdown:(int)(time/slowdown);
		int imageNumber=(gifTime %loadSize);
		return new IndexColection(-1, imageNumber);
	}
	public AlphaComposite createTransition(int mainTime,int slowdown,boolean input) throws IOException {
		int transitionTime = mainTime-transitionStarTime;
		boolean transitionOff;
		int numbertransitions = 20;
		double multiplier = 10/(double)(numbertransitions);
		int gifTime = (transitionTime%slowdown==0)?transitionTime/slowdown:(int)(transitionTime/slowdown);
		int index=(input)?(20-(gifTime%numbertransitions)):(gifTime%numbertransitions)+1;
		transitionOff =(gifTime/(double)(numbertransitions)>=1)?true:false;

		float alpha = (!transitionOff)?(float)(multiplier*index * 0.1f):
			(input)?(float)(multiplier*1* 0.1f):(float)(multiplier*numbertransitions* 0.1f);
			AlphaComposite alcom = AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, alpha);
			return alcom;
	}
	
	public void changeButton(MouseEvent e,int newButton) {
		int x = e.getX();
		int y = e.getY();
		Rectangle2D button;
		for (int i = 0; i < sceneOBjectGroup.getIndexLlist().size(); i++) {
			if(sceneOBjectGroup.getIndexLlist().get(i)>=7 && sceneOBjectGroup.getIndexLlist().get(i)<=9) {
				button= new Rectangle(sceneOBjectGroup.getCoordinates().get(i).x, 
						sceneOBjectGroup.getCoordinates().get(i).y, 400, 140);
				if(button.contains(x, y)) {
					sceneOBjectGroup.setIndex(i, newButton);
				}
			}
		}
	}
	public void changeButtonAndRestoreAll(MouseEvent e,int newButton) {
		int x = e.getX();
		int y = e.getY();
		Rectangle2D button;
		for (int i = 0; i < sceneOBjectGroup.getIndexLlist().size(); i++) {
			if(sceneOBjectGroup.getIndexLlist().get(i)>=7 && sceneOBjectGroup.getIndexLlist().get(i)<=9) {
				button= new Rectangle(sceneOBjectGroup.getCoordinates().get(i).x, 
						sceneOBjectGroup.getCoordinates().get(i).y, 400, 140);
				if(button.contains(x, y)) {
					sceneOBjectGroup.setIndex(i, newButton);
				} 
				else
					sceneOBjectGroup.setIndex(i, 7);
			}
			
		}
	}
	public String getNameObjectScene(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Rectangle2D button;
		for (int i = 0; i < sceneOBjectGroup.getIndexLlist().size(); i++) {
			if(sceneOBjectGroup.getIndexLlist().get(i)>=7 && sceneOBjectGroup.getIndexLlist().get(i)<=9) {
				button= new Rectangle(sceneOBjectGroup.getCoordinates().get(i).x, 
						sceneOBjectGroup.getCoordinates().get(i).y, 400, 140);
				if(button.contains(x, y))
				return sceneOBjectGroup.getNamelist()[i];
			}
		}
		return null;
	}	

	public int [][] createSceneTimeList(int[] scene,int sceneStart) {
		int [][] finalScene= new int[scene.length][3];
		int sum=sceneStart;
		for (int i = 0; i < scene.length; i++) {
			for (int j = 0; j < 3; j++) {
				finalScene[i][j]=(j==0)?sum:(j==1)?sum+scene[i]:sum+scene[i]+4;
			}
			sum=sum+scene[i]+4;
		}
		return finalScene;
	}
	public void menuObjectsScene() {
		ArrayList<Integer> indexList = new ArrayList<Integer>(Arrays.asList(10,6,7,7,7,21,22,23,5,32));
		ArrayList<Point> coordinates = new ArrayList<Point>(Arrays.asList(new Point(0,0),new Point(600, 200),
				new Point(450, 200),new Point(450, 360),new Point(450, 520),
				new Point(450, 200),new Point(450, 360),new Point(450, 520),
				new Point(-250, -100),new Point(0, 0)));
		String[] namelist = new String[] {"fondoMenu","hunterMenu","start","screenshots","quit",
				"start text","screenshots text","quit text","splashMenu","logoMenu"};
		sceneOBjectGroup = new SceneOBjectGroup(indexList, coordinates, namelist);
	}
	public void pauseObjectsScene() {
		ArrayList<Integer> indexList = new ArrayList<Integer>(Arrays.asList(11,7,7,27,24,33,35,36));
		ArrayList<Point> coordinates = new ArrayList<Point>(Arrays.asList(new Point(0, 0),
				new Point(450, 200),new Point(450, 360),
				new Point(450, 200),new Point(450, 360),
				new Point(0, 0),
				new Point(400, 10),new Point(740, 20)
				));
		String[] namelist = new String[] {"menu pause","continue","main menu",
				"continue text","main menu text","pausa texto","save","screenImage"};
		sceneOBjectGroup = new SceneOBjectGroup(indexList, coordinates, namelist);
	}
	public void winObjectsScene() {
		ArrayList<Integer> indexList = new ArrayList<Integer>(Arrays.asList(11,7,24,37,38,39));
		ArrayList<Point> coordinates = new ArrayList<Point>(Arrays.asList(new Point(0, 0),
				new Point(450, 460),
				new Point(450, 460),
				new Point(10, -20),new Point(10, -20),new Point(10, -35)
				));
		String[] namelist = new String[] {"menu pause","main menu win",
				"main menu text","pausa texto","texto1","texto2","texto3"};
		sceneOBjectGroup = new SceneOBjectGroup(indexList, coordinates, namelist);
	}
	public void defeatObjectsScene() {
		ArrayList<Integer> indexList = new ArrayList<Integer>(Arrays.asList(11,7,24,40,41,42));
		ArrayList<Point> coordinates = new ArrayList<Point>(Arrays.asList(new Point(0, 0),
				new Point(450, 460),
				new Point(450, 460),
				new Point(10, -20),new Point(10, -20),new Point(10, -35)
				));
		String[] namelist = new String[] {"menu pause","main menu win",
				"main menu text","pausa texto","texto1","texto2"};
		sceneOBjectGroup = new SceneOBjectGroup(indexList, coordinates, namelist);
	}
	public void deleteObjectsScene() {
		ArrayList<Integer> indexList = new ArrayList<Integer>(Arrays.asList(35,36));
		ArrayList<Point> coordinates = new ArrayList<Point>(Arrays.asList(
				new Point(400, 10),new Point(740, 20)
				));
		String[] namelist = new String[] {"save","screenImage"};
		SceneOBjectGroup sceneOBjectGroup = new SceneOBjectGroup(indexList, coordinates, namelist);
		setSceneOBjectGroup(sceneOBjectGroup);
	}
	public void screenShotObjectsSceneNext() {
		actualScreen = ((actualScreen+1)<spriteSizeList.size())?(actualScreen+1):actualScreen;
		screenShotObjectsScene(actualScreen);
	}
	public void screenShotObjectsSceneBack() {
		actualScreen = ((actualScreen-1)>spritesNumber)?(actualScreen-1):actualScreen;
		screenShotObjectsScene(actualScreen);
	}
	public void screenShotObjectsScene(int screen) {
		ArrayList<Integer> indexList = new ArrayList<Integer>(Arrays.asList(screen,7,7,7,25,26,24));
		ArrayList<Point> coordinates = new ArrayList<Point>(Arrays.asList(
				new Point(0, 0),new Point(285, 560),new Point(685, 560),new Point(485, 420),
				new Point(285, 560),new Point(685, 560),new Point(485, 420)
				));
		String[] namelist = new String[] {"image 1","back","next","main menu",
				"back text","next text","main menu text"};
		SceneOBjectGroup sceneOBjectGroup = new SceneOBjectGroup(indexList, coordinates, namelist);
		setSceneOBjectGroup(sceneOBjectGroup);
	}
	public void setPanelScreenShot(JPanel panelScreenShot) {
		this.panelScreenShot = panelScreenShot;
		initScreenShots = true;
	}
	public int getTime() {
		return time;
	}
	public boolean isPause() {
		return pause;
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public SceneOBjectGroup getSceneOBjectGroup() {
		return sceneOBjectGroup;
	}
	public void setSceneOBjectGroup(SceneOBjectGroup sceneOBjectGroup) {
		this.sceneOBjectGroup = sceneOBjectGroup;
	}
	public ArrayList<Rectangle2D> getVirtualButonList() {
		return virtualButonList;
	}
	public ArrayList<Integer> getSpriteSizeList() {
		return spriteSizeList;
	}
	public void addSpriteSizeList(int size){
		spriteSizeList.add(size);
	}
	public void addLoadSize(int size){
		loadSize =size;
	}
	public int getPlaceOpacity() {
		return placeOpacity;
	}
	public void setPlaceOpacity(int placeOpacity) {
		this.placeOpacity = placeOpacity;
	}
	public int getEndScene() {
		return endScene;
	}
	public void setEndScene(int endScene) {
		this.endScene = endScene;
	}
	public String[][] getTextMatrix() {
		return TextMatrix;
	}
	public int getTransitionStarTime() {
		return transitionStarTime;
	}
	public void setTransitionStarTime(int transitionStarTime) {
		this.transitionStarTime = transitionStarTime;
	}
	public boolean isNewScreenshot() {
		return newScreenshot;
	}
	public void setNewScreenshot(boolean newScreenshot) {
		this.newScreenshot = newScreenshot;
	}
	public int getNewScreenshotNumber() {
		return newScreenshotNumber;
	}
	public void setNewScreenshotNumber(int newScreenshotNumber) {
		this.newScreenshotNumber = newScreenshotNumber;
	}
    public TimeGem getTimeGem() {
    	return timeGem;
    }
	public boolean isRestart() {
		return restart;
	}
	public void setRestart(boolean restart) {
		this.restart = restart;
	}
	public boolean isWin() {
		return win;
	}
	public void setWin(boolean win) {
		this.win = win;
	}
	public int getCurrentTimeScene() {
		return currentTimeScene;
	}
	public void setCurrentTimeScene(int currentTimeScene) {
		this.currentTimeScene = currentTimeScene;
	}
	public String getCurrentScene() {
		return currentScene;
	}
	public void setCurrentScene(String currentScene) {
		this.currentScene = currentScene;
	}
	public String getChangeScene() {
		return changeScene;
	}
	public void setChangeScene(String changeScene) {
		this.changeScene = changeScene;
	}
	public void setSpritesNumber(int spritesNumber) {
		this.spritesNumber = spritesNumber;
		actualScreen = spritesNumber;
	}
	public boolean isScreenShot() {
		return screenShot;
	}
	public boolean isSave() {
		return save;
	}
	public void setLoadSize(int loadSize) {
		this.loadSize = loadSize;
	}
	public int getEndPrelude() {
		return endPrelude;
	}
	public void setEndPrelude(int endPrelude) {
		this.endPrelude = endPrelude;
	}
	public boolean isSkipe() {
		return skipe;
	}
	public void setSkipe(boolean skipe) {
		this.skipe = skipe;
	}
	public static int getCountDown() {
		return COUNT_DOWN;
	}
}