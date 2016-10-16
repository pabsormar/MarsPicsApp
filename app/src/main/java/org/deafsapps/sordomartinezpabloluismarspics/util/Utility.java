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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ${USER} on ${DATE}.
 *
 * Abstract class which comprises several static methods to facilitate certain operations
 * along the application lifecycle
 */
public abstract class Utility {

    /**
     * Converts Cursor values into an array of ContentValues ready to interact with a
     * ContentProvider or database
     *
     * @param cursor  the {@link Cursor} object whose content will be converted
     * @return  an {@link ArrayList} of {@link ContentValues}
     */
    public static ContentValues[] buildContentValuesFromCursor(Cursor cursor) {
        final ContentValues[] returnContentValues = new ContentValues[cursor.getCount()];
        ContentValues auxContentValues;

        int idx = 0;
        if(cursor.moveToFirst()) {
            do {
                auxContentValues = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, auxContentValues);
                returnContentValues[idx] = auxContentValues;
                idx++;
            } while(cursor.moveToNext());
        }

        cursor.close();

        return returnContentValues;
    }
}
