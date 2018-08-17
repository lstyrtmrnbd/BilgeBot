package com.knox.bilgebot;

import java.awt.*;

public class MouseThread extends Thread
{
    private boolean operable = true;

    public MouseMover mouseMover;

    public static MouseThread INSTANCE;

    public MouseThread(boolean smooth)
    {
        super("Mouse Move Thread");
        this.setDaemon(true);
        this.setPriority(Thread.MAX_PRIORITY);
        try
        {
            Robot robot = new Robot();
            if (smooth) {
                mouseMover = new MouseSmoother(robot);
            } else {
                mouseMover = new MouseJumper(robot);
            }
        } catch (AWTException e)
        {
            Status.I.log("Could not create MouseThread robot", Status.Severity.ERROR);
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
            mouseMover.moveMouse();
        }
    }
}
