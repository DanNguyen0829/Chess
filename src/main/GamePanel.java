//This class shows how the game looks and controls the game loop
package main;

import chess_piece.*;
import chessboard.*;
import mouse.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    //instance variables and game components

    //window configs
    private final int width = 1000;
    private final int height = 800;

    //Game loop control variables
    private final int FPS = 60;
    Thread gameThread; //Thread is a class to create a game loop

    //Chess game variables
    private final ChessBoard chess_board;
    private final ArrayList<ChessPiece> piece_lists = new ArrayList<ChessPiece>();
    private final Mouse mouse = new Mouse();
    private ChessPiece selected_piece;
    private int turn_counter = 1;


    //Constructor
    public GamePanel()
    {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        addPieces();
        chess_board = new ChessBoard(piece_lists);
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
            if (deltaTime >= drawInterval) //if the amount of time that passed is >= the draw interval, the screen should be updated
            {
                update();
                repaint();
                deltaTime -= drawInterval;
            }
        }
    }

    public void update() //shows what is new on the screen
    {
        if(Mouse.isPressed) {
            if (selected_piece == null) { //When we have not picked up a piece
                
                //Set the selected piece to the piece where the mouse has clicked based the location of the square
                ChessPiece clicked_piece = ChessBoard.piece_squares[Mouse.y_position / ChessBoard.squareSize][Mouse.x_position / ChessBoard.squareSize];
                //y: row and x: col

                selected_piece = clicked_piece; //The piece is only selected if it has correct color

            }
            else move_piece(); //When we have successfully "picked up" the piece, we can now move it
        }
        else //When the piece is released, we update the new position for the piece
        {
            if (selected_piece!=null) {
                if (selected_piece.isValidMove)
                {
                    selected_piece.updatePosition();
                    removePiece(selected_piece.getRow(), selected_piece.getCol());
                    turn_counter++;
                }
                else selected_piece.reset();
                selected_piece = null;
            }
        }
    }

    public void move_piece() //Moves the piece with the mouse by updating the position of the piece with the position of the mouse
    {
        selected_piece.setDrawRow(Mouse.y_position-ChessBoard.squareSize/2);
        selected_piece.setDrawCol(Mouse.x_position-ChessBoard.squareSize/2);
        selected_piece.setRow((selected_piece.getDrawRow() + ChessBoard.squareSize / 2) / ChessBoard.squareSize);
        selected_piece.setCol((selected_piece.getDrawCol() + ChessBoard.squareSize / 2) / ChessBoard.squareSize);

        selected_piece.setValidMove(selected_piece.isValidMove(selected_piece.getCol(), selected_piece.getRow())); //Checks to see whether the piece's move is valid or not
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        chess_board.drawBoard(graphics2D);
        for(ChessPiece piece: piece_lists)
        {
            piece.drawPiece(graphics2D);
        }

        if(selected_piece!=null)
        {
            if(selected_piece.isValidMove)
            {
                graphics2D.setColor(Color.white);
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                graphics2D.fillRect(selected_piece.getCol() * ChessBoard.squareSize, selected_piece.getRow() * ChessBoard.squareSize, ChessBoard.squareSize, ChessBoard.squareSize);
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

                selected_piece.drawPiece(graphics2D);
            }
            else
            {
                graphics2D.setColor(Color.red);
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                graphics2D.fillRect(selected_piece.getCol()* ChessBoard.squareSize, selected_piece.getRow()* ChessBoard.squareSize, ChessBoard.squareSize, ChessBoard.squareSize);
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

                selected_piece.drawPiece(graphics2D);
            }
        }

    }

    public void addPieces() //Adds the pieces into our list
    {
        // white pieces
        piece_lists.add(new Pawn("white", 6, 0));
        piece_lists.add(new Pawn("white", 6, 1));
        piece_lists.add(new Pawn("white", 6, 2));
        piece_lists.add(new Pawn("white", 6, 3));
        piece_lists.add(new Pawn("white", 6, 4));
        piece_lists.add(new Pawn("white", 6, 5));
        piece_lists.add(new Pawn("white", 6, 6));
        piece_lists.add(new Pawn("white", 6, 7));
        piece_lists.add(new Rook("white", 7, 0));
        piece_lists.add(new Knight("white", 7, 1));
        piece_lists.add(new Bishop("white", 7, 2));
        piece_lists.add(new Queen("white", 7, 3));
        piece_lists.add(new King("white", 7, 4));
        piece_lists.add(new Bishop("white", 7, 5));
        piece_lists.add(new Knight("white", 7, 6));
        piece_lists.add(new Rook("white", 7, 7));
        // black pieces
        piece_lists.add(new Pawn("black", 1, 0));
        piece_lists.add(new Pawn("black", 1, 1));
        piece_lists.add(new Pawn("black", 1, 2));
        piece_lists.add(new Pawn("black", 1, 3));
        piece_lists.add(new Pawn("black", 1, 4));
        piece_lists.add(new Pawn("black", 1, 5));
        piece_lists.add(new Pawn("black", 1, 6));
        piece_lists.add(new Pawn("black", 1, 7));
        piece_lists.add(new Rook("black", 0, 0));
        piece_lists.add(new Knight("black", 0, 1));
        piece_lists.add(new Bishop("black", 0, 2));
        piece_lists.add(new Queen("black", 0, 3));
        piece_lists.add(new King("black", 0, 4));
        piece_lists.add(new Bishop("black", 0, 5));
        piece_lists.add(new Knight("black", 0, 6));
        piece_lists.add(new Rook("black", 0, 7));
    }

    public void removePiece(int row, int col)
    {
        for(int i=0; i<piece_lists.size(); i++)
        {
            if((piece_lists.get(i)!=selected_piece))
            {
                if(piece_lists.get(i).getRow()==row && piece_lists.get(i).getCol()==col)
                {
                    piece_lists.remove(i);
                }
            }
        }
    }

    /*public String turnColor()
    {
        if(turn_counter%2==0) return "black";
        else return "white";
    }*/

}
