// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.Drive;

public class VisionSubsystem extends SubsystemBase {
  private Drive m_drive;

  public VisionSubsystem(Drive drive) {
    m_drive = drive;
  }

  @Override
  public void periodic() {
    LimelightHelpers.PoseEstimate mt1 =
        LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight-reef");
    boolean doRejectUpdate = false;

    if (mt1.tagCount == 1 && mt1.rawFiducials.length == 1) {
      if (mt1.rawFiducials[0].ambiguity > .7) {
        doRejectUpdate = true;
      }
      if (mt1.rawFiducials[0].distToCamera > 3) {
        doRejectUpdate = true;
      }
    }
    if (mt1.tagCount == 0) {
      doRejectUpdate = true;
    }

    if (!doRejectUpdate) {
      m_drive.addVisionMeasurement(mt1.pose, mt1.timestampSeconds, VecBuilder.fill(.5, .5, .5));
    }
  }
}
