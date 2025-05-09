package chess_piece;

import main.ChessBoard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
        rowPosition = ChessBoard.squareSize*row;
        colPosition = ChessBoard.squareSize*col;

    }

    public BufferedImage getImage(String path_image)
    {
        BufferedImage tempImage = null;
        try
        {
           tempImage  = ImageIO.read(getClass().getResourceAsStream(path_image+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempImage;
    }

    public int getRow()
    {
        return rowPosition;
    }

    public int getCol()
    {
        return colPosition;
    }

    public String getColor()
    {
        return color;
    }

    public String getPieceType()
    {
        return pieceType;
    }
}
