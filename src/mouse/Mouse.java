package mouse;

import java.awt.event.*;

public class Mouse extends MouseAdapter{

    //variables to keep track where the mouse is located and whether it is pressed or not
    public static int x_position;
    public static int y_position;
    public static boolean isPressed;

    //Methods to initialize those variables
    @Override
    public void mousePressed(MouseEvent event)
    {
        isPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
        isPressed = false;
    }

    @Override
    public void mouseDragged(MouseEvent event)
    {
        x_position = event.getX();
        y_position = event.getY();
    }

    @Override
    public void mouseMoved(MouseEvent event)
    {
        x_position = event.getX();
        y_position = event.getY();
    }

}
