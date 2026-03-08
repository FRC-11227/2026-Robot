package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkClosedLoopController;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.CAN;

public class IntakeSubsystem extends SubsystemBase {
    private final SparkMax intakeDeployMotor;
    private final SparkMax intakeRollingMotor;
    private SparkClosedLoopController deployIntakeController;
    private RelativeEncoder deployEncoder;
    private Boolean isDeployed;
    
    /** Create a new IntakeSubsystem */
    public IntakeSubsystem() {
        isDeployed = false;
        intakeDeployMotor = new SparkMax(CAN.INTAKE_DEPLOY, MotorType.kBrushless);
        intakeRollingMotor = new SparkMax(CAN.INTAKE_ROLLING, MotorType.kBrushless);
        deployIntakeController = intakeDeployMotor.getClosedLoopController();
        deployEncoder = intakeDeployMotor.getEncoder();

        SparkMaxConfig intakeDeployConfig = new SparkMaxConfig();
        SparkMaxConfig intakeRollingConfig = new SparkMaxConfig();

        intakeDeployConfig
            .smartCurrentLimit(IntakeConstants.INTAKE_DEPLOY_CURRENT_LIMIT)
            .voltageCompensation(12);
        intakeDeployConfig.closedLoop
            .p(IntakeConstants.deploy_kP)
            .i(IntakeConstants.deploy_kI)
            .d(IntakeConstants.deploy_kD);
        intakeRollingConfig
            .smartCurrentLimit(IntakeConstants.INTAKE_ROLLING_CURRENT_LIMIT)
            .voltageCompensation(12);

        intakeDeployMotor.configure(intakeDeployConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        intakeRollingMotor.configure(intakeRollingConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /**
     *  Deploy intake
     */
    public void deployIntake() {
        deployIntakeController.setSetpoint(IntakeConstants.DEFAULT_INTAKE_DEPLOY_SPEED, SparkMax.ControlType.kVelocity);
    }

    /**
     *  Retract intake
     */
    public void retractIntake() {
        deployIntakeController.setSetpoint(-IntakeConstants.DEFAULT_INTAKE_DEPLOY_SPEED, SparkMax.ControlType.kVelocity);
    }

    public void stopIntake() {
        intakeDeployMotor.set(0);
    }

    public void setRollers() {
        intakeRollingMotor.set(IntakeConstants.DEFAULT_INTAKE_ROLLER_SPEED);
    }

    public void stopRollers() {
        intakeRollingMotor.set(0);
    }

    /**
     * Command to run the deployIntake() method
     */
    public Command pushPullIntake() {
        if (isDeployed) {
            isDeployed = false;
            return this.runEnd(() -> retractIntake(), () -> stopIntake());
        }
        isDeployed = true;
        return this.runEnd(() -> {
            deployIntake();
            Timer.delay(1);
        }, () -> stopIntake());
    }

    public Command roll() {
        return this.runEnd(() -> setRollers(), () -> stopRollers());
    }
 

    /**
     * Sequence to shoot the ball. Runs the spinUpShooter() method for SPIN_UP_SECONDS,
     * then runs the shoot() method until the command is cancelled. When the command is cancelled,
     * runs the stop() command
     */
    // public Command shootSequence() {
    //     return spinUpCommand()
    //         .withTimeout(IntakeConstants.SPIN_UP_SECONDS)
    //         .andThen(shootCommand())
    //         .finallyDo(() -> stop());
    // }
}
