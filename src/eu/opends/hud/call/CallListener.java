 /**
 * @file CallListener.java
 * @brief This file is associated with a call.
 * @details This file is composed of CallListner class.
 */

 /**
 * @namespace eu.opends.hud.call
 * @brief Package for implementing call in HUD
 * @details This package consists of a call layout class and call core class 
 *          for implementing the call function     
 */
package eu.opends.hud.call;

import eu.opends.bluetooth.ProcessConnectionThread;

/**
* @brief It is a class related to call
* @details This class is synchronized with the call state of the mobile .
*          And the call action is implemented.
* @author Jo-kwanghyeon
*/
public class CallListener {
  private static int call_state = 0;
  private static String sender = "";
  private static boolean flag = false;

  public static int getCallState(){
    return call_state;
  }

  public static void setCallState(int state){
    call_state = state;
  }

  /**
   * @brief Method to check whether or not the call state.
   * @param Nothing
   * @return It returns true if during a call , it returns false otherwise.
   */
  public static boolean isCall(){
    return flag;
  }

  public static String getSender() {
    return sender;
  }

  public static void setSender(String sender) {
    CallListener.sender = sender;
    flag = true;
  }

  /**
   * @brief It wants to change to the end state of the call.
   * @param Nothing
   * @return Nothing
   */
  public static void setEnd(){
    call_state = 0;
    flag = false;
  }

  /**
   * @brief It send a message to reject a phone call to the mobile in this method. 
   * @param Nothing
   * @return Nothing
   */
  public static void endCall(){
    ProcessConnectionThread.sendData("endCall");
    call_state = 0;
    flag = false;
  }

  /**
   * @brief It sends a message of receiving a phone call to the mobile in this method.
   * @param Nothing
   * @return Nothing
   */
  public static void onCall(){
    ProcessConnectionThread.sendData("onCall");
    call_state = 1;
  }
}