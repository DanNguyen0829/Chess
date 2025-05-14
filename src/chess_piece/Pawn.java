package chess_piece;

public class Pawn extends  ChessPiece{
    public Pawn(String color, int row, int col)
    {
        super("Pawn", color, row, col);
        if(color.equals("white")) this.setImage(getImage("/Piece/white_pawn"));
        else this.setImage(getImage("/Piece/black_pawn"));
    }
}
