// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.CommandSwerveDrivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static Command autoPath_Right(CommandSwerveDrivetrain subsystem) {
    try{
      PathPlannerPath path = PathPlannerPath.fromPathFile("Right Path");
      return AutoBuilder.followPath(path);
    }catch(Exception err){
      return Commands.none();
    }
  }
  // public static Command rotateToTag(CommandSwerveDrivetrain swerveDrivetrain) {
  //   return swerveDrivetrain.rotateToTarget();
  // }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
