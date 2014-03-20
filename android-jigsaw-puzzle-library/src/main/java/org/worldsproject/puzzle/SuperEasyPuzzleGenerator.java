package org.worldsproject.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import org.worldsproject.puzzle.enums.Difficulty;

import java.util.Random;

public class SuperEasyPuzzleGenerator {
    private static final Random RAN = new Random();

    private Context context;
    private Bitmap image;
    private int pieceSize;
    private Difficulty difficulty;

    Random ran = new Random();

    public SuperEasyPuzzleGenerator(Context c) {
        this.context = c;
    }

    public Puzzle generatePuzzle(Context context, Bitmap img,
                                 Difficulty difficulty, String location) {
        // Do we need to scale, and if so, by how much?
        this.pieceSize = difficulty.pieceSize();
        this.difficulty = difficulty;

        //We are going to make a 6 piece puzzle so we know the size.
        int wid = pieceSize * 3;
        int hei = pieceSize * 2;

        this.image = Bitmap.createScaledBitmap(img, wid,
                hei, false);
        // Now we need to get our width and height.
        int puzzle_width = (this.image.getWidth() / this.pieceSize);
        int puzzle_height = (this.image.getHeight() / this.pieceSize);

        // We use a linear array, because it is easier to iterate through and
        // each mask
        // knows who its neighbors are. We do not need to represent that again.
        Mask[] masks = new Mask[puzzle_width * puzzle_height];

        // Now we have an image that is able to be cut up perfectly.
        // Let's pick the left upper corner. Now ths is fixed.
        Mask startPoint = new Mask(context, true, false, difficulty);
        masks[0] = startPoint;

        // We need some flags. Because we are iterating through in a predictable
        // manner, it will be easy to know which edge and corner we are on.
        //
        // Corner_number keeps track of which corner we have passed. If we are
        // not yet past
        // corner number 1, and we see and edge (although we should only be
        // seeing edges) we know
        // that it is a top edge.
        //
        // left_edge will toggle after corner number 1, and before corner 2 as
        // left and right
        // alternate in appearance.
        int corner_number = 0;

        boolean left_edge = true;

        // We start at 1 because we already have our start point.
        for (int i = 1; i < masks.length; i++) {
            if (isCorner(i, puzzle_width, puzzle_height)) {
                corner_number++;
                //righ upper cornet
                if (corner_number == 1) {
                    masks[i] = new Mask(context, true, true, difficulty);
                    //left bottom corner
                } else if (corner_number == 2) {
                    masks[i] = new Mask(context, false, false, difficulty);
                    //Rign bottom corner
                } else if (corner_number == 3) {
                    masks[i] = new Mask(context, false, true, difficulty);
                }
                continue;
            }

            // This is all of the top edge cases.
            if (corner_number < 1) {
                masks[i] = new Mask(context, true, false, false, difficulty);
                masks[i].rotate(1);
                continue;
            }

            // This handles all of the bottom edge cases.
            if (corner_number == 2) {
                masks[i] = new Mask(context, true, true, false, difficulty);
                masks[i].rotate(3);
                continue;
            }

            /* heinatt: This is out for now. There are no left or right edges as the puzzle is so small.
            // Now the only possible edge that we could have reached so far are
            // the left
            // and right edges, so we can safely toggle.
            if (isEdge(i, puzzle_width, puzzle_height)) {
                if (left_edge) {
                    masks[i] = new Mask(context,
                            !masks[i - puzzle_width].isBottom(), RB(), RB(),
                            difficulty);
                } else {
                    masks[i] = new Mask(context, RB(), !masks[i - 1].isRight(),
                            !masks[i - puzzle_width].isBottom(), difficulty);
                    masks[i].rotate(2);
                }

                left_edge = !left_edge;
                continue;
            }

            // The only possible option now are the full pieces.
            masks[i] = new Mask(context, !(masks[i - puzzle_width].isBottom()),
                    RB(), RB(), !(masks[i - 1].isRight()), difficulty);
        */
        }

        Bitmap[] images = new Bitmap[masks.length];
        int position = 0;
        int offset = masks[0].getOffset();
        for (int y = 0; y < this.image.getHeight(); y += this.pieceSize) {
            for (int x = 0; x < this.image.getWidth(); x += this.pieceSize) {
                Bitmap store = Bitmap.createBitmap(pieceSize + (2 * offset),
                        pieceSize + (2 * offset), Bitmap.Config.ARGB_8888);

                Canvas c = new Canvas(store);
                c.drawBitmap(this.image, -x + offset, -y + offset, null);

                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

                c.drawBitmap(masks[position].getMask(), 0, 0, paint);

                images[position] = store;
                position++;
            }
        }

        position = 0;
        Bitmap puzzleShadow = Bitmap.createBitmap(wid + (2 * offset), hei + (2 * offset), Bitmap.Config.ARGB_8888);
        Canvas puzzleShadowCanvas = new Canvas(puzzleShadow);
        Paint paint = new Paint();
        paint.setAlpha(150);
        paint.setColor(Color.GRAY);
        for (int y = 0; y < this.image.getHeight(); y += this.pieceSize) {
            for (int x = 0; x < this.image.getWidth(); x += this.pieceSize) {
                puzzleShadowCanvas.drawBitmap(images[position], x, y, paint);
                position++;
            }
        }

        return new Puzzle(context, images, location, puzzle_width, difficulty, puzzleShadow);
    }

    /*
     * There are only 4 possible corner types, thus we simply generate a random
     * int from [0,4) and choose a corresponding corner.
     */
    private Mask getRandomCorner() {
        int which = RAN.nextInt(4);
        Mask rv;

        switch (which) {
            case 0:
                rv = new Mask(context, false, false, difficulty);
                break;
            case 1:
                rv = new Mask(context, false, true, difficulty);
                break;
            case 2:
                rv = new Mask(context, true, false, difficulty);
                break;
            default:
                rv = new Mask(context, true, true, difficulty);
                break;
        }

        //rv.rotate(1);
        return rv;
    }

    /*
     * The 4 possible corners are 0, (puzzle_width * puzzle_height)-1, 0 +
     * puzzle_width-1, (puzzle_height*puzzle_width-1)
     */
    private boolean isCorner(int position, int puzzle_width, int puzzle_height) {
        return (position == 0 || position == puzzle_width - 1
                || position == (puzzle_width * puzzle_height) - 1 || position == (puzzle_height * puzzle_width)
                - puzzle_width);
    }

    /*
     * Edges are a generalize corner case, as a corner IS an edge, just an edge
     * on two sides. Thus only one of the conditions needs to be true. Note that
     * because a corner is a special cased edge, one should check to see if it
     * is a corner before checking for an edge.
     */
    private boolean isEdge(int position, int puzzle_width, int puzzle_height) {
        return (position > 0 && position < puzzle_width)
                || (position > (puzzle_height * (puzzle_width - 1)) && position < (puzzle_width * puzzle_height))
                || (position % puzzle_width == 0)
                || ((position + 1) % puzzle_width == 0);
    }

    private boolean RB() {
        return RAN.nextBoolean();
    }
}
