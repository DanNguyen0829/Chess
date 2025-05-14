package chess_piece;

public class King extends ChessPiece{

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
        if( Math.abs(col-this.getColPosition())>1 || Math.abs(row-this.getOld_rowPosition())>1) return false;
        return true;
    }

}
