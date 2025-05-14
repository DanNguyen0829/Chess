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

                if (clicked_piece!=null && clicked_piece.getColor().equals(turnColor())) {
                    selected_piece = clicked_piece; //The piece is only selected if it has correct color
                }
            }
            else move_piece(); //When we have successfully "picked up" the piece, we can now move it
        }
        else //When the piece is released, we update the new position for the piece
        {
            if (selected_piece!=null) {
                if (selected_piece.isValidMove)
                {
                    selected_piece.updatePosition();
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
        piece_lists.add(new King("white", 0, 0));
        piece_lists.add(new King("black", 0, 1));
        piece_lists.add(new Pawn("white",0, 2));
        piece_lists.add(new Pawn("black", 0, 3));
    }

    public String turnColor()
    {
        if(turn_counter%2==0) return "black";
        else return "white";
    }

}
