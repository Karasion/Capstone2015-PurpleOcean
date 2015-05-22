/*
*  This file is part of OpenDS (Open Source Driving Simulator).
*  Copyright (C) 2014 Rafael Math
*
*  OpenDS is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*
*  OpenDS is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with OpenDS. If not, see <http://www.gnu.org/licenses/>.
*/

package eu.opends.input;

import java.util.ArrayList;

import eu.opends.basics.SimulationBasics;

/**
 * 
 * @author Rafael Math
 */
public class KeyMapping
{
	// common keys
	public static KeyMapping TOGGLE_KEYMAPPING = new KeyMapping("toggle_keymapping", "show/hide key mapping", new String[] {"KEY_F1"});
	public static KeyMapping SHUTDOWN = new KeyMapping("shutdown", "exit simulator", new String[] {"KEY_ESCAPE"});
	public static KeyMapping TOGGLE_CAM = new KeyMapping("toggle_cam", "change camera view", new String[] {"KEY_V"});
	public static KeyMapping TOGGLE_MESSAGEBOX = new KeyMapping("toggle_messagebox", "show/hide messages", new String[] {"KEY_M"});
	
	// simulator keys
	public static KeyMapping ACCELERATE = new KeyMapping("accelerate", "accelerate", new String[] {"KEY_UP"});
	public static KeyMapping ACCELERATE_BACK = new KeyMapping("accelerate_back", "accelerate back", new String[] {"KEY_DOWN"});
	public static KeyMapping STEER_LEFT = new KeyMapping("steer_left", "steer left", new String[] {"KEY_LEFT"});
	public static KeyMapping STEER_RIGHT = new KeyMapping("steer_right", "steer right", new String[] {"KEY_RIGHT"});	
	public static KeyMapping BRAKE = new KeyMapping("brake", "brake", new String[] {"KEY_SPACE"});
	public static KeyMapping TOGGLE_WIREFRAME = new KeyMapping("toggle_wireframe", "wire frame on/off", new String[] {"KEY_W"});
	public static KeyMapping TOGGLE_ENGINE = new KeyMapping("toggle_engine", "engine on/off", new String[] {"KEY_E"});
	public static KeyMapping TOGGLE_PAUSE = new KeyMapping("toggle_pause", "pause/resume", new String[] {"KEY_P"});
	public static KeyMapping START_PAUSE = new KeyMapping("start_pause", "pause simulation", new String[] {"KEY_O"});
	public static KeyMapping STOP_PAUSE = new KeyMapping("stop_pause", "resume simulation", new String[] {"KEY_I"});
	public static KeyMapping TOGGLE_TRAFFICLIGHTMODE = new KeyMapping("toggle_trafficlightmode", "change traffic light mode", new String[] {"KEY_A"});
	public static KeyMapping TOGGLE_RECORD_DATA = new KeyMapping("toggle_record_data", "start/stop recording", new String[] {"KEY_S"});
	public static KeyMapping TOGGLE_TOPVIEW = new KeyMapping("toggle_topview", "top view on/off", new String[] {"KEY_U"});
	public static KeyMapping TOGGLE_BACKMIRROR = new KeyMapping("toggle_backmirror", "back view mirror", new String[] {"KEY_BACK"});
	public static KeyMapping SHIFT_UP = new KeyMapping("shift_up", "shift up", new String[] {"KEY_PGUP"});
	public static KeyMapping SHIFT_DOWN = new KeyMapping("shift_down", "shift down", new String[] {"KEY_PGDN","BUTTON_1"});
	public static KeyMapping TOGGLE_AUTOMATIC = new KeyMapping("toggle_automatic", "automatic/manual transmission", new String[] {"KEY_END","BUTTON_0"});
	public static KeyMapping HORN = new KeyMapping("horn", "horn", new String[] {"KEY_H"});
	public static KeyMapping TOGGLE_MIN_SPEED = new KeyMapping("toggle_min_speed", "maintain min speed on/off", new String[] {"KEY_D"});
	public static KeyMapping CRUISE_CONTROL = new KeyMapping("cruise_control", "cruise control on/off", new String[] {"KEY_X"});
	public static KeyMapping RESET_CAR = new KeyMapping("reset_car", "reset car", new String[] {"KEY_R"});
	public static KeyMapping RESET_CAR_POS1 = new KeyMapping("reset_car_pos1", "reset car (pos 1)", new String[] {"KEY_1"});
	public static KeyMapping RESET_CAR_POS2 = new KeyMapping("reset_car_pos2", "reset car (pos 2)", new String[] {"KEY_2"});
	public static KeyMapping RESET_CAR_POS3 = new KeyMapping("reset_car_pos3", "reset car (pos 3)", new String[] {"KEY_3"});
	public static KeyMapping RESET_CAR_POS4 = new KeyMapping("reset_car_pos4", "reset car (pos 4)", new String[] {"KEY_4"});
	public static KeyMapping RESET_CAR_POS5 = new KeyMapping("reset_car_pos5", "reset car (pos 5)", new String[] {"KEY_5"});
	public static KeyMapping RESET_CAR_POS6 = new KeyMapping("reset_car_pos6", "reset car (pos 6)", new String[] {"KEY_6"});
	public static KeyMapping RESET_CAR_POS7 = new KeyMapping("reset_car_pos7", "reset car (pos 7)", new String[] {"KEY_7"});
	public static KeyMapping RESET_CAR_POS8 = new KeyMapping("reset_car_pos8", "reset car (pos 8)", new String[] {"KEY_8"});
	public static KeyMapping RESET_CAR_POS9 = new KeyMapping("reset_car_pos9", "reset car (pos 9)", new String[] {"KEY_9"});
	public static KeyMapping RESET_CAR_POS10 = new KeyMapping("reset_car_pos10", "reset car (pos 10)", new String[] {"KEY_0"});
	public static KeyMapping RESET_FUEL_CONSUMPTION = new KeyMapping("reset_fuel_consumption", "reset fuel consumption", new String[] {"KEY_T"});
	public static KeyMapping TOGGLE_STATS = new KeyMapping("toggle_stats", "toggle stats", new String[] {"KEY_F4"});
	public static KeyMapping TOGGLE_CINEMATIC = new KeyMapping("toggle_cinematics", "toggle camera flight", new String[] {"KEY_RETURN"});
	public static KeyMapping TOGGLE_HEADLIGHT = new KeyMapping("toggle_headlight", "toggle head light", new String[] {"KEY_L"});
	public static KeyMapping REPORT_LANDMARK = new KeyMapping("report_landmark", "report landmark", new String[] {"KEY_SPACE"});
	public static KeyMapping TOGGLE_PHYSICS_DEBUG = new KeyMapping("toggle_physics_debug", "toggle physics debug", new String[] {"KEY_F6"});
	public static KeyMapping CLOSE_INSTRUCTION_SCREEN = new KeyMapping("close_instruction_screen", "close instruction screen", new String[] {/*"KEY_F7"*/"KEY_F5"});
	public static KeyMapping REPORT_REACTION = new KeyMapping("report_reaction", "report reaction", new String[] {"KEY_G", "BUTTON_7"});
	public static KeyMapping OBJECT_ROTATE_LEFT_FAST = new KeyMapping("rotate_object_left_fast", "fast rotate object left", new String[] {"KEY_F7"});
	public static KeyMapping OBJECT_ROTATE_RIGHT_FAST = new KeyMapping("rotate_object_right_fast", "fast rotate object right", new String[] {"KEY_F8"});
	public static KeyMapping OBJECT_ROTATE_LEFT = new KeyMapping("rotate_object_left", "rotate object left", new String[] {"KEY_F9"});
	public static KeyMapping OBJECT_ROTATE_RIGHT = new KeyMapping("rotate_object_right", "rotate object right", new String[] {"KEY_F10"});
	public static KeyMapping OBJECT_SET = new KeyMapping("set_object", "set object", new String[] {"KEY_F11"});
	public static KeyMapping OBJECT_TOGGLE = new KeyMapping("toggle_object", "toggle object", new String[] {"KEY_F12"});
	public static KeyMapping TURN_LEFT = new KeyMapping("turn_left", "flash left turn signal", new String[] {"KEY_J"});
	public static KeyMapping TURN_RIGHT = new KeyMapping("turn_right", "flash right turn signal", new String[] {"KEY_K"});
	public static KeyMapping HAZARD_LIGHTS = new KeyMapping("hazard_lights", "flash hazard lights", new String[] {"KEY_F"});
	
	// TODO Insert HUD keys with G-HUB
	public static KeyMapping HUD_DISPLAY = new KeyMapping("hud_display", "hud on/off", new String[] {"KEY_B"});
	public static KeyMapping LEFT_KEY = new KeyMapping("ghub_left", "move left to menu cursor ", new String[] {"KEY_F2"});
	public static KeyMapping RIGHT_KEY = new KeyMapping("ghub_right", "move right to menu cursor ", new String[] {"KEY_F3"});
	public static KeyMapping PUSH_KEY = new KeyMapping("ghub_push", "menu select ", new String[] {"KEY_N"});
	public static KeyMapping DOWN_KEY = new KeyMapping("ghub_down", "menu select ", new String[] {"KEY_Z"});
	public static KeyMapping UP_KEY = new KeyMapping("ghub_up", "menu select ", new String[] {"KEY_C"});
	
	// TODO Insert HUD tool keys
	public static KeyMapping HUD_TOOL = new KeyMapping("hud_tool", "hud tool on/off", new String[] {"KEY_Q"});
	public static KeyMapping HUD_TOOL_KEY = new KeyMapping("hud_tool_key", "hud element delete", new String[] {"KEY_DELETE"});
	
	// analyzer keys
	public static KeyMapping GOTO_NEXT_DATAPOINT = new KeyMapping("goto_next_datapoint", "next data point", new String[] {"KEY_UP"});
	public static KeyMapping GOTO_PREVIOUS_DATAPOINT = new KeyMapping("goto_previous_datapoint", "previous data point", new String[] {"KEY_DOWN"});
	public static KeyMapping GO_FORWARD = new KeyMapping("go_forward", "move forwards", new String[] {"KEY_RIGHT"});
	public static KeyMapping GO_BACKWARD = new KeyMapping("go_backward", "move backwards", new String[] {"KEY_LEFT"});
	public static KeyMapping TOGGLE_POINTS = new KeyMapping("toggle_points", "show points", new String[] {"KEY_1"});
	public static KeyMapping TOGGLE_LINE = new KeyMapping("toggle_line", "show line", new String[] {"KEY_2"});
	public static KeyMapping TOGGLE_CONE = new KeyMapping("toggle_cone", "show cone", new String[] {"KEY_3"});
	public static KeyMapping TOGGLE_REPLAY = new KeyMapping("toggle_replay", "start/stop replay", new String[] {"KEY_RETURN"});
	
	
	// look up table of joystick buttons
	/*
	inputManager.addMapping(KeyMapping.TOGGLE_AUTOMATIC.getID(), new JoyButtonTrigger(0,1));
	inputManager.addMapping(KeyMapping.TOGGLE_CAM.getID(), new JoyButtonTrigger(0,3));
	inputManager.addMapping(KeyMapping.REPORT_LANDMARK.getID(), new JoyButtonTrigger(0,7));
	inputManager.addMapping(KeyMapping.SHIFT_DOWN.getID(), new JoyButtonTrigger(0,8));
	inputManager.addMapping(KeyMapping.SHIFT_UP.getID(), new JoyButtonTrigger(0,9));
	inputManager.addMapping(KeyMapping.CLOSE_INSTRUCTION_SCREEN.getID(), new JoyButtonTrigger(0,14));
	inputManager.addListener(simulatorActionListener, KeyMapping.TOGGLE_CAM.getID());
	 */
	
	public static ArrayList<KeyMapping> getSimulatorActionKeyMappingList()
	{
		ArrayList<KeyMapping> keyMappingList = new ArrayList<KeyMapping>();
		
		// specify order of key mapping list (if available)
		keyMappingList.add(KeyMapping.TOGGLE_KEYMAPPING);
		keyMappingList.add(KeyMapping.SHUTDOWN);
		keyMappingList.add(KeyMapping.ACCELERATE);
		keyMappingList.add(KeyMapping.ACCELERATE_BACK);
		keyMappingList.add(KeyMapping.STEER_LEFT);
		keyMappingList.add(KeyMapping.STEER_RIGHT);
		keyMappingList.add(KeyMapping.BRAKE);
		keyMappingList.add(KeyMapping.TOGGLE_CAM);
		keyMappingList.add(KeyMapping.TOGGLE_WIREFRAME);
		keyMappingList.add(KeyMapping.TOGGLE_ENGINE);		
		keyMappingList.add(KeyMapping.TOGGLE_PAUSE);
		keyMappingList.add(KeyMapping.START_PAUSE);
		keyMappingList.add(KeyMapping.STOP_PAUSE);
		keyMappingList.add(KeyMapping.TOGGLE_TRAFFICLIGHTMODE);
		keyMappingList.add(KeyMapping.TOGGLE_MESSAGEBOX);
		keyMappingList.add(KeyMapping.TOGGLE_RECORD_DATA);
		keyMappingList.add(KeyMapping.TOGGLE_TOPVIEW);
		keyMappingList.add(KeyMapping.TOGGLE_BACKMIRROR);
		keyMappingList.add(KeyMapping.SHIFT_UP);
		keyMappingList.add(KeyMapping.SHIFT_DOWN);
		keyMappingList.add(KeyMapping.TOGGLE_AUTOMATIC);
		keyMappingList.add(KeyMapping.HORN);
		keyMappingList.add(KeyMapping.TOGGLE_MIN_SPEED);
		keyMappingList.add(KeyMapping.CRUISE_CONTROL);
		keyMappingList.add(KeyMapping.RESET_CAR);
		keyMappingList.add(KeyMapping.RESET_CAR_POS1);
		keyMappingList.add(KeyMapping.RESET_CAR_POS2);
		keyMappingList.add(KeyMapping.RESET_CAR_POS3);
		keyMappingList.add(KeyMapping.RESET_CAR_POS4);
		keyMappingList.add(KeyMapping.RESET_CAR_POS5);
		keyMappingList.add(KeyMapping.RESET_CAR_POS6);
		keyMappingList.add(KeyMapping.RESET_CAR_POS7);
		keyMappingList.add(KeyMapping.RESET_CAR_POS8);
		keyMappingList.add(KeyMapping.RESET_CAR_POS9);
		keyMappingList.add(KeyMapping.RESET_CAR_POS10);
		keyMappingList.add(KeyMapping.RESET_FUEL_CONSUMPTION);
		keyMappingList.add(KeyMapping.TOGGLE_STATS);
		keyMappingList.add(KeyMapping.TOGGLE_CINEMATIC);
		keyMappingList.add(KeyMapping.TOGGLE_HEADLIGHT);
		keyMappingList.add(KeyMapping.REPORT_LANDMARK);
		keyMappingList.add(KeyMapping.TOGGLE_PHYSICS_DEBUG);
		keyMappingList.add(KeyMapping.CLOSE_INSTRUCTION_SCREEN);
		keyMappingList.add(KeyMapping.REPORT_REACTION);
		keyMappingList.add(KeyMapping.OBJECT_ROTATE_LEFT_FAST);
		keyMappingList.add(KeyMapping.OBJECT_ROTATE_RIGHT_FAST);
		keyMappingList.add(KeyMapping.OBJECT_ROTATE_LEFT);
		keyMappingList.add(KeyMapping.OBJECT_ROTATE_RIGHT);
		keyMappingList.add(KeyMapping.OBJECT_SET);
		keyMappingList.add(KeyMapping.OBJECT_TOGGLE);
		keyMappingList.add(KeyMapping.TURN_LEFT);
		keyMappingList.add(KeyMapping.TURN_RIGHT);
		keyMappingList.add(KeyMapping.HAZARD_LIGHTS);
		// TODO Insert HUD key with G-HUB
		keyMappingList.add(KeyMapping.HUD_DISPLAY);
		keyMappingList.add(KeyMapping.LEFT_KEY);
		keyMappingList.add(KeyMapping.RIGHT_KEY);
		keyMappingList.add(KeyMapping.PUSH_KEY);
		keyMappingList.add(KeyMapping.UP_KEY);
		keyMappingList.add(KeyMapping.DOWN_KEY);
		keyMappingList.add(KeyMapping.HUD_TOOL);
		keyMappingList.add(KeyMapping.HUD_TOOL_KEY);
		
		SimulationBasics.getDrivingTask().getSettingsLoader().lookUpKeyMappings(keyMappingList);
		
		return keyMappingList;
	}
	
	
	public static ArrayList<KeyMapping> getSimulatorAnalogKeyMappingList() 
	{
		ArrayList<KeyMapping> keyMappingList = new ArrayList<KeyMapping>();

		// specify order of key mapping list  (if available)
		// <no mapping yet>
		
		SimulationBasics.getDrivingTask().getSettingsLoader().lookUpKeyMappings(keyMappingList);
		
		return keyMappingList;
	}
	
	
	public static ArrayList<KeyMapping> getDriveAnalyzerActionKeyMappingList() 
	{
		ArrayList<KeyMapping> keyMappingList = new ArrayList<KeyMapping>();

		// specify order of key mapping list  (if available)
		keyMappingList.add(KeyMapping.TOGGLE_KEYMAPPING);
		keyMappingList.add(KeyMapping.SHUTDOWN);
		keyMappingList.add(KeyMapping.TOGGLE_CAM);
		keyMappingList.add(KeyMapping.TOGGLE_POINTS);
		keyMappingList.add(KeyMapping.TOGGLE_LINE);
		keyMappingList.add(KeyMapping.TOGGLE_CONE);
		keyMappingList.add(KeyMapping.GOTO_NEXT_DATAPOINT);
		keyMappingList.add(KeyMapping.GOTO_PREVIOUS_DATAPOINT);
		keyMappingList.add(KeyMapping.TOGGLE_MESSAGEBOX);
		keyMappingList.add(KeyMapping.TOGGLE_REPLAY);
		
		SimulationBasics.getDrivingTask().getSettingsLoader().lookUpKeyMappings(keyMappingList);
		
		return keyMappingList;
	}
	
	
	public static ArrayList<KeyMapping> getDriveAnalyzerAnalogKeyMappingList() 
	{
		ArrayList<KeyMapping> keyMappingList = new ArrayList<KeyMapping>();

		// specify order of key mapping list  (if available)
		keyMappingList.add(KeyMapping.GO_FORWARD);
		keyMappingList.add(KeyMapping.GO_BACKWARD);
		
		SimulationBasics.getDrivingTask().getSettingsLoader().lookUpKeyMappings(keyMappingList);
		
		return keyMappingList;
	}
	
	
	private String ID;
	private String description;
	private String[] defaultKeys;
	private String[] keys = null;
	
	public KeyMapping(String ID, String description, String[] defaultKeys)
	{
		this.ID = ID;
		this.description = description;
		this.defaultKeys = defaultKeys;
	}
		
	
	public String getID()
	{
		return ID;
	}
	
	
	public String toString()
	{
		return ID;
	}
	
	
	public String getDescription()
	{
		return description;
	}
	
	
	public String[] getDefaultKeys()
	{
		return defaultKeys;
	}
	
	
	public void setKeys(String[] keys)
	{
		this.keys = keys;
	}
	
	
	public String[] getKeys()
	{
		if(keys != null)
			return keys;
		else
			return defaultKeys;
	}

}
