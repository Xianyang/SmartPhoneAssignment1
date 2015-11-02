package hk.hku.cs.assignment1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class RankActivity extends AppCompatActivity {

    TextView _whiteWinsTimes;
    TextView _blackWinsTimes;
    TextView _drawTimes;
    TextView _totalTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        _whiteWinsTimes = (TextView)findViewById(R.id.WITHE_WINS_TIMES);
        _blackWinsTimes = (TextView)findViewById(R.id.BLACK_WINS_TIMES);
        _drawTimes = (TextView)findViewById(R.id.DRAW_TIMES);
        _totalTimes = (TextView)findViewById(R.id.TOTAL_TIMES);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("HIGH_SOCRE", 0);
        String whiteWinsTimes = String.format("%d", sharedPref.getInt("WHITE_WIN_TIMES", 0));
        String blackWinsTimes = String.format("%d", sharedPref.getInt("BLACK_WIN_TIMES", 0));
        String drawTimes = String.format("%d", sharedPref.getInt("DRAW_TIMES", 0));
        String totalTimes = String.format("%d", sharedPref.getInt("TOTAL_TIMES", 0));

        _whiteWinsTimes.setText(whiteWinsTimes);
        _blackWinsTimes.setText(blackWinsTimes);
        _drawTimes.setText(drawTimes);
        _totalTimes.setText(totalTimes);
    }

}
