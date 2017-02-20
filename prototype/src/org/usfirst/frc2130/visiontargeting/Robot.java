// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc2130.visiontargeting;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Threads;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

import java.awt.geom.Arc2D.Float;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import edu.wpi.first.wpilibj.vision.VisionPipeline;
import edu.wpi.first.wpilibj.vision.VisionThread;
import gripvision.GripPipeline;

import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc2130.visiontargeting.commands.*;
import org.usfirst.frc2130.visiontargeting.subsystems.*;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */



public class Robot extends IterativeRobot {



    NetworkTable table;

    Command autonomousCommand;

    public VisionThread visionThread;

    public static  Object imgLock;

    public static double centerX;

    public static double run;


    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 240;

    private boolean run1;

    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static robotDrive robotDrive;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        RobotMap.init();

        // BEGIN AUTOGENERATED CODE, SOURCE = ROBOTBUILDER ID = CONSTRUCTORS
        robotDrive = new robotDrive();

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID = CONSTRUCTORS
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // instantiate the command used for the autonomous period
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

        autonomousCommand = new AutonomousCommand();

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

        // Start automatic capture from the main CameraServer. A UsbCamera
        // object is automatically created for device 0 and returned.
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();

        // Set the default resolution.
        camera.setResolution(IMG_WIDTH, IMG_HEIGHT);

        // Create an output stream that will be used to send images to the
        // smart dashboard if desired.
        CvSource outputStream = CameraServer.getInstance().putVideo(
            "HSV_THRESH", IMG_WIDTH, IMG_HEIGHT);

        // Create a new VisionThread and assign it to the visionThread
        // reference. Uses the autogenerated GripPipeline to run the
        // processing steps.  The third argument is an anonymous function
        // with the pipeline supplied as an argument.
        visionThread = new VisionThread(camera,
                                        new GripPipeline(),
                                        pipeline -> {

            // This section provides protected access to the member variables
            // shared between the vision thread and the other threads on the
            // the robot.
            synchronized (imgLock) {
                // Check to see if the pipeline has completed output and
                // detected contour points.
                if (!pipeline.filterContoursOutput().isEmpty()) {
                    // If points were found set to true to activate motors.
                    run1 = true;
                } else {
                    // Otherwise deactivate.
                    run1 = false;
                }
            }

            // OPTIONALLY
            // Now that the pipeline is done, update the smart dashboard. We
            // can use any image from a processing step in the pipeline. For
            // example, display the hsv thresholded output.
            outputStream.putFrame(pipeline.hsvThresholdOutput());

            // There are some cool things you could do by generating
            // additional outputs from the GRIP code that are not actually
            // used for the image processing, like drawing a box around
            // any detected contours. You could also do additional processing
            // of the image here like adding coordinates as overlays
            // on top of the image.

        });

        // Kick off the vision thread
        visionThread.start();

    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */

    public void autonomousPeriodic() {

        // This does not need to be in a separate thread since it already
        // called periodically by the scheduler.
        synchronized (imgLock) {
            if (run1){
                Robot.robotDrive.runVisMotor();
            } else {
                Robot.robotDrive.stopMotors();
            }
        }

        Scheduler.getInstance().run();



    }
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {



        LiveWindow.run();
    }
}
