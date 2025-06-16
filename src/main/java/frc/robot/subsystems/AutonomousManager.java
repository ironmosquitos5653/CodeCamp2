package frc.robot.subsystems;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.drive.Drive;

/** Add your docs here. */
public class AutonomousManager {
  Drive m_Drive;
  ArmSubsystem m_armSubsystem;

  public AutonomousManager(Drive drive, ArmSubsystem armSubsystem) {
    m_Drive = drive;
    m_armSubsystem = armSubsystem;
  }

  public void initialize() {
    NamedCommands.registerCommand("ArmOut", Commands.runOnce(() -> m_armSubsystem.setOut()));
    NamedCommands.registerCommand("ArmOff", Commands.runOnce(() -> m_armSubsystem.setOff()));
  }
}
