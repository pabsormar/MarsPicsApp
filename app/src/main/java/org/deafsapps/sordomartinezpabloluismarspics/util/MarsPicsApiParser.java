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

package org.deafsapps.sordomartinezpabloluismarspics.util;

import android.content.Context;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.deafsapps.sordomartinezpabloluismarspics.BuildConfig;
import org.deafsapps.sordomartinezpabloluismarspics.R;
import org.deafsapps.sordomartinezpabloluismarspics.data.MarsPicsContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ${USER} on ${DATE}.
 *
 * An {@Link AsyncTaskLoader} subclass to query the NASA Open API
 */
public class MarsPicsApiParser extends AsyncTaskLoader<MatrixCursor> implements MarsPicsJsonHeaders{

    private static final String TAG_MARS_PICS_API_PARSER = MarsPicsApiParser.class.getSimpleName();
    private static final int HTTP_TIMEOUT_MILLIS = 3000;

    public MarsPicsApiParser(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public @Nullable MatrixCursor loadInBackground() {
        try {
            /*
             * Construct the URL for the NASA Open API (NOA) query
             * Possible parameters are available at NOA's page
             * https://api.nasa.gov/index.html#getting-started
             */
            final String NASA_API_BASE_URL =
                    "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?";
            final String NUMBER_ITEMS = "sol";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(NASA_API_BASE_URL).buildUpon()
                    .appendQueryParameter(NUMBER_ITEMS, "10")
                    .appendQueryParameter(API_KEY, getContext().getString(R.string.NASA_OPEN_API_KEY))
                    .build();

            URL url = new URL(builtUri.toString());   // Throws 'MalformedURLException'

            HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();   // Throws 'IOException'
            myConnection.setRequestMethod("GET");
            myConnection.setConnectTimeout(HTTP_TIMEOUT_MILLIS);
            myConnection.addRequestProperty("Content-type", "application/json");

            int respCode = myConnection.getResponseCode();   // Throws 'IOException'
            if (BuildConfig.DEBUG) { Log.d(TAG_MARS_PICS_API_PARSER, "The response is: " + respCode); }

            if (respCode == HttpURLConnection.HTTP_OK) {
                StringBuilder resultJsonString = new StringBuilder();

                InputStream myInStream = myConnection.getInputStream();   // Throws 'IOException'
                BufferedReader myBufferedReader = new BufferedReader(new InputStreamReader(myInStream));

                String line;
                while ((line = myBufferedReader.readLine()) != null) {
                    resultJsonString.append(line).append("\n");
                }
                myInStream.close();   // Always close the 'InputStream'
                myConnection.disconnect();

                return parseJsonString(resultJsonString.toString());
            }
        } catch (java.net.SocketTimeoutException e) {
            return null;
        } catch (java.io.IOException e) {
            return null;
        }

        return null;
    }

    @Override
    public void onCanceled(MatrixCursor data) {
        super.onCanceled(data);
    }

    @Override
    protected boolean onCancelLoad() {
        return super.onCancelLoad();
    }

    @Nullable
    private static MatrixCursor parseJsonString(String jsonString) {
        final MatrixCursor matrixCursor = new MatrixCursor(new String[] {
                MarsPicsContract.PicItemEntry._ID,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_TAG,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK}
        );

        try {
            JSONObject nasaJson = new JSONObject(jsonString);
            JSONArray photosArray = nasaJson.getJSONArray(NASA_API_LIST);
            if (BuildConfig.DEBUG) { Log.d(TAG_MARS_PICS_API_PARSER, "JSON array size: "
                    + String.valueOf(photosArray.length())); }

            // looping through all photos
            for (int idx = 0; idx < photosArray.length(); idx++) {
                int tag;
                String earthDate;
                String image;
                String cameraFullName;

                JSONObject photoObject = photosArray.getJSONObject(idx);
                tag = photoObject.getInt(NASA_API_TAG);
                earthDate = photoObject.getString(NASA_API_DATE);
                image = photoObject.getString(NASA_API_IMG);

                JSONObject cameraDetails = photoObject.getJSONObject(NASA_API_CAMERA);
                cameraFullName = cameraDetails.getString(NASA_API_FULL_NAME);

                matrixCursor.addRow(new Object[] {
                        idx,
                        tag,
                        earthDate,
                        cameraFullName,
                        image}
                );
            }

            return matrixCursor;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
