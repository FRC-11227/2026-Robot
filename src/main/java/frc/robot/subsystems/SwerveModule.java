package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.controls.VelocityDutyCycle;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class SwerveModule {
    private final TalonFX m_driveMotor;
    private final TalonFX m_steerMotor;
    
    public SwerveModule(int driveMotorID, int steerMotorID) {
        m_driveMotor = new TalonFX(driveMotorID);
        m_steerMotor = new TalonFX(steerMotorID);
        
        configureDriveMotor();
        configureSteerMotor();
    }
    
    private void configureDriveMotor() {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Voltage.PeakForwardVoltage = 12.0;
        config.Voltage.PeakReverseVoltage = -12.0;
        config.CurrentLimits.StatorCurrentLimit = 60; 
        config.CurrentLimits.StatorCurrentLimitEnable = true;
        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        
        m_driveMotor.getConfigurator().apply(config);
    }
    
    private void configureSteerMotor() {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Voltage.PeakForwardVoltage = 12.0;
        config.Voltage.PeakReverseVoltage = -12.0;
        config.CurrentLimits.StatorCurrentLimit = 60; 
        config.CurrentLimits.StatorCurrentLimitEnable = true;
        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        
        m_steerMotor.getConfigurator().apply(config);
    }

    public void setSpeedAngle(double rotationsPerSec, double direction) {
        m_steerMotor.setControl(new PositionDutyCycle(direction));
        m_driveMotor.setControl(new VelocityDutyCycle(rotationsPerSec));
    }
}