package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimbConstants;
import frc.robot.Constants.CANConstants;

public class ClimbSubsystem extends SubsystemBase {
    private final SparkMax climbMotor;
    private final TalonFX forkMotor;

    public ClimbSubsystem() {
        climbMotor = new SparkMax(CANConstants.CLIMB, MotorType.kBrushless);
        forkMotor = new TalonFX(CANConstants.FORK);

        SparkMaxConfig climbConfig = new SparkMaxConfig();
        TalonFXConfiguration forkConfig = new TalonFXConfiguration();

        climbConfig
            .smartCurrentLimit(ClimbConstants.CLIMB_CURRENT_LIMIT)
            .voltageCompensation(12);
        
        forkConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        forkConfig.CurrentLimits.SupplyCurrentLimit = ClimbConstants.FORK_SUPPLY_CURRENT_LIMIT;
        forkConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        forkConfig.CurrentLimits.StatorCurrentLimit = ClimbConstants.FORK_STATOR_CURRENT_LIMIT;
        forkConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        forkConfig.Voltage.PeakForwardVoltage = 12;
        forkConfig.Voltage.PeakReverseVoltage = -12;

        climbMotor.configure(climbConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        forkMotor.getConfigurator().apply(forkConfig);

        SmartDashboard.putNumber("Climber speed when climbing", ClimbConstants.DEFAULT_CLIMB_SPEED);
        SmartDashboard.putNumber("Fork speed when forking", ClimbConstants.DEFAULT_FORK_SPEED);
    }

    /**
     *  start climb motor
     */
    public void startClimb() {
        climbMotor.set(SmartDashboard.getNumber("Climber speed when climbing", ClimbConstants.DEFAULT_CLIMB_SPEED));
    }

    /**
     *  reverse climb motor
     */
    public void reverseClimb() {
        climbMotor.set(-1 * SmartDashboard.getNumber("Climber speed when climbing", ClimbConstants.DEFAULT_CLIMB_SPEED));
    }

    /**
     *  stop climb motor
     */
    public void stopClimb() {
        climbMotor.set(0);
    }

    /**
     *  push fork out
     */
    public void pushFork() {
        forkMotor.set(SmartDashboard.getNumber("Fork speed when forking", ClimbConstants.DEFAULT_FORK_SPEED));
    }

    /**
     *  pull fork in
     */
    public void pullFork() {
        forkMotor.set(-1 * SmartDashboard.getNumber("Fork speed when forking", ClimbConstants.DEFAULT_FORK_SPEED));
    }

    
}
