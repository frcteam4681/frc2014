/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.amhs.robotdrive;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robotdrive extends SimpleRobot {

    //Joysitcks 1 and 2
    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    //Victors for drive
    Victor frontLeft = new Victor(1);
    Victor frontRight = new Victor(2);
    Victor backLeft = new Victor(3);
    Victor backRight = new Victor(4);
    //Other Victors
    Victor shooterLeft = new Victor(7);
    Victor shooterRight = new Victor(6);
    Victor loader = new Victor(8);
    //Limit Switch
    DigitalInput buttonTop = new DigitalInput(1);
    DigitalInput buttonBot = new DigitalInput(2);
    DigitalInput buttonTop_2 = new DigitalInput(3);
    DigitalInput buttonBot_2 = new DigitalInput(4);
    //Doubles for joystick axis
    double ch1, ch3, ch4;
    //doubles for motor speed modifiers
    double frontLeftModifier = 0.9, frontRightModifier = 0.9, backLeftModifier = 1, backRightModifier = 1;
    double shooterLeftModifier = 0.4, shooterRightModifier = 0.4;
    //FOR COMPETITION, CHANGE ^ TO FINAL
    //Other Vars
    boolean old = false;
    boolean isPressed, isPressed2;
    String status;

    //message printed when robot is initialized
    public void robotInit() {
        System.out.println("Robot Initialized");
        disp("Initialized", 1);
        DriverStationLCD.getInstance().updateLCD();
        Timer.delay(2);
    }

    public void autonomous() {
        //(if hot)
        shoot();
        frontRight.set(1);
        frontLeft.set(1);
        backLeft.set(1);
        backRight.set(1);
        Timer.delay(3);
        frontRight.set(0);
        frontLeft.set(0);
        backLeft.set(0);
        backRight.set(0);
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        DriverStationLCD.getInstance().clear();
        DriverStationLCD.getInstance().updateLCD();
        while (isOperatorControl() && isEnabled()) {

            //driving codex
            ch1 = joy1.getX();
            ch3 = joy2.getY();
            ch4 = joy2.getX();

            //Setting motor speeds
            frontLeft.set(frontLeftModifier * specialSquare(-(ch3 + ch1 - ch4)));//port 1
            frontRight.set(frontRightModifier * specialSquare((ch3 - ch1 + ch4)));//port 2
            backLeft.set(backLeftModifier * specialSquare(-(ch3 + ch1 + ch4)));//port 3
            backRight.set(backRightModifier * specialSquare((ch3 - ch1 - ch4)));//port 4

            //Changing the modifiers
            if (!isPressed) {
                if (joy2.getRawButton(6)) {
                    frontLeftModifier = changeValue(frontLeftModifier);
                    isPressed = true;
                }
                if (joy2.getRawButton(11)) {
                    frontRightModifier = changeValue(frontRightModifier);
                    isPressed = true;
                }
                if (joy2.getRawButton(7)) {
                    backLeftModifier = changeValue(backLeftModifier);
                    isPressed = true;
                }
                if (joy2.getRawButton(10)) {
                    backRightModifier = changeValue(backRightModifier);
                    isPressed = true;
                }
            }
            if (!joy2.getRawButton(6) && !joy2.getRawButton(11) && !joy2.getRawButton(7) && !joy2.getRawButton(10)) {
                isPressed = false;
            }
            disp("Front Left: " + frontLeftModifier, 1);
            disp("Front Right: " + frontRightModifier, 2);
            disp("Back Left: " + backLeftModifier, 3);
            disp("Back Right: " + backRightModifier, 4);
            DriverStationLCD.getInstance().updateLCD();
            //Shooter modifier set
            if (!isPressed2) {
                if (joy1.getRawButton(6)) {
                    shooterLeftModifier = changeValue2(shooterLeftModifier);
                    isPressed2 = true;
                }
                if (joy1.getRawButton(11)) {
                    shooterRightModifier = changeValue2(shooterRightModifier);
                    isPressed2 = true;
                }
            }
            if (!joy1.getRawButton(6) && !joy1.getRawButton(11) && !joy1.getRawButton(7) && !joy1.getRawButton(10)) {
                isPressed2 = false;
            }

            //PRIMITIVE LOADING CODE
        /* if(joy1.getRawButton(4))
             * {
             *     loader .set(1);
             * }
             * else
             * {
             *     loader.set(0);
             * }
             */
            //2nd Gen SHOOTING CODE
            //ALL VALUES ARE UNTESTED AND ARE ROUGH GUESTIMATES
            if (joy1.getRawButton(2) && joy1.getTrigger()) {
                shoot();
            }

        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {

    }

    public void disabled() {
        DriverStationLCD.getInstance().clear();
        DriverStationLCD.getInstance().updateLCD();
    }

    public double specialSquare(double setMe) {
        if (setMe > .05) {
            return setMe * setMe;
        } else {
            if (setMe < -.05) {
                return setMe * setMe * -1;
            } else {
                return 0;
            }
        }
    }

    public double changeValue(double value) {
        if (joy2.getRawButton(8)) {
            return (value -= .02);
        }
        if (joy2.getRawButton(9)) {
            return (value += .02);
        }
        return (value);
    }

    public double changeValue2(double value) {
        if (joy1.getRawButton(8)) {
            return (value -= .02);
        }
        if (joy1.getRawButton(9)) {
            return (value += .02);
        }
        return (value);
    }

    public void shoot() {
        while (!buttonTop.get()) {
            shooterLeft.set(shooterLeftModifier * ((1)));
            shooterRight.set(-(shooterRightModifier * 1));
        }
        while (!buttonBot.get()) {
            shooterLeft.set(-0.4);
            shooterRight.set(0.4);
        }
            shooterLeft.set(0);
            shooterRight.set(0);
    }

    public void disp(String msg, int line) {
        switch (line) {
            case 1:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, msg);
                break;
            case 2:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, msg);
                break;
            case 3:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, msg);
                break;
            case 4:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, msg);
                break;
            case 5:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, msg);
                break;
            default:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, msg);
                break;

        }

    }

}
