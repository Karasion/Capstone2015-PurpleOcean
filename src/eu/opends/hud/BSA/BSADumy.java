package eu.opends.hud.BSA;

//Im gisung, Hong sunghyeon
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
