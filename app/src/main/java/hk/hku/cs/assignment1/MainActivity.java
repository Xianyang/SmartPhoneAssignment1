package hk.hku.cs.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_startGame;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_startGame) {
            Intent intent = new Intent(getBaseContext(), GameBoardActivity.class);
            System.out.println("Game Start!");
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_startGame = (Button)findViewById(R.id.btn_startGame);
        btn_startGame.setOnClickListener(this);
    }
}
