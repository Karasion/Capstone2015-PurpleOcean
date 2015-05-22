package eu.opends.hud;

import eu.opends.main.Simulator;

// Jo kwanghyeon
// Template abstract class for users to create HUD class .
public abstract class HUDClassTemplet {
	public abstract void init(Simulator simulator);
	public abstract void attach();
	public abstract void detach();
	public abstract void update();
//	public abstract void regist();
	public void pause()
	{
	}
	public void resume()
	{
	}
	public void key_act_push()
	{
	}
	public void key_act_right()
	{
	}
	public void key_act_left()
	{
	}
	public void key_act_up()
	{
	}
	public void key_act_down()
	{
	}
}
