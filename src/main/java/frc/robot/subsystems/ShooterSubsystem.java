package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    private final CANBus kCanivoreBus = new CANBus("theGoose");
    final TalonFX m_beltMotor = new TalonFX(14, kCanivoreBus);
    final TalonFX m_flywheelMotor = new TalonFX(15, kCanivoreBus);

    final DutyCycleOut m_beltRequest = new DutyCycleOut(0.0);
    final DutyCycleOut m_flywheelRequest = new DutyCycleOut(0.0);

    public Command spinFlywheel(DoubleSupplier speed) {
        return this.runEnd(
            () -> {
                m_flywheelMotor.setControl(m_flywheelRequest.withOutput(speed.getAsDouble()));
                m_beltMotor.setControl(m_beltRequest.withOutput(speed.getAsDouble() / 6));
            },
            () -> {
                m_flywheelMotor.stopMotor();
                m_beltMotor.stopMotor();
            });
    }
}
