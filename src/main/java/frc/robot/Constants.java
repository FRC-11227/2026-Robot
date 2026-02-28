// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
  }

  public static class CANConstants {
    // CAN IDs of all devices in system
    public static final int DRIVETRAIN_FRONT_LEFT = 1; // PLACEHOLDERS
    public static final int DRIVETRAIN_FRONT_RIGHT = 2;
    public static final int DRIVETRAIN_BACK_LEFT = 3;
    public static final int DRIVETRAIN_BACK_RIGHT = 4;
    public static final int STEER_FRONT_LEFT = 5; 
    public static final int STEER_FRONT_RIGHT = 6;
    public static final int STEER_BACK_LEFT = 7;
    public static final int STEER_BACK_RIGHT = 8;
    public static final int FEEDER_LEFT = 9;
    public static final int FEEDER_RIGHT = 10;
    public static final int SHOOTER_LEFT_LEADER = 11;
    public static final int SHOOTER_LEFT_FOLLOWER = 12;
    public static final int SHOOTER_RIGHT_LEADER = 13;
    public static final int SHOOTER_RIGHT_FOLLOWER = 14;
    public static final int HOPPER_DEPLOY = 15;
    public static final int INTAKE_DEPLOY = 16;
    public static final int INTAKE_ROLLING = 17;
    public static final int CLIMB = 18;
    public static final int FORK = 19;
  }

  public static class MotorConstants {
    public static final int CIM_CURRENT_LIMIT = 60;
  }

  public static class DriveConstants {
    // Current limit for drivetrain motors. 60A is a reasonable maximum to reduce
    // likelihood of tripping breakers or damaging CIM motors
    public static final int DRIVE_MOTOR_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;
    public static final double DRIVE_MOTOR_SPEED = 0;
    public static final double kTurnP = 0.01;
    public static final double kTurnI = 0;
    public static final double kTurnD = 0;

    public static final double kTurnToleranceDeg = 5;
    public static final double kTurnRateToleranceDegPerS = 10;
    public static final double kMaxTurnRateDegPerS = 100;
    public static final double kMaxTurnAccelerationDegPerSSquared = 300;

    public static final double ksVolts = 1;
    public static final double kvVoltSecondsPerDegree = 0.8;
    public static final double kaVoltSecondsSquaredPerDegree = 0.15;
  }

  public static class BallConstants {
    public static final int INTAKE_DEPLOY_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;
    public static final int INTAKE_ROLLING_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;
    public static final int HOPPER_DEPLOY_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;
    public static final int FEEDER_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;
    public static final int SHOOTER_SUPPLY_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;
    public static final int SHOOTER_STATOR_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;

    public static final double DEFAULT_INTAKE_ROLLER_SPEED = 0;
    public static final double DEFAULT_INTAKE_DEPLOY_SPEED = 0;
    public static final double DEFAULT_HOPPER_DEPLOY_SPEED = 0;
    public static final double DEFAULT_FEEDER_SPEED = 0;
    public static final double DEFAULT_SHOOTER_SPEED = 0;
    public static final double kShootP = 0.002;
    public static final double kShootI = 0;
    public static final double kShootD = 0;
    public static final double SPIN_UP_SECONDS = 1; // Will be replaced with PID in the future
  }

  public static class ClimbConstants {
    public static final int CLIMB_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;
    public static final int FORK_SUPPLY_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;
    public static final int FORK_STATOR_CURRENT_LIMIT = MotorConstants.CIM_CURRENT_LIMIT;
    public static final double DEFAULT_CLIMB_SPEED = 0;
    public static final double DEFAULT_FORK_SPEED = 0;
  }
}
