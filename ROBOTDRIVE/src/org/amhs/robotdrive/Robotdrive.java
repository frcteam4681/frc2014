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
    DigitalInput buttonTopLeft = new DigitalInput(1);
    DigitalInput buttonBotLeft = new DigitalInput(2);
    DigitalInput buttonTopRight = new DigitalInput(3);
    DigitalInput buttonBotRight = new DigitalInput(4);
    Timer timer = new Timer();
    //Doubles for joystick axis
    double ch1, ch3, ch4;
    //doubles for motor speed modifiers
    double frontLeftModifier = 0.9, frontRightModifier = 0.9, backLeftModifier = 1, backRightModifier = 1;
    double shooterLeftModifier = 1, shooterRightModifier = 1;
    //FOR COMPETITION, CHANGE ^ TO FINAL
    //Other Vars
    boolean reversed = false;
    boolean old = false;
    boolean isPressed, isPressed2;
    //String status;

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

            //-----------------------------
            //DRIVING CODE
            //REVERSE WITH JOY 1 BUTTON 3
            //-----------------------------
            ch1 = joy1.getX();
            ch3 = joy2.getY();
            ch4 = joy2.getX();
            reversed = joy1.getRawButton(3);
            if(!reversed)
            {
                 frontLeft.set(frontLeftModifier * specialSquare(-(ch3 + ch1 - ch4)));//port 1
                 frontRight.set(frontRightModifier * specialSquare((ch3 - ch1 + ch4)));//port 2
                 backLeft.set(backLeftModifier * specialSquare(-(ch3 + ch1 + ch4)));//port 3
                 backRight.set(backRightModifier * specialSquare((ch3 - ch1 - ch4)));//port 4
            }
            else
            {
                 frontLeft.set(frontLeftModifier * specialSquare((ch3 + ch1 - ch4)));//port 1
                 frontRight.set(frontRightModifier * specialSquare(-(ch3 - ch1 + ch4)));//port 2
                 backLeft.set(backLeftModifier * specialSquare((ch3 + ch1 + ch4)));//port 3
                 backRight.set(backRightModifier * specialSquare(-(ch3 - ch1 - ch4)));//port 4
            }
          
            //--------------------------------------
            //MODIIER CHANGING
            //DRIVING MODIFIERS ARE ON JOY 2
            //SHOOTING MODIFIERS ARE 6 AND 11 on Joy 1
            //--------------------------------------
            
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
            //shooter
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
            if (!joy1.getRawButton(6) && !joy1.getRawButton(11)) {
                isPressed2 = false;
            }
           
            //---------------------------------
            //DRIVER STATION OUTPUT
            //disp(String message), int line); Line is from 1-6
            //Default is line 1
            //MESSAGE IS THIS LONG!
            //--------------------------------
            disp("Front Left: " + frontLeftModifier, 1);
            disp("Front Right: " + frontRightModifier, 2);
            disp("Back Left: " + backLeftModifier, 3);
            disp("Back Right: " + backRightModifier, 4);
            if(!reversed)
            {
                disp("  FORWARD", 5);
            }
            else
            {
                disp("BACKWARDS", 5);
            }
            DriverStationLCD.getInstance().updateLCD();
           
           //-------------------------------------
           //LOADING CODE
           //JOY 2 BUTTON 4 LOADS
           //JOY 1 BUTTON 5 REVERSES
           //-------------------------------------
           if(joy2.getRawButton(4) || joy2.getRawButton(5))
           {
               if(joy2.getRawButton(4))
               {
                   loader.set(-1);
               }
               else
               {
                   loader.set(1);
               }
           }
           else
           {
               loader.set(0);
           }
           //------------------------------
           //SHOOTING CODE
           //FIRE WITH JOY 2 TRIGGER AND BUTTION 2
           //RUNS TILL IT HITS EITHER LIMIT SWITCH
           //EMERGENCY STOP IS JOY 1 TRIGGER AND JOY 2 TRIGGER
           //------------------------------
             
            if(joy2.getRawButton(2))
            {
                shooterLeft.set(.4);
                shooterRight.set(-.4);
            }
            else
            {
                shooterLeft.set(0);
                shooterRight.set(0);
            }
            if (joy2.getTrigger() && joy2.getRawButton(2)) {
                shoot();
            }
            //END OPERATOR CONTROLL CODE
        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {

    }

    //This function is called each time the robot is dissabled
    public void disabled() {
        DriverStationLCD.getInstance().clear();
        disp("Turn Me On", 1);
        DriverStationLCD.getInstance().updateLCD();
    }

    //----------------------------------
    //OTHER FUNCTIONS
    //----------------------------------
    
    //SQUARES THE IMPUT AND KEEPS NEGATIVES
    //INCLUDES DEAD ZONES
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
    //MODIFIES THE VALUE OF THE DRIVE MODIFIER THAT IS GIVEN TO IT
    public double changeValue(double value) {
        if (joy2.getRawButton(8)) {
            return (value -= .02);
        }
        if (joy2.getRawButton(9)) {
            return (value += .02);
        }
        return (value);
    }

    //MODIFIES THE SHOOTER MODIEFIER THAT IS GIVEN TO IT
    public double changeValue2(double value) {
        if (joy1.getRawButton(8)) {
            return (value -= .02);
        }
        if (joy1.getRawButton(9)) {
            return (value += .02);
        }
        return (value);
    }

    //SHOOT FUNCTION
    //FAILSAFES:
    //1 TOP LEFT BUTTON
    //2 TOP RIGHT BUTON
    //3 BOTH TRIGGERS 
    public void shoot() {
        while (!buttonTopLeft.get()) {
            shooterLeft.set(1);
            shooterRight.set(-1);
        }
        while (!buttonBotLeft.get()) {
            shooterLeft.set(-0.4);
            shooterRight.set(0.4);
        }
        shooterLeft.set(0);
        shooterRight.set(0);
            
    }

    //DISPLAY CODE
    //SEE DRIVER STATION OUTPUT
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
        //END ROBOT CODE
    }
    
}
