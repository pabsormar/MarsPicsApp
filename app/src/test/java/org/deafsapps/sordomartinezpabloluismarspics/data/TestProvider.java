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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.deafsapps.sordomartinezpabloluismarspics.BuildConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ${USER} on ${DATE}.
 *
 * This test class checks the correct functionality when manipulating the Content Provider
 */
@RunWith(RobolectricTestRunner.class)   // 'Robolectric allows to use a Context instance
@Config(constants = BuildConfig.class)
public class TestProvider {

    private ContentResolver mResolver;

    @Before
    public void setUp() {
        mResolver = RuntimeEnvironment.application.getContentResolver();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetType() {
        // content://org.deafsapps.sordomartinezpabloluismarspics/pics/
        String dirType = mResolver.getType(MarsPicsContract.PicItemEntry.CONTENT_URI);
        // vnd.android.cursor.dir/org.deafsapps.sordomartinezpabloluismarspics/feed
        Assert.assertEquals("Error: the PicEntry CONTENT_URI should return PicItemEntry.CONTENT_TYPE",
                MarsPicsContract.PicItemEntry.CONTENT_TYPE, dirType);

        // content://org.deafsapps.sordomartinezpabloluismarspics/pics/1
        long regId = 1;
        String itemType = mResolver.getType(ContentUris.withAppendedId(
                MarsPicsContract.PicItemEntry.CONTENT_URI, regId));
        // vnd.android.cursor.item/org.deafsapps.latahona/feed/1
        Assert.assertEquals("Error: the PicEntry CONTENT_URI should return PicItemEntry.CONTENT_ITEM_TYPE",
                MarsPicsContract.PicItemEntry.CONTENT_ITEM_TYPE, itemType);
    }

    @Test
    public void testDelete() {
        // Deletes all records from the database (where = null)
        mResolver.delete(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                null,
                null);
        // Makes sure the deletion has been successful
        Cursor cursor = mResolver.query(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertTrue("Error: not all records were deleted", cursor.getCount() == 0);

        cursor.close();
    }

    @Test
    /**
     * The tested {@link ContentResolver#insert(Uri, ContentValues)} method actually
     * calls internally the {@link android.database.sqlite.SQLiteDatabase#insertOrThrow(String,
     * String, ContentValues)} method. Thus, both insertion and update are tested below.
     */
    public void testInsert() {
        ContentValues feedEntryValues = Utilities.createDummyContentValuesObject();

        // Checks the 'insert' process
        Uri feedInsertUri = mResolver.insert(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                feedEntryValues
        );
        long insertedRowId = ContentUris.parseId(feedInsertUri);
        // Verify we got a row back
        assertTrue(insertedRowId != -1);

        // A cursor is the primary interface to the query results
        Cursor cursorFromQuery = mResolver.query(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        // Checks that the Cursor is not empty
        assertTrue("Empty cursor returned", cursorFromQuery.moveToFirst());
        // Checks that the Cursor values match
        Utilities.validateCursor(cursorFromQuery, feedEntryValues);

        // Checks the 'update' process trying to insert a different row with the same TAG value
        feedEntryValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE, "An alternative date");
        Uri feedUpdateUri = mResolver.insert(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                feedEntryValues
        );
        long updatedNumberOfRows = ContentUris.parseId(feedUpdateUri);
        // Verify we got a row back
        assertTrue(updatedNumberOfRows != -1);

        cursorFromQuery.close();
    }

    @Test
    public void testBulkInsert() {
        final int numInsertions = 10;
        ContentValues[] bulkInsertContentValues = Utilities
                .createDummyContentValuesSomeObjects(numInsertions);

        int insertCount = mResolver.bulkInsert(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                bulkInsertContentValues
        );
        assertEquals(insertCount, numInsertions);

        // A cursor is your primary interface to the query results.
        Cursor cursorFromQuery = mResolver.query(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        // we should have as many records in the database as we've inserted
        assertEquals(cursorFromQuery.getCount(), numInsertions);

        // and let's make sure they match the ones we created
        cursorFromQuery.moveToFirst();
        for (int idx = 0; idx < numInsertions; idx++, cursorFromQuery.moveToNext() ) {
            Utilities.validateCursor(cursorFromQuery, bulkInsertContentValues[idx]);
        }

        cursorFromQuery.close();
    }

    @Test
    public void testQuery() {
        ContentValues feedEntryValues = Utilities.createDummyContentValuesObject();

        Uri feedInsertUri = mResolver.insert(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                feedEntryValues);
        long insertedRowId = ContentUris.parseId(feedInsertUri);
        // Verify we got a row back
        assertTrue(insertedRowId != -1);

        // Test the basic content provider query
        Cursor cursorFromQuery = mResolver.query(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        // Checks that the number of rows returned matches
        assertTrue("Number of registers returned should be 1", cursorFromQuery.getCount() == 1);
        // Checks that the Cursor is not empty
        assertTrue("Empty cursor returned", cursorFromQuery.moveToFirst());
        // Make sure we get the correct cursor out of the database
        Utilities.validateCursor(cursorFromQuery, feedEntryValues);

        cursorFromQuery.close();
    }

    @Test
    public void testQueries() {
        final int numIter = 10;
        ContentValues[] feedEntryValues = Utilities.createDummyContentValuesSomeObjects(numIter);
        // Performs several insertions into the database
        for (int iter = 0; iter < numIter; iter++) {
            Uri feedInsertUri = mResolver.insert(
                    MarsPicsContract.PicItemEntry.CONTENT_URI,
                    feedEntryValues[iter]);
            long insertedRowId = ContentUris.parseId(feedInsertUri);
            // Verify we got a row back
            assertTrue(insertedRowId != -1);
        }

        // Test the basic content provider query
        Cursor cursorFromQuery = mResolver.query(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        // Checks that the number of rows returned matches
        assertTrue("Number of registers returned should be " + numIter,
                cursorFromQuery.getCount() == numIter);
        // Checks that the Cursor is not empty
        assertTrue("Empty cursor returned", cursorFromQuery.moveToFirst());
        // Make sure we get the correct cursor out of the database
        Utilities.validateCursorArray(cursorFromQuery, feedEntryValues);

        cursorFromQuery.close();
    }

    @Test
    public void testUpdate() {
        ContentValues feedEntryValues = Utilities.createDummyContentValuesObject();

        Uri feedInsertUri = mResolver.insert(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                feedEntryValues);
        long locationRowId = ContentUris.parseId(feedInsertUri);
        // Verify we got a row back
        assertTrue(locationRowId != -1);

        // Creates new values to be updated
        ContentValues updatedValues = new ContentValues(feedEntryValues);
        updatedValues.put(MarsPicsContract.PicItemEntry._ID, locationRowId);   // Same row
        updatedValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE, "Another dummy title");

        Cursor cursorFromQuery = mResolver.query(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        // Updates the register and checks that only 1 row has been affected
        int count = mResolver.update(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                updatedValues,
                MarsPicsContract.PicItemEntry._ID + "= ?",
                new String[] { Long.toString(locationRowId)});
        assertEquals(count, 1);

        cursorFromQuery.close();
    }
}
