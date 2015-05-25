 /**
 * @file BSAHud.java
 * @brief This file is associated with a BSA output.
 * @details This file is composed of BSAHud class.
 */

 /**
 * @namespace eu.opends.hud.BSA
 * @brief Package for implementing BSA in HUD
 * @details This package consists of a BSA layout class and BSA core class for implementing the BSA function
 */
package eu.opends.hud.BSA;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.hud.HUDManagement;
import eu.opends.hud.HUDClassTemplate;
import eu.opends.main.Simulator;

//Im gisung, Jo kwanghyeon
/**
 * @brief This class serves to output information related to the BSA to Hud.
 * @details  In the simulator , If the user selects the BSA function , if the car has been detected around the user , 
 * 			print to information related to the BSA to the appropriate position of HUD.
 * @author Im-gisung,Jo-kwanghyeon
 *
 */
public class BSAHud extends HUDClassTemplate {

  private static Simulator sim;
  private static Node bsaGui;

  private static Picture bsaScreen;
  private static Picture alertIcon_en, alertIcon_dis;
  private static int my_state;

  /**
   * @brief It is a method of initializing the related elements to BSA
   * @details register the associated element to BSA icon elements and BSA content in the simulator object 
   * @param simulator a Simulator object
   * @return nothing
   * 
   */
  public void init(Simulator simulator)
  {
    sim = simulator;
    bsaGui=new Node("BSAGui");

    bsaScreen = new Picture("bsaScreen");
    bsaScreen.setImage(sim.getAssetManager(), "Textures/icons/alert/alert_safe.png", true);
    bsaScreen.setWidth(231);
    bsaScreen.setHeight(219);
    bsaScreen.setPosition(900,140);

    alertIcon_en = new Picture("alertIcon");
    alertIcon_en.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_alert.png",true);
    alertIcon_en.setWidth(80);
    alertIcon_en.setHeight(80);

    alertIcon_dis = new Picture("alertIcon");
    alertIcon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_alert_c.png",true);
    alertIcon_dis.setWidth(80);
    alertIcon_dis.setHeight(80);

    HUDManagement.setMenuIcon(alertIcon_en, alertIcon_dis, my_state);

    bsaGui.attachChild(bsaScreen);
  }

  /**
   * @brief Is a method to be executed in real time on the simulator .
   * @details If the value of the BSA sensor is detected , it will update the BSA image .
   * @param nothing
   * @return nothing
   */
  public void update()
  {
    if(HUDManagement.getKeyFlag()) {      
      if(BSADumy.getDetectFlag()) {
        attach();
        if(BSADumy.getBackFlag())
          bsaScreen.setImage(sim.getAssetManager(), "Textures/icons/alert/alert_backside.png", true);
        else
          bsaScreen.setImage(sim.getAssetManager(), "Textures/icons/alert/alert_rightside.png", true);
      }
      else{
        bsaScreen.setImage(sim.getAssetManager(), "Textures/icons/alert/alert_safe.png", true);
        if(HUDManagement.getState() != my_state)
          detach();
      }

      if(HUDManagement.getState() == my_state)
        attach();
    }
  }

  /**
   * @brief This method , attach the node associated with the BSA to simulator.
   * @param nothing
   * @return nothing
   */
  public void attach() {
    HUDManagement.attach(bsaGui);
  }

  /**
   * @brief This method , detach the node associated with the BSA to simulator.
   * @param nothing
   * @return nothing
   */
  public void detach() {
    HUDManagement.detach(bsaGui);
  }

  /**
   * @brief When you press the push button in the G-HUB, it is a method to be executed .
   * @details menu of BSA is selected .
   * @param nothing
   * @return nothing
   */
  public void key_act_push()
  {
    HUDManagement.escapeMenu();
  }


  /**
   * @brief This method to register an instance of class BSA to HUDManagement.
   * @param nothing
   * @return nothing
   */
  public static void regist() {
    BSAHud bsa = new BSAHud();
    my_state = HUDManagement.regist(bsa);
  }
}
