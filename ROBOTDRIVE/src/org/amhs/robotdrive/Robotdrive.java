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
    Joystick joyLeft = new Joystick(1);
    Joystick joyRight = new Joystick(2);
    //Victors for drive Ports 1-4
    Victor frontLeftMotor = new Victor(1);
    Victor frontRightMotor = new Victor(2);
    Victor backLeftMotor = new Victor(3);
    Victor backRightMotor = new Victor(4);
    //Other Motor controllers 
    Victor shooterLeftMotor = new Victor(7);
    Victor shooterRightMotor = new Victor(6);
    Talon loadMotor = new Talon(8);
    Talon loaderRaiseLowerMotor = new Talon(9);
    //Other Variables for loader
    int isRaising = 1;
    boolean start = true;
    //Limit Switch Ports 1-4
    DigitalInput buttonTopLeft = new DigitalInput(1);
    DigitalInput buttonBotLeft = new DigitalInput(2);
    DigitalInput buttonTopRight = new DigitalInput(3);
    DigitalInput buttonBotRight = new DigitalInput(4);
   //Timers for shooter and loader
    Timer loadingArmTimer = new Timer();
    Timer shootingTimer = new Timer();
    //Doubles for joystick axis
    double joyLeftXAxis, joyRightXAxis, joyRightYAxis;
    //doubles for motor speed modifiers
    double frontLeftModifier = 0.9, frontRightModifier = 0.9, backLeftModifier = 1, backRightModifier = 1;
    double shooterLeftModifier = 1, shooterRightModifier = 1;
    //FOR COMPETITION, CHANGE ^ TO FINAL
    //Other  Assorted Vars
    boolean reversed = false;
    boolean old = false;
    boolean isPressed, isPressed2;
    //String status;

    //message printed when robot is initialized
    public void robotInit() {
        System.out.println("/n/n-----------------------/nRobot Initialized/n--------------------------/n/n");
        disp("Initialized", 1);
        DriverStationLCD.getInstance().updateLCD();
        Timer.delay(2);
    }

    public void autonomous() {
       
        frontLeftMotor.set(.60);
        frontRightMotor.set(-.60);
        backLeftMotor.set(.60);
        backRightMotor.set(-.60);
        Timer.delay(3.5);
        frontLeftMotor.set(0);
        frontRightMotor.set(0);
        backLeftMotor.set(0);
        backRightMotor.set(0);
        shooterLeftMotor.set(0.55);
        shooterRightMotor.set(-0.55);
        Timer.delay(0.4);
        shooterLeftMotor.set(0);
        shooterRightMotor.set(0);       
        pass();
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
            joyLeftXAxis = joyLeft.getX();
            joyRightXAxis = joyRight.getY();
            joyRightYAxis = joyRight.getX();
            reversed = joyLeft.getRawButton(8);
            if(!reversed)
            {
                 frontLeftMotor.set(frontLeftModifier * specialSquare(-(joyRightXAxis + joyLeftXAxis - joyRightYAxis)));//port 1
                 frontRightMotor.set(frontRightModifier * specialSquare((joyRightXAxis - joyLeftXAxis + joyRightYAxis)));//port 2
                 backLeftMotor.set(backLeftModifier * specialSquare(-(joyRightXAxis + joyLeftXAxis + joyRightYAxis)));//port 3
                 backRightMotor.set(backRightModifier * specialSquare((joyRightXAxis - joyLeftXAxis - joyRightYAxis)));//port 4
            }
            else
            {
                 frontLeftMotor.set(frontLeftModifier * specialSquare((joyRightXAxis + joyLeftXAxis - joyRightYAxis)));//port 1
                 frontRightMotor.set(frontRightModifier * specialSquare(-(joyRightXAxis - joyLeftXAxis + joyRightYAxis)));//port 2
                 backLeftMotor.set(backLeftModifier * specialSquare((joyRightXAxis + joyLeftXAxis + joyRightYAxis)));//port 3
                 backRightMotor.set(backRightModifier * specialSquare(-(joyRightXAxis - joyLeftXAxis - joyRightYAxis)));//port 4
            }
          
            //--------------------------------------
            //MODIIER CHANGING
            //DRIVING MODIFIERS ARE ON JOY 2
            //SHOOTING MODIFIERS ARE 6 AND 11 on Joy 1
            //--------------------------------------
            
            //Changing the modifiers
            if (!isPressed) {
                if (joyRight.getRawButton(6)) {
                    frontLeftModifier = changeValue(frontLeftModifier);
                    isPressed = true;
                }
                if (joyRight.getRawButton(11)) {
                    frontRightModifier = changeValue(frontRightModifier);
                    isPressed = true;
                }
                if (joyRight.getRawButton(7)) {
                    backLeftModifier = changeValue(backLeftModifier);
                    isPressed = true;
                }
                if (joyRight.getRawButton(10)) {
                    backRightModifier = changeValue(backRightModifier);
                    isPressed = true;
                }
            }
            if (!joyRight.getRawButton(6) && !joyRight.getRawButton(11) && !joyRight.getRawButton(7) && !joyRight.getRawButton(10)) {
                isPressed = false;
            }
            //shooter
            if (!isPressed2) {
                if (joyLeft.getRawButton(6)) {
                    shooterLeftModifier = changeValue2(shooterLeftModifier);
                    isPressed2 = true;
                }
                if (joyLeft.getRawButton(11)) {
                    shooterRightModifier = changeValue2(shooterRightModifier);
                    isPressed2 = true;
                }
            }
            if (!joyLeft.getRawButton(6) && !joyLeft.getRawButton(11)) {
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
           //JOY 2 BUTTON 5 REVERSES
           //-------------------------------------
           if(joyRight.getRawButton(4) || joyRight.getRawButton(5))
           {
               if(joyRight.getRawButton(4))
               {
                   loadMotor.set(-1);
               }
               else
               {
                   loadMotor.set(1);
               }
           }
           else
           {
               loadMotor.set(0);
           }
           //------------------------------
           //LOADING ARM CODE
           //RAISE/LOWER WITH ONE BUTTON
           //ALTERNATES BETWEEN UP AND DOWN
           //------------------------------
               
               if(start && joyLeft.getRawButton(3))
               {
                   loadingArmTimer.start();
                   start = false;
                   System.out.println(isRaising);
               }
               if(!start)
               {
                    loaderRaiseLowerMotor.set(isRaising);
               }
               if(loadingArmTimer.get() > 3)
               {
                   isRaising = isRaising*-1;
                   System.out.println(isRaising);
                   loadingArmTimer.stop();
                   loadingArmTimer.reset();
                   loaderRaiseLowerMotor.set(0);
                   start = true;
               }
           
           //SHOOTING CODE
           //FIRE WITH JOY 2 TRIGGER AND BUTTION 2
           //RUNS TILL IT HITS EITHER LIMIT SWITCH
           //EMERGENCY STOP IS JOY 1 TRIGGER AND JOY 2 TRIGGER
           //------------------------------
            if(joyRight.getRawButton(2))
            {
                shooterLeftMotor.set(.4);
                shooterRightMotor.set(-.4);
            }
            else
            {
                shooterLeftMotor.set(0);
                shooterRightMotor.set(0);
            }
            if (joyRight.getTrigger() && joyRight.getRawButton(2)) {
                shoot();
            }
            //-----------------------------
            //PASSING CODE
            //CONTROLS ARE THE SAME AS THE LEFT SIDE
            //EXCEPT ITS ON THE LEFT SIDE
            //-----------------------------
            if(joyLeft.getRawButton(2))
            {
                shooterLeftMotor.set(.4);
                shooterRightMotor.set(-.4);
            }
            else
            {
                shooterLeftMotor.set(0);
                shooterRightMotor.set(0);
            }
            if (joyLeft.getTrigger() && joyLeft.getRawButton(2)) {
                pass();
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
        if (joyRight.getRawButton(8)) {
            return (value -= .02);
        }
        if (joyRight.getRawButton(9)) {
            return (value += .02);
        }
        return (value);
    }

    //MODIFIES THE SHOOTER MODIEFIER THAT IS GIVEN TO IT
    public double changeValue2(double value) {
        if (joyLeft.getRawButton(8)) {
            return (value -= .02);
        }
        if (joyLeft.getRawButton(9)) {
            return (value += .02);
        }
        return (value);
    }

    //SHOOT FUNCTION
    //FAILSAFES:
    //1 TOP LEFT BUTTON
    //2 TOP RIGHT BUTON
    public void shoot() {
       
        shootingTimer.start();
        theRobot:
        while (!buttonTopLeft.get() && !buttonTopRight.get()) {
            
           
            
            shooterLeftMotor.set(1);
            shooterRightMotor.set(-1);
            
            if(shootingTimer.get() >= 1.5){
                break theRobot;
            }
        }
        while (!buttonBotLeft.get() && !buttonBotRight.get()) {
            shooterLeftMotor.set(-0.4);
            shooterRightMotor.set(0.4);
        }
        shooterLeftMotor.set(0);
        shooterRightMotor.set(0);
        shootingTimer.stop();
    }
//SHOOT FUNCTION
    //FAILSAFES:
    //1 TOP LEFT BUTTON
    //2 TOP RIGHT BUTON
    // values subject to change
    public void pass() {
       
        shootingTimer.start();
        theRobot:
        if(!joyLeft.getRawButton(3))
        {
        while (!buttonTopLeft.get() && !buttonTopRight.get()) {
            
           
            
            shooterLeftMotor.set(.8);
            shooterRightMotor.set(-.8);
            
            if(shootingTimer.get() >= 1.5){
                break theRobot;
            }
        }
        while (!buttonBotLeft.get() && !buttonBotRight.get()) {
            shooterLeftMotor.set(-0.1);
            shooterRightMotor.set(0.1);
        }
        shooterLeftMotor.set(0);
        shooterRightMotor.set(0);
        }
        else
        {
            while (!buttonTopLeft.get() && !buttonTopRight.get()) {
            
           
            
            shooterLeftMotor.set(.6);
            shooterRightMotor.set(-.6);
            
            if(shootingTimer.get() >= 1.5){
                break theRobot;
            }
        }
        while (!buttonBotLeft.get() && !buttonBotRight.get()) {
            shooterLeftMotor.set(-0.1);
            shooterRightMotor.set(0.1);
        }
        shooterLeftMotor.set(0);
        shooterRightMotor.set(0);
        }
        shootingTimer.stop();
    }
    //LOADER ARM FUNCTION
    //RAISES OR LOWERS THE LOADER ARM
    //VALUES ARE UNTESTED!!!!!!!!!!!!!!!


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
