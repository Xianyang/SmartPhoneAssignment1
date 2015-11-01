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
    ImageView _whiteView;
    private ImageView _toPutView;

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
        _toPutView = new ImageView(context);
        _toPutView.setImageResource(R.drawable.black_chess_t);
        _toPutView.setAlpha(0.0f);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );

        addView(_blackView, params);
        addView(_whiteView, params);
        addView(_toPutView, params);

        update();
    }

    public BoardSquare(Context context, AttributeSet attrs) { super(context, attrs); }

    public void update() {
        GameBoard.BoardCellState state = _board.getCellStateAtColumnAndRow(_row, _column);
        _whiteView.setAlpha(state==GameBoard.BoardCellState.BOARD_CELL_STATE_WHITE?1.0f:0.0f);
        _blackView.setAlpha(state==GameBoard.BoardCellState.BOARD_CELL_STATE_BLACK?1.0f:0.0f);
        _toPutView.setAlpha(state==GameBoard.BoardCellState.BOARD_CELL_STATE_TOPUT?1.0f:0.0f);
    }
}
