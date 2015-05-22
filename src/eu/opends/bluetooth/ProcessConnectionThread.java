package eu.opends.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

import eu.opends.hud.call.CallListener;
import eu.opends.hud.sms.SmsRecv;

public class ProcessConnectionThread implements Runnable{

  private StreamConnection mConnection;

  // Constant that indicate command from devices
  private static final int EXIT_CMD = -1;
  // add part
  private static final int SMS_RECV = 4;
  private static final int CALL_RECV = 5;
  private static final int CALL_END = 6;
  private static final int CALL_ON = 7;
  private static OutputStream outputStream;
  
  public ProcessConnectionThread(StreamConnection connection)
  {
    mConnection = connection;
  }

  @Override
  public void run() {
    try {
      // prepare to receive data
      InputStream inputStream = mConnection.openInputStream();
      outputStream = mConnection.openOutputStream();
      byte[] temp = new byte[512];
      BtData data;

//      System.out.println("waiting for input");

      while (true) {
        inputStream.read(temp);
//        System.out.println("test");
        data =(BtData)BtData.deserialize(temp);
//        System.out.println(data.getDataType());

        if (data.getDataType() == EXIT_CMD) {	
          System.out.println("finish process");
          break;
        } else if (data.getDataType() == SMS_RECV) {
//          System.out.println(data.getMsg());
//          System.out.println("from "+ data.getSender());
          SmsRecv.addMsg(data.getMsg(), data.getSender());
        } else if (data.getDataType() == CALL_RECV) {
//          System.out.println("CALL Recv");
//          System.out.println("from" + data.getSender());
          CallListener.setSender(data.getSender());
        } else if (data.getDataType() == CALL_END) {
//          System.out.println("CALL END");
          CallListener.setEnd();
        } else if (data.getDataType() == CALL_ON) {
//          System.out.println("CALL ON");
          CallListener.setCallState(1);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } 

  }

  public static void sendData(String str){
    try {
      String strOnCall = "onCall";

      if(str.equals(strOnCall)) {
        BtData data = new BtData(5, str, "0");
//        System.out.println("send :" + str);
        outputStream.write(BtData.serialize(data));
      } else{
        BtData data = new BtData(6, str, "0");
//        System.out.println("send :" + str);
        outputStream.write(BtData.serialize(data));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
