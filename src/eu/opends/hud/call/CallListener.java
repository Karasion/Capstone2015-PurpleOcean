package eu.opends.hud.call;

import eu.opends.bluetooth.ProcessConnectionThread;

//Jo kwanhyeon
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

  public static void setEnd(){
    System.out.println("End Call by phone");
    call_state = 0;
    flag = false;
  }

  public static void endCall(){
    //    System.out.println("endCall");
    ProcessConnectionThread.sendData("endCall");
    call_state = 0;
    flag = false;
  }

  public static void onCall(){
    //    System.out.println("onCall");
    ProcessConnectionThread.sendData("onCall");
    call_state = 1;
  }
}