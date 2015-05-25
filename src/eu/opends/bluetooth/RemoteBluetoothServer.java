/**
 * @file RemoteBluetoothServer.java
 * @brief This file is associated with the Bluetooth communication.
 * @details This file is composed of RemoteBluetoothServer class.
 */

/**
 * @namespace eu.opends.bluetooth
 * @brief This package is a set of classes related to Bluetooth communication .
 * @details This package is composed of Bluetooth data classes and Bluetooth communication class .
 */
package eu.opends.bluetooth;

/**
 * @brief This class creates a server for Bluetooth communication .
 * @details Server is implemented as a thread .
 * @author Im-gisung,Jo-kwanghyeun
 *
 */
public class RemoteBluetoothServer{
	private static Thread waitThread = new Thread(new WaitThread());
	/**
	 * @brief This method is start a thread .
	 * @param nothing
	 * @return nothing
	 */
	public static void start() {
	  waitThread.start();
	}
	/**
	 * @brief This method is stop a thread.
	 * @param nothing
	 * @return nothing
	 */
	public static void stop() {
	  waitThread.interrupt();
	}
}
