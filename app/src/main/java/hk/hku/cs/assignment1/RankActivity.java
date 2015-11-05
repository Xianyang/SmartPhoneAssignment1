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

        Rank rank = new Rank();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("HIGH_SOCRE", 0);
        String whiteWinsTimes = rank.getASideWinTimes(sharedPref, Rank.WinOrLossState.WHITE_WIN);
        String blackWinsTimes = rank.getASideWinTimes(sharedPref, Rank.WinOrLossState.BLACK_WIN);
        String drawTimes = rank.getASideWinTimes(sharedPref, Rank.WinOrLossState.DRAW);
        String totalTimes = rank.getTotalGameTimes(sharedPref);

        _whiteWinsTimes.setText(whiteWinsTimes);
        _blackWinsTimes.setText(blackWinsTimes);
        _drawTimes.setText(drawTimes);
        _totalTimes.setText(totalTimes);
    }

}
