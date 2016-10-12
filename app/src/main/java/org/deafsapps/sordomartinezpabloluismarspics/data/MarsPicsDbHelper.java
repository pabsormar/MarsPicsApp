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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ${USER} on ${DATE}.
 *
 * This class implements a SQLiteOpenHelper-based object to create and operate against databases
 */
public class MarsPicsDbHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "marspics.db";

    public MarsPicsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold feed items
        final String SQL_CREATE_PIC_ITEM_TABLE = "CREATE TABLE " +
                MarsPicsContract.PicItemEntry.TABLE_NAME + " (" +
                MarsPicsContract.PicItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE + " TEXT NOT NULL, " +
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME + " TEXT NOT NULL, " +
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PIC_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for the database.
        db.execSQL("DROP TABLE IF EXISTS " + MarsPicsContract.PicItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
