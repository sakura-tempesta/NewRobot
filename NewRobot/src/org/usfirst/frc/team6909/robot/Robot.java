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
	private PWMTalonSRX arm_right, arm_left;
	private Spark spark_rightRear, spark_rightFront,
				   spark_leftRear , spark_leftFront;
	private SpeedControllerGroup drive_right,drive_left;
	private DifferentialDrive myDrive;
	private Spark lift;

	private double XofDrive, YofDrive;
	private double YofLift;
	private boolean is_leftTriggerOn,is_rightTriggerOn;


	private double NoReact=0.1;

	private double dataProcessing(double value) {

		/*try {
			Class<?> Class_box = XboxController.class;
			Object Object_box = box;
			Method Method_box = Class_box.getMethod(functionType, Hand.class);

			value = (double) box.invoke(Method_box, hand);

		}catch(ReflectiveOperationException e ) {

		}*/


		value *= Math.abs(value) > NoReact ? 1 : 0 ;

		return value;
	}; //不感帯未満なら0

	@Override
	public void robotInit() {
		box_drive = new XboxController(0);
		box_lift  = new XboxController(1);

		arm_right = new PWMTalonSRX(5);
		arm_left  = new PWMTalonSRX(6);

		spark_rightRear  = new Spark(3);
		spark_rightFront = new Spark(2);
		spark_leftRear   = new Spark(1);
		spark_leftFront  = new Spark(0);

		drive_right = new SpeedControllerGroup(spark_rightFront, spark_rightRear);
		drive_left  = new SpeedControllerGroup(spark_leftFront , spark_leftRear);

		myDrive = new DifferentialDrive(drive_right, drive_left);

		lift = new Spark(4);
	}

	@Override
	public void teleopPeriodic() {

	double arm_value = 0;

	{


		XofDrive = dataProcessing(box_drive.getX(Hand.kRight));

		YofDrive = dataProcessing(box_drive.getY(Hand.kLeft));

		YofLift = dataProcessing(box_lift.getY(Hand.kRight));


		is_leftTriggerOn  = box_lift.getTriggerAxis(Hand.kLeft)  > NoReact;
		is_rightTriggerOn = box_lift.getTriggerAxis(Hand.kRight) > NoReact;

		if(is_leftTriggerOn && is_rightTriggerOn) {
			arm_value = 0;
		}else {
			arm_value = is_leftTriggerOn ? -1.0 : arm_value ;
			arm_value = is_rightTriggerOn ? 1.0 : arm_value ;
	}

	}//データ処理

	{
		myDrive.arcadeDrive(YofDrive, -XofDrive);//直感と逆

		arm_left.set(arm_value);
		arm_right.set(-arm_value);

		lift.set(-YofLift);//直感と逆

	}//実行

	}

}
