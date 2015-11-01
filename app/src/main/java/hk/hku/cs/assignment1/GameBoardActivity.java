package hk.hku.cs.assignment1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class GameBoardActivity extends AppCompatActivity implements View.OnClickListener{

    GameBoard _board;
    ArrayList <BoardSquare> _cellArray = new ArrayList<BoardSquare>(64);

    Button _newGameBtn;
    Button _retractBtn;
    Button _hintsonBtn;

    TextView _whiteScoreTextView;
    TextView _blackScoreTextView;
    ImageView _turnImageView;

    @Override
    public void onClick(View v) {
        if (v.getId()  == R.id.btn_new_game) {
            _board.startNewGame();
            updateView();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        _board = new GameBoard();
        _newGameBtn = (Button)findViewById(R.id.btn_new_game);
        _newGameBtn.setOnClickListener(this);
        _retractBtn = (Button)findViewById(R.id.btn_retract);
        _retractBtn.setOnClickListener(this);
        _hintsonBtn = (Button)findViewById(R.id.btn_hints_on);
        _hintsonBtn.setOnClickListener(this);
        _whiteScoreTextView = (TextView)findViewById(R.id.txt_white_score);
        _blackScoreTextView = (TextView)findViewById(R.id.txt_black_score);
        _turnImageView = (ImageView)findViewById(R.id.img_turn);
    }

    public void boardClick(BoardSquare boardSquare) {
        int row = boardSquare._row;
        int column = boardSquare._column;
        if (_board.getCellStateAtColumnAndRow(row, column) == GameBoard.BoardCellState.BOARD_CELL_STATE_TOPUT) {
            _board.makeMoveToACell(row, column);

            updateView();
        }
    }

    public void updateView() {
        for (BoardSquare boardSquare1 : _cellArray) {
            boardSquare1.update();
        }

        // 修改分数
        String score = String.format(":%d", _board._whiteScore);
        _whiteScoreTextView.setText(score);
        score = String.format(":%d", _board._blackScore);
        _blackScoreTextView.setText(score);

        // 修改Turn
        _turnImageView.setImageResource(_board._nextMove == GameBoard.BoardCellState.BOARD_CELL_STATE_BLACK?R.drawable.black_chess:R.drawable.white_chess);

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        GridLayout gridLayout = (GridLayout)findViewById(R.id.gl);
        int width = gridLayout.getWidth() / 8;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardClick((BoardSquare)v);
            }
        };

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                GridLayout.Spec rowSpec = GridLayout.spec(i);
                GridLayout.Spec colSpec = GridLayout.spec(j);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.width = width;
                params.height = width;
                params.setGravity(Gravity.TOP);

                ImageView imageView = new ImageView(this);
                BoardSquare boardSquare = new BoardSquare(this, _board, i, j);
                _cellArray.add(boardSquare);
                boardSquare.setOnClickListener(listener);

                if ((i + j) %2 == 1) {
                    imageView.setImageResource(R.drawable.dark_green);
                } else {
                    imageView.setImageResource(R.drawable.light_green);
                }

                gridLayout.addView(imageView, params);
                gridLayout.addView(boardSquare, params);
            }
        }
    }
}
