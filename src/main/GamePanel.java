//This class shows how the game looks and controls the game loop
package main;

import chess_piece.*;
import chessboard.*;
import mouse.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

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
    private String[] promotionList = {"Knight", "Rook", "Bishop", "Queen"};
    public static final ArrayList<ChessPiece> piece_lists = new ArrayList<ChessPiece>();
    private final Mouse mouse = new Mouse();
    private ChessPiece selected_piece;
    private int turn_counter = 1;
    public boolean gameIsOver;


    //Constructor
    public GamePanel()
    {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        addPieces();
        chess_board = new ChessBoard(piece_lists);
        gameIsOver = whiteHasNoMove() || blackHasNoMove();
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

        /*The game will run as long the thread is created and both sides still have moves make to make
        *The game will, therefore, end when one of the sides have no legal move left (checkmate or stalemate)
         */
        do {
            currentTime = System.nanoTime();
            deltaTime += currentTime - startTime;
            startTime = currentTime;
            if (deltaTime >= drawInterval) //if the amount of time that passed is >= the draw interval, the screen should be updated
            {
                update();
                repaint();
                deltaTime -= (long) drawInterval;
            }
        }
        while(gameThread!=null && !gameIsOver);
    }

    public void update() //shows what is new on the screen
    {
        if(Mouse.isPressed) {
            if (selected_piece == null) { //When we have not picked up a piece
                if(Mouse.y_position / ChessBoard.squareSize<=7 && Mouse.x_position / ChessBoard.squareSize<=7 )
                {
                    //Set the selected piece to the piece where the mouse has clicked based the location of the square
                    ChessPiece clicked_piece = ChessBoard.piece_squares[Mouse.y_position / ChessBoard.squareSize][Mouse.x_position / ChessBoard.squareSize];
                    //y: row and x: col
                    if (clicked_piece!= null && clicked_piece.getColor().equals(turnColor()))
                    {
                        selected_piece = clicked_piece; //The piece is only selected if it has correct color
                    }

                }
            }
            else move_piece(); //When we have successfully "picked up" the piece, we can now move it
        }
        else //When the piece is released, we update the new position for the piece
        {
            if (selected_piece!=null) {
                if (selected_piece.isValidMove)
                {
                    //Checking if this a short castling move
                    if(selected_piece.getPieceType().equals("King") && selected_piece.canCastleShortSide())
                    {
                        //Move the right side rook to the left side of the king
                        ChessBoard.piece_squares[selected_piece.getRow()][7].updatePosition(selected_piece.getCol()-1, selected_piece.getRow());
                    }
                    //Checking if this a long castling move
                    if(selected_piece.getPieceType().equals("King") && selected_piece.canCastleLongSide())
                    {
                        //Move the left side rook to the right side of the king
                        ChessBoard.piece_squares[selected_piece.getRow()][0].updatePosition(selected_piece.getCol()+1, selected_piece.getRow());
                    }

                    selected_piece.updatePosition(); //Updates the position of the piece
                    removePiece(selected_piece.getRow(), selected_piece.getCol()); //Remove the opposing piece that was occupying that square
                    selected_piece.hasMove=true; //The piece has now moved

                    //Promoting the pawn
                    if(selected_piece.getPieceType().equals("Pawn") && (selected_piece.getRow()==0 || selected_piece.getRow()==7))
                    {
                        String color = selected_piece.getColor();
                        int row = selected_piece.getRow();
                        int col = selected_piece.getCol();

                        selected_piece = null;
                        removePiece(row, col);

                        Scanner input = new Scanner(System.in);
                        System.out.println("Choose which piece to promote your pawn (Type out the first letter)");
                        for(String piece: promotionList)
                        {
                            System.out.print(piece + " ");
                        }
                        System.out.println();
                        String chosenLetter = input.nextLine();
                        promote(chosenLetter, color, row, col);
                    }

                    turn_counter++;

                    gameIsOver = whiteHasNoMove() || blackHasNoMove();

                }
                else{
                    selected_piece.reset();
                }
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
    public void paintComponent(Graphics graphics) //The method responsible for drawing things on the screen
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

        //Displaying the game's status to the side of the screen
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 25));
        graphics2D.setColor(Color.white);

        if(turnColor().equals("white"))
        {
            graphics2D.drawString("WHITE'S TURN", 810, 650);
        }
        else
        {
            graphics2D.drawString("BLACK'S TURN", 810, 150);
        }

        //Display checkmate or stalemate when the game is over
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(new Font("Arial", Font.PLAIN, 25));
        graphics2D.setColor(Color.red);

        if(gameIsOver)
        {
            ChessPiece whiteKing = ChessBoard.piece_squares[getKingRow("white")][getKingCol("white")];
            ChessPiece blackKing = ChessBoard.piece_squares[getKingRow("black")][getKingCol("black")];

            if(whiteHasNoMove() && whiteKing.isInCheck())
            {
                graphics2D.drawString("CHECKMATE!", 810, 400);
                graphics2D.drawString("BLACK WINS", 810, 440);
            }
            else if(blackHasNoMove() && blackKing.isInCheck())
            {
                graphics2D.drawString("CHECKMATE!", 810, 400);
                graphics2D.drawString("WHITE WINS", 810, 440);
            }
            else
            {
                graphics2D.drawString("STALEMATE!", 810, 400);
                graphics2D.drawString("DRAW", 810, 440);
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

    //Remove a piece based on its location form the chess pieces list
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

    //These next two methods are for getting the king's location based on the color
    public static int getKingRow(String color)
    {
        for(ChessPiece piece: piece_lists)
        {
            if(piece.getPieceType().equals("King") && piece.getColor().equals(color))
            {
                return  piece.getRow();
            }
        }
        return -1;
    }
    public static int getKingCol(String color)
    {
        for(ChessPiece piece: piece_lists)
        {
            if(piece.getPieceType().equals("King") && piece.getColor().equals(color))
            {
                return  piece.getCol();
            }
        }
        return -1;
    }


    public boolean whiteHasNoMove()
    {
        for(ChessPiece piece: piece_lists)
        {
            if(piece.getColor().equals("white") && piece.hasPossibleMove()) return false; //If a piece still have legal move, return false;
        }
        return true;
    }

    public boolean blackHasNoMove()
    {
        for(ChessPiece piece: piece_lists)
        {
            if(piece.getColor().equals("black") && piece.hasPossibleMove()) {
                return false;
            }
            ; //If a piece still have legal move, return false;
        }
        return true;
    }

    //Return what color has the turn based on the turn counter
    public String turnColor()
    {
        if(turn_counter%2==0) return "black";
        else return "white";
    }

    //Promoting the pawn based on recursion
    public void promote(String letter, String color, int row, int col)
    {
        //The following if and else if statements acts as base cases
        if(letter.equals("K"))
        {
            ChessPiece promotePiece = new Knight(color, row, col);
            piece_lists.add(promotePiece);
            ChessBoard.piece_squares[row][col] = promotePiece;
            return;
        }
        if(letter.equals("R"))
        {
            ChessPiece promotePiece = new Rook(color, row, col);
            piece_lists.add(promotePiece);
            ChessBoard.piece_squares[row][col] = promotePiece;
            return;
        }
        if (letter.equals("B"))
        {
            ChessPiece promotePiece = new Bishop(color, row, col);
            piece_lists.add(promotePiece);
            ChessBoard.piece_squares[row][col] = promotePiece;
            return;
        }
        if (letter.equals("Q"))
        {
            ChessPiece promotePiece = new Queen(color, row, col);
            piece_lists.add(promotePiece);
            ChessBoard.piece_squares[row][col] = promotePiece;
            return;
        }

        System.out.println("TRY AGAIN!");
        System.out.println();
        Scanner input = new Scanner(System.in);
        System.out.println("Choose which piece to promote your pawn (Type out the first letter)");
        for(String piece: promotionList)
        {
            System.out.print(piece + " ");
        }
        System.out.println();
        String chosenLetter = input.nextLine();
        promote(chosenLetter, color, row, col); //Recursion call
    }

}
