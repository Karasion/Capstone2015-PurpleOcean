/**
* @file MusicHud.java
* @brief This file is associated with a MusicPlayer
* @details This file is composed of MusicHud class.
*/

package kr.ac.kookmin.cs.music;


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
* @brief This class serves to output information related to the MusicPlayer to HUD
* @details This class is the simulator , if the user performs a musicplayer function ,
*          is output information related to the musicplayer to the appropriate position of HUD.
* @author Lee-MinJae , Jo-KwangHyeon
*/
public class MusicHud extends HUDClassTemplate{
  private static SimulationBasics sim;
  private static Node musicPanel;
  private static int hud_state;

  // element for music player 
  private static BitmapText musicList;
  public static MusicPlayer mp3 = new MusicPlayer("C:/³ë·¡");
  private static Picture musicCursor;
  private static Picture[] musicPlayerIcon = new Picture[3];
  private static Picture musicEquilizer;
  private static Picture musicBackground;
  private static Picture musicHighlight;
  private static BitmapText musicName;

  private static int musicCursorPosX;
  private static int[] musicCursorPosY = new int[5];

  private static int x,y;

  private static Picture icon_en, icon_dis;
  
  /**
  * @brief To initialize the MusicPlayer to HUD.
  * @details To initialize the position and size of the required icon and list	
  * @param param a Simulator argument
  * return Nothing
  */
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
    musicList.setLocalTranslation(x-250,y-20,0);

    musicCursorPosX = x - 283;
    musicCursorPosY[0] = y-43;
    musicCursorPosY[1] = y-76;
    musicCursorPosY[2] = y-108;
    musicCursorPosY[3] = y-140;
    musicCursorPosY[4] = y-172;

    musicCursor = new Picture("musicCursor");
    musicCursor.setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_arrow.png", true);
    musicCursor.setWidth(10);
    musicCursor.setHeight(11);
    musicCursor.setPosition(musicCursorPosX, musicCursorPosY[0]);

    musicPlayerIcon[0] = new Picture("previous");
    musicPlayerIcon[0].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_backSkip.png", true);
    musicPlayerIcon[0].setWidth(46);
    musicPlayerIcon[0].setHeight(46);
    musicPlayerIcon[0].setPosition(x-225, y-180);

    musicPlayerIcon[1] = new Picture("play and pause");
    musicPlayerIcon[1].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_pause.png", true);
    musicPlayerIcon[1].setWidth(64);
    musicPlayerIcon[1].setHeight(64);
    musicPlayerIcon[1].setPosition(x-155, y-190);

    musicPlayerIcon[2] = new Picture("next");
    musicPlayerIcon[2].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_nextSkip.png", true);
    musicPlayerIcon[2].setWidth(46);
    musicPlayerIcon[2].setHeight(46);
    musicPlayerIcon[2].setPosition(x-65,y-180);

    musicEquilizer = new Picture("music equilizer");
    musicEquilizer.setImage(sim.getAssetManager(), "Textures/icons/music/equlizer_arrow_merged.png", true);
    musicEquilizer.setWidth(307);
    musicEquilizer.setHeight(72);
    musicEquilizer.setPosition(690,226);
    
    musicBackground = new Picture("musicBackground");
    musicBackground.setImage(sim.getAssetManager(), "Textures/icons/music/directory_listbox.png", true);
    musicBackground.setWidth(322);
    musicBackground.setHeight(160);
    musicBackground.setPosition(690, 158);
    
    musicHighlight = new Picture("musicHighlight");
    musicHighlight.setImage(sim.getAssetManager(), "Textures/icons/music/directory_highlight.png", true);
    musicHighlight.setWidth(322);
    musicHighlight.setHeight(29);
    musicHighlight.setPosition(690, 288);

    musicName = new BitmapText(font,false);
    musicName.setName("music name");
    musicName.setText("fhdhahf");
    musicName.setSize(font.getCharSet().getRenderedSize());
    musicName.setLocalTranslation(x-210, y-30, 0);

    /* menu icon initialization */
    icon_en = new Picture("musicIcon_en");
    icon_en.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_music.png", true);
    icon_en.setWidth(80);
    icon_en.setHeight(80);

    icon_dis = new Picture("musicIcon_dis");
    icon_dis.setImage(sim.getAssetManager(), "Textures/icons/menubar/menubar_music_c.png", true);
    icon_dis.setWidth(80);
    icon_dis.setHeight(80);

    HUDManagement.setMenuIcon(icon_en, icon_dis, hud_state);
  }
  /**
  * @brief This class , to update the HUD MusicPlayer.
  * @details This method , in a state where the music is selected , 
  * the name of the music that was selected for musicName, and outputs the musicList while selecting the music
  * @return nothing
  */
  public void update()
  {
    if(mp3.playingF)
      musicName.setText(mp3.getPlayingMusicName());
    else
      musicList.setText(mp3.getList());
  }
  /**
  * @brief This method , Attach the node associated with the MusicPlayer to simulator
  * @param nothing
  * @return nothing
  */
  public void attach()
  {
    musicPlayerAttach();
    HUDManagement.attach(musicPanel);
  }
  /**
   * @brief This method , Detach the node associated with the MusicPlayer to simulator
   * @param nothing
   * @return nothing
   */
  public void detach()
  {
    HUDManagement.detach(musicPanel);
  }
  
  /**
   * @brief This method , Detach the node associated with the MusicPlayer to simulator
   * @param nothing
   * @return nothing
   */
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
  /**
  * @brief This method is an operation when the music is selected by the HUD
  * @param nothing
  * @return nothing
  * @exception InterruptedException
  */
  public static void selectMusic()
  {
	  //mp3.playingF is true of the state : a state in which music is being played
    if(mp3.playingF)
    {
    	//mp3.pauseF is true of the state : a state in which music is stopped
      if(mp3.pauseF)
        try {
        	//mp3.playingModeSelectState is true of the state : a state in which the music list is output
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
          // The music is performed again
          else
            mp3.resumeMusic();
          musicPlayerIcon[1].setImage(sim.getAssetManager(), "Textures/icons/music/musicplayer_pause.png", true);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      // Music is stopped 
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
    	// Mp3player is executed if the selected file is mp3 files
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

  /**
  * @brief This method , turn off the HUD music player feature or prints the music list
  * @details State music is being played : Print a list of music
  * 		 Music stopped               : Go menu bar
  * @param nothing
  * @return nothing
  */
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
      musicPanel.attachChild(musicHighlight);
      musicPanel.attachChild(musicBackground);
      // updating cursor position
      mp3.adjustCursorPos();
      moveMusicCursor();
    }
    else
      HUDManagement.escapeMenu();
  }

  /**
  * @brief This method , move to music cursor
  * @param nothing
  * @return nothing
  */
  public static void moveMusicCursor()
  {
    int index = mp3.getCursorPos();
    musicCursor.setPosition(musicCursorPosX,musicCursorPosY[index]);
    musicHighlight.setPosition(690, 288-(32*index));
  }
  
  /**
  * @brief This method , attach the music list and music title
  * @details Music playing : attach the music title 
			 Music stopped : attach a list of music
  * @param nothing
  * @return nothing
  */
  private static void musicPlayerAttach()
  {
    if(mp3.playingF)
    {
      // detach element of previous state   
      musicPanel.detachChild(musicList);
      musicPanel.detachChild(musicCursor);
      musicPanel.detachChild(musicHighlight);
      musicPanel.detachChild(musicBackground);
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
      musicPanel.attachChild(musicHighlight);
      musicPanel.attachChild(musicBackground);
    }

  }
}
