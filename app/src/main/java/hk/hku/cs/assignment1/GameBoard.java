package hk.hku.cs.assignment1;

/**
 * Created by luoxianyang on 11/1/15.
 */

public class GameBoard extends Board {
    int _whiteScore;
    int _blackScore;
    BoardCellState _nextMove;
    public boolean _isHintsOn;

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

        getToPutCell();
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
        int [] origin = new int[]{row, column};
        int []target = moveOneStepToADirection(origin, directionNumber);
        int moveIndex = 1;

        while (origin[0] >= 0 && origin[0] <= 7 && origin[1] >= 0 && origin[1] <= 7) {
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
                if (stateAtTargetCell == BoardCellState.BOARD_CELL_STATE_EMPTY) {
                    return false;
                }
            }
            moveIndex++;
            target = moveOneStepToADirection(target, directionNumber);
        }

        return false;
    }

    private int[] moveOneStepToADirection(int[] origin, int directionNumber) {
        switch (directionNumber) {
            case 0: // Up
                origin[0]--;
                break;
            case 1: // Right
                origin[1]++;
                break;
            case 2: // Down
                origin[0]++;
                break;
            case 3: // Left
                origin[1]--;
                break;
            case 4: // RightUp
                origin[0]--;
                origin[1]++;
                break;
            case 5: // RightDown
                origin[0]++;
                origin[1]++;
                break;
            case 6: // LeftDown
                origin[0]++;
                origin[1]--;
                break;
            case 7: // LeftUp
                origin[0]--;
                origin[1]--;
                break;
            default:
                break;
        }
        return origin;
    }

    // 放下棋子 若game over则返回true
    public boolean isGameOverWhenMakeMoveToACell(int row, int column) {
        super.setCellStateAtColumnAndRowWithState(_nextMove, row, column);

        // 翻转，改变8个方向其他的棋子
        for (int directionNumber = 0; directionNumber < 8; directionNumber++) {
            flipChessFromOriginAtADirection(row, column, directionNumber, _nextMove);
        }

        _whiteScore = super.countCellsOfState(BoardCellState.BOARD_CELL_STATE_WHITE);
        _blackScore = super.countCellsOfState(BoardCellState.BOARD_CELL_STATE_BLACK);
        _nextMove = invertState(_nextMove);

        int toPutCount = getToPutCell();
        // 判断游戏是否需要change turn 或 end
        if (toPutCount == 0) {
            // change turn
            _nextMove = invertState(_nextMove);
            toPutCount = getToPutCell();
            if (toPutCount == 0) {
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

        int [] origin = new int[]{row, column};
        int [] target;

        BoardCellState oppenentsState = invertState(toState);
        BoardCellState targetState;

        do {
            target = moveOneStepToADirection(origin, directionNumber);
            targetState = super.getCellStateAtColumnAndRow(target[0], target[1]);
            setCellStateAtColumnAndRowWithState(toState, target[0], target[1]);
        } while (target[0] >= 0 && target[0] <= 7 && target[1] >= 0 && target [1] <= 7 && targetState == oppenentsState);
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
