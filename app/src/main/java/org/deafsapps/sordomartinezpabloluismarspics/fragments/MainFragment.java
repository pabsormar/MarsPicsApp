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
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.deafsapps.sordomartinezpabloluismarspics.R;
import org.deafsapps.sordomartinezpabloluismarspics.data.MarsPicsContract;
import org.deafsapps.sordomartinezpabloluismarspics.util.MyListAdapter;

/**
 * Created by ${USER} on ${DATE}.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.Callback} interface
 * to handle interaction events with the hosting Activity.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG_MAIN_FRAGMENT = MainFragment.class.getSimpleName();
    private static final int URL_LOADER = 0;

    private Callback mListener;
    private MyListAdapter mAdapter;

    public MainFragment() {}

    public static MainFragment newInstance(@Nullable Bundle bundle) {
        MainFragment mainFragment = new MainFragment();

        if (bundle != null) {
            mainFragment.setArguments(bundle);
        }

        return mainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        MatrixCursor matrixCursor = new MatrixCursor(new String[] {
                MarsPicsContract.PicItemEntry._ID,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK}
        );
        matrixCursor.addRow(new Object[] {
                "1",
                "Date",
                "Full Camera Name",
                "http://img.wallpaperfolder.com/f/6640000320AB/peacock-feathers-images-stock-pictures.jpg"}
        );

        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        mAdapter = new MyListAdapter(getActivity(), matrixCursor);

        ContentValues contentValues = new ContentValues();
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                "Dummy date");
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                "Dummy camera full name");
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK,
                "http://img.wallpaperfolder.com/f/6640000320AB/peacock-feathers-images-stock-pictures.jpg");
        Uri uri = getContext().getContentResolver().insert(MarsPicsContract.PicItemEntry.CONTENT_URI, contentValues);
        listView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Initializes the CursorLoader
        getLoaderManager().initLoader(URL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /*
        public void onButtonPressed(Uri uri) {
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }
    */
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

    /**
     * Callback invoked when the system has initialized the Loader and is ready to start
     * the query.
     *
     * @param loaderId  the ID value passed to the 'initLoader()' call
     * @param args  any extra optional parameters
     * @return  a CursorLoader which will be used to start the query
     */
    @Override
    public @Nullable Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case URL_LOADER:
                return new CursorLoader(
                        getContext(),
                        MarsPicsContract.PicItemEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
            default:
                // An invalid Id was passed in
                return null;
        }
    }

    /**
     * Defines the callback that {@Link CursorLoader} calls when it has finished its query
     *
     * @param loader
     * @param cursor  the {@Link Cursor} holding the data to update the adapter
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        /*
         * Moves the query results into the adapter, causing the ListView fronting this
         * adapter to re-display
         */
        mAdapter.swapCursor(cursor);
    }

    /**
     * Invoked when the {@Link CursorLoader} is being reset
     *
     * @param loader
     */
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
        void onMainFragmentInteraction(Uri uri);
    }
}
