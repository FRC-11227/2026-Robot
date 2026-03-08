package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;

import frc.robot.LimelightHelpers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.CAN;

public class ShooterSubsystem extends SubsystemBase {

    static NetworkTableInstance inst = NetworkTableInstance.getDefault();
    static NetworkTable table = inst.getTable("datatable");
    static DoublePublisher distancePub = table.getDoubleTopic("distanceFromGoal").publish();

    private final CANBus kCanivoreBus = new CANBus("theGoose");

    final TalonFX m_leftFlywheelLead = new TalonFX(CAN.leftFlywheelLead, kCanivoreBus);
    final TalonFX m_leftFlywheelFollow = new TalonFX(CAN.leftFlywheelFollow, kCanivoreBus);
    final TalonFX m_leftFlywheelFeeder = new TalonFX(CAN.leftFLywheelFeeder, kCanivoreBus);

    final TalonFX m_rightFlywheelLead = new TalonFX(CAN.rightFlywheelLead, kCanivoreBus);
    final TalonFX m_rightFlywheelFollow = new TalonFX(CAN.rightFlywheelFollow, kCanivoreBus);
    final TalonFX m_rightFlywheelFeeder = new TalonFX(CAN.rightFlywheelFeeder, kCanivoreBus);

    final VelocityVoltage m_VelocityVoltageRequest = new VelocityVoltage(0).withSlot(0);

    final Slot0Configs flywheelSlot0Configs = new Slot0Configs();
    final Slot0Configs feederSlot0Configs = new Slot0Configs();

    final TalonFXConfiguration flywheelConfigs = new TalonFXConfiguration();
    final TalonFXConfiguration feederConfigs = new TalonFXConfiguration();

    public static double limelightDeg = 5.0; 
    public static double limelightHeightIn = 28.0; 
    public static double goalHeightIn = 44.25;


    static InterpolatingDoubleTreeMap shooterTable = new InterpolatingDoubleTreeMap();
    static List<Double> shooterDistList = new ArrayList<>(Arrays.asList());

    public ShooterSubsystem() {
        // Check constants.java file to see the values provided
        flywheelSlot0Configs.kS = ShooterConstants.flywheel_kS; // Add 0.1 V output to overcome static friction
        flywheelSlot0Configs.kV = ShooterConstants.flywheel_kV; // A velocity target of 1 rps results in 0.12 V output
        flywheelSlot0Configs.kP = ShooterConstants.flywheel_kP; // An error of 1 rps results in 0.11 V output
        flywheelSlot0Configs.kI = ShooterConstants.flywheel_kI; // no output for integrated error
        flywheelSlot0Configs.kD = ShooterConstants.flywheel_kD; // no output for error derivative

        m_leftFlywheelLead.getConfigurator().apply(flywheelSlot0Configs);
        m_leftFlywheelFollow.getConfigurator().apply(flywheelSlot0Configs);
        m_rightFlywheelLead.getConfigurator().apply(flywheelSlot0Configs);
        m_rightFlywheelFollow.getConfigurator().apply(flywheelSlot0Configs);

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

        shooterTable.put(shooterDistList.get(0), 0.0);
        shooterTable.put(shooterDistList.get(1), 0.0);
        shooterTable.put(shooterDistList.get(2), 0.0);
        shooterTable.put(shooterDistList.get(3), 0.0);
        shooterTable.put(shooterDistList.get(4), 0.0);
        shooterTable.put(shooterDistList.get(5), 0.0);
        shooterTable.put(shooterDistList.get(6), 0.0);
        shooterTable.put(shooterDistList.get(7), 0.0);
        shooterTable.put(shooterDistList.get(8), 0.0);
        shooterTable.put(shooterDistList.get(9), 0.0);
    }

    public static double getDistance() {
        double ty = LimelightHelpers.getTY("limelight");
        double limelightSightHeight = goalHeightIn - limelightHeightIn;
        double dist = limelightSightHeight / Math.tan(limelightDeg + ty);
        distancePub.set(dist);
        return dist;
    }

    public static ArrayList<Double> findFloorCeil(double distance) {
        ArrayList<Double> results = new ArrayList<>();
        int index = Collections.binarySearch(shooterDistList, distance);
        double floorValue = 0.0;
        double ceilingValue = 0.0;

        if (index >= 0) {
            floorValue = shooterDistList.get(index);
            ceilingValue = shooterDistList.get(index);
        } else {
            int insertionPoint = -index - 1;

            if (insertionPoint > 0) {
                floorValue = shooterDistList.get(insertionPoint - 1);
            }
            if (insertionPoint < shooterDistList.size()) {
                ceilingValue = shooterDistList.get(insertionPoint);
            }
        }
        results.add(floorValue);
        results.add(ceilingValue);
        return results;
    }

    public static double getVelocity(double distance) {
        ArrayList<Double> results = findFloorCeil(distance);
        double percent = (results.get(1) - results.get(0)) == 0 ? 0 : (distance - results.get(0)) / (results.get(1) - results.get(0));
        double valueBetween = shooterTable.get(results.get(1)) - shooterTable.get(results.get(0));
        return results.get(0) + valueBetween * percent;
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

    public Command shootLeft() {
        double motorVelocity = m_leftFlywheelLead.getVelocity().getValueAsDouble();
        double targetVelocity = getVelocity(1);
        return this.runEnd(
            () -> {
                m_leftFlywheelLead.setControl(m_VelocityVoltageRequest.withVelocity(8).withFeedForward(0.5));
                if (motorVelocity > targetVelocity) {
                    m_leftFlywheelFeeder.setControl(m_VelocityVoltageRequest.withVelocity(targetVelocity).withFeedForward(0.5));
                }
            },
            () -> {
                m_leftFlywheelLead.stopMotor();
                m_leftFlywheelFeeder.stopMotor();
            }
        );
    }

    public Command shootRight() {
        double motorVelocity = m_rightFlywheelLead.getVelocity().getValueAsDouble();
        double targetVelocity = getVelocity(1);
        return this.runEnd(
            () -> {
                m_rightFlywheelLead.setControl(m_VelocityVoltageRequest.withVelocity(0).withFeedForward(0.5));
                if (motorVelocity > targetVelocity) {
                    m_rightFlywheelFeeder.setControl(m_VelocityVoltageRequest.withVelocity(targetVelocity).withFeedForward(0.5));
                }
            },
            () -> {
                m_rightFlywheelLead.stopMotor();
                m_rightFlywheelFeeder.stopMotor();
            }
        );
    }

    @Override
    public void periodic() {
        double ty = LimelightHelpers.getTY("limelight");
        double limelightSightHeight = goalHeightIn - limelightHeightIn;
        double dist = limelightSightHeight / Math.tan(limelightDeg + ty);
        System.out.println(dist);
    }
}
