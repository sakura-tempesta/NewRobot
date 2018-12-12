/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6909.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private XboxController box_drive, box_lift;
	private PWMTalonSRX rightarm, leftarm;
	private Spark rightRear, rightFront,
				   leftRear , leftFront;
	private SpeedControllerGroup m_right,m_left;
	private DifferentialDrive myDrive;
	private Spark lift;

	private double XofDrive, YofDrive;
	private double YofLift;
	private boolean is_leftTriggerOn,is_rightTriggerOn;


	private double NoReact=0.1;

	@Override
	public void robotInit() {
		box_drive = new XboxController(0);
		box_lift  = new XboxController(1);

		rightarm = new PWMTalonSRX(5);
		leftarm  = new PWMTalonSRX(6);

		rightRear  = new Spark(3);
		rightFront = new Spark(2);
		leftRear   = new Spark(1);
		leftFront  = new Spark(0);

		m_right    = new SpeedControllerGroup(rightFront, rightRear);
		m_left     = new SpeedControllerGroup(leftFront , leftRear);

		myDrive = new DifferentialDrive(m_right,m_left);

		lift = new Spark(4);
	}

	@Override
	public void teleopPeriodic() {
	double armValue = 0;

	XofDrive = box_drive.getX(Hand.kRight);
	XofDrive *= Math.abs(XofDrive) > NoReact ? 1 : 0 ;

	YofDrive = box_drive.getY(Hand.kLeft);
	YofDrive *= Math.abs(YofDrive) > NoReact ? 1 : 0 ;

	YofLift = box_lift.getY(Hand.kRight);
	YofLift *= Math.abs(YofLift) > NoReact ? 1 : 0 ;

	is_leftTriggerOn  = box_lift.getTriggerAxis(Hand.kLeft)  > NoReact;
	is_rightTriggerOn = box_lift.getTriggerAxis(Hand.kRight) > NoReact;

	if(is_leftTriggerOn && is_rightTriggerOn) {
		armValue = 0;
	}else {
		armValue = is_leftTriggerOn ? -1.0 : armValue ;
		armValue = is_rightTriggerOn ? 1.0 : armValue ;
	}


	myDrive.arcadeDrive(YofDrive, -XofDrive);//直感と逆

	leftarm.set(armValue);
	rightarm.set(-armValue);

	lift.set(-YofLift);//直感と逆

	}
}
