package chess_piece;

import chessboard.*;
import main.GamePanel;


public class King extends ChessPiece{

    private boolean shortSide = false;
    private boolean longSide = false;

    public King(String color, int row, int col)
    {
        super("King", color, row, col);
        if(color.equals("white")) this.setImage(getImage("/Piece/white_king"));
        else this.setImage(getImage("/Piece/black_king"));
    }

    @Override
    public boolean isValidMove(int col, int row)
    {
        if (!this.is_within_board(col, row)) return false; //If not in board then return false
        if (this.did_not_move(col, row)) return false;
        if(ChessBoard.piece_squares[row][col]!=null && ChessBoard.piece_squares[row][col].getColor().equals(getColor())) return false;

        //Checking the special "Castling move"
        if(!hasMove && row == this.getOld_rowPosition() && Math.abs(col-this.getColPosition())==2)
        {
            //Castling short side
            if (col > this.getColPosition()) {
                if(ChessBoard.piece_squares[row][7].hasMove)
                {
                    return false;
                }
                if(ChessBoard.piece_squares[row][this.getColPosition()+1]!=null || ChessBoard.piece_squares[row][this.getColPosition()+2] != null)
                {
                    return false;
                }
                //Can't castle through check
                boolean beingChecked = isInCheck(col-1,row);
                if(beingChecked) return false;

                beingChecked = isInCheck(col,row);
                if(beingChecked) return false;

                shortSide = true;
                return true;
            }

            //Castling long side
            else if(col <this.getColPosition())
            {
                if(ChessBoard.piece_squares[row][0].hasMove)
                {
                    return false;
                }
                if(ChessBoard.piece_squares[row][this.getColPosition()-1]!=null || ChessBoard.piece_squares[row][this.getColPosition()-2] != null || ChessBoard.piece_squares[row][this.getColPosition()-3] != null )
                {
                    return false;
                }

                boolean beingChecked = isInCheck(col+1,row);
                if(beingChecked) return false;

                beingChecked = isInCheck(col,row);
                if(beingChecked) return false;

                longSide = true;
                return true;
            }
        }

        //Checking the king's main movement
        if (Math.abs(col-this.getColPosition())>1 || Math.abs(row-this.getOld_rowPosition())>1) return false;

        boolean beingChecked = isInCheck(col,row);
        if(beingChecked) return false;

        return true;
    }

    //Checks to see if moving the king to the new spot will cause it to go in check
    @Override
    public boolean isInCheck(int col, int row)
    {
        ChessPiece temp = ChessBoard.piece_squares[row][col];
        ChessBoard.piece_squares[row][col] = this;
        ChessBoard.piece_squares[this.getOld_rowPosition()][this.getColPosition()] = null;

        for(ChessPiece piece: GamePanel.piece_lists)
        {
            if(this.getColor().equals("white"))
            {
                //Scans to see whether the black pieces is checking the white king
                if(piece != temp && piece.getColor().equals("black") && piece.isValidMove(col, row))
                {
                    ChessBoard.piece_squares[row][col] = temp;
                    ChessBoard.piece_squares[this.getOld_rowPosition()][this.getColPosition()] = this;
                    return true;
                }
            }
            else {
                if(piece != temp && piece.getColor().equals("white") && piece.isValidMove(col, row))
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

    @Override
    public boolean canCastleShortSide() {
        return shortSide;
    }

    @Override
    public boolean canCastleLongSide() {
        return longSide;
    }
}
