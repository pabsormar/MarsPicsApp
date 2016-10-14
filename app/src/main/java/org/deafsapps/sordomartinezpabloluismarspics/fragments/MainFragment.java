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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.deafsapps.sordomartinezpabloluismarspics.R;
import org.deafsapps.sordomartinezpabloluismarspics.data.MarsPicsContract;
import org.deafsapps.sordomartinezpabloluismarspics.util.MarsPicsApiParser;
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
    private static final int CONTENT_PROVIDER_LOADER = 100;
    private static final int HTTP_API_LOADER = 200;

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

        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        mAdapter = new MyListAdapter(getActivity(), null);

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "onItemClick triggered", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Initializes the CursorLoader
        getLoaderManager().initLoader(CONTENT_PROVIDER_LOADER, null, this);
        getLoaderManager().initLoader(HTTP_API_LOADER, null, this).forceLoad();
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

    @Override
    public @Nullable Loader onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case CONTENT_PROVIDER_LOADER:
                return new CursorLoader(
                        getContext(),
                        MarsPicsContract.PicItemEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
            case HTTP_API_LOADER:
                return new MarsPicsApiParser(getContext());
            default:
                // An invalid Id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        /*
         * Moves the query results into the adapter, causing the ListView fronting this
         * adapter to re-display
         */
        switch (loader.getId()) {
            case CONTENT_PROVIDER_LOADER:
                mAdapter.swapCursor(cursor);
            case HTTP_API_LOADER:

                //getContext().getContentResolver().bulkInsert(MarsPicsContract.PicItemEntry.CONTENT_URI,
                //        new ContentValues[]);
                Log.e(TAG_MAIN_FRAGMENT, "HTTP query loaded");
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
        void onMainFragmentInteraction(Uri uri);
    }
}
