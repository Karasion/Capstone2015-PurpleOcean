package eu.opends.hud.BSA;

import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.hud.HUDManagement;
import eu.opends.hud.HUDClassTemplet;
import eu.opends.main.Simulator;

//Im gisung, Jo kwanghyeon
public class BSAHud extends HUDClassTemplet {

  private static Simulator sim;
  private static Node bsaGui;

  private static Picture bsaScreen;
  private static Picture alertIcon_en, alertIcon_dis;
  private static int my_state;

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
    alertIcon_en.setWidth(57);
    alertIcon_en.setHeight(57);

    alertIcon_dis = new Picture("alertIcon");
    alertIcon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_alert_c.png",true);
    alertIcon_dis.setWidth(57);
    alertIcon_dis.setHeight(57);

    HUDManagement.setMenuIcon(alertIcon_en, alertIcon_dis, my_state);

    bsaGui.attachChild(bsaScreen);
  }

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

  public void attach() {
    HUDManagement.attach(bsaGui);
  }

  public void detach() {
    HUDManagement.detach(bsaGui);
  }

  public void key_act_push()
  {
    HUDManagement.escapeMenu();
  }


  public static void regist() {
    BSAHud bsa = new BSAHud();
    my_state = HUDManagement.regist(bsa);
  }
}
