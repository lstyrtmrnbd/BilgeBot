package com.knox.bilgebot;

import java.awt.*;
import java.awt.event.InputEvent;

import static java.lang.Thread.sleep;


public class MouseJumper implements MouseMover {

    private Robot robot;

    private int destX;
    private int destY;

    private boolean hasMove = false;

    public MouseJumper(Robot robot) {

        this.robot = robot;
    }

    public void moveMouse() {

        try //Wait for a move
        {
            sleep(10);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        while (hasMove)
        {

            Point origin = MouseInfo.getPointerInfo().getLocation();

            robot.mouseMove(destX, destY);

            Point moved = MouseInfo.getPointerInfo().getLocation();

            robot.mousePress(InputEvent.BUTTON1_MASK);
            try {
                sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            try {
                sleep(30); // 500 in MouseMoveThread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasMove = false;

            robot.mouseMove(origin.x, origin.y); // return to origin
        }
    }

    public long setDestination(int x, int y)
    {
        destX = x;
        destY = y;

        hasMove = true;

        return 0;
    }

    public boolean hasMove()
    {
        return hasMove;
    }
}
