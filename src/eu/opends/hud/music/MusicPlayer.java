package eu.opends.hud.music;
import java.io.File;

import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.FilenameFilter;

//Lee minjae, Im gisung
public class MusicPlayer extends Thread{

  private Player player; 
  private ArrayList<String> musicList = new ArrayList<String>();
  private ArrayList<String> dirList = new ArrayList<String>();
  private ArrayList<String> totalList = new ArrayList<String>();
  private String path;
  private String currentDir;
  private ArrayList<String> previousDir = new ArrayList<String>();
  private String rootDir;

  // MP3 close condition
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

  // constructor that takes the name of an MP3 file
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
  //
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
  public int getCursorPos()
  {
    int pos = presentCursor - startInd;
    return pos;
  }
  // find to mp3 file list 
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
  // directory list find;
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
  // make total List ( dirList + music file List)
  private void makeTotalList()
  {
    totalList.clear();
    for(int i=0; i<dirList.size(); i++)
      totalList.add(dirList.get(i));
    for(int i=0; i<musicList.size(); i++)
      totalList.add(musicList.get(i));
  }
  // move up cursor
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
  // move down cursor
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
  // 
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
  // music list return;
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
  public void close() {
    if(runFlag)
    {
      player.close();
      endFlag=true;
    }
  }

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

  // play the MP3 file to the sound card
  public void play() {
    try {
      FileInputStream fis = new FileInputStream(path);
      //System.out.println("next path"+ path);
      player = new Player(fis);
    }

    catch (Exception e) {
      System.out.println("Problem playing file " + path);
      System.out.println(e);
    }

  }
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
  @SuppressWarnings("deprecation")
  public void pause() throws InterruptedException{
    System.out.println("Pause");
    pauseF=true;
    isPlay = false;
    this.suspend();
  }
  @SuppressWarnings("deprecation")
  public void resumeMusic() throws InterruptedException{
    System.out.println("Resume");
    pauseF=false;
    isPlay = true;
    this.resume();
  }
  class Mp3Filter implements FilenameFilter {
    public boolean accept(File dir, String name) {
      return (name.endsWith(".mp3"));
    }
  }
  // playing mode flag reset;
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
        // TODO Auto-generated catch block
        e.printStackTrace();
      } 
    }
    playingF=false;
    nextF=false;
    previousF=false;
  }
}