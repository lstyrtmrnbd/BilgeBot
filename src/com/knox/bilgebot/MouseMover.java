package com.knox.bilgebot;

/**
 * The interface by which the mouse is moved, so that it may be divorced from the threading logic.
 */
public interface MouseMover {

    void moveMouse();

    long setDestination(int x, int y);

    boolean hasMove();
}
