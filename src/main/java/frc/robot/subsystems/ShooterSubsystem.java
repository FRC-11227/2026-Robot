package frc.robot.subsystems;

<<<<<<< HEAD
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.RawFiducial;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CAN;
import frc.robot.Constants.ShooterConstants;
=======
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.CAN;
>>>>>>> Eric

public class ShooterSubsystem extends SubsystemBase {
    private final CANBus kCanivoreBus = new CANBus("theGoose");

    final TalonFX m_leftFlywheelLead = new TalonFX(CAN.leftFlywheelLead, kCanivoreBus);
    final TalonFX m_leftFlywheelFollow = new TalonFX(CAN.leftFlywheelFollow, kCanivoreBus);
    final TalonFX m_leftFlywheelFeeder = new TalonFX(CAN.leftFLywheelFeeder, kCanivoreBus);

    final TalonFX m_rightFlywheelLead = new TalonFX(CAN.rightFlywheelLead, kCanivoreBus);
    final TalonFX m_rightFlywheelFollow = new TalonFX(CAN.rightFlywheelFollow, kCanivoreBus);
    final TalonFX m_rightFlywheelFeeder = new TalonFX(CAN.rightFlywheelFeeder, kCanivoreBus);

<<<<<<< HEAD
    final VelocityTorqueCurrentFOC m_velocityTorqueRequest = new VelocityTorqueCurrentFOC(0).withSlot(0);
=======
    final VelocityVoltage m_VelocityVoltageRequest = new VelocityVoltage(0).withSlot(0);
>>>>>>> Eric

    final Slot0Configs flywheelSlot0Configs = new Slot0Configs();
    final Slot0Configs feederSlot0Configs = new Slot0Configs();

<<<<<<< HEAD
    final DutyCycleOut m_DutyCycle = new DutyCycleOut(0.0);

    final DoublePublisher flywheelSpeedPub;
    final DoublePublisher distancePub;
    final DoublePublisher limelightTYPub;
    final DoublePublisher heightDiffPub;

    private double currentFlywheelSetpoint;
    private double limelightDistance;


    public ShooterSubsystem(DoubleTopic flywheelVelocity, DoubleTopic distance, DoubleTopic limelightTY, DoubleTopic heightDiff) {
        // Check constants.java file to see the values provided
        flywheelSlot0Configs.kS = ShooterConstants.flywheel_kS;
        flywheelSlot0Configs.kV = ShooterConstants.flywheel_kV;
        flywheelSlot0Configs.kP = ShooterConstants.flywheel_kP;
        flywheelSlot0Configs.kI = ShooterConstants.flywheel_kI;
        flywheelSlot0Configs.kD = ShooterConstants.flywheel_kD;
=======
    final TalonFXConfiguration flywheelConfigs = new TalonFXConfiguration();
    final TalonFXConfiguration feederConfigs = new TalonFXConfiguration();

    public ShooterSubsystem() {
        // Check constants.java file to see the values provided
        flywheelSlot0Configs.kS = ShooterConstants.flywheel_kS; // Add 0.1 V output to overcome static friction
        flywheelSlot0Configs.kV = ShooterConstants.flywheel_kV; // A velocity target of 1 rps results in 0.12 V output
        flywheelSlot0Configs.kP = ShooterConstants.flywheel_kP; // An error of 1 rps results in 0.11 V output
        flywheelSlot0Configs.kI = ShooterConstants.flywheel_kI; // no output for integrated error
        flywheelSlot0Configs.kD = ShooterConstants.flywheel_kD; // no output for error derivative
>>>>>>> Eric

        m_leftFlywheelLead.getConfigurator().apply(flywheelSlot0Configs);
        m_leftFlywheelFollow.getConfigurator().apply(flywheelSlot0Configs);
        m_rightFlywheelLead.getConfigurator().apply(flywheelSlot0Configs);
        m_rightFlywheelFollow.getConfigurator().apply(flywheelSlot0Configs);

<<<<<<< HEAD
        m_rightFlywheelLead.getConfigurator().apply(new MotorOutputConfigs().withInverted(InvertedValue.Clockwise_Positive));
        m_rightFlywheelFeeder.getConfigurator().apply(new MotorOutputConfigs().withInverted(InvertedValue.Clockwise_Positive));

        m_leftFlywheelLead.setNeutralMode(NeutralModeValue.Coast);
        m_leftFlywheelFollow.setNeutralMode(NeutralModeValue.Coast);
        m_rightFlywheelLead.setNeutralMode(NeutralModeValue.Coast);
        m_rightFlywheelFollow.setNeutralMode(NeutralModeValue.Coast);

        m_leftFlywheelFeeder.setNeutralMode(NeutralModeValue.Brake);
        m_rightFlywheelFeeder.setNeutralMode(NeutralModeValue.Brake);

=======
>>>>>>> Eric
        // set follow flywheels to follow their leader motors
        m_rightFlywheelFollow.setControl(new Follower(m_rightFlywheelLead.getDeviceID(), MotorAlignmentValue.Aligned));
        m_leftFlywheelFollow.setControl(new Follower(m_leftFlywheelLead.getDeviceID(), MotorAlignmentValue.Aligned));

        feederSlot0Configs.kS = ShooterConstants.feeder_kS;
        feederSlot0Configs.kV = ShooterConstants.feeder_kV;
        feederSlot0Configs.kP = ShooterConstants.feeder_kP;
        feederSlot0Configs.kI = ShooterConstants.feeder_kI;
        feederSlot0Configs.kD = ShooterConstants.feeder_kD;

        m_leftFlywheelFeeder.getConfigurator().apply(feederSlot0Configs);
        m_rightFlywheelFeeder.getConfigurator().apply(feederSlot0Configs);

<<<<<<< HEAD
        flywheelSpeedPub = flywheelVelocity.publish();
        distancePub = distance.publish();
        limelightTYPub = limelightTY.publish();
        heightDiffPub = heightDiff.publish();

        currentFlywheelSetpoint = 0.0;
        limelightDistance = 0;
    }

    public void setFlywheelSpeed(double rps) {
        m_rightFlywheelLead.setControl(m_velocityTorqueRequest.withVelocity(rps));
        m_leftFlywheelLead.setControl(m_velocityTorqueRequest.withVelocity(rps));
    }

    public void setFeederSpeed(double rps) {
        m_leftFlywheelFeeder.setControl(m_velocityTorqueRequest.withVelocity(rps));
        m_rightFlywheelFeeder.setControl(m_velocityTorqueRequest.withVelocity(rps));
    }

    public void stopFlywheels() {
        m_leftFlywheelLead.stopMotor();
        m_rightFlywheelLead.stopMotor();
    }

    public void stopFeeder() {
        m_leftFlywheelFeeder.stopMotor();
        m_rightFlywheelFeeder.stopMotor();
    }

    public void stopSystem() {
        stopFlywheels();
        stopFeeder();
    } 

    public boolean flywheelAtVelocity(double rps, double tolerance) {
        return m_rightFlywheelLead.getVelocity().isNear(rps, tolerance)
            && m_leftFlywheelLead.getVelocity().isNear(rps, tolerance);
    }

    public boolean ready() {
        return flywheelAtVelocity(currentFlywheelSetpoint, 2);
    }

    public Command shootSequence() {
        return
            runOnce(() -> pickScoringFlywheelSetpoint())
            .andThen(run(() -> setFlywheelSpeed(currentFlywheelSetpoint))
            .until(this::ready)
            .andThen(run(() -> setFeederSpeed(ShooterConstants.feederSetpointRPS)))
            .finallyDo(this::stopSystem)
            );
    }

    public Command shootPauseSequence() {
        return new ParallelCommandGroup(
            this.runOnce(() -> setFlywheelSpeed(50)),
            new ConditionalCommand(this.runOnce(() -> setFeederSpeed(30)), this.runOnce(() -> stopFeeder()), this::ready).repeatedly()
        ).finallyDo(() -> this.stopSystem());
    }

    public void pickScoringFlywheelSetpoint() {
        SmartDashboard.putNumber("DecidedDistance", limelightDistance);
        currentFlywheelSetpoint = ShooterConstants.lerpTable.get(limelightDistance);
    }

    public void updateDistanceToHub() {
        Set<Integer> targetIds = Set.of(2, 5, 10, 18, 21, 26);

        // Get raw AprilTag/Fiducial data
        RawFiducial[] fiducials = LimelightHelpers.getRawFiducials("");

        // Find the "Best" tag (Filtered -> Sorted -> First)
        Optional<RawFiducial> bestTag = Arrays.stream(fiducials)
            .filter(f -> targetIds.contains(f.id))
            .min(Comparator.comparingDouble(f -> f.ambiguity)); // .min() finds the lowest value

        if (bestTag.isPresent()) {
            limelightDistance = bestTag.get().distToCamera;
        }
    }

    @Override
    public void periodic() {
        flywheelSpeedPub.set(m_leftFlywheelLead.getVelocity().getValueAsDouble());
        
        updateDistanceToHub();
        distancePub.set(limelightDistance);

        SmartDashboard.putNumber("FlywheelSetpoint", currentFlywheelSetpoint);
=======
        flywheelConfigs.CurrentLimits.StatorCurrentLimit = ShooterConstants.FLYWHEEL_STATOR_CURRENT_LIMIT;
        flywheelConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
        flywheelConfigs.CurrentLimits.SupplyCurrentLimit = ShooterConstants.FLYWHEEL_SUPPLY_CURRENT_LIMIT;
        flywheelConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
        flywheelConfigs.Voltage.PeakForwardVoltage = 12;
        flywheelConfigs.Voltage.PeakReverseVoltage = -12;

        m_leftFlywheelLead.getConfigurator().apply(flywheelConfigs);
        m_leftFlywheelFollow.getConfigurator().apply(flywheelConfigs);
        m_rightFlywheelLead.getConfigurator().apply(flywheelConfigs);
        m_rightFlywheelFollow.getConfigurator().apply(flywheelConfigs);

        feederConfigs.CurrentLimits.StatorCurrentLimit = ShooterConstants.FEEDER_STATOR_CURRENT_LIMIT;
        feederConfigs.CurrentLimits.StatorCurrentLimitEnable = true;
        feederConfigs.CurrentLimits.SupplyCurrentLimit = ShooterConstants.FEEDER_SUPPLY_CURRENT_LIMIT;
        feederConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
        feederConfigs.Voltage.PeakForwardVoltage = 12;
        feederConfigs.Voltage.PeakReverseVoltage = -12;

        m_leftFlywheelFeeder.getConfigurator().apply(feederConfigs);
        m_rightFlywheelFeeder.getConfigurator().apply(feederConfigs);
    }

    public Command spinFlywheel(DoubleSupplier speed) {
        return this.runEnd(
            () -> {
                // set velocity to 8 rps, add 0.5 V to overcome gravity
                m_rightFlywheelLead.setControl(m_VelocityVoltageRequest.withVelocity(8).withFeedForward(0.5));
                m_leftFlywheelLead.setControl(m_VelocityVoltageRequest.withVelocity(8).withFeedForward(0.5));

                // set velocity to 2 rps, add 0.5 V to overcome gravity
                m_leftFlywheelFeeder.setControl(m_VelocityVoltageRequest.withVelocity(2).withFeedForward(0.5));
                m_rightFlywheelFeeder.setControl(m_VelocityVoltageRequest.withVelocity(2).withFeedForward(0.5));
            },
            () -> {
                m_rightFlywheelLead.stopMotor();
                m_leftFlywheelLead.stopMotor();
                m_rightFlywheelFeeder.stopMotor();
                m_leftFlywheelFeeder.stopMotor();
            });
    }

    public Command shootLeft(Double speed) {
        return this.runEnd(
            () -> {
                m_leftFlywheelLead.setControl(m_VelocityVoltageRequest.withVelocity(speed).withFeedForward(0.5));
                m_leftFlywheelFeeder.setControl(m_VelocityVoltageRequest.withVelocity(speed).withFeedForward(0.5));
            },
            () -> {
                m_leftFlywheelLead.stopMotor();
                m_leftFlywheelFeeder.stopMotor();
            }
        );
    }

    public Command shootRight(Double speed) {
        return this.runEnd(
            () -> {
                m_rightFlywheelLead.setControl(m_VelocityVoltageRequest.withVelocity(speed).withFeedForward(0.5));
                m_rightFlywheelFeeder.setControl(m_VelocityVoltageRequest.withVelocity(speed).withFeedForward(0.5));
            },
            () -> {
                m_rightFlywheelLead.stopMotor();
                m_rightFlywheelFeeder.stopMotor();
            }
        );
>>>>>>> Eric
    }
}
