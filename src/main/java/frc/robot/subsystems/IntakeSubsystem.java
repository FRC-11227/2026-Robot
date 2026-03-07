package frc.robot.subsystems;
import java.io.ObjectInputFilter.Config;
import java.util.function.DoubleSupplier;

// import com.ctre.phoenix6.CANBus;
// import com.ctre.phoenix6.configs.Slot0Configs;
// import com.ctre.phoenix6.controls.DutyCycleOut;
// import com.ctre.phoenix6.controls.Follower;
// import com.ctre.phoenix6.controls.VelocityVoltage;
// import com.ctre.phoenix6.hardware.TalonFX;
// import com.ctre.phoenix6.signals.MotorAlignmentValue;
// Not needed (Highley likely)


import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveModule.ClosedLoopOutputType;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.CAN;
import frc.robot.Constants.ShooterConstants;
// Not needed (Mabye)
import frc.robot.Constants.intakeConstants;

import com.revrobotics.spark.SparkClosedLoopController;

public class IntakeSubsystem extends SubsystemBase{
    

    final SparkMax intake_motor = new SparkMax(CAN.intakeMotor, MotorType.kBrushless);
    final SparkMax intakeMotorAngle = new SparkMax(CAN.intakeMotorAngle, MotorType.kBrushless);
    private final SparkClosedLoopController intake_pidController;

    public IntakeSubsystem(){
    intake_pidController = intake_motor.getClosedLoopController();
    SparkMaxConfig config = new SparkMaxConfig();

    config.closedLoop
        .p(intakeConstants.intakeP)
        .i(intakeConstants.intakeI)
        .d(intakeConstants.intakeD); //Values are zero currently
    
    intake_motor.configure(config, com.revrobotics.ResetMode.kResetSafeParameters, com.revrobotics.PersistMode.kNoPersistParameters);
    } // This is the configuration for motor smoothness *could be modified*

    public Command intakeBalls(double rpm){
        return this.runEnd(
            () -> {
                intake_pidController.setSetpoint(rpm, SparkMax.ControlType.kVelocity);

            },
            () -> {
                stopIntake();
            });
    }

    public void stopIntake(){
        intake_motor.stopMotor();
    }

    public void liftIntake(){
        
    }
    


}
