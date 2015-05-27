/**
* @file MusicPlayer.java
* @brief This file , run the MusicPlayer function
* @details This file is composed of MusicPlayer class.
*/

package kr.ac.kookmin.cs.music;
import java.io.File;

import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.FilenameFilter;

/**
* @brief This class is a core class associated with the music player
* @details There are basic methods necessary to music playback such as run,resume,pause,next,previous function
* @author Lee-MinJae , Im-GiSung
*/
public class MusicPlayer extends Thread{

  private Player player; 
  private ArrayList<String> musicList = new ArrayList<String>();
  private ArrayList<String> dirList = new ArrayList<String>();
  private ArrayList<String> totalList = new ArrayList<String>();
  private String path;
  private String currentDir;
  private ArrayList<String> previousDir = new ArrayList<String>();
  private String rootDir;

  public boolean runFlag=false;
  private boolean endFlag=false;
  public static boolean pauseF=false;
  private boolean previousF=false;
  private boolean nextF=false;
  private boolean selectF=false;
  public static boolean isPlay = false;
  public static boolean playingF=false;
  public static boolean playingModeSelectState=false;
  public boolean selectingF=false;
  int pos;

  //for position of selected file 
  private int presentCursor=0;
  //index for show list  
  private int startInd;
  private int endInd;

  // constant element for distinct file and directory
  public final int ISDIR=20;
  public final int ISFILE=21;

  /**
  * @brief To initialize the MusicPlayer
  * @details Initializing the MusicPlayer to root Directory you received as a parameter
  * @param a String argument
  * @return nothing
  */
  public MusicPlayer(String rootDir) {
    this.currentDir = rootDir;
    this.path = currentDir;
    this.rootDir = rootDir;
    findDirList();
    findMusicList();
    makeTotalList();
    startInd=0;
    if(totalList.size()<5)
      endInd=totalList.size();
    else
      endInd=5;
  }
 
  /**
  * @brief This method , the ability to browse the selected file
  * @details If the selected file is mp3 files , start the selected file , returns ISFILE
  * 		 If the selected file is Directory , update the Directory List , returns ISFDIR
  * @param nothing
  * @return ISFILE or ISDIR
  */
  public int selectFile()
  {
    String selectedFile = totalList.get(presentCursor);
    this.path = currentDir + "/" + selectedFile;
    if(selectedFile.endsWith(".mp3"))
    {
      pos = musicList.indexOf(selectedFile);
      if(runFlag==false)
      {
        runFlag=true;
        isPlay = true;
        this.start();
      }
      else
      {
        if(playingModeSelectState)
          playingModeSelectState=false;
        else
          player.close();
        selectF=true;
        selectingF=false;
      }
      return ISFILE;
    }
    else
    {
      if(selectedFile.equals(".."))
        currentDir=previousDir.remove(previousDir.size()-1);
      else
      {
        previousDir.add(currentDir);
        currentDir = currentDir + "/" + selectedFile;
      }
      System.out.println(currentDir);
      findDirList();
      findMusicList();
      makeTotalList();
      startInd=0;
      if(totalList.size()<5)
        endInd=totalList.size();
      else
        endInd=5;
      presentCursor=0;

      return ISDIR;
    }
  }
  /**
  * @brief  This method returns the position of the current position of the cursor
  * @param nothing
  * @return  a integer argument
  */
  public int getCursorPos()
  {
    int pos = presentCursor - startInd;
    return pos;
  }
  /**
  * @brief This method , added to the musicList only mp3 files in the folder
  * @param nothing
  * @return nothing
  */
  private void findMusicList(){
    File home = new File(currentDir);
    musicList.clear();
    if(home.listFiles(new Mp3Filter()).length > 0)
    {
      for (File file : home.listFiles(new Mp3Filter())) 
      {
        musicList.add(file.getName());
      }
    }  
  }
  /**
  * @brief This method , to add a list of folders to dirList
  * 
  * @param nothing
  * @return nothing
  */
  private void findDirList(){
    File dir = new File(currentDir);
    dirList.clear();
    if(!currentDir.equals(rootDir))
      dirList.add("..");

    for(File file : dir.listFiles())
    {
      if(file.isDirectory())
        dirList.add(file.getName());
    } 
  }
  /**
  * @brief This method adds a dirList and musicList together
  * @param nothing
  * @return nothing
  */
  private void makeTotalList()
  {
    totalList.clear();
    for(int i=0; i<dirList.size(); i++)
      totalList.add(dirList.get(i));
    for(int i=0; i<musicList.size(); i++)
      totalList.add(musicList.get(i));
  }
  
  /**
  * @brief This method move up the cursor
  * @param nothing
  * @return nothing
  */
  public void upMoveCursor()
  {
    presentCursor--;
    if(presentCursor < 0)
      presentCursor = 0;
    else
    {
      if(presentCursor < startInd)
      {
        startInd--;
        if(endInd-startInd>5)
          endInd--;
      }
    }
  }
  /**
   * @brief This method move down the cursor
   * @param nothing
   * @return nothing
   */
  public void downMoveCursor()
  {
    presentCursor++;
    if(presentCursor >= totalList.size())
      presentCursor = totalList.size()-1;
    else
    {
      if(presentCursor >= endInd)
      {
        endInd++;
        if(endInd-startInd>5)
          startInd++;
      }
    }
  }
 
  /**
  * @brief This method sets the index for output to lease a file that is currently running
  * @param nothing
  * @return nothing
  */
  public void adjustCursorPos()
  {
    presentCursor=totalList.indexOf(musicList.get(pos));
    if(totalList.size()-presentCursor>=5)
    {
      startInd=presentCursor;
      endInd=startInd+5;
    }
    else
    {
      endInd=totalList.size();
      if(endInd<=5)
        startInd=0;
      else
        startInd=endInd-5;
    }
  }
  /**
   * @brief This method return the music List
   * @param nothing
   * @return String List  
   */
  public String getList()
  {
    String showList="";
    for(int i=startInd;i<endInd;i++)
    {
      if(totalList.get(i).length()>20)
        showList+=totalList.get(i).substring(0, 19)+"...\n";
      else
        showList+=totalList.get(i)+"\n";
    }
    return showList;
  }
  
  /**
   * @brief This method close the music player
   * @param nothing
   * @return nothing
   */
  public void close() {
    if(runFlag)
    {
      player.close();
      endFlag=true;
    }
  }
  
  /**
  * @brief This method returns the file name to match the type
  * @param nothing
  * @return String argument printFormat
  */
  public String getPlayingMusicName()
  {
    String musicName=musicList.get(pos);
    String printFormat="";

    if(musicName.length() < 16)
      printFormat+=musicName;
    else if(musicName.length() >15 && musicName.length() <=30)
      printFormat+=musicName.substring(0, 16) + "\n" + musicName.substring(16, musicName.length());
    else
      printFormat+=musicName.substring(0, 16) + "\n" + musicName.substring(16, 30)+"...";
    return printFormat;
  }
  /**
  * @brief This method play the mp3 file to the sound card
  * @param nothing
  * @return nothing
  * @exception StringIndexOutOfRangeException
  */
  public void play() {
    try {
      FileInputStream fis = new FileInputStream(path);
      player = new Player(fis);
    }

    catch (Exception e) {
      System.out.println("Problem playing file " + path);
      System.out.println(e);
    }

  }
  /**
  * @brief This method , to play the next song
  * @details Play songs of the following pos of MusicList.
  * 		 If if the current song is the last song , play the first song
  * @param nothing
  * @return nothing
  */
  public void next()
  {
    if(pauseF)
    {
      try {
        if(!playingModeSelectState)
        {
          resumeMusic();
          pauseF=true;
          playingModeSelectState = true;
        }
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    player.close();
    if(pos== musicList.size()-1) 
      pos = 0;
    else 
      pos++;
    path = currentDir + "/" + musicList.get(pos);
    nextF=true;
  }
  /**
   * @brief This method , to play the previous song
   * @details Play songs of the previous  pos of MusicList.
   * 		 If if the current song is the first song , play the last song
   * @param nothing
   * @return nothing
   * @exception InterruptedException
   */
  public void previous()
  {
    if(pauseF)
    {
      try {
        if(!playingModeSelectState)
        {
          resumeMusic();
          pauseF=true;
          playingModeSelectState = true;
        }
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    player.close();
    if(pos== 0) pos = musicList.size()-1;
    else 
      pos--;
    path = currentDir + "/" + musicList.get(pos);
    previousF=true;
  }
  
  /**
  * @brief This method , to play a song to suit each state
  * @details Automatically it will play the next song and if the song is finished
  * @param nothing
  * @return nothing
  */
  public void run() {

    try { 
      while (true) { 
        synchronized(this) {
          if(playingModeSelectState || selectingF) 
            continue;
          play();
          player.play();
          if(endFlag)
          {
            runFlag=false;
            System.exit(1);
          }
          else if(previousF)
            previousF=false;
          else if(nextF)
            nextF=false;
          else if(selectF)
            selectF=false;
          else if(pos<musicList.size())
            this.next();
        }	
      }
    }
    catch (Exception e) { 
      System.out.println(e);
    }
  }
  /**
  * @brief This method , to pause a song
  * @param nothing
  * @return nothing
  */
  @SuppressWarnings("deprecation")
  public void pause() throws InterruptedException{
    System.out.println("Pause");
    pauseF=true;
    isPlay = false;
    this.suspend();
  }
  /**
  * @brief This method , to resume a song
  * @param nothing
  * @return nothing
  */
  @SuppressWarnings("deprecation")
  public void resumeMusic() throws InterruptedException{
    System.out.println("Resume");
    pauseF=false;
    isPlay = true;
    this.resume();
  }
  /**
  * @brief This class is a filter class extension of the file to determine whether it is mp3
  * @author Lee-MinJae
  */
  class Mp3Filter implements FilenameFilter {
	  /**

	  * @brief name it returns True if the mp3 file , it returns false if there is no

	  * @param File argument and String name

	  * @return boolean argument
	  * 
	  */
    public boolean accept(File dir, String name) {
      return (name.endsWith(".mp3"));
    }
  }
  /**
  * @brief This method terminates the player, to reset the flag
  * @param nothing
  * @return nothing
  * @exception InterruptedException
  */
  public void deletePlayingMode()
  {
    if(pauseF && !playingModeSelectState)
    {
      try {
        resumeMusic();
        player.close();
        pauseF=true;
        selectF=true;
        selectingF=true;
      } catch (InterruptedException e) {
        // TODO Auto-generated catch blockaz
        e.printStackTrace();
      } 
    }
    playingF=false;
    nextF=false;
    previousF=false;
  }
}