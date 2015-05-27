/**
 * @file HUDManagement.java
 * @brief This file is associated with a HUD API and HUD Management .
 * @details This file is composed of HUDManagement class.
 */

/**
 * @namespace eu.opends.hud
 * @brief This package is a set of classes related to HUD.
 * @details This package is composed of HUD management class
 *          and HUD function class .
 */
package kr.ac.kookmin.cs.hud;

import java.util.ArrayList;

import kr.ac.kookmin.cs.tool.HudLayoutTool;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.basics.SimulationBasics;
import eu.opends.car.Car;
import eu.opends.main.Simulator;

/**
 * @brief Class that manages the functions of the HUD panel.
 * @details This class manages the functions by state separately divided ,
 *          it serves to provide an API related functions to be used externally
 * @author Jo-kwanghyeon, Im-gisung
 */
public class HUDManagement {
  private static SimulationBasics sim; 

  // camera ego mode flag
  private static boolean egoFlag = false;
  // hud on/off flag
  private static boolean keyOn=false;

  private static Node nodeGui;
  private static Node hud;
  private static Node hudMenu;

  // default hud element
  private static BitmapText currentSpeedText;
  private static BitmapText distanceText;
  private static Picture  navigatorSign,backGround;

  // constant value for present state
  public static final int NON_STATE = -1;

  // State variable declare
  private static int currentState = NON_STATE;
  private static int stateNum = 0;

  private static ArrayList<HUDClassTemplate> hudList = new ArrayList<HUDClassTemplate>();
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

  /**
   * @brief Method to initialize the basic HUD element and HUD layout class element
   * @details Call init () function of the HUD Layout Class registered at HUDRegister,
   *          initializes the element, and sets the position of the menu icons .
   * @param simulator a Simulator 
   * @return Nothing
   */
  public static void init(Simulator simulator)
  {
    sim = simulator;

    nodeGui = sim.getGuiNode();
    hud = new Node("HUD");

    nodeGui.attachChild(hud);
    
    //get font
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
    backGround.setWidth(841);
    backGround.setHeight(338);
    backGround.setPosition(x-380, y-310);
    backGround.setImage(sim.getAssetManager(), "Textures/icons/panel/panel.png", true);

    for(int i = 0; i < stateNum; i++){
      hudList.get(i).init(simulator);
    }

    //set menu icons position by number of menu icons
    hudMenuInit();		
    HudLayoutTool.init(simulator);
  }

  /**
   * @brief Method to set the position of the HUD menu icon
   * @details Position array for icon determines the total number of icons .
              And the index of the array of position (menuStartIndex, menuEndIndex) also set .
   * @param Nothing
   * @return Nothing
   */
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
    menubarPosY=y-280;

    /* cursor position setting */
    cursorPosX[0]=menubarPosX[0]-5;
    cursorPosX[1]=menubarPosX[1]-5;
    cursorPosX[2]=menubarPosX[2]-5;
    cursorPosX[3]=menubarPosX[3]-5;
    cursorPosX[4]=menubarPosX[4]-5;
    cursorPosY=menubarPosY;

    for(int i = menuStartIndex; i <= menuEndIndex; i++){
      picArryMenuEn[j].setPosition(menubarPosX[i], menubarPosY);
      picArryMenuDis[j++].setPosition(menubarPosX[i], menubarPosY);
    }

    for(int i = 0; i < menuNum; i++){
      hudMenu.attachChild(picArryMenuEn[i]);
    }

    cursorIcon = new Picture("cursorIcon");
    cursorIcon.setImage(sim.getAssetManager(),"Textures/icons/menubar/menubar_arrow.png",true);
    cursorIcon.setWidth(90);
    cursorIcon.setHeight(15);
    cursorIcon.setPosition(cursorPosX[menuStartIndex], cursorPosY);

    cursorPos = menuStartIndex;		
  }

  /**
   * @brief Method to update the state of the HUD in real time
   * @details This method is , give me so that HUD panel is real-time updates 
   *          by calling the update () method of the registered HUD Layout Class.
   * @param Nothing
   * @return Nothing
   */
  public static void update()
  {
    Car car = ((Simulator)sim).getCar();
    updateCurrentSpeedText(car);
    updateNavigatorSign();
    HudLayoutTool.updatingHudTool();
    if(keyOn){
      for(int i = 0; i < stateNum; i++){
        hudList.get(i).update();
        //				System.out.println("update state : " + i);
      }
    }
  }

  /**
   * @brief Method to read the current speed value in OpendDS for HUD speed display function
   * @details Get the current speed using getCurrentSpeedKmh () method of OpenDS,
   *          to be displayed in the HUD.
   * @param car a car
   * @return Nothing 
   */
  private static void updateCurrentSpeedText(Car car)
  {
    String carSpeed;

    carSpeed = (int)car.getCurrentSpeedKmh() + "Km/h";
    currentSpeedText.setText(carSpeed);		
  }

  /**
   * @brief Method to update the direction of navigation
   * @details It will update the navigation sign that has been changed in the DisplayNavigatorAction.java.
   * @param Nothing
   * @return Nothing
   */
  private static void updateNavigatorSign()
  {
    if(navigatorSign.getName().equals("right")) {
      navigatorSign.setWidth(98);
      navigatorSign.setHeight(136);
      navigatorSign.setImage(sim.getAssetManager(), "Textures/icons/navi/arrow_right.png", true);
    }
    else if(navigatorSign.getName().equals("left")) {
      navigatorSign.setWidth(98);
      navigatorSign.setHeight(136);
      navigatorSign.setImage(sim.getAssetManager(), "Textures/icons/navi/arrow_left.png", true);
    }
    else if(navigatorSign.getName().equals("straight")) {
      navigatorSign.setWidth(66);
      navigatorSign.setHeight(136);
      navigatorSign.setImage(sim.getAssetManager(), "Textures/icons/navi/arrow_straight.png", true);
    }
  }

  /**
   * @brief Method to add a HUD basic functions in HUD panel
   * @details The speed display and navigation function is a HUD basic functions
   *          using the attachChild () method and add it to the HUD panel .
   * @param Nothing
   * @return Nothing
   */
  // attach and detach method for each feature display 
  private static void defaultFunctionAttach()
  {
    hud.attachChild(distanceText);
    hud.attachChild(currentSpeedText);
    hud.attachChild(navigatorSign);
  }

  /**
   * @brief Method to add the main screen of HUD to HUD panel
   * @details It'll add the HUD background color and menu using the attachChild () method.
   * @param Nothing
   * @return Nothing
   */
  public static void hudAttach()
  {
    hud.attachChild(backGround);
    /* menubar attach */
    hud.attachChild(hudMenu);
    hud.attachChild(cursorIcon);

    /* function attach */
    defaultFunctionAttach();
  }

  /**
   * @brief Method to delete HUD to HUD panel
   * @details It'll delete all HUD Layout using the detachAllChildren () method.
   * @param Nothing
   * @return Nothing
   */
  public static void hudDetach()
  {
    hud.detachAllChildren();
  }

  /**
   * @brief Method to back up the previous state of HUD
   * @details Back up the previous state of the HUD, it is changed to a state that is input.
   * @param chageState a integer
   * @return Nothing
   */
  public static void backupHUD(int changeState)
  {
    //System.out.println("backup : " + currentState);
    if(currentState != NON_STATE){
      hudList.get(currentState).pause();
      hudList.get(currentState).detach();
    }
    backupState[backupCnt++] = currentState;
    currentState = changeState;				
  }

  /**
   * @brief Method to return the state of HUD to a previous state
   * @details To restore the state of HUD to a previous state,
   *          call the resume () at this time HUD Layout Class.
   * @param Nothing
   * @return Returns 1 if successful , returns -1 if failed
   */
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

  /**
   * @brief Method to set whether the press and hold the HUD key
   * @details If the state of the HUD keys on, is changed to off.
   *          If the state of the HUD keys off, is changed to on.
   * @param  Nothing
   * @return Nothing 
   */
  public static void keyFlagSetting()
  {
    if(keyOn)
      keyOn=false;
    else
      keyOn=true;
  }

  /**
   * @brief Method for setting the direction of navigation and the rest of the distance .
   * @param Stirng argument naviType, distance
   * @return Nothing
   */
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

  public static boolean getKeyFlag()
  {
    return keyOn;
  }

  public static int getState()
  {
    return currentState;
  }

  /**
   * @brief Method to move the cursor of HUD menu on the left
   * @param Nothing
   * @return Nothing
   */
  /* methods for cursor move */
  // left move of cursor
  public static void leftMoveCursor()
  {
    cursorPos--;
    if(cursorPos < menuStartIndex)
      cursorPos = menuEndIndex;

    cursorIcon.setPosition(cursorPosX[cursorPos], cursorPosY);
  }

  /**
   * @brief Method to move the cursor of HUD menu on the right
   * @param Nothing
   * @return Nothing
   */
  // right move of cursor
  public static void rightMoveCursor()
  {
    cursorPos++;
    if(cursorPos > menuEndIndex)
      cursorPos=menuStartIndex;

    cursorIcon.setPosition(cursorPosX[cursorPos], cursorPosY);
  }

  /**
   * @brief Method to run the registered function in HUD menu
   * @details Run the selected function , to disable other menu icon.
   * @param Nothing
   * @return Nothing
   */
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

  /**
   * @brief Method to get out from the menu function running
   * @details This method is to remove the HUD layout and change the state to nonstate.
   * @param Nothing
   * @return Nothing
   */
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

  /**
   * @brief This Method is to disable the menu icon.
   * @details This Method is to disable the menu icon without input state.
   * @param Integer argument state
   * @return Nothing
   */
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

  /**
   * @brief This Method is to enable the menu icon.
   * @param Nothing
   * @return Nothing
   */
  // all menu enabled
  public static void enableMenu()
  {
    hudMenu.detachAllChildren();
    for(int i = 0; i < menuNum; i++){
      hudMenu.attachChild(picArryMenuEn[i]);
    }
    hud.attachChild(cursorIcon);
  }

  /**
   * @brief The Method is the ability to attach the HUD layout to the panel .
   * @param Node argument subGui
   * @return Nothing
   */
  //HUD API
  public static void attach(Node subGui)
  {
    hud.attachChild(subGui);
  }

  /**
   * @brief The Method is the ability to detach the HUD layout to the panel .
   * @param Node argument subGui
   * @return Nothing
   */
  public static void detach(Node subGui)
  {
    hud.detachChild(subGui);
  }

  /**
   * @brief The Method is a function of registering the HUD layout class to HUDManagement.
   * @details Now this method is included in the execution flow , and must be used HUDRegister.
   * @param HUDClassTemplate argument hud
   * @return Integer stateNum(State number of HUD layout class)
   */
  public static int regist(HUDClassTemplate hud) {
    hudList.add(hud);
    return stateNum++;
  }

  /**
   * @brief The Method is a function of add the icon of HUD menu bar
   * @details This is required disable Icon, state number and enable icon.
   * @param Picture argument pic_en, pic_dis, state
   * @return Nothing
   */
  public static void setMenuIcon(Picture pic_en, Picture pic_dis, int state){
    picArryMenuEn[menuNum] = pic_en;
    picArryMenuDis[menuNum] = pic_dis;
    menuState[menuNum] = state;
    menuNum++;
  }

  /**
   * @brief This method is to map the action of the key.
   * @details It will handle the action at the time of right key input.
   *          In the keyboard , it is F2 key 
   * @param Nothing
   * @return Nothing
   */
  public static void leftKeyAct(){
    hudList.get(currentState).key_act_left();
  }
  /**
   * @brief This method is to map the action of the key.
   * @details It will handle the action at the time of left key input.
   *          In the keyboard , it is F3 key 
   * @param Nothing
   * @return Nothing
   */
  public static void rightKeyAct(){
    hudList.get(currentState).key_act_right();
  }
  /**
   * @brief This method is to map the action of the key.
   * @details It will handle the action at the time of up key input.
   *          In the keyboard , it is c key 
   * @param Nothing
   * @return Nothing
   */
  public static void upKeyAct(){
    hudList.get(currentState).key_act_up();
  }
  /**
   * @brief This method is to map the action of the key.
   * @details It will handle the action at the time of down key input.
   *          In the keyboard , it is z key 
   * @param Nothing
   * @return Nothing
   */
  public static void downKeyAct(){
    hudList.get(currentState).key_act_down();
  }
  /**
   * @brief This method is to map the action of the key.
   * @details It will handle the action at the time of push key input.
   *          In the keyboard , it is n key 
   * @param Nothing
   * @return Nothing
   */
  public static void pushKeyAct(){
    hudList.get(currentState).key_act_push();
  }
}
