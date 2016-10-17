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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.deafsapps.sordomartinezpabloluismarspics.BuildConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by ${USER} on ${DATE}.
 *
 * This test class checks the correct functionality when manipulating the database
 */
@RunWith(RobolectricTestRunner.class)   // 'Robolectric allows to use a Context instance
@Config(constants = BuildConfig.class)
public class TestDb {

    private Context mContext;
    private MarsPicsDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    @Before
    public void setUp() throws Exception {
        mContext = RuntimeEnvironment.application;
        assertNotNull("Context is null!", mContext);
        mDbHelper = new MarsPicsDbHelper(mContext);
        assertNotNull("MarsPicsDbHelper is null!", mDbHelper);
        mDb = mDbHelper.getWritableDatabase();
        assertNotNull("SQLiteDatabase is null!", mDb);
    }

    @After
    public void tearDown() throws Exception {
        mDb.close();
        mDbHelper.close();
    }

    @Test
    public void testCreateDb() throws Exception {
        // Checks whether the database was created
        assertTrue("DB did not open", mDb.isOpen());
    }

    @Test
    public void testDbColumns() {
        // Query the database and receive a Cursor back
        Cursor cursorQuery = mDb.query(
                MarsPicsContract.PicItemEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        String[] queriedColumnNames = cursorQuery.getColumnNames();
        String[] actualColumnNames = {MarsPicsContract.PicItemEntry.COLUMN_ITEM_TAG,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK};

        // This offset responds to the fact that there is an "_id" field automatically crated
        int dbColumnOffset = 1;
        for (int idx = dbColumnOffset; idx < queriedColumnNames.length; idx++) {
            assertTrue("Column name with index " + idx + " does not match",
                    queriedColumnNames[idx].equals(actualColumnNames[idx - dbColumnOffset]));
        }

        cursorQuery.close();
    }

    @Test
    public void testInsertDbEntry() {
        // Create feed values
        ContentValues feedEntryValues = Utilities.createDummyContentValuesObject();
        // Insert ContentValues into database and get a row ID back
        long feedEntryRowId = mDb.insert(
                MarsPicsContract.PicItemEntry.TABLE_NAME,
                null,
                feedEntryValues
        );
        assertTrue(feedEntryRowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursorQuery = mDb.query(
                MarsPicsContract.PicItemEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        assertTrue("The Cursor instance is empty", cursorQuery.moveToFirst());
        assertTrue("The Cursor instance has more than one row", !cursorQuery.moveToNext());

        cursorQuery.close();
    }

    @Test
    public void testDeleteDbEntry() {
        // Create feed values
        ContentValues feedEntryValues = Utilities.createDummyContentValuesObject();
        // Insert ContentValues into database and get a row ID back
        long feedEntryRowId = mDb.insert(
                MarsPicsContract.PicItemEntry.TABLE_NAME,
                null,
                feedEntryValues
        );
        assertTrue(feedEntryRowId != -1);

        int rowsAffected = mDb.delete(
                MarsPicsContract.PicItemEntry.TABLE_NAME,
                "_id=?",
                new String[] { String.valueOf(feedEntryRowId) }
        );
        assertTrue("Wrong number of rows affected by the deletion", rowsAffected == 1);
    }

    @Test
    public void testDeleteDb(){
        // To test whether the database is successfully deleted, all connections to it must be
        // removed first
        mDb.close();
        assertTrue("DB could not be deleted",
                mContext.deleteDatabase(MarsPicsDbHelper.DATABASE_NAME));
    }
}
