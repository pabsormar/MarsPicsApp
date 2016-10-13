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

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.deafsapps.sordomartinezpabloluismarspics.R;
import org.deafsapps.sordomartinezpabloluismarspics.fragments.DetailFragment;
import org.deafsapps.sordomartinezpabloluismarspics.fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private static final String DETAIL_FRAGMENT_TAG = DetailFragment.class.getSimpleName();
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_activity_main_detail) != null) {
            // The detail container view will be present only in the landscape layouts
            // (res/layout-land). If this view is present, then the Activity should be
            // in two-pane mode.
            mTwoPane = true;
            Log.d(DETAIL_FRAGMENT_TAG, "Loading detail Fragment...");
            //if (savedInstanceState == null) {
                Log.d(DETAIL_FRAGMENT_TAG, "Loading detail Fragment...");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_activity_main_detail, DetailFragment.newInstance(null), DETAIL_FRAGMENT_TAG)
                        .commit();
            //}

        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    public void onMainFragmentInteraction(Uri uri) {

    }
}
