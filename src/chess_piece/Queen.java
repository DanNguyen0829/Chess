package chess_piece;

import chessboard.*;


public class Queen extends  ChessPiece{
    public Queen(String color, int row, int col)
    {
        super("Queen", color, row, col);
        if(color.equals("white")) this.setImage(getImage("/Piece/white_queen"));
        else this.setImage(getImage("/Piece/black_queen"));
    }

    @Override
    public boolean isValidMove(int col, int row) {
        if (!this.is_within_board(col, row)) return false;
        if (this.did_not_move(col, row)) return false;

        int currentRow = this.getOld_rowPosition();
        int currentCol = this.getColPosition();

        if (ChessBoard.piece_squares[row][col] != null &&
                ChessBoard.piece_squares[row][col].getColor().equals(this.getColor())) {
            return false;
        }

        int rowDiff = row - currentRow;
        int colDiff = col - currentCol;

        if (Math.abs(rowDiff) == Math.abs(colDiff)) {
            int rowStep = rowDiff > 0 ? 1 : -1;
            int colStep = colDiff > 0 ? 1 : -1;

            int checkRow = currentRow + rowStep;
            int checkCol = currentCol + colStep;

            while (checkRow != row && checkCol != col) {
                if (ChessBoard.piece_squares[checkRow][checkCol] != null) {
                    return false;
                }
                checkRow += rowStep;
                checkCol += colStep;
            }
            boolean beingChecked = isInCheck(col, row);
            if(beingChecked) return false;

            return true;
        }

        if (currentCol == col) {
            int step = rowDiff > 0 ? 1 : -1;
            for (int r = currentRow + step; r != row; r += step) {
                if (ChessBoard.piece_squares[r][col] != null) {
                    return false;
                }
            }
            boolean beingChecked = isInCheck(col, row);
            if(beingChecked) return false;

            return true;
        }

        if (currentRow == row) {
            int step = colDiff > 0 ? 1 : -1;
            for (int c = currentCol + step; c != col; c += step) {
                if (ChessBoard.piece_squares[row][c] != null) {
                    return false;
                }
            }
            boolean beingChecked = isInCheck(col, row);
            if(beingChecked) return false;

            return true;
        }

        return false;
    }
}
