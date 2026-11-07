package frc.robot.subsystems;


    public boolean ready() {
        return flywheelAtVelocity(currentFlywheelSetpoint, 2);
    }

    public Command autoShootSequence() {
        return
            runOnce(() -> pickScoringFlywheelSetpoint())
            .andThen(run(() -> setFlywheelSpeed(currentFlywheelSetpoint))
            .until(this::ready)
            .andThen(run(() -> setFeederSpeed(ShooterConstants.feederSetpointRPS)))
            .finallyDo(this::stopSystem)
            );
    }

    public Command shootSequence() {
        return
            runOnce(() -> setFlywheelSpeed(SmartDashboard.getNumber("FlywheelSetpointRPS", 60.0)))
            .until(this::ready)
            .andThen(run(() -> setFeederSpeed(ShooterConstants.feederSetpointRPS)))
            .finallyDo(this::stopSystem);
    }

    public Command pass(){
        return
            runOnce(() -> setFlywheelSpeed(ShooterConstants.passSpeed))
            .until(this::ready)
            .andThen(run(() -> setFeederSpeed(ShooterConstants.feederSetpointRPS)))
            .finallyDo(this::stopSystem);
    }

    public double determineManualShooterSpeed(DoubleSupplier triggerValue, CommandXboxController controller){
        if(triggerValue.getAsDouble() < 0.2){ //less than 20% threashold
            return 5.0; //close fixed shot
        }
        controller.setRumble(RumbleType.kBothRumble, triggerValue.getAsDouble());
        return (triggerValue.getAsDouble() * 45.0) + 5.0; //between 5 and 50 rps when over threashold
    }

    public Command shootManual(DoubleSupplier triggerValue, CommandXboxController controller){
        return run(() -> {
                setFlywheelSpeed(determineManualShooterSpeed(triggerValue, controller));
                setFeederSpeed(ShooterConstants.feederSetpointRPS);
            })
            .finallyDo(() -> {
                this.stopSystem();
                controller.setRumble(RumbleType.kBothRumble, 0);
            });
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
        updateDistanceToHub();
        SmartDashboard.putNumber("LimelightDistance", limelightDistance);
        SmartDashboard.putNumber("FlywheelSetpoint", currentFlywheelSetpoint);
        SmartDashboard.putNumber("LeftFlywheelVelocity", m_leftFlywheel1.getVelocity().getValueAsDouble());
    }
}
