package main;

import java.awt.image.BufferedImage;

public class ChessPiece {
    //instance variables;
    private BufferedImage image;
    private String pieceType;
    private int rowPosition;
    private int colPosition;
    private String color;

    public ChessPiece(String type, String color, int row, int col)
    {
        pieceType = type;
        this.color = color;
        rowPosition = ChessBoard.squaresize*row;
        colPosition = ChessBoard.squaresize*col;
    }
}
