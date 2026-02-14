package frc.robot.subsystems;
import frc.robot.Constants.CANConstants;
import frc.robot.Constants.DriveConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;



public class DrivetrainSubsystem extends SubsystemBase {

    private final SwerveModule m_frontLeft;
    private final SwerveModule m_frontRight;
    private final SwerveModule m_backLeft;
    private final SwerveModule m_backRight;
    
    public DrivetrainSubsystem() {

        m_frontLeft = new SwerveModule(
            CANConstants.DRIVETRAIN_FRONT_LEFT,
            CANConstants.STEER_FRONT_LEFT
        );
        m_frontRight = new SwerveModule(
            CANConstants.DRIVETRAIN_FRONT_RIGHT,
            CANConstants.STEER_FRONT_RIGHT
        );
        m_backLeft = new SwerveModule(
            CANConstants.DRIVETRAIN_BACK_LEFT,
            CANConstants.STEER_BACK_LEFT
        );
        m_backRight = new SwerveModule(
            CANConstants.DRIVETRAIN_BACK_RIGHT,
            CANConstants.STEER_BACK_RIGHT
        );
    }

    public void drive(double xSpeed, double ySpeed) {
        double direction = Math.atan(ySpeed + xSpeed);
        m_frontLeft.setSpeedAngle(DriveConstants.DRIVE_MOTOR_SPEED, direction);
        m_frontRight.setSpeedAngle(DriveConstants.DRIVE_MOTOR_SPEED, direction);
        m_backLeft.setSpeedAngle(DriveConstants.DRIVE_MOTOR_SPEED, direction);
        m_backRight.setSpeedAngle(DriveConstants.DRIVE_MOTOR_SPEED, direction);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}