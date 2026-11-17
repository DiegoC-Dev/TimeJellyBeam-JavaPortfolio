package presenters;

import models.Game;
import models.IndexColection;
import models.ResourcePath;
import models.SceneOBjectGroup;
import models.Sprite;
import views.GameWindow;
import views.JWWindowProgress;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SecondaryLoop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GamePresenter implements KeyListener,MouseListener, MouseMotionListener{

    private static final int[] PRELUDE_SCENE_DURATIONS = new int[] {2, 9, 9, 12, 9, 4, 4};
    private static final int ANIMATION_TICKS_PER_SECOND = 500;
    private static final long NANOS_PER_ANIMATION_TICK = 1_000_000_000L / ANIMATION_TICKS_PER_SECOND;
    private static final long MAX_FRAME_ELAPSED_NANOS = 100_000_000L;

    private Game game;
    private GameWindow window;
    private int seg = 0;
    private long accumulatedNanos = 0L;
    
    public GamePresenter(){
    	System.out.println("vrs 7.9");
        try {
        	window = new GameWindow(this);
        	game = new Game();
        	init();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	private void init() throws IOException {
		addLoadSprite();
		window.draw();
		mainTimer();
		viewData();
		fillSpriteList();
		initPrelude();
		game.writeData();
	}
	private void viewData() {
		window.updateHero(game.getHero());
		window.updateObjectList(game.getGameObjectList());
		window.updateEnemys(game.getEnemyList());
		window.updateGems(game.getGemList());
		window.setGemTime(game.getTimeGem());
		window.setInitTime(game.getCountDown());
	}
	private void initPrelude() {
		game.setChangeScene("prelude");
	}
	private void addLoadSprite() throws IOException {
		window.setLoadSprite(createSprite("src/load_gif"));
		game.addLoadSize(extractSpriteSize("src/load_gif"));
	}
	private void fillSpriteList() throws IOException {
		addSpriteList("src/img/Java.png");
		addSpriteList("src/timeRepresentation_gif");
		addSpriteList("src/brakingStone_gif");
		addSpriteList("src/jellyProglogue_gif");
		addSpriteList("src/img/blackHunter.png");
		addSpriteList("src/img/splash.png");
		addSpriteList("src/hunterMenu_gif");
		addSpriteList("src/img/boton1.png");
		addSpriteList("src/img/boton2.png");
		addSpriteList("src/img/boton3.png");
		addSpriteList("src/img/fondoMenu.png");
		addSpriteList("src/img/Menu t.png");
		addSpriteList("src/img/gema 2.png");
		addSpriteList("src/img/texto 0.png");
		addSpriteList("src/img/texto 1.png");
		addSpriteList("src/img/texto 2.png");
		addSpriteList("src/img/texto 3.png");
		addSpriteList("src/img/texto 4.png");
		addSpriteList("src/img/texto 5.png");
		addSpriteList("src/img/texto 6.png");
		addSpriteList("src/img/texto 6.png");
		addSpriteList("src/img/comenzar.png");
		addSpriteList("src/img/screenshots.png");
		addSpriteList("src/img/Salir.png");
		addSpriteList("src/img/menu principal.png");
		addSpriteList("src/img/anterior.png");
		addSpriteList("src/img/siguiente.png");
		addSpriteList("src/img/reanudar.png");
		addSpriteList("src/littleGem_gif");
		addSpriteList("src/hunterRightAttack_gif");
		addSpriteList("src/hunterLeftAttack_gif");
		addSpriteList("src/bigGem_gif");
		addSpriteList("src/img/logoMenu.png");
		addSpriteList("src/img/pausa.png");
		addSpriteList("src/img/escena inicio.png");
		addSpriteList("src/img/save.png");
		addSpriteList("src/img/screenShotImage.png");
		addSpriteList("src/img/winText1.png");
		addSpriteList("src/img/winText2.png");
		addSpriteList("src/img/tiempoWin.png");
		addSpriteList("src/img/defeat0.png");
		addSpriteList("src/img/defeat1.png");
		addSpriteList("src/img/defeat2.png");
		game.setSpritesNumber(game.getSpriteSizeList().size());
		int screenCount = ResourcePath.countFolderFiles("src/screenShoots");
		for (int i = 0; i < screenCount; i++) {
			window.addSpriteList(createSprite("src/screenShoots/screenshot "+(i+1)+".png"));	
			game.addSpriteSizeList(extractSpriteSize("src/screenShoots/screenshot "+(i+1)+".png"));
		}
	}
	private void addSpriteList(String path) throws IOException {
		window.addSpriteList(createSprite(path));
		game.addSpriteSizeList(extractSpriteSize(path));
	}
	private void addScrenShot(boolean screenShoot) {
		try {
			if(screenShoot) {
				for (int i = 0; i < game.getNewScreenshotNumber(); i++) {
					window.addSpriteList(createSprite("src/screenShoots/screenshot "+(i+1)+".png"));
					game.addSpriteSizeList(extractSpriteSize("src/screenShoots/screenshot "+(i+1)+".png"));				
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private int extractSpriteSize(String path) {
		if(isGif(path)) {
			int count = ResourcePath.countFolderFiles(path);
			return (count > 0) ? count : 1;
		}
		else
			return 1;	
	}
	private Sprite createSprite(String path) throws IOException {
		ArrayList<BufferedImage> sprite = new ArrayList<BufferedImage>();
		if(isGif(path)){
			int count = ResourcePath.countFolderFiles(path);
			for (int i = 0; i < count; i++) {
				sprite.add(ImageIO.read(ResourcePath.resolve(path+"/frame-"+(i+1)+".gif")));
			}
		}else 
			sprite.add(ImageIO.read(ResourcePath.resolve(path)));
		return new Sprite(sprite,path);
	}
	private void mainTimer() {
		Timer gameLoop = new Timer(16, new ActionListener(){
			int cont=0;
			int transitionSlowdown= 10;
			long lastTickNanos = System.nanoTime();
			long tickAccumulatedNanos = 0L;
			public void actionPerformed(ActionEvent actionEvent) {
				long now = System.nanoTime();
				long elapsedNanos = Math.min(now - lastTickNanos, MAX_FRAME_ELAPSED_NANOS);
				lastTickNanos = now;

				game.getHero().setPause(game.isPause());
				game.setPanelScreenShot(window.getPanel());
				addScrenShot(game.isNewScreenshot());
				if(!game.isPause()) {
					accumulatedNanos += elapsedNanos;
					if(accumulatedNanos >= 1_000_000_000L) {
						accumulatedNanos -= 1_000_000_000L;
						seg++;
						if(game.getCurrentScene().equals("game")) {
							game.setTime(game.getTime()-1);
							window.setTime(game.getTime());
						}
					}
					tickAccumulatedNanos += elapsedNanos;
					while(tickAccumulatedNanos >= NANOS_PER_ANIMATION_TICK) {
						tickAccumulatedNanos -= NANOS_PER_ANIMATION_TICK;
						cont++;
						if(game.getCurrentScene().equals("game")) {
							game.setEnemyMovements(cont);
						}
					}
				}

					try {
						loadScene(cont, seg, transitionSlowdown);
						preludeAnimation(cont,seg,transitionSlowdown);
						menuScene(cont,seg,transitionSlowdown);
						introductoryScene(cont, seg, transitionSlowdown);
						gameScene(cont,seg,transitionSlowdown);
						victoryScene(cont, seg, transitionSlowdown);
						defeatScene(cont, seg, transitionSlowdown);
						screenShotScene(cont, seg,transitionSlowdown);
					} catch (IOException e) {
						e.printStackTrace();
				}
			}
		});
		gameLoop.start();
	}
	private void gameMechanics(int cont) throws IOException {
		int extraTime=2;
		window.updateHero(game.getHero()); 
		game.setEnemyMovements(cont);
		window.updateEnemys(game.getEnemyList());
		window.setIndexColectionEnemyList(addHunterIndexCollectionList(cont, 10, new int[] {29,29,29,29,29}));
		window.setIndexColectionGemList(addIndexCollectionList(cont, 60, new int[] {28}));
		window.setIndexGemTime(addIndexCollectionList(cont, 60, new int[] {31})[0]);
		game.successfulAttack();
		game.defeatCondition();
		game.winCondition();
		window.setSave(game.isSave());
		window.setScreenShot(game.isScreenShot());

		if(game.consumedGem(game.getHero())) {
			game.setTime(game.getTime()+extraTime);
			window.setTime(game.getTime());	
		}
//		if(game.getTime()==0) {
//			window.setTimeExausted(true);						
//		}
	}
	private IndexColection[] addHunterIndexCollectionList(int cont,int slowdownImage, int[] indexSpriteList) throws IOException {
		int adder = 123;
		IndexColection[] indexCollectionList = new IndexColection[indexSpriteList.length];
		for (int i = 0; i < indexSpriteList.length; i++) {
			indexCollectionList[i]=(game.getSpriteSizeList().size()>1)?game.createIdexCollection(cont+(i*adder), slowdownImage, indexSpriteList[i]):
				new IndexColection(indexSpriteList[i], 0);
			
		}
		return indexCollectionList;
	}
	private IndexColection[] addIndexCollectionList(int cont,int slowdownImage, int[] indexSpriteList) throws IOException {
		IndexColection[] indexCollectionList = new IndexColection[indexSpriteList.length];
		for (int i = 0; i < indexSpriteList.length; i++) {
			indexCollectionList[i]=(game.getSpriteSizeList().size()>1)?game.createIdexCollection(cont, slowdownImage, indexSpriteList[i]):
				new IndexColection(indexSpriteList[i], 0);
		}
		return indexCollectionList;
	}
	private IndexColection addIndexLoad(int cont,int slowdownImage) throws IOException {
		return game.createIdexLoad(cont, slowdownImage);
	}
	public boolean isGif(String path){
		return (Character.compare(path.charAt(path.length()-4),'_')==0)?true:false;
	}
	public void placeScene(int seg,int cont,int slow ,boolean tInput,int sceneStart,int placeOpacity,int endScene,int[] indexSpriteList,int transitionSlowdown,Point[] positionImages) throws IOException {
		boolean transitionInput= tInput;
		boolean setAlphaComposite=false;
		if(seg==sceneStart)
			game.setTransitionStarTime(cont);
		if(seg>=sceneStart && seg<endScene) {
			transitionInput = true;
			setAlphaComposite= true;
			if(indexSpriteList!=null) {
				if(!game.getCurrentScene().equals("load")) {
					window.setIndexColectionList(addIndexCollectionList(cont,slow, indexSpriteList));
					window.setPositionImages(positionImages);					
				}
				else {
					window.setIndexLoad(addIndexLoad(cont, slow));
					window.setPositionImageLoad(positionImages[0]);					
				}
			}
		}
		if(seg==placeOpacity)
			game.setTransitionStarTime(cont);
		if(seg>=placeOpacity && seg<endScene) {
			transitionInput = false;
		}
		if(seg>(sceneStart+1) && seg<placeOpacity)
			setAlphaComposite=false;
		if(setAlphaComposite)
			window.setAlphaComposite(game.createTransition(cont, transitionSlowdown, transitionInput));
	}
	private void createAutomaticScene(int cont, int seg, int transitionSlowdown,
			int[][] scene, int[][] indexSpriteList,
			Point[][] positionImages, int[] slowDownImage,String nextScene) throws IOException {
		boolean transitionInput = true;	
		int sceneParts = 3;
		int sceneStart =0;
		int placeOpacity = 0;
		int endScene =0;
		int timeNextScene=scene[scene.length-1][2];
//		if(game.isSkip()) {
//			timeNextScene= seg;
//		}
		if(seg==timeNextScene) {
			game.setCurrentTimeScene(timeNextScene);
			game.setCurrentScene(nextScene);
			game.setChangeScene(nextScene);
			if(nextScene.equals("menu")) {
				game.stopPreludeSound();
				game.playMenuSound();
			}
		}
		
		for (int i = 0; i < scene.length; i++) {
			for (int j = 0; j < sceneParts; j++) {
				sceneStart = (j==0)?scene[i][j]:sceneStart;
				placeOpacity =(j==1)?scene[i][j]:placeOpacity;
				endScene =(j==2)?scene[i][j]:endScene;
			}
//			if(game.isSkip()) {
//				i=scene.length-1;
//			}
			placeScene(seg,cont,slowDownImage[i],transitionInput,sceneStart,placeOpacity,endScene,
					indexSpriteList[i],transitionSlowdown,positionImages[i]);				
		}
	}
	private void preludeAnimation(int cont,int seg,int transitionSlowdown) throws IOException {
		if(game.getCurrentScene().equals("prelude")) {
			String nextScene = "menu";
//			if(game.isSkipe()) {
//				game.setCurrentTimeScene(seg);
//				game.setCurrentScene(nextScene);
//				game.setChangeScene(nextScene);
//			}
//			else {
				
				int timeStart =game.getCurrentTimeScene();
				int []TimeScene= PRELUDE_SCENE_DURATIONS;
				int [][] scene= game.createSceneTimeList(TimeScene,timeStart);
				if(seg==timeStart)
					game.setEndPrelude(scene[scene.length-1][2]);
				int[][] indexSpriteList =new int[][] {new int[]{0,13},new int[]{1,14},new int[]{2,15},new int[]{3,16},
					new int[]{4,17},new int[]{18},new int[]{19}};
					Point[][] positionImages = new Point[][] {new Point[] {new Point(468,226),new Point(-30,-20)},
						new Point[] {new Point(55,215),new Point(-30,0)},
						new Point[] {new Point(155, 134),new Point(-30,0)},
						new Point[] {new Point(85, 196),new Point(-30,0)},
						new Point[] {new Point(185, 134),new Point(-30,0)},
						new Point[] {new Point(0,0)},new Point[] {new Point(0,0)}};	
						int[] slowDownImage = new int[] {1,30,30,80,1,1,1};
						int fadeStart = scene[scene.length-2][0];
						int fadeEnd = scene[scene.length-1][2];
						if(seg >= fadeStart) {
							game.fadePreludeVolume(computeFadeOutVolume(fadeStart, fadeEnd));
						}
						createAutomaticScene(cont, seg, transitionSlowdown,
								scene, indexSpriteList, positionImages, slowDownImage,nextScene);
//			}
		}
	}
	private void introductoryScene(int cont,int seg,int transitionSlowdown) throws IOException {
		if(game.getCurrentScene().equals("introduction")) {
			int timeStart =game.getCurrentTimeScene();
			String nextScene = "game";
			int []TimeScene= new int[] {16};
			int [][] scene= game.createSceneTimeList(TimeScene,timeStart);
			int[][] indexSpriteList =new int[][]{new int[]{34}};
			Point[][] positionImages = new Point[][] {new Point[] {new Point(0,0)}};	
			int[] slowDownImage = new int[] {1};
			game.fadeMenuVolume(computeFadeOutVolume(scene[0][0], scene[0][2]));
			
			createAutomaticScene(cont, seg, transitionSlowdown,
					scene, indexSpriteList, positionImages, slowDownImage,nextScene);
			if(seg==scene[scene.length-1][2]) {
				window.setInitGame(true);
				game.deleteObjectsScene();
				game.stopMenuSound();
				game.playBackgroundSound();
			}
		}
	}
	private void loadScene(int cont, int seg,int transitionSlowdown) throws IOException {
		if(game.getCurrentScene().equals("load")) {
		boolean transitionInput = true;	
		int sceneStart =0;
		int[]indexSpriteList =new int[]{-1};
		Point[] positionImages = new Point[] {new Point(585,284)};	
		int slowDownImage = 20;
		if(game.getChangeScene().equals("load")) {
			placeEndScene(seg);
		}
			if(seg==game.getEndScene()){
				game.setCurrentScene("prelude");
				game.setCurrentTimeScene(seg);
				window.setFinishLoad(true);
				game.playPreludeSound();
			}
			placeScene(seg,cont,slowDownImage,transitionInput,
					sceneStart,game.getPlaceOpacity(),game.getEndScene(),
					indexSpriteList,transitionSlowdown-5,positionImages);				
		}
	}
	private void menuScene(int cont, int seg,int transitionSlowdown) throws IOException {
		if(game.getCurrentScene().equals("menu")) {
//			game.playMenuSound();
			boolean transitionInput = true;
			if(game.getChangeScene().equals("menu")) {
				placeEndScene(seg);
			}
			if(seg==game.getEndScene()){
				if(game.getChangeScene().equals("introduction")) {						
					game.setCurrentScene("introduction");
				}
				if(game.getChangeScene().equals("screenShots")) {	
					game.screenShotObjectsSceneBack();
					game.setCurrentScene("screenShots");
				}
				game.setCurrentTimeScene(seg);
			}
			placeScene(seg,cont,20,transitionInput,
					game.getCurrentTimeScene(),game.getPlaceOpacity(),game.getEndScene(),
					toArrayInteger(game.getSceneOBjectGroup().getIndexLlist()),transitionSlowdown,
					toArrayPoint(game.getSceneOBjectGroup().getCoordinates()));
		}
	}
	private void gameScene(int cont, int seg,int transitionSlowdown) throws IOException {
		if(game.getCurrentScene().equals("game")) {
			gameMechanics(cont);
				boolean transitionInput = true;
				if(game.getChangeScene().equals("game")) {
					placeEndScene(seg);
				} else if(game.getChangeScene().equals("menu")) {
					game.fadeBackgroundVolume(computeFadeOutVolume(game.getEndScene()-3, game.getEndScene()));
				}
				if(seg==game.getEndScene()){
					window.setInitGame(false);
					game.setCurrentTimeScene(seg);
					if(game.getChangeScene().equals("victory")) {						
						game.stopBackgroundSound();
						game.setCurrentScene("victory");
						window.win(true);
						game.winObjectsScene();
						game.restartHero();
						game.setTime(game.getCountDown());
						window.setInitTime(game.getCountDown());
						game.restartGems();
					}
					if(game.getChangeScene().equals("defeat")) {						
						game.stopBackgroundSound();
						game.setCurrentScene("defeat");
//						window.defeat(true);
						game.defeatObjectsScene();
						game.restartHero();
						game.setTime(game.getCountDown());
						window.setInitTime(game.getCountDown());
						game.restartGems();
					}
					if(game.getChangeScene().equals("menu")) {
						game.stopBackgroundSound();
						game.setCurrentScene("menu");
						game.setCurrentTimeScene(seg);
						game.menuObjectsScene();
						game.playMenuSound();
					}
				}
				placeScene(seg,cont,1,transitionInput,
						game.getCurrentTimeScene(),game.getPlaceOpacity(),game.getEndScene(),
						toArrayInteger(game.getSceneOBjectGroup().getIndexLlist()),transitionSlowdown,
						toArrayPoint(game.getSceneOBjectGroup().getCoordinates()));
		}
	}
	private void victoryScene(int cont, int seg,int transitionSlowdown) throws IOException {
		if(game.getCurrentScene().equals("victory")) {
			boolean transitionInput = true;
			if(game.getChangeScene().equals("victory")) {
				placeEndScene(seg);
			}
			if(seg==game.getEndScene()){
				game.setCurrentScene("menu");
				game.setCurrentTimeScene(seg);
				game.menuObjectsScene();
				window.win(false);
				game.restartHero();
				game.playMenuSound();
			}
			placeScene(seg,cont,1,transitionInput,
					game.getCurrentTimeScene(),game.getPlaceOpacity(),game.getEndScene(),
					toArrayInteger(game.getSceneOBjectGroup().getIndexLlist()),transitionSlowdown,
					toArrayPoint(game.getSceneOBjectGroup().getCoordinates()));
		}
	}
	private void defeatScene(int cont, int seg,int transitionSlowdown) throws IOException {
		if(game.getCurrentScene().equals("defeat")) {
			boolean transitionInput = true;
			if(game.getChangeScene().equals("defeat")) {
				placeEndScene(seg);
			}
			if(seg==game.getEndScene()){
				game.setCurrentScene("menu");
				game.setCurrentTimeScene(seg);
				game.menuObjectsScene();
//				window.defeat(false);f
				game.restartHero();
				game.playMenuSound();
			}
			placeScene(seg,cont,1,transitionInput,
					game.getCurrentTimeScene(),game.getPlaceOpacity(),game.getEndScene(),
					toArrayInteger(game.getSceneOBjectGroup().getIndexLlist()),transitionSlowdown,
					toArrayPoint(game.getSceneOBjectGroup().getCoordinates()));
		}
	}

	private void screenShotScene(int cont, int seg,int transitionSlowdown) throws IOException {
		if(game.getCurrentScene().equals("screenShots")) {
				boolean transitionInput = true;
				if(game.getChangeScene().equals("screenShots")) {
					placeEndScene(seg);
				}
				if(seg==game.getEndScene()){
					game.setCurrentScene("menu");
					game.setCurrentTimeScene(seg);
					game.menuObjectsScene();
				}
				placeScene(seg,cont,1,transitionInput,
						game.getCurrentTimeScene(),game.getPlaceOpacity(),game.getEndScene(),
						toArrayInteger(game.getSceneOBjectGroup().getIndexLlist()),transitionSlowdown,
						toArrayPoint(game.getSceneOBjectGroup().getCoordinates()));
		}
	}
	public void placeEndScene(int seg){
		game.setPlaceOpacity(seg+1);
		game.setEndScene(seg+3);
	}

	public int[] toArrayInteger(ArrayList<Integer> arrayList) {
		int[] ret = new int[arrayList.size()];
		for (int i=0; i < ret.length; i++)
			ret[i] = arrayList.get(i);
		return ret;
	}
	public Point[] toArrayPoint(ArrayList<Point> arrayList) {
		Point[] ret = new Point[arrayList.size()];
		for (int i=0; i < ret.length; i++)
			ret[i] = arrayList.get(i);
		return ret;
	}
	public static void main(String[] args) {
		new GamePresenter();
	}    
    
	private float computeFadeOutVolume(int startSeg, int endSeg) {
		if(endSeg <= startSeg) {
			return 1.0f;
		}
		float exactSeg = seg + (accumulatedNanos / 1_000_000_000.0f);
		float progress = (exactSeg - startSeg) / (endSeg - startSeg);
		return Math.max(0.0f, Math.min(1.0f, 1.0f - progress));
	}

	private void advancePreludeScene() {
		if(!game.getCurrentScene().equals("prelude")) {
			return;
		}
		int[][] scene = game.createSceneTimeList(PRELUDE_SCENE_DURATIONS, game.getCurrentTimeScene());
		for(int i = 0; i < scene.length; i++) {
			if(seg >= scene[i][0] && seg < scene[i][2]) {
				if(i < scene.length - 1) {
					seg = scene[i][2];
					accumulatedNanos = 0L;
				} else {
					skipPreludeToMenu();
				}
				return;
			}
		}
	}

	private void skipPreludeToMenu() {
		if(!game.getCurrentScene().equals("prelude")) {
			return;
		}
		game.setCurrentTimeScene(seg);
		game.setCurrentScene("menu");
		game.setChangeScene("menu");
		accumulatedNanos = 0L;
		game.stopPreludeSound();
		game.playMenuSound();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(game.getCurrentScene().equals("prelude")) {
			if(key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER || key == KeyEvent.VK_RIGHT) {
				advancePreludeScene();
				return;
			}
			if(key == KeyEvent.VK_ESCAPE) {
				skipPreludeToMenu();
				return;
			}
		}
		key = key==37?key+10:key;
		game.setPause(key==80 && !game.isPause()?true:key==80 && game.isPause()?false:game.isPause());
		System.out.println(key);
		if(game.isPause()) {
			game.pauseObjectsScene();
		}
		else
			game.deleteObjectsScene();
		if(key==73 && game.getCurrentScene().equals("load")) {
//		if(key==27 && game.getCurrentScene().equals("load")) {
			game.setSkipe(false);
//			System.out.println("activo");
		}
	
		if(!game.isPause() && game.getCurrentScene().equals("game")&& !game.isWin()) {
			game.getHero().receiveKeys(key);
		}
//		game.soundBackdgoundPause(key);/////////////----------------------------------------------------------
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		key = key==37?key+10:key;
		game.getHero().removePressedKey(key);
    }
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		if(!game.getCurrentScene().equals("load") &&
				!game.getCurrentScene().equals("prelude") &&
				!game.getCurrentScene().equals("introduction"))
		game.changeButtonAndRestoreAll(window.toLogicalMouseEvent(e), 8);	
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(game.getCurrentScene().equals("prelude")) {
			advancePreludeScene();
			return;
		}
		MouseEvent logicalEvent = window.toLogicalMouseEvent(e);
		game.changeButton(logicalEvent,9);
		if(game.getNameObjectScene(logicalEvent)!=null) {
			if(game.getNameObjectScene(logicalEvent).equals("start")){
				game.setChangeScene("introduction");
			} 
			else if(game.getNameObjectScene(logicalEvent).equals("screenshots")){	
				game.setChangeScene("screenShots");
			}
			else if(game.getNameObjectScene(logicalEvent).equals("quit")){				
				System.exit(0);
			}
			else if(game.getNameObjectScene(logicalEvent).equals("continue")){
				game.deleteObjectsScene();
				game.setPause(false);
			}
			else if(game.getNameObjectScene(logicalEvent).equals("main menu")){				
				game.setPause(false);
				game.setChangeScene("menu");
			}else if(game.getNameObjectScene(logicalEvent).equals("back")){
				game.screenShotObjectsSceneBack();
			}else if(game.getNameObjectScene(logicalEvent).equals("next")){
				game.screenShotObjectsSceneNext();
			}
			else if(game.getNameObjectScene(logicalEvent).equals("main menu win")){				
				game.setChangeScene("menu");
			}
			
			System.out.println(game.getNameObjectScene(logicalEvent));
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		}
	public void setvisibleFrame() {
		window.setVisible(true);
	}
}