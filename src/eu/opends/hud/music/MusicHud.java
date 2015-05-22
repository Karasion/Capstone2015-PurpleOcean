package eu.opends.hud.music;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.basics.SimulationBasics;
import eu.opends.hud.HUDManagement;
import eu.opends.hud.HUDClassTemplet;
import eu.opends.main.Simulator;

//Lee minjae Jo kwanghyeon
public class MusicHud extends HUDClassTemplet{
  private static SimulationBasics sim;
  private static Node musicPanel;
  private static int hud_state;

  // element for music player 
  private static BitmapText musicList;
  public static MusicPlayer mp3 = new MusicPlayer("C:/³ë·¡");
  private static Picture musicCursor;
  private static Picture[] musicPlayerIcon = new Picture[3];
  private static Picture musicEquilizer;
  private static BitmapText musicName;

  private static int musicCursorPosX;
  private static int[] musicCursorPosY = new int[5];

  private static int x,y;

  private static Picture icon_en, icon_dis;

  public void init(Simulator simulator)
  {
    sim = simulator;
    musicPanel = new Node("Music Panel");

    BitmapFont font = sim.getAssetManager().loadFont("Interface/Fonts/MSNeoGothic/MSNeoGothic.fnt");

    x=sim.getSettings().getWidth()/2;
    y=sim.getSettings().getHeight()/2-200;

    // element for music player 
    musicList = new BitmapText(font,false);
    musicList.setName("music list");
    musicList.setText("");
    musicList.setSize(font.getCharSet().getRenderedSize());
    musicList.setColor(ColorRGBA.White);
    musicList.setLocalTranslation(x-250,y,0);

    musicCursorPosX = x - 280;
    musicCursorPosY[0] = y-30;
    musicCursorPosY[1] = y-60;
    musicCursorPosY[2] = y-95;
    musicCursorPosY[3] = y-125;
    musicCursorPosY[4] = y-155;

    musicCursor = new Picture("musicCursor");
    musicCursor.setImage(sim.getAssetManager(), "Textures/icons/music_arrow.png", true);
    musicCursor.setWidth(22);
    musicCursor.setHeight(22);
    musicCursor.setPosition(musicCursorPosX, musicCursorPosY[0]);

    musicPlayerIcon[0] = new Picture("previous");
    musicPlayerIcon[0].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_backSkip.png", true);
    musicPlayerIcon[0].setWidth(50);
    musicPlayerIcon[0].setHeight(51);
    musicPlayerIcon[0].setPosition(x-225, y-180);

    musicPlayerIcon[1] = new Picture("play and pause");
    musicPlayerIcon[1].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_pause.png", true);
    musicPlayerIcon[1].setWidth(68);
    musicPlayerIcon[1].setHeight(69);
    musicPlayerIcon[1].setPosition(x-155, y-190);

    musicPlayerIcon[2] = new Picture("next");
    musicPlayerIcon[2].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_nextSkip.png", true);
    musicPlayerIcon[2].setWidth(50);
    musicPlayerIcon[2].setHeight(51);
    musicPlayerIcon[2].setPosition(x-65,y-180);

    musicEquilizer = new Picture("music equilizer");
    musicEquilizer.setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_equilizer.png", true);
    musicEquilizer.setWidth(188);
    musicEquilizer.setHeight(70);
    musicEquilizer.setPosition(x-215,y-120);

    musicName = new BitmapText(font,false);
    musicName.setName("music name");
    musicName.setText("fhdhahf");
    musicName.setSize(font.getCharSet().getRenderedSize());
    musicName.setLocalTranslation(x-230, y-30, 0);

    /* menu icon initialization */
    icon_en = new Picture("musicIcon_en");
    icon_en.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_music.png", true);
    icon_en.setWidth(57);
    icon_en.setHeight(57);

    icon_dis = new Picture("musicIcon_dis");
    icon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_music_c.png", true);
    icon_dis.setWidth(57);
    icon_dis.setHeight(57);

    System.out.println("Music Hud init!");

    HUDManagement.setMenuIcon(icon_en, icon_dis, hud_state);
  }

  public void update()
  {
    if(mp3.playingF)
      musicName.setText(mp3.getPlayingMusicName());
    else
      musicList.setText(mp3.getList());
  }

  public void attach()
  {
    musicPlayerAttach();
    HUDManagement.attach(musicPanel);
  }
  public void detach()
  {
    HUDManagement.detach(musicPanel);
  }
  public static void regist()
  {
    MusicHud music = new MusicHud();
    hud_state = HUDManagement.regist(music);
  }
  public void pause()
  {
    if(mp3.isPlay){
      try {
        System.out.println("pause");
        mp3.pause();
        mp3.pauseF = true;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  public void resume()
  {
    if(!mp3.isPlay){
      try {
        mp3.resumeMusic();
        mp3.pauseF = false;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  public void key_act_push()
  {
    selectMusic();
  }
  public void key_act_right()
  {
    if(mp3.playingF)
      mp3.next(); 
    else
    {
      mp3.downMoveCursor();
      moveMusicCursor();
    }
  }
  public void key_act_left()
  {
    if(mp3.playingF)
      mp3.previous();
    else
    {
      mp3.upMoveCursor();
      moveMusicCursor();
    }
  }
  public void key_act_down()
  {
    musicPlayerUndo();
  }

  // select function of music player
  public static void selectMusic()
  {
    if(mp3.playingF)
    {
      if(mp3.pauseF)
        try {
          if(mp3.playingModeSelectState)
          {
            mp3.playingModeSelectState=false;
            mp3.pauseF=false;
          }
          else if(mp3.selectingF)
          {
            mp3.selectingF=false;
            mp3.pauseF=false;
          }
          else
            mp3.resumeMusic();
          musicPlayerIcon[1].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_pause.png", true);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      else
        try {
          mp3.pause();
          musicPlayerIcon[1].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_play.png", true);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
    }
    else
    {
      if(mp3.selectFile()==mp3.ISFILE)
      {
        mp3.playingF=true;
        musicPlayerAttach();
        musicPlayerIcon[1].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_pause.png", true);
      }
      else
        moveMusicCursor();
    }
  }

  // undo function for music player 
  public static void musicPlayerUndo()
  {
    if(mp3.playingF)
    {
      musicPanel.detachChild(musicEquilizer);
      for(int i=0; i<3; i++)
        musicPanel.detachChild(musicPlayerIcon[i]);
      musicPanel.detachChild(musicName);
      mp3.deletePlayingMode();

      // back up to element for previous state
      musicPanel.attachChild(musicList);
      musicPanel.attachChild(musicCursor);
      // updating cursor position
      mp3.adjustCursorPos();
      moveMusicCursor();
    }
    else
      HUDManagement.escapeMenu();
  }

  // move to music cursor
  public static void moveMusicCursor()
  {
    int index = mp3.getCursorPos();
    musicCursor.setPosition(musicCursorPosX,musicCursorPosY[index]);
  }
  private static void musicPlayerAttach()
  {
    if(mp3.playingF)
    {
      // detach element of previous state   
      musicPanel.detachChild(musicList);
      musicPanel.detachChild(musicCursor);
      // attach element of next state
      musicPanel.attachChild(musicEquilizer);
      for(int i=0; i<3; i++)
        musicPanel.attachChild(musicPlayerIcon[i]);
      musicPanel.attachChild(musicName);
    }
    else
    {
      musicPanel.attachChild(musicList);
      musicPanel.attachChild(musicCursor);
    }

  }
}
