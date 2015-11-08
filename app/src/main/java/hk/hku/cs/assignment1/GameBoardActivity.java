package hk.hku.cs.assignment1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoardActivity extends AppCompatActivity implements View.OnClickListener{

    GameBoard _board;
    ArrayList <BoardSquare> _cellArray = new ArrayList<>(64);

    Button _newGameBtn;
    Button _rankBtn;
    Button _hintsonBtn;

    TextView _whiteScoreTextView;
    TextView _blackScoreTextView;
    ImageView _turnImageView;

    Timer _timer;
    TimerTask _timerTask;

    @Override
    public void onClick(View v) {
        if (v.getId()  == R.id.btn_new_game) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to Start New Game?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _board.startNewGame();
                            updateView();
                            _timer = new Timer();
                            _timer.schedule(_timerTask, 2000, 50);
                        }
                    });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (v.getId() == R.id.btn_hints_on) {
            _hintsonBtn.setText(_hintsonBtn.getText() == "HINTS ON"?"HINTS OFF":"HINTS ON");
            _board._isHintsOn = _hintsonBtn.getText() == "HINTS OFF"?true:false;
            updateView();
        } else if (v.getId() == R.id.btn_rank) {
            Intent intent = new Intent(getBaseContext(), RankActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        _board = new GameBoard();
        _newGameBtn = (Button)findViewById(R.id.btn_new_game);
        _newGameBtn.setOnClickListener(this);
        _rankBtn = (Button)findViewById(R.id.btn_rank);
        _rankBtn.setOnClickListener(this);
        _hintsonBtn = (Button)findViewById(R.id.btn_hints_on);
        _hintsonBtn.setOnClickListener(this);
        _hintsonBtn.setText("HINTS OFF");
        _board._isHintsOn = true;
        _whiteScoreTextView = (TextView)findViewById(R.id.txt_white_score);
        _blackScoreTextView = (TextView)findViewById(R.id.txt_black_score);
        _turnImageView = (ImageView)findViewById(R.id.img_turn);

        _timerTask = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Random r = new Random();
                        int rIndex = r.nextInt(_board._countOfBoardAvailableEachTurn) + 1;
                        for (int row = 0; row < 8; row++) {
                            for (int column = 0; column < 8; column++) {
                                GameBoard.BoardCellState state = _board.getCellStateAtColumnAndRow(row, column);
                                if (state == GameBoard.BoardCellState.BOARD_CELL_STATE_TO_PUT_BLACK ||
                                        state == GameBoard.BoardCellState.BOARD_CELL_STATE_TO_PUT_WHITE) {
                                    if (--rIndex == 0) {
                                        if (_board.isGameOverWhenMakeMoveToACell(row, column)) {
                                            // Game over
                                            updateView();
                                            gameOver();
                                            if (_timer != null) {
                                                _timer.cancel();
                                                _timer = null;
                                            }
                                            return;
                                        } else {
                                            updateView();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        };
        _timer = new Timer();
        //_timer.schedule(_timerTask, 2000, 200);
    }

    // 棋盘被点击
    public void boardClick(BoardSquare boardSquare) {
        int row = boardSquare._row;
        int column = boardSquare._column;
        if (_board.getCellStateAtColumnAndRow(row, column) == GameBoard.BoardCellState.BOARD_CELL_STATE_TO_PUT_WHITE
                || _board.getCellStateAtColumnAndRow(row, column) == GameBoard.BoardCellState.BOARD_CELL_STATE_TO_PUT_BLACK) {
            if (_board.isGameOverWhenMakeMoveToACell(row, column)) {
                // Game over
                updateView();
                gameOver();
            } else {
                updateView();
            }
        }
    }

    public void updateView() {
        for (BoardSquare boardSquare : _cellArray) {
            boardSquare.update();
        }

        // 修改分数
        String score = String.format("%d", _board._whiteScore);
        _whiteScoreTextView.setText(score);
        score = String.format("%d", _board._blackScore);
        _blackScoreTextView.setText(score);

        // 修改Turn
        _turnImageView.setImageResource(_board._nextMove == GameBoard.BoardCellState.BOARD_CELL_STATE_BLACK?R.drawable.black_chess:R.drawable.white_chess);
    }

    public void gameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 保存数据到本地
        Rank rank = new Rank();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("HIGH_SOCRE", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (_board._whiteScore > _board._blackScore) {
            builder.setMessage("Game Over, White Wins!");
            rank.aSideWinsAgain(sharedPref, editor, Rank.WinOrLossState.WHITE_WIN);
        } else if (_board._whiteScore < _board._blackScore) {
            builder.setMessage("Game Over, Black Wins!");
            rank.aSideWinsAgain(sharedPref, editor, Rank.WinOrLossState.BLACK_WIN);
        } else {
            builder.setMessage("Game Over, You Draw");
            rank.aSideWinsAgain(sharedPref, editor, Rank.WinOrLossState.DRAW);
        }

        builder.setCancelable(false);
        builder.setPositiveButton("Start New Game",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _board.startNewGame();
                        updateView();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // get screen width
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
