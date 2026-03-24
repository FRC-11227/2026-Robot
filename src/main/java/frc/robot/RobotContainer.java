// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.events.EventTrigger;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class RobotContainer {
    

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(DriveConstants.MaxSpeed * 0.05).withRotationalDeadband(DriveConstants.MaxAngularRate * 0.05) // Add a 5% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(DriveConstants.MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    public final ShooterSubsystem shooter = new ShooterSubsystem();
    public final IntakeSubsystem intake = new IntakeSubsystem();
    Trigger stopIntakeTrigger = new EventTrigger("Stop Intake");

    /* Path follower */
    private final SendableChooser<Command> autoChooser;

    public RobotContainer() {
        SmartDashboard.putNumber("FlywheelSetpoint", 0.0);

        SmartDashboard.putData("Swerve Drive", new Sendable() {
            @Override
            public void initSendable(SendableBuilder builder) {
                builder.setSmartDashboardType("SwerveDrive");

                builder.addDoubleProperty("Front Left Angle", () -> drivetrain.getModule(0).getCurrentState().angle.getRadians(), null);
                builder.addDoubleProperty("Front Left Velocity", () -> drivetrain.getModule(0).getCurrentState().speedMetersPerSecond, null);

                builder.addDoubleProperty("Front Right Angle", () -> drivetrain.getModule(1).getCurrentState().angle.getRadians(), null);
                builder.addDoubleProperty("Front Right Velocity", () -> drivetrain.getModule(0).getCurrentState().speedMetersPerSecond, null);

                builder.addDoubleProperty("Back Left Angle", () -> drivetrain.getModule(2).getCurrentState().angle.getRadians(), null);
                builder.addDoubleProperty("Back Left Velocity", () -> drivetrain.getModule(0).getCurrentState().speedMetersPerSecond, null);

                builder.addDoubleProperty("Back Right Angle", () -> drivetrain.getModule(3).getCurrentState().angle.getRadians(), null);
                builder.addDoubleProperty("Back Right Velocity", () -> drivetrain.getModule(0).getCurrentState().speedMetersPerSecond, null);

                builder.addDoubleProperty("Robot Angle", () -> drivetrain.getState().Pose.getRotation().getRadians(), null);
            }
        });

        NamedCommands.registerCommand("Align", drivetrain.aimAtHub());
        NamedCommands.registerCommand("Shoot 5s", shooter.autoShootSequence().withTimeout(5));
        NamedCommands.registerCommand("Shoot 1s", shooter.autoShootSequence().withTimeout(1));
        NamedCommands.registerCommand("Intake", Commands.runOnce(() -> {intake.setRollerSpeed(3800); intake.setIntakePosition(IntakeConstants.armDown);}));
        NamedCommands.registerCommand("Stop Intake", Commands.runOnce(() -> {intake.stopIntakeAngle(); intake.stopRollers();}));

        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Mode", autoChooser);

        configureBindings();

        // Warmup PathPlanner to avoid Java pauses
        CommandScheduler.getInstance().schedule(FollowPathCommand.warmupCommand());
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * DriveConstants.MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * DriveConstants.MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * DriveConstants.MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
            //todo hold intake arm 
        );



        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        // joystick.b().whileTrue(drivetrain.applyRequest(() ->
        //     point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        // ));

        //joystick.rightTrigger(0.5).whileTrue(shooter.pass());
        
        
        // joystick.leftTrigger(0.5).whileTrue(drivetrain.applyRequest(() -> 
        //     drive.withVelocityX(0 * DriveConstants.MaxSpeed / 3) // Don't drive
        //         .withVelocityY(0 * DriveConstants.MaxSpeed / 3) 
        //         .withRotationalRate(-drivetrain.limelight_aim_proportional() * DriveConstants.MaxAngularRate) // turn toward target
        // ).finallyDo(() -> LimelightHelpers.setPipelineIndex("", 0)));
        
        //X-lock while X button pressed
        joystick.x().whileTrue(drivetrain.applyRequest(() -> brake));

        // Reset the field-centric heading on Dpad up press
        joystick.povUp().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        //intake control
        joystick.y().onTrue(intake.intakeUp(IntakeConstants.intakeRotateSpeed)); //stow intake with Y
        joystick.leftBumper().whileTrue(intake.intakeBalls()); //intake down and intaking with left bumper
        joystick.a().whileTrue(Commands.run(() -> intake.intakeRollers.set(-0.7)).finallyDo(() -> intake.stopRollers())); //outtake with B
        joystick.leftTrigger(0.05).whileTrue(intake.jiggleIntakeWithHeight(joystick::getLeftTriggerAxis));
        
        //shooter control
        joystick.rightBumper().whileTrue(
            drivetrain.aimAtHub().andThen(
                Commands.parallel(
                    drivetrain.applyRequest(() -> idle),
                    shooter.autoShootSequence()
                )
            )
        );
        joystick.b().whileTrue(Commands.run(() -> shooter.setFeederSpeed(-30.0)).finallyDo(() -> shooter.stopFeeder())); //reverse feeders for unjamming



        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();

        // // Simple drive forward auton
        // final var idle = new SwerveRequest.Idle();
        // return Commands.sequence(
        //     // Reset our field centric heading to match the robot
        //     // facing away from our alliance station wall (0 deg).
        //     drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
        //     // Then slowly drive forward (away from us) for 5 seconds.
        //     drivetrain.applyRequest(() ->
        //         drive.withVelocityX(0.5)
        //             .withVelocityY(0)
        //             .withRotationalRate(0)
        //     )
        //     .withTimeout(5.0),
        //     // Finally idle for the rest of auton
        //     drivetrain.applyRequest(() -> idle)
        // );
    }
}
