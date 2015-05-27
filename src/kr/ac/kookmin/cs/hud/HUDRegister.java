package kr.ac.kookmin.cs.hud;

import kr.ac.kookmin.cs.BSA.BSAHud;
import kr.ac.kookmin.cs.call.CallHud;
import kr.ac.kookmin.cs.music.MusicHud;
import kr.ac.kookmin.cs.sms.SmsHud;

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
