package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TeleOpMech extends LinearOpMode {
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
            double denominator = Math.max(abs(y) + abs(x) + abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);


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