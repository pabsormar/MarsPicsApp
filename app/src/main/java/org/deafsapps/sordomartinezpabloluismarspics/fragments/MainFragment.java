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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.deafsapps.sordomartinezpabloluismarspics.BuildConfig;
import org.deafsapps.sordomartinezpabloluismarspics.R;
import org.deafsapps.sordomartinezpabloluismarspics.activities.MainActivity;
import org.deafsapps.sordomartinezpabloluismarspics.data.MarsPicsContract;
import org.deafsapps.sordomartinezpabloluismarspics.util.MarsPicsApiParser;
import org.deafsapps.sordomartinezpabloluismarspics.util.MyListAdapter;
import org.deafsapps.sordomartinezpabloluismarspics.util.Utility;

/**
 * Created by ${USER} on ${DATE}.
 *
 * A simple {@link ListFragment} subclass. This object already includes some convenient
 * interfaces and inherits such as the overridden method
 * {@link ListFragment#onListItemClick(ListView, View, int, long)}.
 * Activities that contain this fragment must implement the {@link MainFragment.Callback} interface
 * to handle interaction events with the hosting Activity.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG_MAIN_FRAGMENT = MainFragment.class.getSimpleName();
    private static final int CONTENT_PROVIDER_LOADER = 100;
    private static final int HTTP_API_LOADER = 200;

    private Callback mListener;
    private MyListAdapter mAdapter;

    private boolean isLandscape;
    private long mNumPage = 0;
    private final long mNumListItems = 20;

    public MainFragment() {}

    public static MainFragment newInstance(@Nullable Bundle bundle) {
        MainFragment mainFragment = new MainFragment();

        if (bundle != null) {
            mainFragment.setArguments(bundle);
        }

        return mainFragment;
    }

    public void setLandscape(boolean landscape) {
        this.isLandscape = landscape;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        setLandscape(args.getBoolean(MainActivity.KEY_LANDSCAPE_ORIENTATION));
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mAdapter = new MyListAdapter(getActivity(), null);
        setListAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Initializes the CursorLoader
        getLoaderManager().initLoader(CONTENT_PROVIDER_LOADER, null, this);
        getLoaderManager().initLoader(HTTP_API_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mListener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        listView.smoothScrollToPosition(position);
        view.setSelected(true);

        mListener.onMainFragmentInteraction(mNumPage * mNumListItems + position);
    }

    @Override
    public @Nullable Loader onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case CONTENT_PROVIDER_LOADER:
                return new CursorLoader(
                    getContext(),
                    MarsPicsContract.PicItemEntry.CONTENT_URI,
                    null,
                    MarsPicsContract.PicItemEntry._ID + ">=? AND " + MarsPicsContract.PicItemEntry._ID + "<?",
                    new String[] { String.valueOf(mNumPage * mNumListItems),
                            String.valueOf((1 + mNumPage) * mNumListItems) },
                    null);
            case HTTP_API_LOADER:
                return new MarsPicsApiParser(getContext());
            default:
                // An invalid Id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        // Moves the query results into the adapter, causing the ListView fronting this
        // adapter to re-display
        if (BuildConfig.DEBUG) { Log.d(TAG_MAIN_FRAGMENT, "Loader Id: " + loader.getId()); }
        switch (loader.getId()) {
            case CONTENT_PROVIDER_LOADER:
                if (BuildConfig.DEBUG) { Log.d(TAG_MAIN_FRAGMENT, "Provider loaded"); }
                mAdapter.swapCursor(cursor);
                break;
            case HTTP_API_LOADER:
                if (BuildConfig.DEBUG) { Log.d(TAG_MAIN_FRAGMENT, "HTTP query loaded"); }
                if (cursor != null) {
                    ContentValues[] contentValues = Utility.buildContentValuesFromCursor(cursor);
                    getContext().getContentResolver().bulkInsert(
                            MarsPicsContract.PicItemEntry.CONTENT_URI,
                            contentValues
                    );
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:
                // An invalid Id was passed in
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        /*
         * Clears out the adapter's reference to the Cursor to prevent memory leaks
         */
        mAdapter.swapCursor(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface Callback {
        void onMainFragmentInteraction(long actualPosition);
    }
}
