package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.Follower;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.BallConstants;
import frc.robot.Constants.CANConstants;

public class BallSubsystem extends SubsystemBase {
    private final SparkMax intakeDeployMotor;
    private final SparkMax hopperDeployMotor;
    private final SparkMax intakeRollingMotor;
    private final SparkMax feederMotorLeft;
    private final SparkMax feederMotorRight;
    private final TalonFX shooterLeftLeader;
    private final TalonFX shooterLeftFollower;
    private final TalonFX shooterRightLeader;
    private final TalonFX shooterRightFollower;
    
    /** Create a new BallSubsystem */
    public BallSubsystem() {
        intakeDeployMotor = new SparkMax(CANConstants.INTAKE_DEPLOY, MotorType.kBrushless);
        intakeRollingMotor = new SparkMax(CANConstants.INTAKE_ROLLING, MotorType.kBrushless);
        hopperDeployMotor = new SparkMax(CANConstants.HOPPER_DEPLOY, MotorType.kBrushless);
        feederMotorLeft = new SparkMax(CANConstants.FEEDER_LEFT, MotorType.kBrushless);
        feederMotorRight = new SparkMax(CANConstants.FEEDER_RIGHT, MotorType.kBrushless);
        shooterLeftLeader  = new TalonFX(CANConstants.SHOOTER_LEFT_LEADER);
        shooterLeftFollower = new TalonFX(CANConstants.SHOOTER_LEFT_FOLLOWER);
        shooterRightLeader  = new TalonFX(CANConstants.SHOOTER_RIGHT_LEADER);
        shooterRightFollower = new TalonFX(CANConstants.SHOOTER_RIGHT_FOLLOWER);
        

        SparkMaxConfig intakeDeployConfig = new SparkMaxConfig();
        SparkMaxConfig intakeRollingConfig = new SparkMaxConfig();
        SparkMaxConfig hopperDeployConfig = new SparkMaxConfig();
        SparkMaxConfig feederConfig = new SparkMaxConfig();
        TalonFXConfiguration shooterConfig = new TalonFXConfiguration();

        intakeDeployConfig
            .smartCurrentLimit(BallConstants.INTAKE_DEPLOY_CURRENT_LIMIT)
            .voltageCompensation(12);
        intakeRollingConfig
            .smartCurrentLimit(BallConstants.INTAKE_ROLLING_CURRENT_LIMIT)
            .voltageCompensation(12);
        hopperDeployConfig
            .smartCurrentLimit(BallConstants.HOPPER_DEPLOY_CURRENT_LIMIT)
            .voltageCompensation(12);
        feederConfig
            .smartCurrentLimit(BallConstants.FEEDER_CURRENT_LIMIT)
            .voltageCompensation(12);

        shooterConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        shooterConfig.CurrentLimits.SupplyCurrentLimit = BallConstants.SHOOTER_SUPPLY_CURRENT_LIMIT;
        shooterConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        shooterConfig.CurrentLimits.StatorCurrentLimit = BallConstants.SHOOTER_STATOR_CURRENT_LIMIT;
        shooterConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        shooterConfig.Voltage.PeakForwardVoltage = 12;
        shooterConfig.Voltage.PeakReverseVoltage = -12;
        shooterConfig.Slot0.kP = BallConstants.kShootP; 
        shooterConfig.Slot0.kI = BallConstants.kShootI;
        shooterConfig.Slot0.kD = BallConstants.kShootD;

        intakeDeployMotor.configure(intakeDeployConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        intakeRollingMotor.configure(intakeRollingConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        hopperDeployMotor.configure(hopperDeployConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        feederMotorLeft.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        feederMotorRight.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        shooterLeftLeader.getConfigurator().apply(shooterConfig);
        shooterRightLeader.getConfigurator().apply(shooterConfig);
        shooterLeftFollower.setControl(new Follower(shooterLeftLeader.getDeviceID(), MotorAlignmentValue.Aligned));
        shooterRightFollower.setControl(new Follower(shooterRightLeader.getDeviceID(), MotorAlignmentValue.Aligned));

        SmartDashboard.putNumber("Roller speed when intaking", BallConstants.DEFAULT_INTAKE_ROLLER_SPEED);
        SmartDashboard.putNumber("Intake deploy speed when deploying intake", BallConstants.DEFAULT_INTAKE_DEPLOY_SPEED);
        SmartDashboard.putNumber("Hopper deploy speed when deploying hopper", BallConstants.DEFAULT_HOPPER_DEPLOY_SPEED);
        SmartDashboard.putNumber("Feeder speed when feeding balls", BallConstants.DEFAULT_FEEDER_SPEED);
        SmartDashboard.putNumber("Shooter speed when shooting balls", BallConstants.DEFAULT_SHOOTER_SPEED);
    }

    /**
     *  Deploy hopper
     */
    public void deployHopper() {
        hopperDeployMotor.set(SmartDashboard.getNumber("Hopper deploy speed when deploying hopper", BallConstants.DEFAULT_HOPPER_DEPLOY_SPEED));
    }

    /**
     *  Retract hopper
     */
    public void retractHopper() {
        hopperDeployMotor.set(-1 * SmartDashboard.getNumber("Hopper deploy speed when deploying hopper", BallConstants.DEFAULT_HOPPER_DEPLOY_SPEED));
    }

    /**
     *  Deploy intake
     */
    public void deployIntake() {
        intakeDeployMotor.set(SmartDashboard.getNumber("Intake deploy speed when deploying intake", BallConstants.DEFAULT_INTAKE_DEPLOY_SPEED));
    }

    /**
     *  Retract intake
     */
    public void retractIntake() {
        intakeDeployMotor.set(-1 * SmartDashboard.getNumber("Intake deploy speed when deploying intake", BallConstants.DEFAULT_INTAKE_DEPLOY_SPEED));
    }

    /**
     *  Set rollers to intake balls
     */
    public void intake() {
        intakeRollingMotor.set(SmartDashboard.getNumber("Roller speed when intaking", BallConstants.DEFAULT_INTAKE_ROLLER_SPEED));
    }

    /** 
     * Set rollers to eject balls. Exactly backwards from intaking speeds
     */
    public void eject() {
        intakeRollingMotor.set(-1 * SmartDashboard.getNumber("Roller speed when intaking", BallConstants.DEFAULT_INTAKE_ROLLER_SPEED));
    }

    /**
     * Set motors to shoot and feed balls on left side. 
     */
    public void shootLeft() {
        feederMotorLeft.set(SmartDashboard.getNumber("Feeder speed when feeding balls", BallConstants.DEFAULT_FEEDER_SPEED));
        shooterLeftLeader.set(SmartDashboard.getNumber("Shooter speed when shooting balls", BallConstants.DEFAULT_SHOOTER_SPEED));
    }

    /**
     * Set motors to shoot and feed balls on right side. 
     */
    public void shootRight() {
        feederMotorRight.set(SmartDashboard.getNumber("Feeder speed when feeding balls", BallConstants.DEFAULT_FEEDER_SPEED));
        shooterRightLeader.set(SmartDashboard.getNumber("Shooter speed when shooting balls", BallConstants.DEFAULT_SHOOTER_SPEED));
    }

    /**
     * Command to run the shoot() method
     */
    public Command shootLeftCommand() {
        return this.run(() -> shootLeft());
    }

    /**
     * Stop all rollers in the system
     */
    public void stop() {
        intakeRollingMotor.set(0);
    }

    /**
     * Sequence to shoot the ball. Runs the spinUpShooter() method for SPIN_UP_SECONDS,
     * then runs the shoot() method until the command is cancelled. When the command is cancelled,
     * runs the stop() command
     */
    // public Command shootSequence() {
    //     return spinUpCommand()
    //         .withTimeout(BallConstants.SPIN_UP_SECONDS)
    //         .andThen(shootCommand())
    //         .finallyDo(() -> stop());
    // }
}
