package frc.robot.subsystems;

<<<<<<< HEAD
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.CAN;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {
    private final CANBus kCanivoreBus = new CANBus("theGoose");

    final SparkMax intakeRollers = new SparkMax(CAN.intakeRollers, MotorType.kBrushless);
    final TalonFX intakeAngle = new TalonFX(CAN.intakeAngle, kCanivoreBus);

    final DutyCycleOut dutyCycleOutRequest = new DutyCycleOut(0);

    // Replaces PositionVoltage — Motion Magic handles the trapezoid profile internally
    final MotionMagicVoltage motionMagicRequest = new MotionMagicVoltage(0);

    public IntakeSubsystem() {
        TalonFXConfiguration config = new TalonFXConfiguration();

        // PID + feedforward gains (Slot 0)
        Slot0Configs slot0 = config.Slot0;
        slot0.kS = IntakeConstants.arm_kS;
        slot0.kV = IntakeConstants.arm_kV;
        slot0.kP = IntakeConstants.arm_kP;
        slot0.kI = IntakeConstants.arm_kI;
        slot0.kD = IntakeConstants.arm_kD;

        // Motion Magic profile constraints — add these to IntakeConstants
        MotionMagicConfigs mm = config.MotionMagic;
        mm.MotionMagicCruiseVelocity = IntakeConstants.armCruiseVelocity;   // rot/s
        mm.MotionMagicAcceleration   = IntakeConstants.armAcceleration;     // rot/s²
        mm.MotionMagicJerk           = IntakeConstants.armJerk;             // rot/s³ (0 = disabled)

        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        intakeAngle.getConfigurator().apply(config);

        SparkMaxConfig rollerConfig = new SparkMaxConfig();
        rollerConfig
            .voltageCompensation(12)
            .inverted(true);

        intakeRollers.configure(rollerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public double calculateJiggle() {
        double time = Timer.getFPGATimestamp();
        double frequency = IntakeConstants.jiggleFrequency;
        double amplitude  = IntakeConstants.jiggleAmplitude;
        double offset     = IntakeConstants.jiggleOffset;
        double num = amplitude * Math.sin(2 * Math.PI * frequency * time) + offset;
        SmartDashboard.putNumber("JiggleSetpoint", num);
        return num;
    }

    public void setRollerSpeed(double speed) {
        intakeRollers.set(speed);
    }

    public void stopRollers() {
        intakeRollers.stopMotor();
    }

    public void setIntakeAngleSpeed(double speed) {
        intakeAngle.setControl(dutyCycleOutRequest.withOutput(speed));
    }

    /** Sends a Motion Magic trapezoidal move to the target position (rotations). */
    public void setIntakePosition(double position) {
        intakeAngle.setControl(motionMagicRequest.withPosition(position));
    }

    public void stopIntakeAngle() {
        intakeAngle.stopMotor();
    }

    public Command spinRollers(double speed) {
        return this.run(() -> setRollerSpeed(speed))
            .finallyDo(() -> stopRollers());
    }

    public boolean intakeIsAtHardStop() {
        return intakeAngle.getStatorCurrent().getValueAsDouble() > IntakeConstants.intakeRotateCurrentLimit;
    }

    public Command intakeDown(double speed) {
        return this.runEnd(
            () -> setIntakePosition(IntakeConstants.armDown),
            () -> stopIntakeAngle()
        );
    }

    public Command intakeUp(double speed) {
        return this.run(() -> setIntakePosition(IntakeConstants.armUp));
    }

    public Command jiggleIntake() {
        return this.runEnd(
            () -> {
                setIntakePosition(calculateJiggle());
                setRollerSpeed(IntakeConstants.jiggleRollerSpeed);
            },
            () -> {
                stopIntakeAngle();
                stopRollers();
            }
        );
    }

    public Command intakeBalls() {
        return this.runEnd(
            () -> {
                setRollerSpeed(0.7);
                setIntakePosition(-0.01);
            },
            () -> {
                stopIntakeAngle();
                stopRollers();
            }
        );
    }
<<<<<<< HEAD
=======
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
>>>>>>> origin/Eric
}
=======
}
>>>>>>> origin/Mayiken
