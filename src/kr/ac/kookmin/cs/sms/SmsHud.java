 /**
 * @file SmsHud.java
 * @brief This file is associated with a sms output.
 * @details This file is composed of SmsHud class.
 */

 /**
 * @namespace kr.ac.kookmin.cs.sms
 * @brief Package for implementing sms in HUD
 * @details This package consists of a sms layout class and sms core class for implementing the sms function
 * 			
 */
package kr.ac.kookmin.cs.sms;

import kr.ac.kookmin.cs.hud.HUDClassTemplate;
import kr.ac.kookmin.cs.hud.HUDManagement;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.basics.SimulationBasics;
import eu.opends.main.Simulator;

 /**
 * @brief This class serves to output information related to the sms to Hud.
 * @details In the simulator , if the user selects a sms function , is output information related to the sms to the appropriate position of HUD.
 * @author Im-Gisung, Jo-Kwanghyeon, Ha-gimyeong 
 *
 */
public class SmsHud extends HUDClassTemplate{
  private static SimulationBasics sim; 
  private static Node smsGui, smsNum;
  private static BitmapText smsText, smsNewMsgNum;
  private static int hud_state;

  private static int x,y;

  private static Picture icon_en, icon_dis;

  /**
   * @brief It is a method of initializing the related elements to sms
   * @details register the associated element to SMS icon elements and character content in the simulator object 
   * @param simulator a Simulator object
   * @return nothing
   * 
   */
  public void init(Simulator simulator)
  {
    sim = simulator;
    smsGui = new Node("smsGui");
    smsNum = new Node("smsNum");

    BitmapFont font = sim.getAssetManager().loadFont("Interface/Fonts/MSNeoGothic/MSNeoGothic.fnt");

    x=sim.getSettings().getWidth()/2;
    y=sim.getSettings().getHeight()/2-200;

    smsText = new BitmapText(font,false);
    smsText.setName("smsText");
    smsText.setText("");
    smsText.setSize(font.getCharSet().getRenderedSize());
    smsText.setColor(ColorRGBA.White);
    smsText.setLocalTranslation(x-250,y,0);

    smsNewMsgNum = new BitmapText(font,false);
    smsNewMsgNum.setName("smsNewMsgNum");
    smsNewMsgNum.setText("");
    smsNewMsgNum.setSize(21);
    smsNewMsgNum.setColor(ColorRGBA.White);
    smsNewMsgNum.setLocalTranslation(852,130,0);

    smsGui.attachChild(smsText);
    smsNum.attachChild(smsNewMsgNum);

    /* menu icon initialization */
    icon_en = new Picture("messageIcon_en");
    icon_en.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms.png", true);
    icon_en.setWidth(80);
    icon_en.setHeight(80);

    icon_dis = new Picture("messageIcon_dis");
    icon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms_c.png", true);
    icon_dis.setWidth(80);
    icon_dis.setHeight(80);

    HUDManagement.setMenuIcon(icon_en, icon_dis, hud_state);
  }
  /**
   * @brief This method , attach the node associated with the sms to simulator.
   * @param nothing
   * @return nothing
   */
  public void attach()
  {
    HUDManagement.attach(smsGui);
  }
  
  /**
   * @brief This method , detach the node associated with the sms to simulator.
   * @param nothing
   * @return nothing
   */
  public void detach()
  {
    HUDManagement.detach(smsGui);
    SmsRecv.clearMsg();
  }
  
  /**
   * @brief This method to register an instance of class SmsHud to HUDManagement.
   * @param nothing
   * @return nothing
   */
  public static void regist()
  {
    SmsHud sms = new SmsHud();
    hud_state = HUDManagement.regist(sms);
  }
  /**
   * @brief When you press the push button in the G-HUB, it is a method to be executed .
   * @details menu of sms is selected .
   * @param nothing
   * @return nothing
   */
  public void key_act_push()
  {
    HUDManagement.escapeMenu();
  }
  /**
   * @brief When you press the right button in the G-HUB, it is a method to be executed .
   * @details Is output the next msg.
   * @param nothing
   * @return nothing
   */
  public void key_act_right()
  {
    SmsRecv.rightMsg();
  }
  /**
   * @brief When you press the left button in the G-HUB, it is a method to be executed .
   * @details It is output earlier msg.
   * @param nothing
   * @return nothing
   */
  public void key_act_left()
  {
    SmsRecv.leftMsg();
  }

  /**
   * @brief Is a method to be executed in real time on the simulator .
   * @details If there are new messages , and outputs the number of unacknowledged messages in the message menu .
   * @param nothing
   * @return nothing
   */
  public void update()
  {

    if(SmsRecv.isMsg())
    {
      int msgN=SmsRecv.getNewMsgNum();

      if(msgN<10)
        smsNewMsgNum.setLocalTranslation(856,130,0);
      else
        smsNewMsgNum.setLocalTranslation(856,130,0);

      if(msgN>0){
        icon_en.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms_badge.png", true);
        icon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms_badge_c.png", true);
        smsNewMsgNum.setText(String.valueOf(msgN));
        HUDManagement.attach(smsNum);
      }
      else{
        smsNewMsgNum.setText("");
        icon_en.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms.png", true);
        icon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms_c.png", true);
      }
      if(HUDManagement.getState() == hud_state)
        smsText.setText(SmsRecv.getNewMsg());
    }
  }
}
