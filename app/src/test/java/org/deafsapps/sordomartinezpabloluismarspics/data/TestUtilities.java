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

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by ${USER} on ${DATE}.
 *
 * This test class provides some utilities to be used within the test data package
 */
public class TestUtilities {
    public static ContentValues createDummyContentValuesObject() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                "Dummy date");
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                "Dummy camera full name");
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK,
                "Dummy image link");

        return contentValues;
    }

    public static ContentValues[] createDummyContentValuesSomeObjects(int numInsertions) {
        ContentValues[] returnContentValues = new ContentValues[numInsertions];

        for (int iter = 0; iter < numInsertions; iter++) {
            ContentValues contentValues = new ContentValues();
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

    // Checks whether the Cursor values are equal to those expected
    public static void validateCursor(Cursor cursorFromQuery, ContentValues feedEntryValues) {
        Set<Map.Entry<String, Object>> expectedValues = feedEntryValues.valueSet();
        for (Map.Entry<String, Object> entry : expectedValues) {
            String columnName = entry.getKey();
            int idx = cursorFromQuery.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found ", idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + cursorFromQuery.getString(idx) +
                    "' did not match the expected value '" +
                    expectedValue + "'", expectedValue, cursorFromQuery.getString(idx));
        }
    }
}
