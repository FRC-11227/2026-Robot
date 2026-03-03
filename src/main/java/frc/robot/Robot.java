// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.HootAutoReplay;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    private Command m_autonomousCommand;

    private final RobotContainer m_robotContainer;

    /* log and replay timestamp and joystick data */
    private final HootAutoReplay m_timeAndJoystickReplay = new HootAutoReplay()
<<<<<<< HEAD
            .withTimestampReplay()
            .withJoystickReplay();
=======
        .withTimestampReplay()
        .withJoystickReplay();
>>>>>>> main

    public Robot() {
        m_robotContainer = new RobotContainer();
    }

    @Override
    public void robotPeriodic() {
        m_timeAndJoystickReplay.update();
<<<<<<< HEAD
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void disabledExit() {
    }
=======
        CommandScheduler.getInstance().run(); 
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {}
>>>>>>> main

    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        if (m_autonomousCommand != null) {
            CommandScheduler.getInstance().schedule(m_autonomousCommand);
        }
    }

    @Override
<<<<<<< HEAD
    public void autonomousPeriodic() {
    }

    @Override
    public void autonomousExit() {
    }
=======
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}
>>>>>>> main

    @Override
    public void teleopInit() {
        if (m_autonomousCommand != null) {
            CommandScheduler.getInstance().cancel(m_autonomousCommand);
        }
    }

    @Override
<<<<<<< HEAD
    public void teleopPeriodic() {
    }

    @Override
    public void teleopExit() {
    }
=======
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}
>>>>>>> main

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
<<<<<<< HEAD
    public void testPeriodic() {
    }

    @Override
    public void testExit() {
    }

    @Override
    public void simulationPeriodic() {
    }
=======
    public void testPeriodic() {}

    @Override
    public void testExit() {}

    @Override
    public void simulationPeriodic() {}
>>>>>>> main
}
