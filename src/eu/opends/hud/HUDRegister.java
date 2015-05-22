package eu.opends.hud;

import eu.opends.hud.BSA.BSAHud;
import eu.opends.hud.call.CallHud;
import eu.opends.hud.music.MusicHud;
import eu.opends.hud.sms.SmsHud;

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
