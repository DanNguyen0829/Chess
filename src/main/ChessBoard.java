package main;

import java.awt.*;

public class ChessBoard {

    public static final int squaresize = 100;

    public void drawBoard(Graphics g)
    {
        for(int row=0; row<8; row++)
        {
            for(int col=0; col<8; col++)
            {
                if( (row+col)%2 == 0)  g.setColor(new Color(255, 255, 204)); //Light squares
                else g.setColor(new Color(102, 51, 0)); //Dark squares;

                g.fillRect(col * squaresize, row * squaresize, squaresize, squaresize);
            }
        }
    }
}
