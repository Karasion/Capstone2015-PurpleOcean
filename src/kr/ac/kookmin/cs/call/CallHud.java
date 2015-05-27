 /**
 * @file CallHud.java
 * @brief This file is associated with a call.
 * @details This file is composed of CallHud class.
 */

 /**
 * @namespace eu.opends.hud.call
 * @brief Package for implementing call in HUD
 * @details This package consists of a call layout class and call core class 
 *          for implementing the call function     
 */
package kr.ac.kookmin.cs.call;

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
* @brief This class serves to output information related to the call to HUD.
* @details In the simulator , if call ringing ,
*          It is output information related to the call to the appropriate position of HUD.
* @author Jo-kwanghyeon
*/
public class CallHud extends HUDClassTemplate {	
  private static SimulationBasics sim; 

  private static Node callGui;
  private static BitmapText callText;
  private static Picture callAccept, callReject;
  private static Picture callEnd;

  private static int state = 0;
  private static final int CALL_IDLE = 0;
  private static final int CALL_RING = 1;
  private static final int CALL_ON = 2;

  private static int hud_state;

  private static int x,y;

  /**
   * @brief It is a method of initializing the related elements to call
   * @param simulator a Simulator object
   * @return nothing
   * 
   */
  public void init(Simulator simulator)
  {
    sim = simulator;
    callGui = new Node("callGui");

    BitmapFont font = sim.getAssetManager().loadFont("Interface/Fonts/MSNeoGothic/MSNeoGothic.fnt");

    x=sim.getSettings().getWidth()/2;
    y=sim.getSettings().getHeight()/2-200;

    callText = new BitmapText(font,false);
    callText.setName("callText");
    callText.setText("");
    callText.setSize(font.getCharSet().getRenderedSize()+10);
    callText.setColor(ColorRGBA.Yellow);
    callText.setLocalTranslation(x-210,y,0);

    callAccept = new Picture("call Accept");
    callAccept.setWidth(91);
    callAccept.setHeight(66);
    callAccept.setPosition(x-225, y-150);
    callAccept.setImage(sim.getAssetManager(), "Textures/icons/calling/call_accept.png", true);

    callReject = new Picture("call Reject");
    callReject.setWidth(91);
    callReject.setHeight(66);
    callReject.setPosition(x-75, y-150);
    callReject.setImage(sim.getAssetManager(), "Textures/icons/calling/call_reject.png", true);

    callEnd = new Picture("call End");
    callEnd.setWidth(58);
    callEnd.setHeight(78);
    callEnd.setPosition(x-150, y-150);
    callEnd.setImage(sim.getAssetManager(), "Textures/icons/calling/call_stop.png", true);
  }

  /**
   * @brief This method to register an instance of class CallHud to HUDManagement.
   * @param nothing
   * @return nothing
   */
  public static void regist()
  {
    CallHud call = new CallHud();
    hud_state = HUDManagement.regist(call);
  }

  /**
   * @brief Is a method to be executed in real time on the simulator .
   * @details Change If this is the phone to a mobile phone , HUD state to call state.
   *          And according to the communication state , to change the layout.
   * @param nothing
   * @return nothing
   */
  public void update()
  {		
    if(CallListener.isCall() && state != CALL_RING && HUDManagement.getState() != hud_state){	
      HUDManagement.backupHUD(hud_state);
      System.out.println("Call Hud update!");
      callText.setText(CallListener.getSender());
      callGui.attachChild(callText);
      callGui.attachChild(callAccept);
      callGui.attachChild(callReject);
      attach();
      HUDManagement.disableMenu(-1);
      state = CALL_RING;
    }

    if(CallListener.getCallState() == 1 && state != CALL_ON ){
      setOnCall();
      state = CALL_ON;
    }

    if(CallListener.isCall() == false && state != CALL_IDLE && HUDManagement.getState() == hud_state){
      callGui.detachAllChildren();
      detach();
      HUDManagement.restoreHUD();
      HUDManagement.enableMenu();
      state = CALL_IDLE;
    }
  }

  /**
   * @brief This method attach the node associated with the call to simulator.
   * @param nothing
   * @return nothing
   */
  public void attach()
  {
    HUDManagement.attach(callGui);
  }

  /**
   * @brief This method detach the node associated with the call to simulator.
   * @param nothing
   * @return nothing
   */
  public void detach()
  {
    HUDManagement.detach(callGui);
  }

  /**
   * @brief This method is to change the layout to match the call to HUD.
   * @param Nothing
   * @return Nothing
   */
  public static void setOnCall()
  {
    callGui.detachChild(callAccept);
    callGui.detachChild(callReject);
    callGui.attachChild(callEnd);
  }

  /**
   * @brief When you press the push button in the G-HUB, it is a method to be executed .
   * @details To end the call , if call offhook state.
   * @param nothing
   * @return nothing
   */
  public void key_act_push()
  {
    if(CallListener.getCallState() == 1)
      CallListener.endCall();
  }
  /**
   * @brief When you press the right button in the G-HUB, it is a method to be executed .
   * @details To end the call , if call ringing state.
   * @param nothing
   * @return nothing
   */
  public void key_act_right()
  {
    if(CallListener.getCallState() == 0)
      CallListener.endCall();
  }
  /**
   * @brief When you press the left button in the G-HUB, it is a method to be executed .
   * @details Receive a call, if call ringing state.
   * @param nothing
   * @return nothing
   */
  public void key_act_left()
  {
    if(CallListener.getCallState() == 0){
      CallListener.onCall();
    }
  }
}
