package eu.opends.hud.call;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.basics.SimulationBasics;
import eu.opends.hud.HUDManagement;
import eu.opends.hud.HUDClassTemplet;
import eu.opends.main.Simulator;

//Jo kwanghyeon
public class CallHud extends HUDClassTemplet {	
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
    callAccept.setWidth(59);
    callAccept.setHeight(76);
    callAccept.setPosition(x-225, y-150);
    callAccept.setImage(sim.getAssetManager(), "Textures/icons/calling/call_accept.png", true);

    callReject = new Picture("call Reject");
    callReject.setWidth(58);
    callReject.setHeight(78);
    callReject.setPosition(x-75, y-150);
    callReject.setImage(sim.getAssetManager(), "Textures/icons/calling/call_reject.png", true);

    callEnd = new Picture("call End");
    callEnd.setWidth(58);
    callEnd.setHeight(78);
    callEnd.setPosition(x-150, y-150);
    callEnd.setImage(sim.getAssetManager(), "Textures/icons/calling/call_reject.png", true);

    System.out.println("Call Hud init!");
  }

  public static void regist()
  {
    CallHud call = new CallHud();
    hud_state = HUDManagement.regist(call);
  }

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

  public void attach()
  {
    HUDManagement.attach(callGui);
  }

  public void detach()
  {
    HUDManagement.detach(callGui);
  }

  public static void setOnCall()
  {
    callGui.detachChild(callAccept);
    callGui.detachChild(callReject);
    callGui.attachChild(callEnd);
  }

  public void key_act_push()
  {
    if(CallListener.getCallState() == 1)
      CallListener.endCall();
  }
  public void key_act_right()
  {
    if(CallListener.getCallState() == 0)
      CallListener.endCall();
  }
  public void key_act_left()
  {
    if(CallListener.getCallState() == 0){
      CallListener.onCall();
    }
  }
}
