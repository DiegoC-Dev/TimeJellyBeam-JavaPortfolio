package views;

import javax.imageio.ImageIO;
import javax.swing.*;

import models.Enemy;
import models.GameObject;
import models.Gem;
import models.Hero;
import models.IndexColection;
import models.Sprite;
import models.TimeGem;
import presenters.GamePresenter;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;

public class GameWindow extends JFrame{

	private GamePanel panelGame;

    public GameWindow(GamePresenter gamePresenter) throws IOException{
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setSize(screenSize.width,screenSize.height);
        setUndecorated(true);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	panelGame = new GamePanel(gamePresenter);
    	add(panelGame, BorderLayout.CENTER);
    	setVisible(true);
    }
    public void updateObjectList(ArrayList<GameObject> gameObjectList) {
    	panelGame.updateObjectList(gameObjectList);
    }
    
    public void updateHero(Hero Hero) {
    	panelGame.updateHero(Hero);
    }
    public void updateEnemys(ArrayList<Enemy> enemyList) {
    	panelGame.updateEnemys(enemyList);
    }
    public void updateGems(ArrayList<Gem> gemList) {
    	panelGame.updateGems(gemList);
    }
    public void win(boolean win) {
    	panelGame.setWin(win);
    }
    public void setTime(int time) {
    	panelGame.setTime(time);
    }
    public void setInitGame(boolean game) {
    	panelGame.setInitGame(game);
    }
    public JPanel getPanel() {
    	return panelGame;
    }
    public JFrame getFrame() {
    	return this;
    }
    public void draw() {
    	panelGame.draw();
    }
	public void setAlphaComposite(AlphaComposite alphaComposite) {
		panelGame.setAlphaComposite(alphaComposite);
	}
	public void setPositionImages(Point[] positionImages) {
		panelGame.setPositionImages(positionImages);
	}
	public void addSpriteList(Sprite sprite) {
		panelGame.addSpriteList(sprite);
	}
	public void setIndexColectionList(IndexColection[] indexSpriteList) {
		panelGame.setIndexColectionList(indexSpriteList);
	}
	public void setIndexColectionEnemyList(IndexColection[] indexColectionEnemList) {
		panelGame.setIndexColectionEnemyList(indexColectionEnemList);
	}
	public void setIndexColectionGemList(IndexColection[] indexColectiondGemList) {
		panelGame.setIndexColectionGemList(indexColectiondGemList);
	}
	public void setIndexGemTime(IndexColection indexGemTime) {
		panelGame.setIndexBigGem(indexGemTime);
	}
	public void setGemTime(TimeGem gemTime) {
		panelGame.setGemTime(gemTime);
	}
	public void setSave(boolean save) {
		panelGame.setSave(save);
	}
	public void setScreenShot(boolean screenShot) {
		panelGame.setScreenShot(screenShot);
	}
	public void setInitTime(int initTime) {
		panelGame.setInitTime(initTime);
	}
	public void setLoadSprite(Sprite loadSprite) {
		panelGame.setLoadSprite(loadSprite);
	}
	public void setPositionImageLoad(Point positionImageLoad) {
		panelGame.setPositionImageLoad(positionImageLoad);
	}
	public void setIndexLoad(IndexColection indexLoad) {
		panelGame.setIndexLoad(indexLoad);
	}
	public void setFinishLoad(boolean finishLoad) {
		panelGame.setFinishLoad(finishLoad);
	}
}