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
import frc.robot.Constants.ShootConstants;
import frc.robot.Constants.CANConstants;

public class ShooterSubsystem extends SubsystemBase {
    private final TalonFX shooterLeftLeader;
    private final TalonFX shooterLeftFollower;
    private final TalonFX shooterRightLeader;
    private final TalonFX shooterRightFollower;
    private final SparkMax feederMotorLeft;
    private final SparkMax feederMotorRight;

    public ShooterSubsystem() {
        shooterLeftLeader  = new TalonFX(CANConstants.SHOOTER_LEFT_LEADER);
        shooterLeftFollower = new TalonFX(CANConstants.SHOOTER_LEFT_FOLLOWER);
        shooterRightLeader  = new TalonFX(CANConstants.SHOOTER_RIGHT_LEADER);
        shooterRightFollower = new TalonFX(CANConstants.SHOOTER_RIGHT_FOLLOWER);
        feederMotorLeft = new SparkMax(CANConstants.FEEDER_LEFT, MotorType.kBrushless);
        feederMotorRight = new SparkMax(CANConstants.FEEDER_RIGHT, MotorType.kBrushless);

        SparkMaxConfig feederConfig = new SparkMaxConfig();
        TalonFXConfiguration shooterConfig = new TalonFXConfiguration();

        feederConfig
            .smartCurrentLimit(ShootConstants.FEEDER_CURRENT_LIMIT)
            .voltageCompensation(12);
        shooterConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        shooterConfig.CurrentLimits.SupplyCurrentLimit = ShootConstants.SHOOTER_SUPPLY_CURRENT_LIMIT;
        shooterConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        shooterConfig.CurrentLimits.StatorCurrentLimit = ShootConstants.SHOOTER_STATOR_CURRENT_LIMIT;
        shooterConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        shooterConfig.Voltage.PeakForwardVoltage = 12;
        shooterConfig.Voltage.PeakReverseVoltage = -12;
        shooterConfig.Slot0.kP = ShootConstants.kShootP; 
        shooterConfig.Slot0.kI = ShootConstants.kShootI;
        shooterConfig.Slot0.kD = ShootConstants.kShootD;

        feederMotorLeft.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        feederMotorRight.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        shooterLeftLeader.getConfigurator().apply(shooterConfig);
        shooterRightLeader.getConfigurator().apply(shooterConfig);
        shooterLeftFollower.setControl(new Follower(shooterLeftLeader.getDeviceID(), MotorAlignmentValue.Aligned));
        shooterRightFollower.setControl(new Follower(shooterRightLeader.getDeviceID(), MotorAlignmentValue.Aligned));

        SmartDashboard.putNumber("Feeder speed when feeding balls", ShootConstants.DEFAULT_FEEDER_SPEED);
        SmartDashboard.putNumber("Shooter speed when shooting balls", ShootConstants.DEFAULT_SHOOTER_SPEED);
    }

    /**
     * Set motors to shoot and feed balls on left side. 
     */
    public void shootLeft() {
        feederMotorLeft.set(SmartDashboard.getNumber("Feeder speed when feeding balls", ShootConstants.DEFAULT_FEEDER_SPEED));
        shooterLeftLeader.set(SmartDashboard.getNumber("Shooter speed when shooting balls", ShootConstants.DEFAULT_SHOOTER_SPEED));
    }

    /**
     * Set motors to shoot and feed balls on right side. 
     */
    public void shootRight() {
        feederMotorRight.set(SmartDashboard.getNumber("Feeder speed when feeding balls", ShootConstants.DEFAULT_FEEDER_SPEED));
        shooterRightLeader.set(SmartDashboard.getNumber("Shooter speed when shooting balls", ShootConstants.DEFAULT_SHOOTER_SPEED));
    }

    /**
     * Command to run the shootLeft() method
     */
    public Command shootLeftCommand() {
        return this.run(() -> shootLeft());
    }

    /**
     * Command to run the shootRight() method
     */
    public Command shootRightCommand() {
        return this.run(() -> shootRight());
    }
}
