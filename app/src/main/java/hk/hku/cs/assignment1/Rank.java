package hk.hku.cs.assignment1;

import android.content.SharedPreferences;

/**
 * Created by luoxianyang on 11/5/15.
 */
public class Rank {

    public Rank() {}

    public enum WinOrLossState {
        BLACK_WIN,
        WHITE_WIN,
        DRAW
    }

    public String getASideWinTimes(SharedPreferences sharedPref, WinOrLossState side) {
        if (side == WinOrLossState.WHITE_WIN) {
            return String.format("%d", sharedPref.getInt("WHITE_WIN_TIMES", 0));
        } else if (side == WinOrLossState.BLACK_WIN) {
            return String.format("%d", sharedPref.getInt("BLACK_WIN_TIMES", 0));
        } else {
            return String.format("%d", sharedPref.getInt("DRAW_TIMES", 0));
        }
    }

    public String  getTotalGameTimes(SharedPreferences sharedPref) {
        return String.format("%d", sharedPref.getInt("TOTAL_TIMES", 0));
    }

    private void totalTimePlusOne(SharedPreferences sharedPref, SharedPreferences.Editor editor) {
        int totalGameTimes = sharedPref.getInt("TOTAL_TIMES", 0);
        editor.putInt("TOTAL_TIMES", ++totalGameTimes);
    }

    public void aSideWinsAgain(SharedPreferences sharedPref, SharedPreferences.Editor editor, WinOrLossState side) {
        if (side == WinOrLossState.WHITE_WIN) {
            int whiteWinTimes = sharedPref.getInt("WHITE_WIN_TIMES", 0);
            editor.putInt("WHITE_WIN_TIMES", ++whiteWinTimes);
        } else if (side == WinOrLossState.BLACK_WIN) {
            int blackWinTimes = sharedPref.getInt("BLACK_WIN_TIMES", 0);
            editor.putInt("BLACK_WIN_TIMES", ++blackWinTimes);
        } else {
            int drawTimes = sharedPref.getInt("DRAW_TIMES", 0);
            editor.putInt("DRAW_TIMES", ++drawTimes);
        }

        totalTimePlusOne(sharedPref, editor);
        editor.apply();
    }
}
