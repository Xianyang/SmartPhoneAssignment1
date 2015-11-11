package hk.hku.cs.assignment1;

import java.util.ArrayList;

/**
 * Created by luoxianyang on 11/1/15.
 */

public class GameBoard extends Board {
    int _whiteScore;
    int _blackScore;
    BoardCellState _nextMove;
    public boolean _isHintsOn;
    int _countOfBoardAvailableEachTurn;

    // 枚举棋子
    public enum BoardCellState {
        BOARD_CELL_STATE_EMPTY,
        BOARD_CELL_STATE_BLACK,
        BOARD_CELL_STATE_WHITE,
        BOARD_CELL_STATE_TO_PUT_BLACK,
        BOARD_CELL_STATE_TO_PUT_WHITE
    }

    // 初始化
    GameBoard() {
        startNewGame();
    }

    public void startNewGame() {
        super.clearBoard();
        super.setCellStateAtColumnAndRowWithState(BoardCellState.BOARD_CELL_STATE_WHITE, 3, 3);
        super.setCellStateAtColumnAndRowWithState(BoardCellState.BOARD_CELL_STATE_BLACK, 4, 3);
        super.setCellStateAtColumnAndRowWithState(BoardCellState.BOARD_CELL_STATE_BLACK, 3, 4);
        super.setCellStateAtColumnAndRowWithState(BoardCellState.BOARD_CELL_STATE_WHITE, 4, 4);

        _whiteScore = 2;
        _blackScore = 2;
        _nextMove = BoardCellState.BOARD_CELL_STATE_BLACK;

        _countOfBoardAvailableEachTurn = getToPutCell();
    }

    // 判断下一步可以放棋子的位置
    private int getToPutCell() {
        int count = 0;
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (isValidMoveToACell(row, column)) {
                    super.setCellStateAtColumnAndRowWithState(_nextMove == BoardCellState.BOARD_CELL_STATE_BLACK?BoardCellState.BOARD_CELL_STATE_TO_PUT_BLACK:BoardCellState.BOARD_CELL_STATE_TO_PUT_WHITE, row, column);
                    count++;
                }
            }
        }
        return count;
    }

    // 判断某个cell是否valid
    public boolean isValidMoveToACell(int row, int column) {
        return isValidMoveToACellWithState(row, column, _nextMove);
    }

    private boolean isValidMoveToACellWithState(int row, int column, BoardCellState state) {
        // 1.check这里是否有棋子
        if (super.getCellStateAtColumnAndRow(row, column) == BoardCellState.BOARD_CELL_STATE_WHITE || super.getCellStateAtColumnAndRow(row, column) == BoardCellState.BOARD_CELL_STATE_BLACK) {
            return false;
        }

        super.setCellStateAtColumnAndRowWithState(BoardCellState.BOARD_CELL_STATE_EMPTY, row, column);

        // 2.check其他8个方向是否可以满足放棋子的条件
        for (int directionNumber = 0; directionNumber < 8; directionNumber++) {
            if (isValidMoveToACellWithDirectionToState(row, column, directionNumber, state)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidMoveToACellWithDirectionToState(int row, int column, int directionNumber, BoardCellState state) {
        int []target = moveOneStepToADirection(new int[]{row, column}, directionNumber);
        int moveIndex = 1;

        while (target[0] >= 0 && target[0] <= 7 && target[1] >= 0 && target[1] <= 7) {
            BoardCellState stateAtTargetCell = super.getCellStateAtColumnAndRow(target[0], target[1]);

            if (moveIndex == 1) {
                if (stateAtTargetCell != invertState(state)) {
                    return false;
                }
            } else {
                // 得到相同颜色
                if (stateAtTargetCell == state) {
                    return true;
                }
                if (stateAtTargetCell == BoardCellState.BOARD_CELL_STATE_EMPTY || stateAtTargetCell == BoardCellState.BOARD_CELL_STATE_TO_PUT_WHITE || stateAtTargetCell == BoardCellState.BOARD_CELL_STATE_TO_PUT_BLACK) {
                    return false;
                }
            }
            moveIndex++;
            target = moveOneStepToADirection(target, directionNumber);
        }

        return false;
    }

    private int[] moveOneStepToADirection(int[] origin, int directionNumber) {
        int []target = new int[]{origin[0], origin[1]};
        switch (directionNumber) {
            case 0: // Up
                target[0]--;
                break;
            case 1: // Right
                target[1]++;
                break;
            case 2: // Down
                target[0]++;
                break;
            case 3: // Left
                target[1]--;
                break;
            case 4: // RightUp
                target[0]--;
                target[1]++;
                break;
            case 5: // RightDown
                target[0]++;
                target[1]++;
                break;
            case 6: // LeftDown
                target[0]++;
                target[1]--;
                break;
            case 7: // LeftUp
                target[0]--;
                target[1]--;
                break;
            default:
                break;
        }
        return target;
    }

    // 放下棋子 若game over则返回true
    public boolean isGameOverWhenMakeMoveToACell(int row, int column) {
        // save last step
        _boards.add(this.copy());
        _lastMoves.add(_nextMove);

        super.setCellStateAtColumnAndRowWithState(_nextMove, row, column);

        // 翻转，改变8个方向其他的棋子
        for (int directionNumber = 0; directionNumber < 8; directionNumber++) {
            flipChessFromOriginAtADirection(row, column, directionNumber, _nextMove);
        }

        _whiteScore = super.countCellsOfState(BoardCellState.BOARD_CELL_STATE_WHITE);
        _blackScore = super.countCellsOfState(BoardCellState.BOARD_CELL_STATE_BLACK);
        _nextMove = invertState(_nextMove);

        _countOfBoardAvailableEachTurn = getToPutCell();
        // 判断游戏是否需要change turn 或 end
        if (_countOfBoardAvailableEachTurn == 0) {
            // change turn
            _nextMove = invertState(_nextMove);
            _countOfBoardAvailableEachTurn = getToPutCell();
            if (_countOfBoardAvailableEachTurn == 0) {
                // game over
                System.out.println("Game Over");
                return true;
            }
        }
        return false;
    }

    // 翻转棋子
    private void flipChessFromOriginAtADirection(int row, int column, int directionNumber, BoardCellState toState) {
        if (!isValidMoveToACellWithDirectionToState(row, column, directionNumber, toState)) {
            return;
        }

        int [] target = new int[]{row, column};

        BoardCellState oppenentsState = invertState(toState);
        BoardCellState targetState;

        do {
            target = moveOneStepToADirection(target, directionNumber);
            targetState = super.getCellStateAtColumnAndRow(target[0], target[1]);
            setCellStateAtColumnAndRowWithState(toState, target[0], target[1]);
        } while (target[0] >= 0 && target[0] <= 7 && target[1] >= 0 && target [1] <= 7 && targetState == oppenentsState);
    }

    

    // back a step
    public void backAStep() {
        if (!_boards.isEmpty()) {
            _board = _boards.get(_boards.size() - 1);
            _boards.remove(_boards.size() - 1);

            _nextMove = _lastMoves.get(_lastMoves.size() - 1);
            _lastMoves.remove(_lastMoves.size() - 1);

            _whiteScore = super.countCellsOfState(BoardCellState.BOARD_CELL_STATE_WHITE);
            _blackScore = super.countCellsOfState(BoardCellState.BOARD_CELL_STATE_BLACK);
        }
    }

    private BoardCellState invertState(BoardCellState state) {
        if (state == BoardCellState.BOARD_CELL_STATE_BLACK) {
            return BoardCellState.BOARD_CELL_STATE_WHITE;
        }

        if (state == BoardCellState.BOARD_CELL_STATE_WHITE) {
            return BoardCellState.BOARD_CELL_STATE_BLACK;
        }
        return BoardCellState.BOARD_CELL_STATE_EMPTY;
    }
}



