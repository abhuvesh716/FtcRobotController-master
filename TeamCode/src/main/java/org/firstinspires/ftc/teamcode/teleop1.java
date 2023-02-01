package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class teleop1 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("front_left");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("back_left");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("front_right");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("back_right");
        Servo claw = hardwareMap.servo.get("claw");
        DcMotor lift = hardwareMap.dcMotor.get("lift_motor");
        double maxSpeed = 0.65;


        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            //y = (y*y)*(abs(y)/y);
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]

            double[] speeds = {
                    (y + x + rx),
                    (y - x - rx),
                    (y - x + rx),
                    (y + x - rx)
            };

            // Because we are adding vectors and motors only take values between
            // [-1,1] we may need to normalize them.

            // Loop through all values in the speeds[] array and find the greatest
            // *magnitude*.  Not the greatest velocity.
            double max = Math.abs(speeds[0]);
            for(int i = 0; i < speeds.length; i++) {
                if ( max < Math.abs(speeds[i]) ) max = Math.abs(speeds[i]);
            }

            // If and only if the maximum is outside of the range we want it to be,
            // normalize all the other speeds based on the given speed value.
            if (max > maxSpeed) {
                for (int i = 0; i < speeds.length; i++) speeds[i] = (speeds[i] / max * maxSpeed);
            }

            // apply the calculated values to the motors.
            motorFrontLeft.setPower(speeds[0]);
            motorFrontRight.setPower(speeds[1]);
            motorBackLeft.setPower(speeds[2]);
            motorBackRight.setPower(speeds[3]);


            if(gamepad1.left_trigger > 0.5){
                lift.setPower(0.5);
            }
            else if(gamepad1.right_trigger > 0.5){
                lift.setPower(-0.5);
            }
            else{
                lift.setPower(0);
            }

            if(gamepad1.b){
                claw.setPosition(0);
            }

            if(gamepad1.a){
                claw.setPosition(0.5);
            }
        }
    }
};