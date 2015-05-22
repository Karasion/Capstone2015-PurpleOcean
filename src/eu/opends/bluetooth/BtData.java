package eu.opends.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class BtData implements Serializable {

  private static final long serialVersionUID = 3459520869516421384L;

  private int dataType;
  private String smsMsg;
  private String sender;

  public BtData()
  {
    this.dataType=-1;
    this.smsMsg=null;
    this.sender=null;
  }
  public BtData(int type)
  {
    this.dataType = type;
    this.smsMsg=null;
    this.sender=null;
  }
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

  public static byte[] serialize(Object obj) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream os = new ObjectOutputStream(out);
    os.writeObject(obj);
    return out.toByteArray();
  }
  public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    ObjectInputStream is = new ObjectInputStream(in);
    return is.readObject();
  }
}
