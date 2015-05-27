/**
 * @file BtData.java
 * @brief This file is associated with a Bluetooth data .
 * @details This file is composed of BtData class .
 */

/**
 * @namespace kr.ac.kookmin.cs.bluetooth
 * @brief This package is a set of classes related to Bluetooth communication .
 * @details This package is composed of Bluetooth data classes and Bluetooth communication class .
 */
package kr.ac.kookmin.cs.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @brief This class is a data format to be transmitted in Bluetooth.
 * @details This class has the caller information and the Bluetooth data type.
 * 			and bluetooth data type is Sms data or call signal data
 * @author Im-gisung,Jo-Kwanghyeon
 *
 */
public class BtData implements Serializable {

  private static final long serialVersionUID = 3459520869516421384L;

  private int dataType;
  private String smsMsg;
  private String sender;

  /**
   * @brief This method is constructor.
   * 		and It initializes the value of an instance.
   * @param nothing 
   */
  public BtData()
  {
    this.dataType=-1;
    this.smsMsg=null;
    this.sender=null;
  }
  /**
   * @brief This method is constructor.
   * 		and It initializes the value of an instance as parameter.
   * @param type an integer 
   */
  public BtData(int type)
  {
    this.dataType = type;
    this.smsMsg=null;
    this.sender=null;
  }
  /**
   * @brief This method is constructor.
   * 		and It initializes the value of an instance as parameters.
   * @param type an integer 
   * @param msg a String object, It is message content.
   * @param sender a String object, It is a caller information .
   */
  public BtData(int type,String msg,String sender)
  {
    this.dataType = type;
    this.smsMsg = msg;
    this.sender = sender;
  }

  
  public String getMsg()
  {
    return smsMsg;
  }

  public int getDataType()
  {
    return dataType;
  }

  public String getSender()
  {
    return sender;
  }

  /**
   * @brief This method is serialize the object data to a byte array .
   * @param obj an Object class
   * @return Byte array
   * @throws IOException
   */
  public static byte[] serialize(Object obj) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream os = new ObjectOutputStream(out);
    os.writeObject(obj);
    return out.toByteArray();
  }
  /**
   * @brief This method is deserialize the byte array as object data.
   * @param data a byte Array
   * @return Object class
   * @throws IOException
   */
  public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    ObjectInputStream is = new ObjectInputStream(in);
    return is.readObject();
  }
}
