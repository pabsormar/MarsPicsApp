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

package org.deafsapps.sordomartinezpabloluismarspics.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by ${USER} on ${DATE}.
 *
 * This class implements a ContentProvider-based object to abstract the interaction with the
 * database
 */
public class MarsPicsProvider extends ContentProvider {

    // Uri matcher possible values
    private static final int PIC_ENTRY = 100;
    private static final int PIC_ENTRIES = 200;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    // The UriMatcher will match each URI so different actions can be performed
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MarsPicsContract.CONTENT_AUTHORITY, MarsPicsContract.PATH_PIC + "/#", PIC_ENTRY);
        matcher.addURI(MarsPicsContract.CONTENT_AUTHORITY, MarsPicsContract.PATH_PIC, PIC_ENTRIES);

        return matcher;
    }

    private MarsPicsDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MarsPicsDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PIC_ENTRY:
                return MarsPicsContract.PicItemEntry.CONTENT_ITEM_TYPE;
            case PIC_ENTRIES:
                return MarsPicsContract.PicItemEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (selection == null) selection = "1";

        rowsDeleted = db.delete(
                MarsPicsContract.PicItemEntry.TABLE_NAME,
                selection,
                selectionArgs
        );

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Uri returnUri;

        long regId = db.insert(
                MarsPicsContract.PicItemEntry.TABLE_NAME,
                null,
                values);System.out.println();
        if (regId > 0) { returnUri = ContentUris.withAppendedId(
                MarsPicsContract.PicItemEntry.CONTENT_URI, regId); }
        else { throw new SQLException("Failed to insert row into " + uri); }

        // Notify registered observers that a row was updated and attempt to sync
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsInserted = 0;

        switch (match) {
            case PIC_ENTRIES:
                db.beginTransaction();
                try {
                    for (ContentValues cv : contentValues) {
                        long newID = db.insert(
                                MarsPicsContract.PicItemEntry.TABLE_NAME,
                                null,
                                cv
                        );
                        if (newID > 0) {
                            rowsInserted++;
                        }
                        else {
                            throw new SQLException("Failed to insert row into " + uri);
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                } finally {
                    db.endTransaction();
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, contentValues);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor retCursor;

        retCursor = db.query(
                MarsPicsContract.PicItemEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated;

        rowsUpdated = db.update(
                MarsPicsContract.PicItemEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    public static ContentValues buildContentValuesFromCursor(Cursor cursor) {
        ContentValues contentValues = new ContentValues();

        while (cursor.moveToNext()) {
            //contentValues.put
        }

        return null;
    }
}
