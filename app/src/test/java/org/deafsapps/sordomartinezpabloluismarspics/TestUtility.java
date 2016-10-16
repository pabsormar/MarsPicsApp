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

package org.deafsapps.sordomartinezpabloluismarspics;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.MatrixCursor;

import org.deafsapps.sordomartinezpabloluismarspics.data.MarsPicsContract;
import org.deafsapps.sordomartinezpabloluismarspics.data.Utilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

/**
 * Created by ${USER} on ${DATE}.
 *
 * This test class checks the correct functionality of the .util.Utility class
 */
@RunWith(RobolectricTestRunner.class)   // 'Robolectric allows to use a Context instance
@Config(constants = BuildConfig.class)
public class TestUtility {

    private ContentResolver mResolver;

    @Before
    public void setUp() throws Exception {
        mResolver = RuntimeEnvironment.application.getContentResolver();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBuildContentValuesFromCursor() throws Exception {
        final int numIterations = 10;

        final MatrixCursor dummyCursor = new MatrixCursor(new String[] {
                MarsPicsContract.PicItemEntry._ID,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_TAG,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK}
        );

        for (int iter = 0; iter < numIterations; iter++) {
            dummyCursor.addRow(new Object[] {
                    iter,
                    iter,
                    "dummy date",
                    "dummy camera full name",
                    "dummy image link"}
            );
        }

        ContentValues[] contentValuesArray = org.deafsapps.sordomartinezpabloluismarspics.util.Utility
                .buildContentValuesFromCursor(dummyCursor);
        assertTrue("ContentValues array size does not match the number of iterations",
                dummyCursor.getCount() == numIterations);
        Utilities.validateCursorArray(dummyCursor, contentValuesArray);

        int insRows = mResolver.bulkInsert(
                MarsPicsContract.PicItemEntry.CONTENT_URI,
                contentValuesArray
        );
        assertTrue("Error: Bulk insertion not performed", insRows != -1);
        assertTrue("Warning: not all rows have been inserted", insRows == numIterations);
    }
}
