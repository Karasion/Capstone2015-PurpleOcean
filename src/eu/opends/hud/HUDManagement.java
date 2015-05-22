package eu.opends.hud;

import java.util.ArrayList;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.basics.SimulationBasics;
import eu.opends.car.Car;
import eu.opends.hud.tool.HudTool;
import eu.opends.main.Simulator;

//Jo kwanghyeon, Im gisung
//HUD module for providing an API to a user .
public class HUDManagement {
	private static SimulationBasics sim; 
	
	// camera ego mode flag
	private static boolean egoFlag = false;
	// hud on/off flag
	private static boolean keyOn=false;
	
	// default hud element
	private static Node nodeGui;
	private static Node hud;
	private static Node hudMenu;
	private static BitmapText currentSpeedText;
	private static BitmapText distanceText;
	private static Picture  navigatorSign,backGround;
	
	// constant value for present state
	public static final int NON_STATE = -1;
	
	// State variable declare
	private static int currentState = NON_STATE;

	private static int stateNum = 0;
	
	private static ArrayList<HUDClassTemplet> hudList = new ArrayList<HUDClassTemplet>();
	private static int [] backupState = new int[10];
	private static int backupCnt = 0;
	
	private static int[] menuState = new int[5];
	
	// Menu variable declare
	
	// position value
	private static int x,y;
	private static int[] menubarPosX = new int[5];
	private static int menubarPosY;
	private static int[] cursorPosX = new int[5];
	private static int cursorPosY;
	
	//index Pos
	private static int menuStartIndex;
	private static int menuEndIndex;
	
	// variable for menubar selecting
	private static int menuNum = 0;
	private static int cursorPos = 0;

	// menubar array
	private static Picture[] picArryMenuEn = new Picture[5];
	private static Picture[] picArryMenuDis = new Picture[5];
	
	// menubar Icon and element for menu function
	private static Picture cursorIcon;

	// constant value for menu_pos 
	private static final int MENU_ALL = -1;
	private static final int MENU_POS1 = 0;
	private static final int MENU_POS2 = 1;
	private static final int MENU_POS3 = 2;
	private static final int MENU_POS4 = 3;
	private static final int MENU_POS5 = 4;
	
	// initialization method of hud panel
	public static void init(Simulator simulator)
	{
		sim = simulator;
		
		nodeGui = sim.getGuiNode();
		hud = new Node("HUD");
		
		nodeGui.attachChild(hud);
		
		BitmapFont ko_Font = sim.getAssetManager().loadFont("Interface/Fonts/MSNeoGothic/MSNeoGothic.fnt");
		
		x=sim.getSettings().getWidth()/2;
		y=sim.getSettings().getHeight()/2-200;
		
		// setting part for default function element 		
		currentSpeedText = new BitmapText(ko_Font,false);
		currentSpeedText.setName("currentSpeedText");
		currentSpeedText.setText("");
		currentSpeedText.setSize(ko_Font.getCharSet().getRenderedSize()+10);
		currentSpeedText.setColor(ColorRGBA.White);
		currentSpeedText.setLocalTranslation(x+170,y-170,0);
		
		distanceText = new BitmapText(ko_Font,false);
		distanceText.setText(null);
		distanceText.setSize(ko_Font.getCharSet().getRenderedSize()+20);
		distanceText.setColor(ColorRGBA.White);
		distanceText.setLocalTranslation(x+50,y-100,0);
		
		navigatorSign = new Picture("straight");
		navigatorSign.setWidth(52);
		navigatorSign.setHeight(101);
		navigatorSign.setPosition(x+170, y-150);
		
		backGround = new Picture("bg");
		backGround.setWidth(857);
		backGround.setHeight(356);
		backGround.setPosition(x-380, y-310);
		backGround.setImage(sim.getAssetManager(), "Textures/icons/panel/panel.png", true);
		
		
		for(int i = 0; i < stateNum; i++){
			hudList.get(i).init(simulator);
		}
		
		hudMenuInit();		
		HudTool.init(simulator);
	}
	
	private static void hudMenuInit() {
		int j=0;
		hudMenu = new Node("menuGui");
		//setting position index
		switch (menuNum){
		case 1:
			menuStartIndex=2;
			menuEndIndex=2;
			break;
		case 2:
			menuStartIndex=1;
			menuEndIndex=2;
			break;
		case 3:
			menuStartIndex=1;
			menuEndIndex=3;
			break;
		case 4:
			menuStartIndex=0;
			menuEndIndex=3;
			break;
		case 5:
			menuStartIndex=0;
			menuEndIndex=4;
			break;
		}

		// setting position value 
		/* menubar position setting */
		if(menuNum%2 == 1){
			menubarPosX[0]=x-300;
			menubarPosX[1]=x-230;
			menubarPosX[2]=x-160;
			menubarPosX[3]=x-90;
			menubarPosX[4]=x-20;
		}else{
			menubarPosX[0]=x-265;
			menubarPosX[1]=x-195;
			menubarPosX[2]=x-125;
			menubarPosX[3]=x-75;
			menubarPosX[4]=0;
		}
		menubarPosY=y-260;

		/* cursor position setting */
		cursorPosX[0]=menubarPosX[0]+19;
		cursorPosX[1]=menubarPosX[1]+19;
		cursorPosX[2]=menubarPosX[2]+19;
		cursorPosX[3]=menubarPosX[3]+19;
		cursorPosX[4]=menubarPosX[4]+19;
		cursorPosY=menubarPosY-9;

		for(int i = menuStartIndex; i <= menuEndIndex; i++){
			picArryMenuEn[j].setPosition(menubarPosX[i], menubarPosY);
			picArryMenuDis[j++].setPosition(menubarPosX[i], menubarPosY);
		}

		for(int i = 0; i < menuNum; i++){
			hudMenu.attachChild(picArryMenuEn[i]);
		}

		cursorIcon = new Picture("cursorIcon");
		cursorIcon.setImage(sim.getAssetManager(),"Textures/icons/menubar/menubar_arrow.png",true);
		cursorIcon.setWidth(22);
		cursorIcon.setHeight(15);
		cursorIcon.setPosition(cursorPosX[menuStartIndex], cursorPosY);

		cursorPos = menuStartIndex;		
	}

	// real-time update of hudDisplay
	public static void update()
	{
		Car car = ((Simulator)sim).getCar();
		updateCurrentSpeedText(car);
		updateNavigatorSign();
		HudTool.updatingHudTool();
		if(keyOn){
			for(int i = 0; i < stateNum; i++){
				hudList.get(i).update();
//				System.out.println("update state : " + i);
			}
		}
	}
	
	private static void updateCurrentSpeedText(Car car)
	{
		String carSpeed;
		
		carSpeed = (int)car.getCurrentSpeedKmh() + "Km/h";
		currentSpeedText.setText(carSpeed);		
	}
	
	private static void updateNavigatorSign()
	{
		if(navigatorSign.getName().equals("right"))
		{
		    navigatorSign.setWidth(98);
	        navigatorSign.setHeight(136);
			navigatorSign.setImage(sim.getAssetManager(), "Textures/icons/navi/arrow_right.png", true);
		}
		else if(navigatorSign.getName().equals("left"))
		{
		    navigatorSign.setWidth(98);
	        navigatorSign.setHeight(136);
			navigatorSign.setImage(sim.getAssetManager(), "Textures/icons/navi/arrow_left.png", true);
		}
		else if(navigatorSign.getName().equals("straight"))
		{
		    navigatorSign.setWidth(66);
	        navigatorSign.setHeight(136);
			navigatorSign.setImage(sim.getAssetManager(), "Textures/icons/navi/arrow_straight.png", true);
		}
	}
	
	// attach and detach method for each feature display 
	private static void defaultFunctionAttach()
	{
		hud.attachChild(distanceText);
		hud.attachChild(currentSpeedText);
		hud.attachChild(navigatorSign);
	}
	
	public static void hudAttach()
	{
	    hud.attachChild(backGround);
	    /* menubar attach */
	    hud.attachChild(hudMenu);
	    hud.attachChild(cursorIcon);
	    
	    /* function attach */
		defaultFunctionAttach();
	}
	
	public static void hudDetach()
	{
	    hud.detachAllChildren();
	}
	
	public static void backupHUD(int changeState)
	{
		System.out.println("backup : " + currentState);
		if(currentState != NON_STATE){
			hudList.get(currentState).pause();
			hudList.get(currentState).detach();
		}
		backupState[backupCnt++] = currentState;
		currentState = changeState;				
	}
	
	public static int restoreHUD(){
		if(backupCnt > 0){
			hudList.get(currentState).detach();
			currentState = backupState[--backupCnt];
			if(currentState != NON_STATE){
				hudList.get(currentState).resume();
				hudList.get(currentState).attach();
			}
			return 1;
		}
		else
			return -1;
	}
	
	public static boolean isCameraEgo()
	{
	  return egoFlag;
	}
	
	// setter
	public static void setCameraEgo(boolean flag)
	{
	  egoFlag = flag;
	}
	public static void keyFlagSetting()
	{
		if(keyOn)
			keyOn=false;
		else
			keyOn=true;
	}
	
	public static void setNaviType(String naviType, String distance)
    {
		String distanceNavi;
		
		navigatorSign.setName(naviType);
		
		if(distance != null)
			distanceNavi = distance + "m";
		else
			distanceNavi = null;
		
		distanceText.setText(distanceNavi);
    }
	
	// getter
	public static boolean getKeyFlag()
	{
		return keyOn;
	}
	
	public static int getState()
	{
	  return currentState;
	}
	
	/* methods for cursor move */
	// left move of cursor
	public static void leftMoveCursor()
	{
	  cursorPos--;
	  if(cursorPos < menuStartIndex)
	    cursorPos = menuEndIndex;
	  
	  cursorIcon.setPosition(cursorPosX[cursorPos], cursorPosY);
	}
	
	// right move of cursor
	public static void rightMoveCursor()
	{
	  cursorPos++;
	  if(cursorPos > menuEndIndex)
	    cursorPos=menuStartIndex;
	  
	  cursorIcon.setPosition(cursorPosX[cursorPos], cursorPosY);
	}
	/* end move methods */
	// select menu
	public static void selectMenu()
	{
		switch(cursorPos - menuStartIndex)
		{
		case MENU_POS1:
			currentState = menuState[0];
			hudList.get(currentState).attach();
			disableMenu(MENU_POS1);
			break;
		case MENU_POS2:
			currentState = menuState[1];
			hudList.get(currentState).attach();
			disableMenu(MENU_POS2);
			break;
		case MENU_POS3:
			currentState = menuState[2];
			hudList.get(currentState).attach();
			disableMenu(MENU_POS3);
			break;
		case MENU_POS4:
			currentState = menuState[3];
			hudList.get(currentState).attach();
			disableMenu(MENU_POS3);
			break;
		case MENU_POS5:
			currentState = menuState[4];
			hudList.get(currentState).attach();
			disableMenu(MENU_POS3);
			break;
		}
	}
	
	// escape menu
	public static void escapeMenu()
	{
	  switch(cursorPos - menuStartIndex)
      {
      case MENU_POS1:
    	  hudList.get(currentState).detach();
    	  currentState=NON_STATE;
    	  break;
      case MENU_POS2:
    	  hudList.get(currentState).detach();
    	  currentState=NON_STATE;
    	  break;
      case MENU_POS3:
    	  hudList.get(currentState).detach();
    	  currentState=NON_STATE;
    	  break;
      case MENU_POS4:
    	  hudList.get(currentState).detach();
    	  currentState=NON_STATE;
    	  break;
      case MENU_POS5:
    	  hudList.get(currentState).detach();
    	  currentState=NON_STATE;
    	  break;
      }
	  enableMenu(); 
	}
	
	// menu icon disable
	public static void disableMenu(int state)
	{
		hudMenu.detachAllChildren();
		switch(state)
		{
		case MENU_ALL:
			for(int i = 0; i < menuNum; i++){
				hudMenu.attachChild(picArryMenuDis[i]);
			}
			hud.detachChild(cursorIcon);
			break;
		case MENU_POS1:
			for(int i = 0; i < menuNum; i++){
				if(i == MENU_POS1)
					hudMenu.attachChild(picArryMenuEn[i]);
				else
					hudMenu.attachChild(picArryMenuDis[i]);
			}
			break;
		case MENU_POS2:
			for(int i = 0; i < menuNum; i++){
				if(i == MENU_POS2)
					hudMenu.attachChild(picArryMenuEn[i]);
				else
					hudMenu.attachChild(picArryMenuDis[i]);
			}
			break;
		case MENU_POS3:
			for(int i = 0; i < menuNum; i++){
				if(i == MENU_POS3)
					hudMenu.attachChild(picArryMenuEn[i]);
				else
					hudMenu.attachChild(picArryMenuDis[i]);
			}
			break;
		case MENU_POS4:
			for(int i = 0; i < menuNum; i++){
				if(i == MENU_POS4)
					hudMenu.attachChild(picArryMenuEn[i]);
				else
					hudMenu.attachChild(picArryMenuDis[i]);
			}
			break;
		case MENU_POS5:
			for(int i = 0; i < menuNum; i++){
				if(i == MENU_POS5)
					hudMenu.attachChild(picArryMenuEn[i]);
				else
					hudMenu.attachChild(picArryMenuDis[i]);
			}
			break;
		}
	}
	
	// all menu enabled
	public static void enableMenu()
	{
		hudMenu.detachAllChildren();
		for(int i = 0; i < menuNum; i++){
	    	  hudMenu.attachChild(picArryMenuEn[i]);
	      }
      hud.attachChild(cursorIcon);
	}
    
    //HUD API
    public static void attach(Node subGui)
    {
    	hud.attachChild(subGui);
    }
    
    public static void detach(Node subGui)
    {
    	hud.detachChild(subGui);
    }

	public static int regist(HUDClassTemplet hud) {
		hudList.add(hud);
		return stateNum++;
	}

	public static void setMenuIcon(Picture pic_en, Picture pic_dis, int state){
		picArryMenuEn[menuNum] = pic_en;
		picArryMenuDis[menuNum] = pic_dis;
		menuState[menuNum] = state;
		menuNum++;		
	}
	
	public static void leftKeyAct(){
		hudList.get(currentState).key_act_left();
	}
	public static void rightKeyAct(){
		hudList.get(currentState).key_act_right();
	}
	public static void upKeyAct(){
		hudList.get(currentState).key_act_up();
	}
	public static void downKeyAct(){
		hudList.get(currentState).key_act_down();
	}
	public static void pushKeyAct(){
		hudList.get(currentState).key_act_push();
	}
}
