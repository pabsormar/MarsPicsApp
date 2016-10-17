package org.deafsapps.sordomartinezpabloluismarspics;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("org.deafsapps.sordomartinezpabloluismarspics", appContext.getPackageName());
    }

    /*
    MatrixCursor matrixCursor = new MatrixCursor(new String[] {
                MarsPicsContract.PicItemEntry._ID,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK}
        );
        matrixCursor.addRow(new Object[] {
                "1",
                "Date",
                "Full Camera Name",
                "http://img.wallpaperfolder.com/f/6640000320AB/peacock-feathers-images-stock-pictures.jpg"}
        );

        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        mAdapter = new MyListAdapter(getActivity(), matrixCursor);

        ContentValues contentValues = new ContentValues();
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE,
                "Dummy date");
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME,
                "Dummy camera full name");
        contentValues.put(MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK,
                "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/fcam/FLB_486265257EDR_F0481570FHAZ00323M_.JPG");
        //int rowsAffected = getContext().getContentResolver().delete(MarsPicsContract.PicItemEntry.CONTENT_URI, null, null);
        //Uri uri = getContext().getContentResolver().insert(MarsPicsContract.PicItemEntry.CONTENT_URI, contentValues);
     */
}
