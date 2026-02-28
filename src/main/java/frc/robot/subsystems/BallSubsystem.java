package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.BallConstants;
import frc.robot.Constants.CAN;

public class BallSubsystem extends SubsystemBase {
    private final SparkMax intakeDeployMotor;
    private final SparkMax hopperDeployMotor;
    private final SparkMax intakeRollingMotor;
    
    
    
    /** Create a new BallSubsystem */
    public BallSubsystem() {
        intakeDeployMotor = new SparkMax(CAN.INTAKE_DEPLOY, MotorType.kBrushless);
        intakeRollingMotor = new SparkMax(CAN.INTAKE_ROLLING, MotorType.kBrushless);
        hopperDeployMotor = new SparkMax(CAN.HOPPER_DEPLOY, MotorType.kBrushless);

        SparkMaxConfig intakeDeployConfig = new SparkMaxConfig();
        SparkMaxConfig intakeRollingConfig = new SparkMaxConfig();
        SparkMaxConfig hopperDeployConfig = new SparkMaxConfig();

        intakeDeployConfig
            .smartCurrentLimit(BallConstants.INTAKE_DEPLOY_CURRENT_LIMIT)
            .voltageCompensation(12);
        intakeRollingConfig
            .smartCurrentLimit(BallConstants.INTAKE_ROLLING_CURRENT_LIMIT)
            .voltageCompensation(12);
        hopperDeployConfig
            .smartCurrentLimit(BallConstants.HOPPER_DEPLOY_CURRENT_LIMIT)
            .voltageCompensation(12);

        intakeDeployMotor.configure(intakeDeployConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        intakeRollingMotor.configure(intakeRollingConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        hopperDeployMotor.configure(hopperDeployConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        SmartDashboard.putNumber("Roller speed when intaking", BallConstants.DEFAULT_INTAKE_ROLLER_SPEED);
        SmartDashboard.putNumber("Intake deploy speed when deploying intake", BallConstants.DEFAULT_INTAKE_DEPLOY_SPEED);
        SmartDashboard.putNumber("Hopper deploy speed when deploying hopper", BallConstants.DEFAULT_HOPPER_DEPLOY_SPEED);
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
        intakeRollingMotor.set(SmartDashboard.getNumber("Roller speed when intaking", BallConstants.DEFAULT_INTAKE_ROLLER_SPEED));
    }

    /**
     *  Retract intake
     */
    public void retractIntake() {
        intakeDeployMotor.set(-1 * SmartDashboard.getNumber("Intake deploy speed when deploying intake", BallConstants.DEFAULT_INTAKE_DEPLOY_SPEED));
        intakeRollingMotor.set(0);
    }

    /**
     * Stop all rollers in the system
     */
    public void stop() {
        intakeRollingMotor.set(0);
    }

    public Command setHopper() {
        return this.run(() -> deployHopper());
    }

    public Command unsetHopper() {
        return this.run(() -> retractHopper());
    }

    /**
     * Command to run the deployIntake() method
     */
    public Command setIntake() {
        return this.run(() -> deployIntake());
    }

    public Command stopIntaking() {
        return this.run(() -> stop());
    }

    public Command unsetIntake() {
        return this.run(() -> retractIntake());
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
