 /**
 * @file BSADumy.java
 * @brief This file is associated with a BSA detect
 * @details This file is composed of BSADumy class.
 */

 /**
 * @namespace kr.ac.kookmin.cs.BSA
 * @brief Package for implementing BSA in HUD
 * @details This package consists of a BSA layout class and BSA core class for implementing the BSA function
 */
package kr.ac.kookmin.cs.BSA;

/**
 * @brief This class , to detect the BSA signal .
 * @author Im-gisung,Hong-sunghyeon
 *
 */
public class BSADumy {
  private static boolean back=false;
  private static boolean detectF=false;
  private static boolean userCarF=false;
  
  
  public static boolean getDetectFlag() {
    return detectF;
  }
  public static boolean getBackFlag() {
    return back;
  }
  public static boolean getUserCarFlag() {
    return userCarF;
  }
  public static void setUserCarFlag(boolean flag) {
    userCarF = flag;
  }
  public static void setDetectFlag(boolean flag) {
    detectF = flag;		
  }
  public static void setBackFlag(boolean flag) {
    back = flag;		
  }
}
