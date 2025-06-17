// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.Drive;

public class VisionSubsystem extends SubsystemBase {
  private Drive m_drive;

  private final Field2d field2d = new Field2d();

  public VisionSubsystem(Drive drive) {
    m_drive = drive;
    ShuffleboardTab tab = Shuffleboard.getTab("Vision");
    tab.addString("Pose", this::getFomattedPose).withPosition(0, 0).withSize(4, 0);
    tab.add("Field", field2d).withPosition(3, 0).withSize(6, 4);
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

    updateField();
  }

  private String getFomattedPose() {
    return getFomattedPose(m_drive.getPose());
  }

  private String getFomattedPose(Pose2d pose) {

    return String.format(
        "(%.2f, %.2f) %.2f degrees", pose.getX(), pose.getY(), pose.getRotation().getDegrees());
  }

  public void updateField() {
    field2d.setRobotPose(m_drive.getPose());
  }
}
