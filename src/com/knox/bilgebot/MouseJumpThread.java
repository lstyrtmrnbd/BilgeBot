package com.knox.bilgebot;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created by Jacob on 7/14/2015.
 */
public class MouseJumpThread extends Thread
{
    private Robot robot;
    private boolean operable = true;

    private int destX;
    private int destY;

    private boolean hasMove = false;

    public static MouseJumpThread INSTANCE;

    public MouseJumpThread()
    {
        super("Mouse Move Thread");
        this.setDaemon(true);
        this.setPriority(Thread.MAX_PRIORITY);
        try
        {
            robot = new Robot();
        } catch (AWTException e)
        {
            Status.I.log("Could not create MouseMoveThread robot", Status.Severity.ERROR);
            e.printStackTrace();
            operable = false;
        }

        INSTANCE = this;
    }

    public void shutdown()
    {
        operable = false;
    }

    @Override
    public void run()
    {
        while (operable)
        {
            try //Wait for a move
            {
                sleep(10);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            while (hasMove && operable)
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
