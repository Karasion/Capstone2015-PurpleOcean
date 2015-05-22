package eu.opends.hud.sms;

import java.util.ArrayList;

//Ha gimyeong, Im gisung
public class SmsRecv {
  private static ArrayList<String> msgList = new ArrayList<String>();
  private static ArrayList<String> senderList = new ArrayList<String>();
  private static int newMsg = 0;
  private static int currentMsg = 0;
  private static int totalMsg = 0;

  // message add method
  public static void addMsg(String msg,String sender)
  {
    msgList.add(msg);
    senderList.add(sender);
    newMsg++;
    totalMsg++;
    currentMsg = totalMsg - 1;
  }

  // new Message check method
  public static boolean isMsg()
  {
    if(totalMsg>0)
      return true;
    else
      return false;
  }

  // show message

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

  public static int getNewMsgNum()
  {
    return newMsg;
  }

  public static void leftMsg()
  {
    currentMsg--;
    if(currentMsg<0)
      currentMsg = totalMsg-1;
  }

  public static void rightMsg()
  {
    currentMsg++;
    if(currentMsg == totalMsg)
      currentMsg = 0;
  }

  public static void clearMsg()
  {
    //    msgList.clear();
    //    senderList.clear();
    newMsg = 0;
  }
}
