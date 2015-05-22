package eu.opends.hud.sms;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.basics.SimulationBasics;
import eu.opends.hud.HUDManagement;
import eu.opends.hud.HUDClassTemplet;
import eu.opends.main.Simulator;

//Ha gimyeong, Im gisung, Jo kwanghyeon
public class SmsHud extends HUDClassTemplet{
  private static SimulationBasics sim; 
  private static Node smsGui, smsNum;
  private static BitmapText smsText, smsNewMsgNum;
  private static int hud_state;

  private static int x,y;

  private static Picture icon_en, icon_dis;

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
    smsNewMsgNum.setSize(20);
    smsNewMsgNum.setColor(ColorRGBA.White);
    smsNewMsgNum.setLocalTranslation(x-160+10,y-260+40,0);

    smsGui.attachChild(smsText);
    smsNum.attachChild(smsNewMsgNum);

    /* menu icon initialization */
    icon_en = new Picture("messageIcon_en");
    icon_en.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms.png", true);
    icon_en.setWidth(57);
    icon_en.setHeight(57);

    icon_dis = new Picture("messageIcon_dis");
    icon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms_c.png", true);
    icon_dis.setWidth(57);
    icon_dis.setHeight(57);

    System.out.println("SMS Hud init!");

    HUDManagement.setMenuIcon(icon_en, icon_dis, hud_state);
  }
  public void attach()
  {
    HUDManagement.attach(smsGui);
  }
  public void detach()
  {
    HUDManagement.detach(smsGui);
    SmsRecv.clearMsg();
  }
  public static void regist()
  {
    SmsHud sms = new SmsHud();
    hud_state = HUDManagement.regist(sms);
  }
  public void key_act_push()
  {
    HUDManagement.escapeMenu();
  }
  public void key_act_right()
  {
    SmsRecv.rightMsg();
  }
  public void key_act_left()
  {
    SmsRecv.leftMsg();
  }

  public void update()
  {

    if(SmsRecv.isMsg())
    {
      int msgN=SmsRecv.getNewMsgNum();

      if(msgN<10)
        smsNewMsgNum.setLocalTranslation(840,139,0);
      else
        smsNewMsgNum.setLocalTranslation(840,139,0);

      if(msgN>0){
        icon_en.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms_badge.png", true);
        icon_en.setWidth(59);
        icon_en.setHeight(59);
        icon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms_badge_c.png", true);
        icon_dis.setWidth(59);
        icon_dis.setHeight(59);
        smsNewMsgNum.setText(String.valueOf(msgN));
        HUDManagement.attach(smsNum);
      }
      else{
        smsNewMsgNum.setText("");
        icon_en.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms.png", true);
        icon_en.setWidth(57);
        icon_en.setHeight(57);
        icon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_sms_c.png", true);
        icon_dis.setWidth(57);
        icon_dis.setHeight(57);
      }
      if(HUDManagement.getState() == hud_state)
        smsText.setText(SmsRecv.getNewMsg());
    }
  }
}
