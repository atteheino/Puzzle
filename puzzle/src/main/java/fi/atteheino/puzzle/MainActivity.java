package fi.atteheino.puzzle;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PlaceholderFragment placeholderFragment = new PlaceholderFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, placeholderFragment)
                    .commit();
        }

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        private ImageView myImage;
        private static final String IMAGEVIEW_TAG = "The Android Logo";


        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            myImage = (ImageView) rootView.findViewById(R.id.image);
            // Sets the tag
            if (myImage != null) {
                myImage.setTag(IMAGEVIEW_TAG);
                // set the listener to the dragging data
                myImage.setOnLongClickListener(new MyClickListener());
            } else {
                Log.e(TAG, "No myImage found");
            }

            rootView.findViewById(R.id.toplinear).setOnDragListener(new MyDragListener());
            rootView.findViewById(R.id.bottomlinear).setOnDragListener(new MyDragListener());
            return rootView;
        }

        private final class MyClickListener implements View.OnLongClickListener {

            @Override
            public boolean onLongClick(View view) {
                // create it from the object's tag
                ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                view.startDrag(data, //data to be dragged
                        shadowBuilder, //drag shadow
                        view, //local data about the drag and drop operation
                        0   //no needed flags
                );


                view.setVisibility(View.INVISIBLE);
                return true;
            }
        }

        class MyDragListener implements View.OnDragListener {
            Drawable normalShape = getResources().getDrawable(R.drawable.normal_shape);
            Drawable targetShape = getResources().getDrawable(R.drawable.target_shape);


            @Override
            public boolean onDrag(View v, DragEvent event) {

                Log.v(TAG, "Entered MyDragListener.onDrag() with event: " + event);
                // Handles each of the expected events
                switch (event.getAction()) {

                    //signal for the start of a drag and drop operation.
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        break;

                    //the drag point has entered the bounding box of the View
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            v.setBackground(targetShape);    //change the shape of the view
                        } else {
                            v.setBackgroundDrawable(targetShape);
                        }
                        break;

                    //the user has moved the drag shadow outside the bounding box of the View
                    case DragEvent.ACTION_DRAG_EXITED:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            v.setBackground(normalShape);    //change the shape of the view back to normal
                        } else {
                            v.setBackgroundDrawable(normalShape);
                        }
                        break;

                    //drag shadow has been released,the drag point is within the bounding box of the View
                    case DragEvent.ACTION_DROP:
                        // if the view is the bottomlinear, we accept the drag item
                        if (v == v.findViewById(R.id.bottomlinear)) {
                            View view = (View) event.getLocalState();
                            ViewGroup viewgroup = (ViewGroup) view.getParent();
                            if (viewgroup != null)
                                viewgroup.removeView(view);

                            //change the text
                            TextView text = (TextView) v.findViewById(R.id.text);
                            text.setText("The item is dropped");

                            LinearLayout containView = (LinearLayout) v;
                            containView.addView(view);
                            view.setVisibility(View.VISIBLE);
                        } else {
                            View view = (View) event.getLocalState();
                            view.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "You can't drop the image here",
                                    Toast.LENGTH_LONG).show();
                            break;
                        }
                        break;

                    //the drag and drop operation has concluded.
                    case DragEvent.ACTION_DRAG_ENDED:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            v.setBackground(normalShape);    //change the shape of the view back to normal
                        } else {
                            v.setBackgroundDrawable(normalShape);
                        }

                    default:
                        break;
                }
                return true;
            }
        }

    }


}
