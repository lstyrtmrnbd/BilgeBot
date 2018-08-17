package com.knox.bilgebot;

import java.awt.*;
import java.awt.event.InputEvent;

import static java.lang.Thread.sleep;

public class MouseSmoother implements MouseMover {

    private Robot robot;

    private long initMoveTime;
    private long totalMoveTime;

    //y=a(x-h)^2+k
    private double _a;
    private double _h;
    private double _k;

    private int destX;
    private int destY;
    private int initX;
    private int initY;
    private double netDistance;

    private int prevMoveX = -1;
    private int prevMoveY = -1;

    private boolean hasMove = false;

    public MouseSmoother(Robot robot) {

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

        if(hasMove())
        {
            initMoveTime = System.currentTimeMillis();
        }

        while (hasMove) {
            if (!MouseInfo.getPointerInfo().getLocation().equals(new Point(prevMoveX, prevMoveY))) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setDestination(destX, destY);
                initMoveTime = System.currentTimeMillis();
                continue;
            }

            Point point = calculateMousePosition(System.currentTimeMillis() - initMoveTime);

            robot.mouseMove(point.x, point.y);
            prevMoveX = point.x;
            prevMoveY = point.y;

            if (point.x == destX && point.y == destY) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hasMove = false;
            }
        }
    }

    public long setDestination(int x, int y) {

        destX = x;
        destY = y;

        initX = MouseInfo.getPointerInfo().getLocation().x;
        initY = MouseInfo.getPointerInfo().getLocation().y;

        prevMoveX = initX;
        prevMoveY = initY;

        _h = initX;
        _k = initY;

        // a=(y-k)/(x-h)^2
        _a = (y - _k) / (Math.pow(x - _h, 2));

        netDistance = Math.sqrt(Math.pow(destX - initX, 2) + Math.pow(destY - initY, 2));
        totalMoveTime = (long) (netDistance * 2 + 300);
        hasMove = true;

        return totalMoveTime;
    }

    private Point calculateMousePosition(long deltaTime) {

        if(deltaTime > totalMoveTime) {
            return new Point(destX, destY);
        }

        double percentDone = ((double) deltaTime) / totalMoveTime;
        int x = (int) (percentDone * (destX - initX)) + initX;
        int y = (int) (_a * Math.pow(x - _h, 2) + _k);

        return new Point(x, y);
    }

    public boolean hasMove() { return hasMove; }
}
