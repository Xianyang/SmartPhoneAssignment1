package hk.hku.cs.assignment1;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by luoxianyang on 11/1/15.
 */
public class Board {
    GameBoard.BoardCellState [][] _board = new GameBoard.BoardCellState[8][8];

    public GameBoard.BoardCellState getCellStateAtColumnAndRow(int row, int column) {
        return _board[row][column];
    }

    public void setCellStateAtColumnAndRowWithState(GameBoard.BoardCellState state, int row, int column) {
        _board [row][column] = state;
    }

    public int countCellsOfState(GameBoard.BoardCellState state) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getCellStateAtColumnAndRow(i, j) == state) {
                    count++;
                }
            }
        }

        return count;
    }

    public void clearBoard() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                _board[row][column] = GameBoard.BoardCellState.BOARD_CELL_STATE_EMPTY;
            }
        }
    }
}
