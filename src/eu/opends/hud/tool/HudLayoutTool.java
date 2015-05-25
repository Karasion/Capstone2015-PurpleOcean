/**
 * @file HudLayoutTool.java
 * @brief  This file is associated with a HUD Layout
 * @details This file is composed of HudLayoutTool class.
 */

/**
 * @namespace eu.opends.hud.tool
 * @brief It is a package for HudLayout
 * @details This package consists of a HudlayoutToo class
 */
package eu.opends.hud.tool;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.basics.SimulationBasics;
import eu.opends.main.Simulator;

//Im gisung
/* simple api class for hud creating*/
/**
 * @brief It is a Hud element tool to try to convenient placement of .
 * @details It is possible to determine the position and size of the element to the simulator screen by operating the element in the shortcut .
 * @author Im-gisung
 *
 */
public class HudLayoutTool {
  private static SimulationBasics sim;
  private static Node hudFrame;
  private static BitmapFont guiFont;

  //constant value
  private static final int MOVE_MODE=10;
  private static final int SIZE_MODE=20;
  private static final int PICTURE_TYPE=30;
  private static final int TEXT_TYPE=40;

  // element list for element attach on Hud
  private static ArrayList<Picture> pictureList=new ArrayList<Picture>();
  private static ArrayList<PictureInfo> pictureInfoList=new ArrayList<PictureInfo>();
  private static ArrayList<BitmapText> textList=new ArrayList<BitmapText>();
  private static ArrayList<BitmapTextInfo> textInfoList=new ArrayList<BitmapTextInfo>();

  private static int moveOffset = 50;
  private static int presentElementInd = 0;
  private static int presentTextInd=0;
  private static BitmapText moveOffsetText;

  // whether hud tool is activated
  public static boolean hudToolActF=false;
  private static int hudToolMode=MOVE_MODE;
  private static int elementType=PICTURE_TYPE;

  // this function initializes to the hud element
  /**
   * @brief This method , to set the elements for using the tool.
   * @param simulator a Simulator object
   * @return nothing
   */
  public static void init(Simulator simulator)
  {
    sim = simulator;
    hudFrame=sim.getGuiNode();

    guiFont = sim.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
    moveOffsetText = new BitmapText(guiFont,false);
    moveOffsetText.setText("");
    moveOffsetText.setSize(guiFont.getCharSet().getRenderedSize()+10);
    moveOffsetText.setColor(ColorRGBA.Yellow);
    moveOffsetText.setLocalTranslation(0, 100, 0);
  }

  // this function is for execute the hud tool
  /**
   * @brief This method is executed the HudLayoutTool mode at startup .
   * @details Read the path of use .png file and .text file as the element from /assets/Tool directory
   * 		.png file is assigned to Picture element, .txt file is assigned to BitmapText element
   * 		and , this element managed to ArrayList.
   * @param nothing
   * @return nothing
   * 
   */
  public static void startHudTool()
  {
    // path:current_workingdir\assets\Tool
    // find to file list in path 
    String path=System.getProperty("user.dir");
    path+="\\assets\\Tool";
    File dir = new File(path);
    File[] filelist = dir.listFiles();
    
    for(File file:filelist)
    {
      if(file.isFile() && file.getName().endsWith(".png")) {
        pictureList.add(new Picture(file.getName()));
        BufferedImage bimg;
        try {
          bimg = ImageIO.read(new File(file.getPath()));
          pictureInfoList.add(new PictureInfo(sim.getSettings().getWidth()/2, sim.getSettings().getHeight()/2,bimg.getWidth(),bimg.getHeight()));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      else if(file.isFile() && file.getName().endsWith(".txt")) {
        BufferedReader in;
        String str="",s;
        
        try {
          // file Read
          in = new BufferedReader(new FileReader(file));
          while((s=in.readLine()) !=null)
            str+=s+"\n";
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        // initial part
        BitmapText tmpText=new BitmapText(guiFont,false);
        tmpText.setName(file.getName());
        tmpText.setText(str);
        System.out.println(str);
        tmpText.setColor(ColorRGBA.White);
        tmpText.setSize(guiFont.getCharSet().getRenderedSize());
        tmpText.setLocalTranslation(sim.getSettings().getWidth()/2, sim.getSettings().getHeight()/2, 0);

        // add element in textlist
        textList.add(tmpText);
        textInfoList.add(new BitmapTextInfo(sim.getSettings().getWidth()/2,sim.getSettings().getHeight()/2,guiFont.getCharSet().getRenderedSize()));

      }

    }

    path="Tool/";
    int i=0;
    // each element initialization
    for(Picture tmp:pictureList) {
      PictureInfo info=pictureInfoList.get(i++);
      tmp.setImage(sim.getAssetManager(), path+tmp.getName(), true);
      tmp.setHeight(info.sizeH);
      tmp.setWidth(info.sizeW);
      tmp.setPosition(info.posX,info.posY);
    }

    hudToolActF=true;
  }

  // moveOffset print on screen
  /**
   * @brief Is a method to be executed in real time on the simulator .
   * @details It will update the mode and offset size and element type of HudTool.
   * @param nothing
   * @return nothing
   */
  public static void updatingHudTool()
  {
    if(HudLayoutTool.hudToolActF) {
      //print move offset
      String tmp ="moveOffset: "+String.valueOf(moveOffset)+"\n"+"element Type:";
      if(elementType==PICTURE_TYPE)
        tmp+="PICTURE"+"\n";
      else
        tmp+="TEXT"+"\n";
      if(hudToolMode==MOVE_MODE)
        tmp+="MODE: MOVE";
      else
        tmp+="MODE: SIZE";
      moveOffsetText.setText(tmp);
      hudFrame.attachChild(moveOffsetText);
      if(!pictureList.isEmpty() && elementType==PICTURE_TYPE)
        hudFrame.attachChild(pictureList.get(presentElementInd));
      if(!textList.isEmpty() && elementType==TEXT_TYPE)
        hudFrame.attachChild(textList.get(presentTextInd));
    }
  }

  // this function attach to next element on hud frame
  /**
   * @brief Paste the next elements to the simulator screen .
   * @details Element Type is the case of the text, it will select an element of the next BitmapText element
   * 		  Element Type is the case of the picture, it will select an element of the next Picture element
   * @param nothing
   * @return nothing
   */
  public static void attachNextElement()
  {
    hudToolMode=MOVE_MODE;
    if(elementType==PICTURE_TYPE) {
      if(!pictureList.isEmpty())
        hudFrame.attachChild(pictureList.get(presentElementInd));
      presentElementInd++;

      if(presentElementInd>=pictureList.size())
        presentElementInd=pictureList.size()-1;
    }
    else if(elementType==TEXT_TYPE) {
      if(!textList.isEmpty())
        hudFrame.attachChild(textList.get(presentTextInd));
      presentTextInd++;

      if(presentTextInd>=textList.size())
        presentTextInd=textList.size()-1;
    }
  }

  //this function attach to previous element on hud frame
  /**
   * @brief Paste the previous elements to the simulator screen .
   * @details Element Type is the case of the text, it will select an element of the previous BitmapText element
   * 		  Element Type is the case of the picture, it will select an element of the previous Picture element
   * @param nothing
   * @return nothing
   */
  public static void attachPreviousElement()
  {
    hudToolMode=MOVE_MODE;
    if(elementType==PICTURE_TYPE) {
      if(!pictureList.isEmpty())
        hudFrame.attachChild(pictureList.get(presentElementInd));
      presentElementInd--;

      if(presentElementInd<0)
        presentElementInd=0;
    }
    else if(elementType==TEXT_TYPE) {
      if(!textList.isEmpty())
        hudFrame.attachChild(textList.get(presentTextInd));
      presentTextInd--;

      if(presentTextInd<0)
        presentTextInd=0;
    }

  }

  // exit HudTool
  /**
   * @brief This method is executed when you exit HudLayTool mode.
   * @details To initialize all elements related to the HudLayoutTool.
   * @param nothing
   * @return nothing
   * 
   */

  public static void exitHudTool()
  {
    for(Picture tmp:pictureList)
      hudFrame.detachChild(tmp);
    for(BitmapText tmp:textList)
      hudFrame.detachChild(tmp);
    
    hudFrame.detachChild(moveOffsetText);
    pictureList.clear();
    pictureInfoList.clear();
    textList.clear();
    textInfoList.clear();
    moveOffset=50;
    presentElementInd=0;
    presentTextInd=0;
    hudToolMode=MOVE_MODE;
    elementType=PICTURE_TYPE;
  }
  
  // up move offSet
  /**
   * @brief This method increases the offset size .
   * @param nothing
   * @return nothing
   * 
   */
  public static void upMoveOffset()
  {
    if(moveOffset==1)
      moveOffset=5;
    else if(moveOffset==5)
      moveOffset=10;
    else if(moveOffset==10)
      moveOffset=50;
    else
      moveOffset=100;
  }
  // down move offset
  /**
   * @brief This method decreases the offset size .
   * @param nothing
   * @return nothing
   * 
   */
  public static void downMoveOffset()
  {
    if(moveOffset==100)
      moveOffset=50;
    else if(moveOffset==50)
      moveOffset=10;
    else if(moveOffset==10)
      moveOffset=5;
    else
      moveOffset=1;
  }

  // position print
  /**
   * @brief This method outputs the size and coordinates of the selected element in the console window .
   * @details  Element Type is the case of the text, and outputs the information of the elements of BitmapText
   * 		  Element Type is the case of the Picture, and outputs the information of the elements of Picture.
   * @param nothing
   * @return nothing
   * 
   */
  public static void posAndSizePrint()
  {
    if(elementType==PICTURE_TYPE) {
      Picture tmp;
      PictureInfo tmpInfo;
      
      if(!pictureList.isEmpty()) {
        tmp=pictureList.get(presentElementInd);
        tmpInfo=pictureInfoList.get(presentElementInd);
        System.out.print(tmp.getName()+": pos("+tmpInfo.posX+", "+tmpInfo.posY+")");
        System.out.println(" size("+tmpInfo.sizeW+", "+tmpInfo.sizeH+")");
      }
    }
    else if(elementType==TEXT_TYPE) {
      BitmapText tmp;
      BitmapTextInfo tmpInfo;
      
      if(!textList.isEmpty()) {
        tmp=textList.get(presentTextInd);
        tmpInfo=textInfoList.get(presentTextInd);
        System.out.print(tmp.getName()+": pos("+tmpInfo.posX+", "+tmpInfo.posY+")");
        System.out.println(" size("+tmpInfo.size+")");

      }
    }
  }
  // move Element position & change size
  /**
   * @brief This method is executed when you press the right key .
   * @details If the mode is move, the element is moved only offset to the right .
   * 		  If the mode is size, the element is increased by offset. 
   * @param nothing
   * @return nothing
   * 
   */
  public static void rightChange()
  {
    if(elementType==PICTURE_TYPE) {
      PictureInfo tmp;
      if(!pictureList.isEmpty()) {
        tmp = pictureInfoList.get(presentElementInd);
        if(hudToolMode==MOVE_MODE) {
          tmp.posX+=moveOffset;
          if(tmp.posX>sim.getSettings().getWidth())
            tmp.posX=sim.getSettings().getWidth();

          pictureList.get(presentElementInd).setPosition(tmp.posX, tmp.posY);
          pictureInfoList.get(presentElementInd).setInfo(tmp);
        }
        else if(hudToolMode==SIZE_MODE) {
          tmp.sizeW+=moveOffset;

          pictureList.get(presentElementInd).setWidth(tmp.sizeW);
          pictureInfoList.get(presentElementInd).setInfo(tmp);
        }
      }
    }
    else if(elementType==TEXT_TYPE) {
      BitmapTextInfo tmpInfo;

      if(!textList.isEmpty()) {
        tmpInfo = textInfoList.get(presentTextInd);
        if(hudToolMode==MOVE_MODE) {
          tmpInfo.posX+=moveOffset;
          if(tmpInfo.posX>sim.getSettings().getWidth())
            tmpInfo.posX=sim.getSettings().getWidth();

          textList.get(presentTextInd).setLocalTranslation(tmpInfo.posX, tmpInfo.posY,0);
          textInfoList.get(presentTextInd).setInfo(tmpInfo);
        }
        else if(hudToolMode==SIZE_MODE) {
          tmpInfo.size+=moveOffset;

          textList.get(presentTextInd).setSize(tmpInfo.size);
          textInfoList.get(presentTextInd).setInfo(tmpInfo);
        }
      }
    }

  }
  /**
   * @brief This method is executed when you press the left key .
   * @details If the mode is move, the element is moved only offset to the left .
   * 		  If the mode is size, the element is decreased by offset. 
   * @param nothing
   * @return nothing
   */
  public static void leftChange()
  {
    if(elementType==PICTURE_TYPE)
    {
      PictureInfo tmp;
      if(!pictureList.isEmpty()) {
        tmp = pictureInfoList.get(presentElementInd);
        if(hudToolMode==MOVE_MODE) {
          tmp.posX-=moveOffset;
          if(tmp.posX<0)
            tmp.posX=0;

          pictureList.get(presentElementInd).setPosition(tmp.posX, tmp.posY);
          pictureInfoList.get(presentElementInd).setInfo(tmp);
        }
        else if(hudToolMode==SIZE_MODE) {
          tmp.sizeW-=moveOffset;
          if(tmp.sizeW<0)
            tmp.sizeW=0;
          pictureList.get(presentElementInd).setWidth(tmp.sizeW);
          pictureInfoList.get(presentElementInd).setInfo(tmp);
        }
      }
    }
    else if(elementType==TEXT_TYPE)
    {
      BitmapTextInfo tmpInfo;

      if(!textList.isEmpty()) {
        tmpInfo = textInfoList.get(presentTextInd);
        if(hudToolMode==MOVE_MODE) {
          tmpInfo.posX-=moveOffset;
          if(tmpInfo.posX<0)
            tmpInfo.posX=0;

          textList.get(presentTextInd).setLocalTranslation(tmpInfo.posX, tmpInfo.posY,0);
          textInfoList.get(presentTextInd).setInfo(tmpInfo);
        }
        else if(hudToolMode==SIZE_MODE) {
          tmpInfo.size-=moveOffset;
          if(tmpInfo.size<0)
            tmpInfo.size=0;

          textList.get(presentTextInd).setSize(tmpInfo.size);
          textInfoList.get(presentTextInd).setInfo(tmpInfo);
        }
      }
    }
  }
  /**
   * @brief This method is executed when you press the up key .
   * @details If the mode is move, the element is moved only offset to the up .
   * 		  If the mode is size, the element is increased by offset. 
   * @param nothing
   * @return nothing
   */
  public static void upChange()
  {
    if(elementType==PICTURE_TYPE) {
      PictureInfo tmp;
      if(!pictureList.isEmpty()) {
        tmp = pictureInfoList.get(presentElementInd);
        if(hudToolMode==MOVE_MODE) {
          tmp.posY+=moveOffset;
          if(tmp.posY>sim.getSettings().getHeight())
            tmp.posY=sim.getSettings().getHeight();

          pictureList.get(presentElementInd).setPosition(tmp.posX, tmp.posY);
          pictureInfoList.get(presentElementInd).setInfo(tmp);
        }
        else if(hudToolMode==SIZE_MODE) {
          tmp.sizeH+=moveOffset;

          pictureList.get(presentElementInd).setHeight(tmp.sizeH);
          pictureInfoList.get(presentElementInd).setInfo(tmp);
        }
      }
    }
    else if(elementType==TEXT_TYPE) {
      BitmapTextInfo tmpInfo;

      if(!textList.isEmpty()) {
        tmpInfo = textInfoList.get(presentTextInd);
        if(hudToolMode==MOVE_MODE) {
          tmpInfo.posY+=moveOffset;
          if(tmpInfo.posY>sim.getSettings().getHeight())
            tmpInfo.posY=sim.getSettings().getHeight();

          textList.get(presentTextInd).setLocalTranslation(tmpInfo.posX, tmpInfo.posY,0);
          textInfoList.get(presentTextInd).setInfo(tmpInfo);
        }
        else if(hudToolMode==SIZE_MODE) {
          tmpInfo.size+=moveOffset;

          textList.get(presentTextInd).setSize(tmpInfo.size);
          textInfoList.get(presentTextInd).setInfo(tmpInfo);
        }
      }

    }

  }
  /**
   * @brief This method is executed when you press the down key .
   * @details If the mode is move, the element is moved only offset to the down .
   * 		  If the mode is size, the element is decreased by offset. 
   * @param nothing
   * @return nothing
   */
  public static void downChange()
  {
    if(elementType==PICTURE_TYPE) {
      PictureInfo tmp;
      if(!pictureList.isEmpty()) {
        tmp = pictureInfoList.get(presentElementInd);
        if(hudToolMode==MOVE_MODE) {
          tmp.posY-=moveOffset;
          if(tmp.posY<0)
            tmp.posY=0;

          pictureList.get(presentElementInd).setPosition(tmp.posX, tmp.posY);
          pictureInfoList.get(presentElementInd).setInfo(tmp);
        }
        else if(hudToolMode==SIZE_MODE) {
          tmp.sizeH-=moveOffset;
          if(tmp.sizeH<0)
            tmp.sizeH=0;

          pictureList.get(presentElementInd).setHeight(tmp.sizeH);
          pictureInfoList.get(presentElementInd).setInfo(tmp);
        }
      }
    }
    else if(elementType==TEXT_TYPE)
    {
      BitmapTextInfo tmpInfo;

      if(!textList.isEmpty()) {
        tmpInfo = textInfoList.get(presentTextInd);
        if(hudToolMode==MOVE_MODE) {
          tmpInfo.posY-=moveOffset;
          if(tmpInfo.posY<0)
            tmpInfo.posY=0;

          textList.get(presentTextInd).setLocalTranslation(tmpInfo.posX, tmpInfo.posY,0);
          textInfoList.get(presentTextInd).setInfo(tmpInfo);
        }
        else if(hudToolMode==SIZE_MODE) {
          tmpInfo.size-=moveOffset;
          if(tmpInfo.size<0)
            tmpInfo.size=0;

          textList.get(presentTextInd).setSize(tmpInfo.size);
          textInfoList.get(presentTextInd).setInfo(tmpInfo);
        }
      }
    }
  }

  // select mode
  /**
   * @brief This method , to select the mode .
   * @details If the mode is move, it converted to the size mode 
   *  		  else if the mode is size, it converted to the move mode
   * @param nothing
   * @return nothing
   */
  public static void selectMode()
  {
    if(hudToolMode==MOVE_MODE)
      hudToolMode=SIZE_MODE;
    else
      hudToolMode=MOVE_MODE;
  }

  // select element Type
  /**
   * @brief This method , to select the element type .
   * @details If the element type is picture type, it converted to the text type 
   *  		  else if the element type is text type, it converted to the picture type
   * @param nothing
   * @return nothing
   */
  public static void selectElementType()
  {
    if(elementType==PICTURE_TYPE)
      elementType=TEXT_TYPE;
    else
      elementType=PICTURE_TYPE;
  }

  // delete Picture
  /**
   * @brief This method , to remove from the list the selected element.
   * @param nothing
   * @return nothing
   */
  public static void deleteElement()
  {
    if(elementType==PICTURE_TYPE) {
      if(pictureList.isEmpty())
        return;
      Picture pic = pictureList.get(presentElementInd);
      PictureInfo info = pictureInfoList.get(presentElementInd);

      if(pic!=null && info!=null) {
        hudFrame.detachChild(pic);
        pictureList.remove(presentElementInd);
        pictureInfoList.remove(presentElementInd);
        presentElementInd--;
        if(presentElementInd<0)
          presentElementInd=0;
      }
    }
    else if(elementType==TEXT_TYPE)
    {
      if(textList.isEmpty())
        return;
      BitmapText text = textList.get(presentTextInd);
      BitmapTextInfo info = textInfoList.get(presentTextInd);

      if(text!=null && info!=null) {
        hudFrame.detachChild(text);
        textList.remove(presentTextInd);
        textInfoList.remove(presentTextInd);
        presentTextInd--;
        if(presentTextInd<0)
          presentTextInd=0;
      }
    }
  }

  // information of element Picture
  /**
   * @brief This class has information about the elements of Picture
   * @param nothing
   * @return nothing
   */
  static class PictureInfo
  {
    public int posX,posY;
    public int sizeW,sizeH;

    /**
     * @brief This method 's constructor
     * @details initializing to instance values as parameters
     * @param posX a integer value, posX is a value of the coordinate x.
     * @param posY a integer value, posY is a value of the coordinate y.
     * @param sizeW a integer value, sizeW is width of the element
     * @param sizeH a integer value, sizeH is height of the element
     * @return nothing
     */
    public PictureInfo(int posX,int posY,int sizeW,int sizeH)
    {
      this.posX=posX;
      this.posY=posY;
      this.sizeW=sizeW;
      this.sizeH=sizeH;
    }


    /**
     * @brief this method will assign the value of an instance of as a parameter .
     * @param info a PictureInfo object
     */
    public void setInfo(PictureInfo info)
    {
      this.posX = info.posX;
      this.posY = info.posY;
      this.sizeW = info.sizeW;
      this.sizeH = info.sizeH;
    }

  }

  // information class of text element
  /**
   * @brief This class has information about the elements of BitmapText
   * @param nothing
   * @return nothing
   */
  static class BitmapTextInfo
  {
    public int posX,posY;
    public int size;

    /**
     * @brief This method 's constructor
     * @details initializing to instance values as parameters
     * @param posX a integer value, posX is a value of the coordinate x.
     * @param posY a integer value, posY is a value of the coordinate y.
     * @param size a integer value, size is a size of the element
     * @return nothing
     */
    BitmapTextInfo(int posX,int posY,int size)
    {
      this.posX=posX;
      this.posY=posY;
      this.size=size;
    }

    /**
     * @brief this method will assign the value of an instance of as a parameter .
     * @param info a BitmapTextInfo object
     */
    public void setInfo(BitmapTextInfo info)
    {
      this.posX=info.posX;
      this.posY=info.posY;
      this.size=info.size;
    }
  }
}
