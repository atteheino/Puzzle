package fi.atteheino.puzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private String difficulty = "supereasy";
    private int[] images;
    private Intent intent;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button) findViewById(R.id.difficultyButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (difficulty.equals("supereasy")) {
                    difficulty = "easy";
                    button.setText(org.worldsproject.puzzle.R.string.easy);
                } else if (difficulty.equals("easy")) {
                    difficulty = "medium";
                    button.setText(org.worldsproject.puzzle.R.string.medium);
                } else if (difficulty.equals("medium")) {
                    difficulty = "hard";
                    button.setText(org.worldsproject.puzzle.R.string.hard);
                } else {
                    difficulty = "supereasy";
                    button.setText(org.worldsproject.puzzle.R.string.supereasy);
                }
            }

        });


        Log.v(TAG, "MainActivity OnCreate passed.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
