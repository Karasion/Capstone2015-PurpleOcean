package eu.opends.trigger;

import eu.opends.hud.HUDManagement;

//Im gisung, Hong sunghyeon
public class DisplayNavigatorAction extends TriggerAction {
	String naviType;
	String distance;
	
	public DisplayNavigatorAction(String naviType, String distance)
	{
		super();
		this.naviType = naviType;
		this.distance = distance;
	}
	
	// override
	protected void execute()
	{
		HUDManagement.setNaviType(naviType, distance);
	}
	// override
	public String toString()
	{
		return "DisplayNavigatorAction: " + naviType;
	}

}
