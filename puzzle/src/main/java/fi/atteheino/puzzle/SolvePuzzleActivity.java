package fi.atteheino.puzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import org.worldsproject.puzzle.PuzzleView;
import org.worldsproject.puzzle.enums.Difficulty;

import java.io.File;


/**
 * Created by Atte on 6.3.2014.
 */
public class SolvePuzzleActivity extends Activity {

    private PuzzleView pv;
    private Bitmap image;
    private Bitmap shadow;
    private Difficulty x;
    private int puzzle;
    private int puzzleShadow;

    private AlertDialog.Builder builder;
    private AlertDialog alert;
    private ImageView preview_view;
    private Dialog preview_dialog;

    public void onStart() {
        super.onStart();
        builder = new AlertDialog.Builder(this);

        puzzle = this.getIntent().getIntExtra("image", 0);
        puzzleShadow = this.getIntent().getIntExtra("image_shadow", 0);
        image = (Bitmap) BitmapFactory.decodeResource(getResources(), puzzle);
        shadow = (Bitmap) BitmapFactory.decodeResource(getResources(), puzzleShadow);
        String difficulty = this.getIntent().getStringExtra("difficulty");

        if (difficulty.equals("supereasy"))
            x = Difficulty.SUPEREASY;
        else if (difficulty.equals("easy"))
            x = Difficulty.EASY;
        else if (difficulty.equals("medium"))
            x = Difficulty.MEDIUM;
        else
            x = Difficulty.HARD;

        pv = (PuzzleView) this.findViewById(org.worldsproject.puzzle.R.id.puzzleView);

        // Now we need to test if it already exists.
        File testExistance = new File(path(puzzle, x.toString()));

        if (testExistance != null && testExistance.exists()) {
            pv.loadPuzzle(path(puzzle, x.toString()));
        } else {
            pv.loadPuzzle(image, x, path(puzzle, x.toString()), shadow);
        }

        preview_view = (ImageView) getLayoutInflater().inflate(
                org.worldsproject.puzzle.R.layout.preview_puzzle, null);
        preview_view.setImageBitmap(image);

        preview_dialog = new Dialog(this);
        preview_dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        preview_dialog.setContentView(preview_view);

        preview_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                preview_dialog.dismiss();
            }
        });

    }

    public void onPause() {
        super.onPause();
        pv.savePuzzle(path(puzzle, x.toString()));
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        b.putInt("puzzle", puzzle);
        b.putInt("puzzle_shadow", puzzleShadow);
        b.putString("difficulty", x.toString());
    }


    @Override
    public void onRestoreInstanceState(Bundle b) {
        super.onRestoreInstanceState(b);
        puzzle = b.getInt("puzzle");
        puzzleShadow = b.getInt("puzzle_shadow");
        x = Difficulty.getEnumFromString(b.getString("difficulty"));
        pv.loadPuzzle(path(puzzle, x.toString()));
    }

    private String path(int puzzle, String difficulty) {
        String rv = null;

        rv = getCacheDir().getAbsolutePath() + "/" + puzzle + "/"
                + difficulty + "/";

        return rv;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solve_puzzle_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.menu_new_game:
                finish();
                return true;
            case R.id.menu_reset:
                pv.shuffle();
                return true;
            case R.id.show_cover:
                preview_dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
