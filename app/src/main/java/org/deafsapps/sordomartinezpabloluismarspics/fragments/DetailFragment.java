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

package org.deafsapps.sordomartinezpabloluismarspics.fragments;


import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.deafsapps.sordomartinezpabloluismarspics.R;
import org.deafsapps.sordomartinezpabloluismarspics.activities.MainActivity;
import org.deafsapps.sordomartinezpabloluismarspics.data.MarsPicsContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private boolean isLandscape;
    private long mPosition = -1;

    public DetailFragment() {}

    public static DetailFragment newInstance(@NonNull Bundle bundle) {
        DetailFragment detailFragment = new DetailFragment();

        if (bundle != null) {
            detailFragment.setArguments(bundle);
        }

        return detailFragment;
    }

    public void setLandscape(boolean landscape) {
        this.isLandscape = landscape;
    }

    public void setmPosition(long mPosition) {
        this.mPosition = mPosition;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        setLandscape(args.getBoolean(MainActivity.KEY_LANDSCAPE_ORIENTATION));
        setmPosition(args.getLong(MainActivity.KEY_LISTVIEW_POSITION));

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View viewRoot = inflater.inflate(R.layout.fragment_detail, container, false);

        enableDetailFragmentToolbarOptions(true);

        if (mPosition != -1)  {
            Uri uri = ContentUris.withAppendedId(
                    MarsPicsContract.PicItemEntry.CONTENT_URI,
                    mPosition
            );
            Cursor cursor = getContext().getContentResolver().query(
                    uri,
                    new String[]{MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK},
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                String imageLink = cursor.getString(0);
                if (imageLink != null) {
                    Picasso.with(getContext())
                            .load(imageLink)
                            .into((ImageView) viewRoot.findViewById(R.id.image_fragment_detail));
                }
            }
        }

        return viewRoot;
    }

    /**
     *
     *
     * @param toBeEnabled
     */
    private void enableDetailFragmentToolbarOptions(boolean toBeEnabled) {
        // If in portrait configuration, some changes are applied to the Toolbar
        if (!isLandscape) {
            // Reports that this fragment would like to participate in populating the options menu
            // by receiving a call to onCreateOptionsMenu and related methods
            this.setHasOptionsMenu(true);

            final ActionBar mActionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
            if (mActionBar != null) {
                // Disables the title visibility
                mActionBar.setDisplayShowTitleEnabled(!toBeEnabled);
                // Shows "Back arrow" on the top left corner
                mActionBar.setDisplayHomeAsUpEnabled(toBeEnabled);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem whichItem) {
        // If 'home' is pressed, the 'onBackPressed()' method from the host 'Activity' is called
        // and 'true' is returned
        if (whichItem.getItemId() == android.R.id.home) {
            enableDetailFragmentToolbarOptions(false);
            getActivity().onBackPressed();
            return true;
        }

        return false;
    }
}
