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


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.deafsapps.sordomartinezpabloluismarspics.R;
import org.deafsapps.sordomartinezpabloluismarspics.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private boolean mTwoPane;

    public DetailFragment() {}

    public static DetailFragment newInstance(@NonNull Bundle bundle) {
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);

        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        mTwoPane = args.getBoolean(MainActivity.KEY_ORIENTATION);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View viewRoot = inflater.inflate(R.layout.fragment_detail, container, false);

        // If in portrait configuration, some changes are applied to the Toolbar
        if (!mTwoPane) {
            // Report that this fragment would like to participate in populating the options menu
            // by receiving a call to onCreateOptionsMenu and related methods
            this.setHasOptionsMenu(true);

            final ActionBar mActionBar = ((AppCompatActivity) this.getActivity()).getSupportActionBar();
            if (mActionBar != null) {
                // Disables the title visibility
                mActionBar.setDisplayShowTitleEnabled(false);
                // Shows "Back arrow" on the top left corner
                mActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        String imageLink = getArguments().getString(MainActivity.KEY_IMAGE_LINK);
        if (imageLink != null) {
            Picasso.with(getContext())
                    .load(imageLink)
                    .into((ImageView) viewRoot.findViewById(R.id.image_fragment_detail));
        }

        return viewRoot;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem whichItem) {
        // If 'home' is pressed, the 'onBackPressed()' method from the host 'Activity' is called
        // and 'true' is returned
        if (whichItem.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(whichItem);
    }
}
