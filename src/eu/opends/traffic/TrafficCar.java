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

package eu.opends.traffic;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
//import com.jme3.scene.shape.Sphere;

import eu.opends.car.Car;
import eu.opends.car.LightTexturesContainer.TurnSignalState;
import eu.opends.environment.TrafficLight;
import eu.opends.environment.TrafficLightCenter;
import eu.opends.environment.TrafficLight.TrafficLightState;
import eu.opends.hud.BSA.BSADumy;
import eu.opends.main.Simulator;

/**
 * 
 * @author Rafael Math
 */
public class TrafficCar extends Car
{
	private String name;
	private Geometry frontGeometry;
	private Geometry centerGeometry;
	private FollowBox followBox;
	private float minForwardSafetyDistance = 10;
	private float minLateralSafetyDistance = 1;
	private float overwriteSpeed = -1;

	
	public TrafficCar(Simulator sim, TrafficCarData trafficCarData)
	{
		this.sim = sim;
		
		// initial position and rotation not needed, as car will automatically be 
		// set to its starting way point with orientation towards next way point
		initialPosition = new Vector3f(0,0,0);
		initialRotation = new Quaternion();
		
		name = trafficCarData.getName();
		
		mass = trafficCarData.getMass();
		
		minSpeed = 0;
		maxSpeed = Float.POSITIVE_INFINITY;
		
		acceleration = trafficCarData.getAcceleration();
		accelerationForce = 0.30375f * acceleration * mass;
		
		decelerationBrake = trafficCarData.getDecelerationBrake();
		maxBrakeForce = 0.004375f * decelerationBrake * mass;
		
		decelerationFreeWheel = trafficCarData.getDecelerationFreeWheel();
		maxFreeWheelBrakeForce = 0.004375f * decelerationFreeWheel * mass;
		
		engineOn = trafficCarData.isEngineOn();
		showEngineStatusMessage(engineOn);
		
		modelPath = trafficCarData.getModelPath();
		
		init();
		
		setupReferencePoints();
		
		/*
		//---------------------------------
		// add bounding sphere to a traffic car which can be hit by the user-controlled car
		Sphere sphere = new Sphere(20, 20, 2.5f);
		Geometry boundingSphere = new Geometry(name + "_boundingSphere", sphere);
		Material boundingSphereMaterial = new Material(sim.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		boundingSphereMaterial.setColor("Color", ColorRGBA.Yellow);
		boundingSphere.setMaterial(boundingSphereMaterial);
		//boundingSphere.setCullHint(CullHint.Always);
		carNode.attachChild(boundingSphere);
		sim.getTriggerNode().attachChild(carNode);
		//---------------------------------
		*/
		
		followBox = new FollowBox(sim, this, trafficCarData.getFollowBoxSettings());
	}
	
	
	public String getName() 
	{
		return name;
	}
	
	
	public void setMinForwardSafetyDistance(float distance) 
	{
		minForwardSafetyDistance = distance;
	}
	
	
	public void setMinLateralSafetyDistance(float distance) 
	{
		minLateralSafetyDistance = distance;
	}
	
	
	public void setToWayPoint(String wayPointID) 
	{
		int index = followBox.getIndexOfWP(wayPointID);
		if(index != -1)
			followBox.setToWayPoint(index);
		else
			System.err.println("Invalid way point ID: " + wayPointID);
	}
	
	
	public void setToWayPoint(int index)
	{
		followBox.setToWayPoint(index);
	}
	
	
	private void setupReferencePoints() 
	{
		// add node representing position of front box
		Box frontBox = new Box(0.01f, 0.01f, 0.01f);
		frontGeometry = new Geometry("frontBox", frontBox);
        frontGeometry.setLocalTranslation(0, 0, -1);
		Material frontMaterial = new Material(sim.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		frontMaterial.setColor("Color", ColorRGBA.Red);
		frontGeometry.setMaterial(frontMaterial);
		Node frontNode = new Node();
		frontNode.attachChild(frontGeometry);
		frontNode.setCullHint(CullHint.Always);
		getCarNode().attachChild(frontNode);
		
		// add node representing position of center box
		Box centerBox = new Box(0.01f, 0.01f, 0.01f);
		centerGeometry = new Geometry("centerBox", centerBox);
		centerGeometry.setLocalTranslation(0, 0, 0);
		Material centerMaterial = new Material(sim.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		centerMaterial.setColor("Color", ColorRGBA.Green);
		centerGeometry.setMaterial(centerMaterial);
		Node centerNode = new Node();
		centerNode.attachChild(centerGeometry);
		centerNode.setCullHint(CullHint.Always);
		getCarNode().attachChild(centerNode);
	}


	public void update(ArrayList<TrafficCar> vehicleList) 
	{
		if(!sim.isPause())
		{
			// update steering
			Vector3f wayPoint = followBox.getPosition();
			steerTowardsPosition(wayPoint);
			
			// update speed
			updateSpeed(vehicleList);
			
			// update lights
			updateLightState();
		}
		
		// update movement of follow box according to vehicle's position
		Vector3f vehicleCenterPos = centerGeometry.getWorldTranslation();
		followBox.update(vehicleCenterPos);
	}
	
	
	private void steerTowardsPosition(Vector3f wayPoint) 
	{
		// get relative position of way point --> steering direction
		// -1: way point is located on the left side of the vehicle
		//  0: way point is located in driving direction 
		//  1: way point is located on the right side of the vehicle
		int steeringDirection = getRelativePosition(wayPoint);
		
		// get angle between driving direction and way point direction --> steering intensity
		// only consider 2D space (projection of WPs to xz-plane)
		float steeringAngle = getAngleBetweenDirections(frontGeometry.getWorldTranslation(), wayPoint, true);
		
		// compute steering intensity in percent
		//  0  degree =   0%
		//  45 degree =  50%
		//  90 degree = 100%
		// >90 degree = 100%
		float steeringIntensity = Math.max(Math.min(2*steeringAngle/FastMath.PI,1f),0f);
		
		// apply steering instruction
		steer(steeringDirection*steeringIntensity);
		
		//System.out.println(steeringDirection*steeringIntensity);
	}

	
	private int getRelativePosition(Vector3f wayPoint)
	{
		// get vehicles center point and point in driving direction
		Vector3f frontPosition = frontGeometry.getWorldTranslation();
		Vector3f centerPosition = centerGeometry.getWorldTranslation();
		
		// convert Vector3f to Point2D.Float, as needed for Line2D.Float
		Point2D.Float centerPoint = new Point2D.Float(centerPosition.getX(),centerPosition.getZ());
		Point2D.Float frontPoint = new Point2D.Float(frontPosition.getX(),frontPosition.getZ());
		
		// line in direction of driving
		Line2D.Float line = new Line2D.Float(centerPoint,frontPoint);
		
		// convert Vector3f to Point2D.Float
		Point2D point = new Point2D.Float(wayPoint.getX(),wayPoint.getZ());

		// check way point's relative position to the line
		if(line.relativeCCW(point) == -1)
		{
			// point on the left --> return -1
			return -1;
		}
		else if(line.relativeCCW(point) == 1)
		{
			// point on the right --> return 1
			return 1;
		}
		else
		{
			// point on line --> return 0
			return 0;
		}
	}
	

	private float getAngleBetweenDirections(Vector3f position1, Vector3f position2, boolean is2DSpace) 
	{
		// get vehicle's center
		Vector3f carCenterPos = centerGeometry.getWorldTranslation();
		
		// vector pointing from vehicle's center towards position 1
		Vector3f frontLine = position1.subtract(carCenterPos);
		if(is2DSpace)
			frontLine.setY(0);
		frontLine.normalizeLocal();
		
		// vector pointing from vehicle's center towards position 2
		Vector3f wayPointLine = position2.subtract(carCenterPos);
		if(is2DSpace)
			wayPointLine.setY(0);
		wayPointLine.normalizeLocal();
		
		// angle between both vectors
		return frontLine.angleBetween(wayPointLine);
	}
	
	
	private void updateSpeed(ArrayList<TrafficCar> vehicleList) 
	{
		float targetSpeed = getTargetSpeed();
		
		if(overwriteSpeed >= 0)
			targetSpeed = Math.min(targetSpeed, overwriteSpeed);
		
		// stop car in order to avoid collision with other traffic objects and driving car
		// also for red traffic lights
		if(obstaclesInTheWay(vehicleList))
		{
		  if(!(BSADumy.getDetectFlag() && !BSADumy.getBackFlag()))
		    targetSpeed = 0; 
		}
		
		float currentSpeed = getCurrentSpeedKmh();
		
		//System.out.print(name + ": " + targetSpeed + " *** " + currentSpeed);
		
		
		// set pedal positions
		if(currentSpeed < targetSpeed)
		{
			// too slow --> accelerate
			setGasPedalIntensity(-1);
			setBrakePedalPressIntensity(0);
			//System.out.println("gas");
			//System.out.print(" *** gas");
		}
		else if(currentSpeed > targetSpeed+1)
		{
			// too fast --> brake
			
			// currentSpeed >= targetSpeed+3 --> brake intensity: 100%
			// currentSpeed == targetSpeed+2 --> brake intensity:  50%
			// currentSpeed <= targetSpeed+1 --> brake intensity:   0%
			float brakeIntensity = (currentSpeed - targetSpeed - 1)/2.0f;
			brakeIntensity = Math.max(Math.min(brakeIntensity, 1.0f), 0.0f);
			
			// formerly use
			//brakeIntensity = 1.0f;
			
			setBrakePedalPressIntensity(brakeIntensity);
			setGasPedalIntensity(0);
			//System.out.println("brake: " + brakeIntensity);
			//System.out.print(" *** brake");
		}
		else
		{
			// else release pedals
			setGasPedalIntensity(0);
			setBrakePedalPressIntensity(0);
			//System.out.print(" *** free");
		}
		
		
		
		// accelerate
		if(engineOn)
			carControl.accelerate(gasPedalPressIntensity * accelerationForce);
		else
			carControl.accelerate(0);
		//System.out.print(" *** " + gasPedalPressIntensity * accelerationForce);
		
		// brake	
		float appliedBrakeForce = brakePedalPressIntensity * maxBrakeForce;
		float currentFriction = 0.2f * maxFreeWheelBrakeForce;
		carControl.brake(appliedBrakeForce + currentFriction);
		
		//System.out.print(" *** " + appliedBrakeForce + currentFriction);
		//System.out.println("");
	}


	public float getTargetSpeed() 
	{
		// maximum speed for current way point segment
		float regularSpeed = followBox.getSpeed();

		// reduced speed to reach next speed limit in time
		float reducedSpeed = followBox.getReducedSpeed();
		
		return Math.max(Math.min(regularSpeed, reducedSpeed),0);
	}
	
	
	/**
	 * Returns the signum of the speed change between this and the previous way point: 
	 * 0 if speed has not changed (or no previous way point available), 
	 * 1 if speed has been increased,
	 * -1 if speed has been decreased.
	 * 
	 * @return
	 * 		The signum of the speed change between this and the previous way point
	 */
	public int getSpeedChange()
	{
		Waypoint previousWP = followBox.getPreviousWayPoint();
		Waypoint currentWP = followBox.getCurrentWayPoint();
		
		if(previousWP == null)
			return 0;
		else
			return (int) Math.signum(currentWP.getSpeed() - previousWP.getSpeed());
	}


	private boolean obstaclesInTheWay(ArrayList<TrafficCar> vehicleList)
	{
		// check distance from driving car
		BSADumy.setUserCarFlag(true);
		if(obstacleTooClose(sim.getCar().getPosition()))
			return true;
		BSADumy.setUserCarFlag(false);

		// check distance from other traffic (except oneself)
		for(TrafficCar vehicle : vehicleList)
		{
			if(!vehicle.getName().equals(name))		
				if(obstacleTooClose(vehicle.getPosition()))
					return true;
		}
		
		// check if red traffic light ahead
		Waypoint nextWayPoint = followBox.getNextWayPoint();
		if(hasRedTrafficLight(nextWayPoint))
			if(obstacleTooClose(nextWayPoint.getPosition()))
				return true;
		
		return false;
	}


	private boolean obstacleTooClose(Vector3f obstaclePos)
	{
		float distanceToObstacle = obstaclePos.distance(getPosition());
		
		// angle between driving direction of traffic car and direction towards obstacle
		// (consider 3D space, because obstacle could be located on a bridge above traffic car)
		float angle = getAngleBetweenDirections(frontGeometry.getWorldTranslation(), obstaclePos, false);
		if(belowSafetyDistance(angle, distanceToObstacle))
			return true;

		// considering direction towards next way point (if available)
		Waypoint nextWP = followBox.getNextWayPoint();
		if(nextWP != null)
		{
			// angle between direction towards next WP and direction towards obstacle
			// (consider 3D space, because obstacle could be located on a bridge above traffic car)
			angle = getAngleBetweenDirections(nextWP.getPosition(), obstaclePos, false);
			if(belowSafetyDistance(angle, distanceToObstacle))
				return true;
		}
		return false;
	}
	
	// chage Im gisung
	// TODO add BSA dummy
	private boolean belowSafetyDistance(float angle, float distance) 
	{	
		float lateralDistance = distance * FastMath.sin(angle);
		float forwardDistance = distance * FastMath.cos(angle);
		
		//if(name.equals("car1"))
		//	System.out.println(lateralDistance + " *** " + forwardDistance);
		// TODO
		if(forwardDistance < /*Math.max(0.5f * getCurrentSpeedKmh(),*/ minForwardSafetyDistance/*)*/)
		{
			if((lateralDistance < minLateralSafetyDistance+1.25) && (forwardDistance > 0))
			{
				if(BSADumy.getUserCarFlag())
				{
					BSADumy.setBackFlag(false);
					BSADumy.setDetectFlag(true);
				}
				if((lateralDistance < minLateralSafetyDistance) && (forwardDistance > 0))
				{
					if(BSADumy.getUserCarFlag())
					{
						BSADumy.setBackFlag(true);
						BSADumy.setDetectFlag(true);
					}
					return true;
				}
			}
			else
				if(BSADumy.getUserCarFlag())
				{
//					System.out.println("detect false");
					BSADumy.setDetectFlag(false);
				}
		}
		else
			if(BSADumy.getUserCarFlag())
			{
//				System.out.println("detect false");
				BSADumy.setDetectFlag(false);
			}

		
		return false;
	}

	
	private boolean hasRedTrafficLight(Waypoint wayPoint)
	{
		if(wayPoint != null)
		{
			String trafficLightID = wayPoint.getTrafficLightID();
			
			TrafficLight trafficLight = TrafficLightCenter.getTrafficLightByName(trafficLightID);

			if(trafficLight != null &&
				 (
					trafficLight.getState() == TrafficLightState.RED ||
					trafficLight.getState() == TrafficLightState.YELLOW ||
					trafficLight.getState() == TrafficLightState.YELLOWRED
				 )
			  )
				return true;
		}
		return false;
	}


	private void updateLightState() 
	{
		// set head light intensity
		Float currentLightIntensity = followBox.getCurrentWayPoint().getHeadLightIntensity();
		if(currentLightIntensity != null)
			lightIntensity = Math.max(0, currentLightIntensity);			
		
		leftHeadLight.setColor(ColorRGBA.White.mult(lightIntensity));
        leftHeadLight.setPosition(carModel.getLeftLightPosition());
        leftHeadLight.setDirection(carModel.getLeftLightDirection());
        
        rightHeadLight.setColor(ColorRGBA.White.mult(lightIntensity));
        rightHeadLight.setPosition(carModel.getRightLightPosition());
        rightHeadLight.setDirection(carModel.getRightLightDirection());
        
        
        // set turn signal
		String currentTurnSignalString = followBox.getCurrentWayPoint().getTurnSignal();
		if(currentTurnSignalString != null && !currentTurnSignalString.isEmpty())
		{
			TurnSignalState currentTurnSignalState = TurnSignalState.valueOf(currentTurnSignalString.toUpperCase());
			
			if(getTurnSignal() != currentTurnSignalState)
				setTurnSignal(currentTurnSignalState);
		}
	}
	
	
	public void overwriteCurrentSpeed(float speed)
	{
		overwriteSpeed = speed;
	}


}
