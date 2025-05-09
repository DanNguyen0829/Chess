//This class shows how the game looks and controls the game loop
package main;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    //instance variables
    private final int width = 1000;
    private final int height = 800;
    private final ChessBoard chess_board;

    private final int FPS = 60;
    Thread gameThread; //Thread is a class to create a game loop

    //Constructor
    public GamePanel()
    {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);
        chess_board = new ChessBoard();
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
        double drawInterval = 1000000000.0/FPS; //It takes 16,666,677 nanoseconds = 0.01667 seconds to draw 1 frame
        long deltaTime = 0;
        long startTime = System.nanoTime();
        long currentTime;

        while(gameThread!=null)
        {
            currentTime = System.nanoTime();
            deltaTime += currentTime - startTime;
            startTime = currentTime;
            if (deltaTime >= drawInterval) //if the amount of time has passed is >= the draw interval, the screen should be updated
            {
                update();
                repaint();
                deltaTime -= drawInterval;
            }
        }
    }

    public void update() //shows what is on the screen
    {



    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        chess_board.drawBoard(graphics2D);
    }
}
