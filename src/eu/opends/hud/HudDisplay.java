package eu.opends.hud;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import eu.opends.basics.SimulationBasics;
import eu.opends.car.Car;
import eu.opends.main.Simulator;

public class HudDisplay {
	private static SimulationBasics sim; 
	
	private static boolean keyOn=false;
	private static Node hudGui;
	private static BitmapText currentSpeedText;
	private static Picture fuelLackSign, navigatorSign;
	private static BitmapText ko_text;
	
	public static void init(Simulator simulator)
	{
		sim = simulator;
		hudGui = sim.getGuiNode();
		BitmapFont guiFont = sim.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
		
		currentSpeedText = new BitmapText(guiFont,false);
		currentSpeedText.setName("currentSpeedText");
		currentSpeedText.setText("");
		currentSpeedText.setSize(guiFont.getCharSet().getRenderedSize()+20);
		currentSpeedText.setColor(ColorRGBA.Yellow);
		currentSpeedText.setLocalTranslation(sim.getSettings().getWidth()/2-100,sim.getSettings().getHeight()/2-100,0);
		
		navigatorSign = new Picture("straight");
		navigatorSign.setImage(sim.getAssetManager(), "Textures/Navigation/crossing_straight.png", true);
		navigatorSign.setWidth(120);
		navigatorSign.setHeight(70);
		navigatorSign.setPosition(sim.getSettings().getWidth()/2+50, sim.getSettings().getHeight()/2-100);
		
	}
	
	// real-time update of hudDisplay
	public static void update()
	{
		Car car = ((Simulator)sim).getCar();
		updateCurrentSpeedText(car);
		
	}
	
	private static void updateCurrentSpeedText(Car car)
	{
		String carSpeed;
		
		carSpeed = (int)car.getCurrentSpeedKmh() + "Km/h";
		
		currentSpeedText.setText(carSpeed);
		updateNavigatorSign();
		
	}
	
	private static void updateNavigatorSign()
	{
		if(navigatorSign.getName().equals("right"))
		{
			navigatorSign.setImage(sim.getAssetManager(), "Textures/Navigation/crossing_right.png", true);
		}
		else if(navigatorSign.getName().equals("left"))
		{
			navigatorSign.setImage(sim.getAssetManager(), "Textures/Navigation/crossing_left.png", true);
		}
		else if(navigatorSign.getName().equals("straight"))
		{
			navigatorSign.setImage(sim.getAssetManager(), "Textures/Navigation/crossing_straight.png", true);
		}
		
	}
	
	private static void CurrentSpeedTextAttach()
	{
		hudGui.attachChild(currentSpeedText);
	}
	
	private static void CurrentSpeedTextDetach()
	{
		hudGui.detachChild(currentSpeedText);
	}
	
	private static void NavigatorSignAttach()
	{
		hudGui.attachChild(navigatorSign);
	}
	
	private static void NavigatorSignDetach()
	{
		hudGui.detachChild(navigatorSign);
	}
	
	public static void hudAttach()
	{
		CurrentSpeedTextAttach();
		NavigatorSignAttach();
	}
	
	public static void hudDetach()
	{
		CurrentSpeedTextDetach();
		NavigatorSignDetach();
	}
	
	public static void keyFlagSettting()
	{
		if(keyOn)
			keyOn=false;
		else
			keyOn=true;
	}
	
	public static boolean getKeyFlag()
	{
		return keyOn;
	}
	
	public static void setNaviType(String naviType)
	{
		navigatorSign.setName(naviType);
	}
	
	
	
	
	

}
