package chess_piece;

import chessboard.*;
import main.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ChessPiece {
    //instance variables;
    private BufferedImage image;
    private String pieceType;
    private int rowPosition;
    private int colPosition;
    private int old_rowPosition;
    private int old_colPosition;
    private String color;
    public boolean isValidMove=true;
    public boolean hasMove;

    //Variables associated with drawing the piece
    private int draw_row; //These are not their actual location on the piece_squares 2D array, but where to draw
    private int draw_col;

    public ChessPiece(String type, String color, int row, int col)
    {
        pieceType = type;
        this.color = color;

        rowPosition = row;
        old_rowPosition = row;

        colPosition = col;
        old_colPosition = col;

        draw_row = ChessBoard.squareSize*row;
        draw_col = ChessBoard.squareSize*col;

        hasMove = false;
    }

    //The next 3 methods are for displaying the pieces on the board
    public BufferedImage getImage(String path_image) //returns the buffered image that will be used
    {
        BufferedImage tempImage = null;
        //The following is the code to load a buffered image from another folder
        try
        {
           tempImage  = ImageIO.read(getClass().getResourceAsStream(path_image+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempImage;
    }

    public void setImage(BufferedImage copy)
    {
        image = copy;
    }

    public void drawPiece(Graphics g)
    {
        g.drawImage(image, draw_col, draw_row, ChessBoard.squareSize, ChessBoard.squareSize, null);
    }



    //Following are getters and setters
    public int getRow()
    {
        return rowPosition;
    }
    public void setRow(int row)
    {
        rowPosition = row;
    }

    public int getCol()
    {
        return colPosition;
    }
    public void setCol(int col)
    {
        colPosition = col;
    }

    public int getDrawRow()
    {
        return draw_row;
    }
    public void setDrawRow(int row)
    {
        draw_row = row;
    }

    public int getDrawCol()
    {
        return draw_col;
    }
    public void setDrawCol(int col)
    {
        draw_col = col;
    }

    public int getOld_rowPosition()
    {
        return old_rowPosition;
    }
    public int getColPosition()
    {
        return old_colPosition;
    }

    public boolean isValidMove(int col, int row)
    {
        return isValidMove;
    }
    public void setValidMove(boolean true_or_false)
    {
        isValidMove = true_or_false;
    }

    public String getColor()
    {
        return color;
    }
    public String getPieceType()
    {
        return pieceType;
    }

    //Equals method to compare two different chess pieces based what type of piece they are and their colors
    public boolean equals(ChessPiece other)
    {
        return this.pieceType.equals(other.getPieceType()) && this.color.equals(other.getColor());
    }


    //Updates the current position of the piece to where the mouse is released
    public void updatePosition()
    {
        draw_col = colPosition* ChessBoard.squareSize;
        draw_row = rowPosition*ChessBoard.squareSize;
        ChessBoard.piece_squares[rowPosition][colPosition] = this;
        ChessBoard.piece_squares[old_rowPosition][old_colPosition] = null;
        old_colPosition = colPosition;
        old_rowPosition = rowPosition;
    }

    //Updates the current position of the piece depending on the parameters
    public void updatePosition(int col, int row)
    {
        colPosition = col;
        rowPosition = row;
        draw_col = colPosition* ChessBoard.squareSize;
        draw_row = rowPosition*ChessBoard.squareSize;
        ChessBoard.piece_squares[rowPosition][colPosition] = this;
        ChessBoard.piece_squares[old_rowPosition][old_colPosition] = null;
        old_colPosition = colPosition;
        old_rowPosition = rowPosition;
    }


    //Checks if the piece is within the chessBoard
    public boolean is_within_board(int col, int row)
    {
        if (0<=col && col<=7 && 0<=row && row<=7) return true;
        return false;
    }

    //Checks if the piece is in the same position or not
    public boolean did_not_move(int col, int row)
    {
        return (old_colPosition==col && old_rowPosition==row);
    }

    //Resets the position of the piece if the move was illegal
    public void reset()
    {
        colPosition = old_colPosition;
        rowPosition = old_rowPosition;

        draw_col = colPosition*ChessBoard.squareSize;
        draw_row = rowPosition*ChessBoard.squareSize;
    }

    //This method is to check whether the king is in check or not
    public boolean isInCheck()
    {
        for(ChessPiece piece: GamePanel.piece_lists)
        {
            if(this.getColor().equals("white"))
            {
                //Scans to see whether the black pieces is checking the white king
                if(piece.getColor().equals("black") && piece.isValidMove(GamePanel.getKingCol("white"), GamePanel.getKingRow("white")))
                {
                    return true;
                }
            }
            else {
                if(piece.getColor().equals("white") && piece.isValidMove(GamePanel.getKingCol("black"), GamePanel.getKingRow("black")))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isInCheck(int col, int row) //Checks to see whether moving to this square will cause the piece to be in check
    {
        ChessPiece temp = ChessBoard.piece_squares[row][col];
        ChessBoard.piece_squares[row][col] = this;
        ChessBoard.piece_squares[this.getOld_rowPosition()][this.getColPosition()] = null;

        for(ChessPiece piece: GamePanel.piece_lists)
        {
            if(this.getColor().equals("white"))
            {
                //Scans to see whether the black pieces is checking the white king
                if(piece != temp && piece.getColor().equals("black") && piece.isValidMove(GamePanel.getKingCol("white"), GamePanel.getKingRow("white")))
                {
                    ChessBoard.piece_squares[row][col] = temp;
                    ChessBoard.piece_squares[this.getOld_rowPosition()][this.getColPosition()] = this;
                    return true;
                }
            }
            else {
                if(piece != temp && piece.getColor().equals("white") && piece.isValidMove(GamePanel.getKingCol("black"), GamePanel.getKingRow("black")))
                {
                    ChessBoard.piece_squares[row][col] = temp;
                    ChessBoard.piece_squares[this.getOld_rowPosition()][this.getColPosition()] = this;
                    return true;
                }
            }
        }
        ChessBoard.piece_squares[row][col] = temp;
        ChessBoard.piece_squares[this.getOld_rowPosition()][this.getColPosition()] = this;
        return false;
    }

    //This method checks to see if the piece has any legal move
    public boolean hasPossibleMove()
    {
        for(int row=0; row<8; row++)
        {
            for(int col=0; col<8; col++)
                if(this.isValidMove(col, row))
                {
                    return true; //If the piece can move to a square then return false
                }
        }

        return false;
    }

    //These methods are mainly for the king

    //Return whether the king can castle short side
    public boolean canCastleShortSide()
    {
        return false;
    }

    //Return whether the king can castle long side
    public boolean canCastleLongSide()
    {
        return false;
    }
}
