 /**
 * @file SmsRecv.java
 * @brief This file is associated with the sms receiving function .
 * @details This file is composed of SmsRecv class.
 */

 /**
 * @namespace eu.opends.hud.sms
 * @brief Package for implementing sms in HUD
 * @details This package consists of a sms layout class and sms core class for implementing the sms function
 * 			
 */
package eu.opends.hud.sms;

import java.util.ArrayList;

 /**
  * @brief It is a class related to the character of the reception of sms.
  * @details This class receives information associated with the message from the Bluetooth server , serves to process .
  * @author Im-gisung, Ha-gimyeong
  *
  */
public class SmsRecv {
  private static ArrayList<String> msgList = new ArrayList<String>();
  private static ArrayList<String> senderList = new ArrayList<String>();
  private static int newMsg = 0;
  private static int currentMsg = 0;
  private static int totalMsg = 0;

  // message add method
  /**
   * @brief This method receives the information of the message , and add it to the list.
   * @param msg is a character content into a single string object .
   * @param sender is a string object , it is information of the caller .
   * @return nothing
   */
  public static void addMsg(String msg,String sender)
  {
    msgList.add(msg);
    senderList.add(sender);
    newMsg++;
    totalMsg++;
    currentMsg = totalMsg - 1;
  }

  // new Message check method
  /**
   * @brief This method , to verify whether the message is in the list .
   * @return true if msg is present in the list, it is false if it does not exist .
   */
  public static boolean isMsg()
  {
    if(totalMsg>0)
      return true;
    else
      return false;
  }

  // show message
  /**
   * @brief This method serves to return the information of the received message is placed in the character string object .
   * @details It returns a string object with the contents of the message sender and the message .
   * @param nothing
   * @return It is a string object with the content and sender information of the message .
   */
  public static String getNewMsg()
  {
    String msg = msgList.get(currentMsg);
    String fmStr = "";
    boolean endState=false;
    int msgLen = msg.length();
    int lineN = 4;
    int retN=0;
    int index=0;
    int lineSize = 11;



    for(int i=0;i<lineN;i++) {
      if(msgLen>0) {
        if(msgLen > lineSize) {
          String tmp=msg.substring(index, index+lineSize);
          for(int j=0; j<tmp.length();j++)
            if(tmp.charAt(j)=='\n') {
              retN++;
              if(retN>3) {
                fmStr+=tmp.substring(0,j+1);
                endState=true;
                break;
              }
            }
          if(endState)
            break;

          fmStr +=msg.substring(index, index+lineSize);
          msgLen-=lineSize;
          index +=lineSize;
        }
        else
        {
          String tmp=msg.substring(index, msg.length());
          for(int j=0; j<tmp.length();j++)
            if(tmp.charAt(j)=='\n') {
              retN++;
              if(retN>3) {
                fmStr+=tmp.substring(0,j+1);
                endState=true;
                break;
              }
            }
          if(endState)
            break;
          fmStr +=msg.substring(index, msg.length());
          msgLen -=lineSize;
        } 

      }

      if(retN<=i) {
        fmStr+="\n";
        retN++;
        if(retN>3)
          break;
      }

    }

    fmStr+="\nfrom:"+senderList.get(currentMsg);
    return fmStr;
  }

  /**
   * @brief This method returns the number of newly received message .
   * @param nothing
   * @return Integer value of newly received message
   */
  public static int getNewMsgNum()
  {
    return newMsg;
  }

  /**
   * @brief This method , to select the next message .
   * @param nothing
   * @return nothing
   */
  public static void leftMsg()
  {
    currentMsg--;
    if(currentMsg<0)
      currentMsg = totalMsg-1;
  }

  /**
   * @brief This method , to select the previous message .
   * @param nothing
   * @return nothing
   */
  public static void rightMsg()
  {
    currentMsg++;
    if(currentMsg == totalMsg)
      currentMsg = 0;
  }

  /**
   * @brief The method initializes the number of newly received messages .
   * @param nothing
   * @return nothing
   */
  public static void clearMsg()
  {
    newMsg = 0;
  }
}
