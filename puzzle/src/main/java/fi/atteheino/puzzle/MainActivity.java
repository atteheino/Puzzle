package fi.atteheino.puzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.worldsproject.puzzle.PuzzleSolveActivity;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private String difficulty = "supereasy";
    private int[] images;
    private Intent intent;
    private Button button;
    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;
    private ImageView fourthImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, PuzzleSolveActivity.class);
        firstImage = (ImageView) findViewById(R.id.firstPuzzleImage);
        firstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("image", R.drawable.imag1858);
                intent.putExtra("difficulty", difficulty);
                MainActivity.this.startActivity(intent);
            }
        });

        secondImage = (ImageView) findViewById(R.id.secondPuzzleImage);
        secondImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("image", R.drawable.koala);
                intent.putExtra("difficulty", difficulty);
                MainActivity.this.startActivity(intent);
            }
        });

        thirdImage = (ImageView) findViewById(R.id.thirdPuzzleImage);
        thirdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("image", R.drawable.penguins);
                intent.putExtra("difficulty", difficulty);
                MainActivity.this.startActivity(intent);
            }
        });

        fourthImage = (ImageView) findViewById(R.id.firstPuzzleImage);
        fourthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("image", R.drawable.tulips);
                intent.putExtra("difficulty", difficulty);
                MainActivity.this.startActivity(intent);
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

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.menu_difficulty_supereasy:
                return setDifficulty(item, "supereasy");
            case R.id.menu_difficulty_easy:
                return setDifficulty(item, "easy");
            case R.id.menu_difficulty_medium:
                return setDifficulty(item, "medium");
            case R.id.menu_difficulty_hard:
                return setDifficulty(item, "hard");
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private boolean setDifficulty(MenuItem item, String difficulty) {
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
        this.difficulty = difficulty;
        return true;
    }

}
