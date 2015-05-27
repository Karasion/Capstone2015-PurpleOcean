package kr.ac.kookmin.cs.hud;

import kr.ac.kookmin.cs.BSA.BSAHud;
import kr.ac.kookmin.cs.call.CallHud;
import kr.ac.kookmin.cs.music.MusicHud;
import kr.ac.kookmin.cs.sms.SmsHud;

/**
* @mainpage Head-Up Display module for Driving Simulator
* @brief HUD module for OpenDS
* @details We have implemented a HUD API that can be used in " OpenDS ".
*          In addition we have implemented a part of the HUD features.
* @author Sung-nahyeon, Lee-minjae, Im-gisung, Jo-kwanghyeon, ha-jimyeong, hong-sunghyeon 
* @version 1.0
*/
//Jo kwanghyeon
//Class to register a HUD user-created in HUD module .
public class HUDRegister {
	public static void hud_enrollment()
	{
		BSAHud.regist();
		CallHud.regist();
		SmsHud.regist();
		MusicHud.regist();
	}
}
