package eu.opends.bluetooth;

public class RemoteBluetoothServer{
	private static Thread waitThread = new Thread(new WaitThread());
	public static void start() {
	  waitThread.start();
	}
	
	public static void stop() {
	  waitThread.interrupt();
	}
}
