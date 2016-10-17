/*
 * MIT License
 *
 * Copyright (c) 2016 Pablo L
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.deafsapps.sordomartinezpabloluismarspics.activities;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.deafsapps.sordomartinezpabloluismarspics.R;
import org.deafsapps.sordomartinezpabloluismarspics.data.MarsPicsContract;
import org.deafsapps.sordomartinezpabloluismarspics.fragments.DetailFragment;
import org.deafsapps.sordomartinezpabloluismarspics.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private static final String TAG_MAIN_FRAGMENT = MainFragment.class.getSimpleName();
    private static final String TAG_DETAIL_FRAGMENT = DetailFragment.class.getSimpleName();
    public static final String KEY_LISTVIEW_POSITION = "KEY_LISTVIEW_POSITION";
    public static final String KEY_LANDSCAPE_ORIENTATION = "KEY_LANDSCAPE_ORIENTATION";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        // Gets a reference to the 'Toolbar' and adding it as ActionBar for the 'Activity'
        final Toolbar appToolbar = (Toolbar) this.findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(appToolbar);

        if (findViewById(R.id.frame_layout_activity_main_detail) != null) {
            // The detail container view will be present only in the landscape layouts
            // (res/layout-land). If this view is present, then the Activity should be
            // in two-pane mode.
            mTwoPane = true;

            FragmentManager fm = getSupportFragmentManager();
            DetailFragment detailFragment = (DetailFragment) fm.findFragmentByTag(TAG_DETAIL_FRAGMENT);
            // First time DetailFragment is searched
            if (detailFragment == null) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(KEY_LANDSCAPE_ORIENTATION, mTwoPane);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout_activity_main_detail, DetailFragment.newInstance(bundle),
                                TAG_DETAIL_FRAGMENT)
                        .commit();
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(KEY_LANDSCAPE_ORIENTATION, mTwoPane);
                getSupportFragmentManager().beginTransaction()
                        .remove(detailFragment)
                        .replace(R.id.frame_layout_activity_main_detail, DetailFragment.newInstance(bundle),
                                TAG_DETAIL_FRAGMENT)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        FragmentManager fm = getSupportFragmentManager();
        MainFragment mainFragment = (MainFragment) fm.findFragmentByTag(TAG_MAIN_FRAGMENT);
        // First time MainFragment is searched
        if (mainFragment == null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(KEY_LANDSCAPE_ORIENTATION, mTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_activity_main, MainFragment.newInstance(bundle),
                            TAG_MAIN_FRAGMENT)
                    .commit();
        } else {
            mainFragment.setLandscape(mTwoPane);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_activity_main, mainFragment,
                            TAG_MAIN_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onMainFragmentInteraction(long position) {
        if (mTwoPane) {
            Uri uri = ContentUris.withAppendedId(
                    MarsPicsContract.PicItemEntry.CONTENT_URI,
                    position
            );
            Cursor cursor = getContentResolver().query(
                    uri,
                    new String[]{MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK},
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                String imageLink = cursor.getString(0);
                // In landscape orientation, an image is loaded onto the ImageView
                Picasso.with(this)
                        .load(imageLink)
                        .into((ImageView) findViewById(R.id.image_fragment_detail));

                cursor.close();
            }
        } else {
            // In portrait orientation, the current Fragment is replaced
            Bundle bundle = new Bundle();
            bundle.putBoolean(KEY_LANDSCAPE_ORIENTATION, mTwoPane);
            bundle.putLong(KEY_LISTVIEW_POSITION, position);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_activity_main,
                            DetailFragment.newInstance(bundle),
                            TAG_DETAIL_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Handles the Activity behaviour when the device back button is tapped. In case there is any
     * Fragment instance stored in the Back Stack, the top-most member is pulled out and loaded
     * onto the Activity. Otherwise, the application exits.
     */
    @Override
    public void onBackPressed() {
        // If there is any 'FragmentTransaction' in the "back stack", it is popped out;
        // otherwise the common back action is performed.
        if (this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            this.getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}
