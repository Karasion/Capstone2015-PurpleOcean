/**
 * @file WaitThread.java
 * @brief This file is associated with the Bluetooth communication.
 * @details This file is composed of WaitThread class.
 */

/**
 * @namespace eu.opends.bluetooth
 * @brief This package is a set of classes related to Bluetooth communication .
 * @details This package is composed of Bluetooth data classes and Bluetooth communication class .
 */
package kr.ac.kookmin.cs.bluetooth;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * @brief This class connect the Bluetooth -enabled mobile application and the server .
 * @details this class is executed when WaitThread start is called.
 * @author Im-gisung,Jo-gwanghyeun
 *
 */
public class WaitThread implements Runnable{

	/** Constructor */
	/**
	 * @brief constructor
	 */
	public WaitThread() {
	}
	
	@Override
	/**
	 * @brief this method is executed when WaitThread Start() is called.
	 * @param nothing
	 * @return nothing 
	 */
	public void run() {
		waitForConnection();		
	}
	
	/* Waiting for connection from devices */
	/**
	 * @brief This method connect the Bluetooth -enabled mobile application and the server.
	 * @details Looking for a device to Bluetooth communication to wait until there is a connection request .
	 * @param nothing
	 * @return nothing
	 */
	private void waitForConnection() {
		// retrieve the local Bluetooth device object
		LocalDevice local = null;
		
		StreamConnectionNotifier notifier;
		StreamConnection connection = null;
		
		// setup the server to listen for connection
		try {
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);
			
			UUID uuid = new UUID("04c6093b00001000800000805f9b34fb", false);
			System.out.println(uuid.toString());
			
            String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
            notifier = (StreamConnectionNotifier)Connector.open(url);
        } catch (BluetoothStateException e) {
        	System.out.println("Bluetooth is not turned on.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// waiting for connection
		while(true) {
			try {
				System.out.println("waiting for connection...");
	            connection = notifier.acceptAndOpen();
	            
	            Thread processThread = new Thread(new ProcessConnectionThread(connection));
	            processThread.start();
	            
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
