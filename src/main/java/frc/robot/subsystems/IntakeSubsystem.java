package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.CAN;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {
    private final CANBus kCanivoreBus = new CANBus("theGoose");

    final SparkMax intakeRollers = new SparkMax(CAN.intakeRollers, MotorType.kBrushless);
    final TalonFX intakeAngle = new TalonFX(CAN.intakeAngle, kCanivoreBus);

    final DutyCycleOut dutyCycleOutRequest = new DutyCycleOut(0);

    final Slot0Configs intakeAngleSlot0Configs = new Slot0Configs();

    public IntakeSubsystem() {
        intakeAngle.setNeutralMode(NeutralModeValue.Brake);
    }

    // private final SparkClosedLoopController intake_pidController;

    // public IntakeSubsystem() {
    //     intake_pidController = intakeRollers.getClosedLoopController();
    //     SparkMaxConfig config = new SparkMaxConfig();

    //     config.closedLoop
    //         .p(intakeConstants.intakeP)
    //         .i(intakeConstants.intakeI)
    //         .d(intakeConstants.intakeD); //Values are zero currently
        
    //     intakeRollers.configure(config, com.revrobotics.ResetMode.kResetSafeParameters, com.revrobotics.PersistMode.kNoPersistParameters);
    // }

    public void setRollerSpeed(double speed) {
        intakeRollers.set(speed);
    }

    public void stopRollers() {
        intakeRollers.stopMotor();
    }

    public void setIntakeSpeed(double speed) {
        intakeAngle.setControl(dutyCycleOutRequest.withOutput(speed));
    }

    public void stopIntake() {
        intakeAngle.stopMotor();
    }

    public Command spinRollers(double speed) {
        return this.run(() -> setRollerSpeed(speed))
            .finallyDo(() -> stopRollers());
    }

    public boolean intakeAtHardStop() {
        return intakeAngle.getStatorCurrent().getValueAsDouble() > IntakeConstants.intakeRotateCurrentLimit;
    }

    public Command intakeUp(double speed) {
        return this.run(() -> setIntakeSpeed(speed * IntakeConstants.intakeUpDirection))
            .until(this::intakeAtHardStop)
            .andThen(this.runOnce(() -> stopIntake()));
    }

    public Command intakeDown(double speed) {
        return this.run(() -> setIntakeSpeed(speed * IntakeConstants.intakeDownDirection))
            .until(this::intakeAtHardStop)
            .andThen(this.runOnce(() -> stopIntake()));
    }
}
