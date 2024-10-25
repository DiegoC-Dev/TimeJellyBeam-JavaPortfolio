package models;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Hero extends GameObject{
	private String imageType; 
	int [][]rescuedSpriteCoords;
	private int index;
	private boolean awake;
	private String savedImage;
    private String savedImgJump;
    private String savedImgVerticalLef;
    private String savedImgVerticalRight;
    private String savedImgInverted;
    private String savedImgNormal;
    private ArrayList<Integer> pressedKeys; 
	protected ArrayList<GameObject> gameObjectList;
	private int graphicTimer = 0;
	private int yBeforeJump=0;
	private int xBeforeJump=0;
	private boolean adhered;
	private boolean fall;
	private boolean collision;
    private boolean jumpUp;
	private boolean jumpRight;
    private boolean jumpLeft;
    private boolean jumpDown;
    private boolean freeMovement;
    private boolean pause;
    int[][] leftCoords = {{ 354, 1, 100, 110 },{ 250, 1, 100, 110 },{ 145, 1, 100, 110 },{ 37, 1, 100, 110 },{ 145, 1, 100, 110 }};
    int[][] rightCoords = {{ 175, 1, 98, 110 },{ 280, 1, 98, 110 },{ 384, 1, 98, 110 },{ 492, 1, 98, 110 },{ 384, 1, 98, 110 }};
    int[][] jumpRightCoords = {{ 25, 215, 108, 120 },{ 139, 215, 115, 120 },{ 252, 215, 115, 120 },{ 372, 215, 115, 120 },{ 494, 215, 115, 120 }};
    int[][] jumpLeftCoords = {{ 490, 215, 115, 120 },{ 380, 215, 115, 120 },{ 260, 215, 115, 120 },{ 136, 215, 115, 120 },{ 26, 215, 115, 120 }};
  	int[][] upLeftCoords = {{ 261, 354, 75, 100 },{ 261, 248, 75, 100},{ 261, 144, 75, 100},{ 261, 36, 75, 100},{ 261, 144, 75, 100}};
	int[][] downLeftCoords = {{ 261, 175, 75, 100},{ 261, 280, 75, 100},{ 261, 384, 75, 110 },{ 261, 494, 75, 110 },{ 261, 384, 75, 110 }};
	int[][] downRightCoords = {{ 20, 175, 80, 100},{ 20, 280, 80, 100},{ 20, 384, 80, 110 },{ 20, 494, 80, 110 },{ 20, 384, 80, 110 }};
    int[][] upRightCoords = {{ 20, 354, 75, 100 },{ 20, 248, 75, 100},{ 20, 144, 75, 100},{ 20, 36, 75, 100},{ 20, 144, 75, 100}};
    int[][] leftInvCoords = {{ 354, 260, 100, 80 },{ 250, 260, 100, 80 },{ 145, 260, 100, 80 },{ 37, 260, 100, 80 },{ 145, 260, 100, 80 }};
    int[][] rightInvCoords = {{ 175, 260, 98, 80 },{ 280, 260, 98, 80 },{ 384, 260, 98, 80 },{ 492, 260, 98, 80 },{ 384, 260, 98, 80 }};
	int[][] spriteCoords;
	int[][] spriteJumpCoords;
	int[][] spriteVerticalLeftCoords;
	int[][] spriteVerticalRightCoords;
	int[][] spriteInvertCoords;
	int[][] spriteNormalCoords;
	private BufferedImage rightInvImg;
	private BufferedImage leftInvImg;
	private BufferedImage upRightImg;
	private BufferedImage downRightImg;
	private BufferedImage downLeftImg;
	private BufferedImage upLeftImg;
	private BufferedImage leftImg;
    private BufferedImage rightImg;
    private BufferedImage img;
    private BufferedImage imgJump;
    private BufferedImage imgVerticalLeft;
    private BufferedImage imgVerticalRight;
    private BufferedImage imgInverted;
    private BufferedImage imgNormal;
	private Image subSprite;

	public Hero(double x, double y, double diameter,ArrayList<GameObject> gameObjectList, Color color) {
		super(x, y, diameter, color);
		imageType="";/////////////////////////////////////
		fall=true;
		jumpUp= true;
		pressedKeys = new ArrayList<Integer>(Arrays.asList(0));
		this.gameObjectList = gameObjectList;
		spriteJumpCoords = jumpRightCoords;
		spriteCoords =rightCoords;
    	spriteVerticalLeftCoords = upLeftCoords;
    	spriteVerticalRightCoords = upRightCoords;
    	spriteInvertCoords = rightInvCoords;
    	spriteNormalCoords = rightCoords;
    	try {
    		rightInvImg = ImageIO.read(new File("src/img/jelly - Right - Inv.png"));
    		leftInvImg = ImageIO.read(new File("src/img/jelly - Left - Inv.png"));
    		upRightImg = ImageIO.read(new File("src/img/jelly - Up - Right.png"));
    		downRightImg = ImageIO.read(new File("src/img/jelly - Down - Right.png"));
    		downLeftImg = ImageIO.read(new File("src/img/jelly - Down - Left.png"));
    		upLeftImg = ImageIO.read(new File("src/img/jelly - Up - Left.png"));
    		leftImg = ImageIO.read(new File("src/img/jelly - Left.png"));
    		rightImg = ImageIO.read(new File("src/img/jelly - Right.png"));
    		img =rightImg;
    		imgJump =rightImg;
    		imgVerticalLeft=upLeftImg;
    		imgVerticalRight=upRightImg;
    		imgInverted = rightInvImg;
    		imgNormal =rightImg;
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	timer();
	}
	
	public void setPressedKeys(ArrayList<Integer> pressedKeys) {
		this.pressedKeys = pressedKeys;
	}

	public void setSpriteCoords(int[][] spriteCoords) {
		this.spriteCoords = spriteCoords;
	}

	public void setCoordinates(Point coordinates) {
		int loop= 0;
		int newY=coordinates.y;
		int newX=coordinates.x;		
		int irregular = perceiveObject(2,2);
		collision=detectCollision(coordinates.getX(), coordinates.getY());
		if(collision) {
			cancelJumps();
			adhered=true;
			if (irregular%2==0) {
				while (collision) {
					loop++;
					if((this.x-coordinates.getX())==0)
						newX =((this.y-coordinates.getY())<0)?((irregular==8)?(newX+1):(newX-1)):((irregular==10)?(newX-1):(newX+1));
					else if((this.y-coordinates.getY())==0)
						newY=((this.x-coordinates.getX())<0)?((irregular==10)?(newY+1):(newY-1)):((irregular==8)?(newY-1):(newY+1));
						newY=(loop>20)?(int)this.y:newY;
						newX=(loop>20)?(int)this.x:newX;
				collision=detectCollision(newX, newY);
				}
				saveCoord(coordinates, newY, newX);					
			}
			loop=0;
		}
		else 
			saveCoord(coordinates, newY, newX);
	}

	private void saveJumpPoint() {
		yBeforeJump=(int)y;
		xBeforeJump=(int)x;
		fall=false;
	}
	private void saveCoord(Point coordinates, int newY, int newX) {
		this.logicalArea = new Area(new Ellipse2D.Double((double)coordinates.getX(), 
				(double)coordinates.getY(), diameter, diameter)) ;
		this.x=newX;
		this.y=newY;
	}
	
	public ArrayList<GameObject> getGameObjectList() {
		return gameObjectList;
	}
	
	private boolean detectCollision(double xArea,double yArea){
		Area ownArea = new Area(new Ellipse2D.Double(xArea, yArea, diameter, diameter)) ;
		for (int i = 0; i < gameObjectList.size(); i++) {
			ownArea.intersect(gameObjectList.get(i).getLogicalArea());			
			if(!ownArea.isEmpty()){
				return true;
			}	
			ownArea = new Area(new Ellipse2D.Double(xArea, yArea, diameter, diameter)) ;
		}
		return false;
	}

	private int perceiveObject(int radiusX,int radiusY) {
		int cont=0;
		cont=detectCollision((x+radiusX), y)?cont+1:cont;
		cont=detectCollision((x-radiusX), y)?cont+3:cont;
		cont=detectCollision(x, (y+radiusY))?cont+5:cont;
		cont=detectCollision(x, (y-radiusY))?cont+9:cont;
		return cont;
	}
	private void cancelJumps(){
		jumpUp=false;
		jumpDown=false;
		jumpRight=false;
		jumpLeft=false;
		freeMovement=true;
	}

	private void timer() {
//		int Herospeed = 25;//slow
//		int Herospeed = 50;//slow
//		int Herospeed = 12;
		int Herospeed = 1;//------------------------
		Timer animationTimer = new Timer(Herospeed, new ActionListener() {
			int keysSum =0;
	    	@Override
			public void actionPerformed(ActionEvent e) {
	    		keysSum = initKeySum();
	    		if(!pause) {
						try {
							graphicMovement(keysSum);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	        	logicalMovement(keysSum);
	    		}
			}
		});
		animationTimer.start();
	}
	
	private void graphicMovement(int keySum) throws IOException {
		int grapWaitingInterval=25;
		index=0;
		saveGeneralImagesData(keySum);
		if(!adhered) {
			index = jumpMotion(keySum);
		}
		else if(keySum!=0) {
			index = movementWhileAdhered(keySum, grapWaitingInterval);	  
		}
		else if(keySum==0 && adhered) {
			firstMoveAfterAdhered();
		}
		if(awake)
			subSprite = img.getSubimage(spriteCoords[index][0],spriteCoords[index][1],spriteCoords[index][2],spriteCoords[index][3]);
		if(!awake && adhered && perceiveObject(1,1)==5 || perceiveObject(1,1)==0)
			subSprite = img.getSubimage(spriteCoords[index][0],spriteCoords[index][1],spriteCoords[index][2],spriteCoords[index][3]);
	}

	private void saveGeneralImagesData(int keySum) {
		if(awake) {
			imgVerticalLeft= (keySum==38||keySum==85||keySum==77)?upLeftImg:(keySum==40||keySum==87||keySum==79)?downLeftImg:imgVerticalLeft;
			imgVerticalRight=(keySum==38||keySum==85||keySum==77)?upRightImg:(keySum==40||keySum==87||keySum==79)?downRightImg:imgVerticalRight;
			imgInverted=(keySum==79||keySum==39||keySum==77)?rightInvImg:(keySum==85||keySum==47||keySum==87)?leftInvImg:imgInverted;
			imgNormal=(keySum==79||keySum==39||keySum==77)?rightImg:(keySum==85||keySum==47||keySum==87)?leftImg:imgNormal;
			
			savedImgVerticalLef= (keySum==38||keySum==85||keySum==77)?"- Up - Left":(keySum==40||keySum==87||keySum==79)?"- Down - Left":savedImgVerticalLef;
			savedImgVerticalRight=(keySum==38||keySum==85||keySum==77)?"- Up - Right":(keySum==40||keySum==87||keySum==79)?"- Down - Right":savedImgVerticalRight;
			savedImgInverted=(keySum==79||keySum==39||keySum==77)?"- Right - Inv":(keySum==85||keySum==47||keySum==87)?"- Left - Inv":savedImgInverted;
			savedImgNormal=(keySum==79||keySum==39||keySum==77)?"- Right":(keySum==85||keySum==47||keySum==87)?"- Left":savedImgNormal;
			
			spriteVerticalLeftCoords=(keySum==38||keySum==85||keySum==77)?upLeftCoords:(keySum==40||keySum==87||keySum==79)?downLeftCoords:spriteVerticalLeftCoords;
			spriteVerticalRightCoords=(keySum==38||keySum==85||keySum==77)?upRightCoords:(keySum==40||keySum==87||keySum==79)?downRightCoords:spriteVerticalRightCoords;
			spriteInvertCoords=(keySum==79||keySum==39||keySum==77)?rightInvCoords:(keySum==85||keySum==47||keySum==87)?leftInvCoords:spriteInvertCoords;
			spriteNormalCoords=(keySum==79||keySum==39||keySum==77)?rightCoords:(keySum==85||keySum==47||keySum==87)?leftCoords:spriteNormalCoords;
		}
		else {
			if(imageType==null)
				System.out.println("problema con imagetype");

			imgVerticalLeft= (!fall)?upLeftImg:(fall)?downLeftImg:imgVerticalLeft;
			imgVerticalRight=(!fall)?upRightImg:(fall)?downRightImg:imgVerticalRight;
			imgInverted=(imageType.equals("- Right"))?rightInvImg:(imageType.equals("- Left"))?leftInvImg:imgInverted;
			imgNormal=(imageType.equals("- Right"))?rightImg:(imageType.equals("- Left"))?leftImg:imgNormal;

			spriteVerticalLeftCoords=(!fall)?upLeftCoords:(fall)?downLeftCoords:spriteVerticalLeftCoords;
			spriteVerticalRightCoords=(!fall)?upRightCoords:(fall)?downRightCoords:spriteVerticalRightCoords;
			spriteInvertCoords=(imageType.equals("- Right"))?rightInvCoords:(imageType.equals("- Left"))?leftInvCoords:spriteInvertCoords;
			spriteNormalCoords=(imageType.equals("- Right"))?rightCoords:(imageType.equals("- Left"))?leftCoords:spriteNormalCoords;
		}
	}

	private void firstMoveAfterAdhered() {
		if(perceiveObject(2,2)==9) {
			img =imgInverted;
			savedImage =savedImgInverted;
			spriteCoords = spriteInvertCoords;
		}
		if(perceiveObject(2,2)==5) {
			img =imgNormal;
			savedImage =savedImgNormal;
			spriteCoords = spriteNormalCoords;
		}
	}

	private int movementWhileAdhered(int keySum, int grapWaitingInterval) {
		graphicTimer++;
		graphicTimer=(graphicTimer == (spriteCoords.length*grapWaitingInterval))?0:graphicTimer;
		
		img = (keySum==47)?leftImg:(keySum==39)?rightImg:img;
		imgJump = (keySum==47)?leftImg:(keySum==39)?rightImg:imgJump;

		savedImage = (keySum==47)?"- Left":(keySum==39)?"- Right":savedImage;
		savedImgJump = (keySum==47)?"- Left":(keySum==39)?"- Right":savedImgJump;
		
		spriteJumpCoords = (keySum==47)?jumpLeftCoords:(keySum==39)?jumpRightCoords:spriteJumpCoords;
		spriteCoords = (keySum==47)?leftCoords:(keySum==39)?rightCoords:spriteCoords;
		
		if(perceiveObject(2,2)==3) {
			img =imgVerticalLeft;
			savedImage =savedImgVerticalLef;
			spriteCoords = spriteVerticalLeftCoords;
		}
		if(perceiveObject(2,2)==1) {
			img =imgVerticalRight;
			savedImage =savedImgVerticalRight;
			spriteCoords = spriteVerticalRightCoords;
		}
		if(perceiveObject(2,2)==9) {
			img =imgInverted;
			savedImage =savedImgInverted;
			spriteCoords = spriteInvertCoords;
		}
		index=(int)(graphicTimer/grapWaitingInterval);
		return index;
	}

	private int jumpMotion(int keySum) {
		if(awake) {
		spriteJumpCoords=(keySum==47||keySum==85||keySum==87)?jumpLeftCoords:(keySum==39||keySum==77||keySum==79)?jumpRightCoords:spriteJumpCoords;
		imgJump = (keySum==47||keySum==85||keySum==87)?leftImg:(keySum==39||keySum==77||keySum==79)?rightImg:imgJump;
		
		savedImage = (keySum==47||keySum==85||keySum==87)?"- Left":(keySum==39||keySum==77||keySum==79)?"- Right":savedImage;
		img= imgJump;
		}
		else {
			spriteJumpCoords=(imageType.equals("- Left"))?jumpLeftCoords:(imageType.equals("- Right"))?jumpRightCoords:spriteJumpCoords;
		}
		
		spriteCoords = spriteJumpCoords;
		if(fall) {
			index=2;
//			index=(perceiveObject(15, 15)==5)?3:index;
//			index=(perceiveObject(5, 5)==5)?4:index;
		}
		else 
			index =(perceiveObject(5, 5)==5)?0:1;
		return index;
	}
	private void logicalMovement(int keysSum) {
		int jumpDistance = 100;
		Point coordinates = new Point((int)x, (int)y);
		int step = 1;
		if(adhered)
			saveJumpPoint();
		fall=((yBeforeJump-jumpDistance)>=y)?true:fall;
		evaluateMovements(keysSum);
		if(jumpDown)
			setMovement(keysSum==39||keysSum==79||keysSum==77?step:keysSum==47||keysSum==85||keysSum==87?-step:0,step,coordinates);
		else if(jumpUp) 
			setMovement(keysSum==39||keysSum==79||keysSum==77?step:keysSum==47||keysSum==85||keysSum==87?-step:0,!fall?-step:step,coordinates);
		else if (jumpRight) 
			setMovement(keysSum==39||keysSum==79||keysSum==77?step:keysSum==47||keysSum==85||keysSum==87?-step:0,!fall?-step:step,coordinates);
		else if (jumpLeft) 
			setMovement(keysSum==39||keysSum==79||keysSum==77?step:keysSum==47||keysSum==85||keysSum==87?-step:0,!fall?-step:step,coordinates);
		else if(freeMovement)
			setCoordinates(new Point(coordinates.x+(keysSum==39||keysSum==79||keysSum==77?step:keysSum==47||keysSum==85||keysSum==87?-step:0), coordinates.y+(keysSum==40||keysSum==79||keysSum==87?step:keysSum==38||keysSum==85||keysSum==77?-step:0)));
	}

	private void setMovement(int shiftX,int shiftY, Point coordinates) {
		setCoordinates(new Point(coordinates.x+shiftX, coordinates.y+shiftY));
	}
	
	private int initKeySum() {
		int keysSum =0;
		for (Integer integer: pressedKeys) {
			keysSum+= integer;
		}
		return keysSum;
	}

	public void addPressedKey(int key) {
		pressedKeys.add(key);
	}
	
	public void removePressedKey(int key) {
		for (int i = 0; i < pressedKeys.size(); i++) {
			if(pressedKeys.get(i)==key) {
				pressedKeys.remove(i);	
			}
		}
	}
	
	public boolean numberFound(int key) {
		for (int i = 0; i < pressedKeys.size(); i++) {
			if(pressedKeys.get(i)==key ) {
				return true;
			}
		}
		return false;
	}
	private void evaluateMovements(int keysSum) {
		int distance =3;
		if((keysSum==47 || keysSum==85 || keysSum==87) && !detectCollision(x-distance, y-distance)&& !detectCollision(x-distance, y+distance) && adhered) {
			adhered=false;
			jumpLeft=true;
			freeMovement = false;
			fall= (keysSum==87 || keysSum==47)?true:false;
		}
		if((keysSum==38 || keysSum==85 || keysSum==77) && !detectCollision(x-distance, y-distance) && !detectCollision(x+distance, y-distance) && adhered) {
			adhered=false;
			jumpUp=true;
			freeMovement = false;
		}
		if((keysSum==40 || keysSum==79 || keysSum==87) && !detectCollision(x-distance, y+distance)&& !detectCollision(x+distance, y+distance) && adhered) {
			adhered=false;
			jumpDown=true;
			freeMovement = false;
			fall= true;
		}
		if((keysSum==39 || keysSum==79 || keysSum==77) && !detectCollision(x+distance, y-distance)&& !detectCollision(x+distance, y+distance) && adhered) {
			adhered=false;
			jumpRight=true;
			freeMovement = false;
			fall= (keysSum==79 || keysSum==39)?true:false;
		}
	}
	public void receiveKeys(int key){
		if(!awake) {
			imgJump = (imageType.equals("- Left") && key==38)?leftImg:(imageType.equals("- Right") && key==38)?rightImg:imgJump;
			spriteJumpCoords=(imageType.equals("- Left") && key==38)?jumpLeftCoords:(imageType.equals("- Right") && key==38)?jumpRightCoords:spriteJumpCoords;
		}
		awake=true;
		if(!numberFound(key)) {
			addPressedKey(key);
		}
	}

	public void setAdhered(boolean adhered) {
		if(adhered)
			cancelJumps();
		this.adhered = adhered;
	}
	public void setSubSprite(Image subSprite) {
		this.subSprite = subSprite;
	}
	public Image getSubSprite() {
		return subSprite;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public boolean isAwake() {
		return awake;
	}
	public String getSavedImage() {
		return savedImage;
	}
	public int getYbeforeJump() {
		return yBeforeJump;
	}
	public int getXbeforeJump() {
		return xBeforeJump;
	}
	public boolean isAdhered() {
		return adhered;
	}
	public boolean isFall() {
		return fall;
	}
	public int getIndex() {
		return index;
	}
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	public int[][] getSpriteCoords() {
		return spriteCoords;
	}
	public void setYBeforeJump(int ybeforeJump) {
		yBeforeJump = ybeforeJump;
	}
	public void setXBeforeJump(int xbeforeJump) {
		xBeforeJump = xbeforeJump;
	}
	public void setFall(boolean fall) {
		this.fall = fall;
	}
	public void setJumpUp(boolean jumpUp) {
		this.jumpUp = jumpUp;
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}
}