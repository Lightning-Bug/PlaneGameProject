package comnitt.boston.planegameproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameRules extends AppCompatActivity {
     TextView GameRules;
     TextView Rules;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rules);

        GameRules=(TextView) findViewById(R.id.textView5);
        Rules=(TextView)findViewById(R.id.textView);

        Rules.setText("1. Kill the enemies by colliding with them.\n" +
                "2. If player lets 10 enemies to go safely then the Game is Over.\n" +
                "3. If player kills 6 friends the Game is Over. \n"+ "4. Score will be automatically updated and Health bar is shown.\n \n"+" 5.Collect points for bonus Health");
    }
}

