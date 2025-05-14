package chessboard;

import java.awt.*;
import java.util.ArrayList;

import chess_piece.*;
public class ChessBoard {

    public static final int squareSize = 100;

    public static ChessPiece[][] piece_squares = new ChessPiece[8][8];

    public ChessBoard(ArrayList<ChessPiece> chess_list)
    {
        for(ChessPiece piece: chess_list)
        {
            piece_squares[piece.getRow()][piece.getCol()] = piece;
        }
    }

    public void drawBoard(Graphics g)
    {
        for(int row=0; row<8; row++)
        {
            for(int col=0; col<8; col++)
            {
                if( (row+col)%2 == 0)  g.setColor(new Color(255, 255, 204)); //Light squares
                else g.setColor(new Color(102, 51, 0)); //Dark squares;

                g.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
            }
        }
    }
}
