/**
 * @file ProcessConnectionThread.java
 * @brief This file is associated with the Bluetooth communication.
 * @details This file is composed of ProcessConnectionThread class.
 */

/**
 * @namespace kr.ac.kookmin.cs.bluetooth
 * @brief This package is a set of classes related to Bluetooth communication .
 * @details This package is composed of Bluetooth data classes and Bluetooth communication class .
 */
package kr.ac.kookmin.cs.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

import kr.ac.kookmin.cs.call.CallListener;
import kr.ac.kookmin.cs.sms.SmsRecv;

/**
 * @brief This class is process the data passed to it is communication with Bluetooth.
 * @details This class is appropriate for processing in accordance with the type of BtData.
 * @author Im-gisung,Jo-KwangHyeon
 *
 */
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
  
  /**
   * @brief This method is constructor.
   * 		and It initializes the value of an instance as parameter.
   * @param connection a StreamConnection object 
   */
  public ProcessConnectionThread(StreamConnection connection)
  {
    mConnection = connection;
  }

  @Override
  /**
   * @brief This method executes a thread for Bluetooth data processing .
   * @details It processes the btData to be transferred while infinite repeat .
   * @param nothing
   */
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

  /**
   * @brief This method transfers the data in the Mobile app .
   * @details This method transfers the value of the phone state to app.
   * @param str a String object, It is Call state.
   * @return nothing
   */
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
