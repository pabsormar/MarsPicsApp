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
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by ${USER} on ${DATE}.
 *
 * This test class provides some utilities to be used within the test data package
 */
public class Utilities {
    public static ContentValues createDummyContentValuesObject() {
        final int tag = 1;

        ContentValues contentValues = new ContentValues();
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_TAG,
                tag);
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                "Dummy date");
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                "Dummy camera full name");
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK,
                "Dummy image link");

        return contentValues;
    }

    public static ContentValues[] createDummyContentValuesSomeObjects(int numInsertions) {
        final ContentValues[] returnContentValues = new ContentValues[numInsertions];
        ContentValues contentValues;

        for (int iter = 0; iter < numInsertions; iter++) {
            contentValues = new ContentValues();
            contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_TAG,
                    iter);
            contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                    "Dummy date " + iter);
            contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                    "Dummy camera full name " + iter);
            contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK,
                    "Dummy image link " + iter);

            returnContentValues[iter] = contentValues;
        }

        return returnContentValues;
    }

    /**
     * Checks whether the Cursor values are equal to those expected
     *
     * @param cursorFromQuery  the {@link Cursor} just queried
     * @param feedEntryValues  the {@link ContentValues} that should have been fetched
     */
    public static void validateCursor(@NonNull Cursor cursorFromQuery, @NonNull ContentValues feedEntryValues) {

        Set<Map.Entry<String, Object>> setContentValues = feedEntryValues.valueSet();
        Iterator itr = setContentValues.iterator();

        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            String columnName = entry.getKey().toString();
            final int idx = cursorFromQuery.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found ", idx == -1);

            final String expectedValue = entry.getValue().toString();
            final String actualValue = cursorFromQuery.getString(idx);
            assertEquals("Value '" + actualValue +
                    "' did not match the expected value '" +
                    expectedValue + "'", expectedValue, actualValue);
        }
    }

    /**
     * Checks whether the array of Cursor values are equal to those expected
     *
     * @param cursorFromQuery  the {@link Cursor} just queried
     * @param feedEntryValues  the {@link ContentValues} array that should have been fetched
     */
    public static void validateCursorArray(Cursor cursorFromQuery, ContentValues[] feedEntryValues) {
        try {
            if (cursorFromQuery.getCount() == feedEntryValues.length) {
                if(cursorFromQuery.moveToFirst()) {
                    for (int idx = 0; idx < cursorFromQuery.getCount(); idx++) {
                        cursorFromQuery.moveToPosition(idx);
                        validateCursor(cursorFromQuery, feedEntryValues[idx]);
                    }
                }
            } else {
                throw new Exception("Both input arguments must have the same size/length");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
