package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
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
    final SparkClosedLoopController rollerLoopController = intakeRollers.getClosedLoopController();


    final TalonFX intakeAngle = new TalonFX(CAN.intakeAngle, kCanivoreBus);

    final VelocityTorqueCurrentFOC intakeVelocityTorqueRequest = new VelocityTorqueCurrentFOC(0).withSlot(0);

    final DutyCycleOut dutyCycleOutRequest = new DutyCycleOut(0);

    // Replaces PositionVoltage — Motion Magic handles the trapezoid profile internally
    final PositionVoltage positionVoltageOut = new PositionVoltage(0);
    final MotionMagicVoltage motionMagicRequest = new MotionMagicVoltage(0);

    public IntakeSubsystem() {
        //config for arm
        TalonFXConfiguration config = new TalonFXConfiguration();
        // PID + feedforward gains (Slot 0)
        Slot0Configs slot0 = config.Slot0;
        slot0.kS = IntakeConstants.arm_kS;
        slot0.kV = IntakeConstants.arm_kV;
        slot0.kP = IntakeConstants.arm_kP;
        slot0.kI = IntakeConstants.arm_kI;
        slot0.kD = IntakeConstants.arm_kD;
        intakeAngle.getConfigurator().apply(slot0);

        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        SparkMaxConfig rollerConfig = new SparkMaxConfig();
        rollerConfig
            .voltageCompensation(12)
            .smartCurrentLimit(80)
            .inverted(true);

        rollerConfig.closedLoop
            .p(IntakeConstants.roller_kP)
            .i(IntakeConstants.roller_kI)
            .d(IntakeConstants.roller_kD)
            .outputRange(IntakeConstants.roller_minV, IntakeConstants.roller_maxV)
            .feedForward
                .kS(IntakeConstants.roller_kS)
                .kV(IntakeConstants.roller_kV,ClosedLoopSlot.kSlot0);
    

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
        //rollerLoopController.setSetpoint(0.6, ControlType.kVelocity);
    }

    public void stopRollers() {
        intakeRollers.stopMotor();
    }

    public void setIntakeAngleSpeed(double speed) {
        intakeAngle.setControl(dutyCycleOutRequest.withOutput(speed));
    }

    /** Sends a Motion Magic trapezoidal move to the target position (rotations). */
    public void setIntakePosition(double position) {
        intakeAngle.setControl(positionVoltageOut.withPosition(position));
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
    
    public Command intakeUp(double speed) {
        return this.run(() -> setIntakeAngleSpeed(speed * IntakeConstants.intakeUpDirection))
            .until(this::intakeIsAtHardStop)
            .andThen(this.runOnce(() -> stopIntakeAngle()));
    }

    public Command intakeDown(double speed) {
        return this.run(() -> setIntakeAngleSpeed(speed * IntakeConstants.intakeDownDirection))
            .until(this::intakeIsAtHardStop)
            .andThen(this.runOnce(() -> stopIntakeAngle()).andThen(runOnce(() -> intakeAngle.setPosition(0))));
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
}