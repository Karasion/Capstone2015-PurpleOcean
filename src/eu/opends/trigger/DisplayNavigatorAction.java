package eu.opends.trigger;

import eu.opends.hud.HudDisplay;


public class DisplayNavigatorAction extends TriggerAction {
	String naviType;
	
	public DisplayNavigatorAction(String naviType)
	{
		super();
		this.naviType = naviType;
	}
	
	// override
	protected void execute()
	{
		System.out.println(naviType);
		HudDisplay.setNaviType(naviType);
	}
	// override
	public String toString()
	{
		return "DisplayNavigatorAction: " + naviType;
	}

}
