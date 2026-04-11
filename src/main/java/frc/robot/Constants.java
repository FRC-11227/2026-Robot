// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import java.util.Map;
import java.util.function.DoubleSupplier;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import frc.robot.generated.TunerConstants;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
  }

  public static class CAN {
    // CANivore bus
    public static final int intakeAngle = 14;
    public static final int leftFlywheel1 = 16;
    public static final int leftFlywheel2 = 17;
    public static final int leftFLywheelFeeder = 18;

    public static final int rightFlywheel1 = 19;
    public static final int rightFlywheel2 = 20;
    public static final int rightFlywheelFeeder = 21;

    // RIO bus
    public static final int pdh = 1;
    public static final int intakeRollers = 2;
  }

  public static class DriveConstants {
    public static double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    public static double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
  }

  public static class IntakeConstants {
    //roller PID values
    public static final double roller_kV = 0.00018;
    public static final double roller_kP = 0.00018;
    public static final double roller_kI = 0;
    public static final double roller_kD = 0;
    public static final double roller_maxV = 12;
    public static final double roller_minV = 0;


    // TODO: Intake angle PID values should be stored here along with gear ratio
    public static final double arm_kS = 0.2;
    public static final double arm_kV = 0;
    public static final double arm_kP = 8;
    public static final double arm_kI = 0;
    public static final double arm_kD = 0;
    public static final double arm_kA = 0;
    public static final double arm_kG = 0.9;

    //public static final double armUp= 0.183195;
    //public static final double armDown = 0.181;
   
    public static final double armCruiseVelocity = 75;  // rot/s  — tune this
    public static final double armAcceleration = 52;  // rot/s² — tune this
    public static final double armJerk = 0.0;  // rot/s³ — 0 disables S-curve smoothing
    public static final double armGearRatio = 24.7;

    //measured using talon fx plotting PID - reference, and position
    //normalized for 0-90
    //arm sits a bit lower than hortizontal so -3 is set point.
    //the sensor to mech ratio is set with phoenix tuner
    //sensor to mech ratio of UHHHHH 24.6
    public static final double armUp= -6.6;
    public static final double armDown = -0.2;
    
    public static final double intakeRotateCurrentLimit = 65;
    public static final double intakeRotateSpeed = 0.15;
    public static final int intakeUpDirection = -1;
    public static final int intakeDownDirection = 1;

    public static final double intakingRollerSpeed = 0.8;

    //free speed 6000rpm assume 90% efficiency and run at 80% speed which is under instability threashold
    public static final double intakingRollerSpeedRPM = 5000;
    
    public static final double intakingPosition = -0.01;

    public static final double jiggleFrequency = 0.5;
    public static final double jiggleAmplitude = 1;
    public static final DoubleSupplier jiggleOffset = () -> 0.5;
    public static final double jiggleRollerSpeed = 0.5;
  }

  public static class ShooterConstants {
    public static final double flywheel_left1_kS = 2.7;
    public static final double flywheel_left2_kS = 2.4;
    public static final double flywheel_right1_kS = 3.8;
    public static final double flywheel_right2_kS = 3.2;

    public static final double flywheel_kV = 0.015;
    public static final double flywheel_kP = 14;
    public static final double flywheel_kI = 0;
    public static final double flywheel_kD = 0;

    public static final double feeder_kS = 24;
    public static final double feeder_kV = 0.05;
    public static final double feeder_kP = 8;
    public static final double feeder_kI = 0;
    public static final double feeder_kD = 0;

    public static final double feederAcceleration = 500;

    public static final double feederSetpointRPS = 60;
    public static final double passSpeed = 80;

    public static final double feederErrorGain = 5.0;

    public static final InterpolatingDoubleTreeMap lerpTableOld = InterpolatingDoubleTreeMap.ofEntries(
      Map.entry(1.57, 47.0),
      Map.entry(2.76, 55.0),
      Map.entry(3.0, 57.0),
      Map.entry(3.13, 60.0),
      Map.entry(3.8, 64.5),
      Map.entry(5.0, 73.0)
    );

    public static final InterpolatingDoubleTreeMap lerpTable = InterpolatingDoubleTreeMap.ofEntries(
      Map.entry(1.01,43.0),
      Map.entry(5.2, 75.0),
      Map.entry(3.7,68.0),
      Map.entry(3.0, 61.0),
      Map.entry(2.23, 55.0),
      Map.entry(1.8, 50.0)
    );
  }

  public static class RobotConstants {
    public static final double limelightHeightInches = 28.0;
    public static final double limelightDegrees = 10.0;    
  }

  public static class FieldConstants {
    public static AprilTagFieldLayout fieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);
    public static double hubTagHeight = fieldLayout.getTags().get(10).pose.getZ();
  }
}
