//This class shows how the game looks and controls the game loop
package main;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    //instance variables
    private int width = 1200;
    private int height = 800;

    private final int FPS = 60;
    Thread gameThread; //Thread is a class to create a game loop

    //Constructor
    public GamePanel()
    {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);
    }

    //The method starts the game
    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() //Responsible for the game loop
    {
        while(gameThread!=null)
        {
            System.out.println("The game is running");
        }
    }

    public void update() //shows what is on the screen
    {

    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
    }
}
