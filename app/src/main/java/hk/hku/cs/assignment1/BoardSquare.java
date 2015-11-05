package hk.hku.cs.assignment1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by luoxianyang on 11/1/15.
 */
public class BoardSquare extends RelativeLayout{

    int _row;
    int _column;
    private GameBoard _board;
    private ImageView _blackView;
    private ImageView _whiteView;
    private ImageView _toPutBlackView;
    private ImageView _toPutWhiteView;

    public BoardSquare(Context context, GameBoard board, int row, int column) {
        super(context);

        this._board = board;
        this._row = row;
        this._column = column;
        this.setClickable(true);

        _blackView = new ImageView(context);
        _blackView.setImageResource(R.drawable.black_chess);
        _blackView.setAlpha(0.0f);
        _whiteView = new ImageView(context);
        _whiteView.setImageResource(R.drawable.white_chess);
        _whiteView.setAlpha(0.0f);
        _toPutBlackView = new ImageView(context);
        _toPutBlackView.setImageResource(R.drawable.black_t);
        _toPutBlackView.setAlpha(0.0f);
        _toPutWhiteView = new ImageView(context);
        _toPutWhiteView.setImageResource(R.drawable.white_t);
        _toPutWhiteView.setAlpha(0.0f);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );

        addView(_blackView, params);
        addView(_whiteView, params);
        addView(_toPutBlackView, params);
        addView(_toPutWhiteView, params);

        update();
    }

    public void update() {
        GameBoard.BoardCellState state = _board.getCellStateAtColumnAndRow(_row, _column);
        _whiteView.setAlpha(state==GameBoard.BoardCellState.BOARD_CELL_STATE_WHITE?1.0f:0.0f);
        _blackView.setAlpha(state==GameBoard.BoardCellState.BOARD_CELL_STATE_BLACK?1.0f:0.0f);
        _toPutBlackView.setAlpha((state==GameBoard.BoardCellState.BOARD_CELL_STATE_TO_PUT_BLACK && _board._isHintsOn)?1.0f:0.0f);
        _toPutWhiteView.setAlpha((state==GameBoard.BoardCellState.BOARD_CELL_STATE_TO_PUT_WHITE && _board._isHintsOn)?1.0f:0.0f);
    }
}
