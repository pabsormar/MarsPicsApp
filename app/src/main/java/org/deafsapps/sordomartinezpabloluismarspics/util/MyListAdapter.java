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
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.deafsapps.sordomartinezpabloluismarspics.BuildConfig;
import org.deafsapps.sordomartinezpabloluismarspics.R;
import org.deafsapps.sordomartinezpabloluismarspics.data.MarsPicsContract;

/**
 * Created by ${USER} on ${DATE}.
 *
 * A {@Link CursorAdapter} subclass to be linked to a {@Link ListView}
 */
public class MyListAdapter extends CursorAdapter {

    public MyListAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
    }

    /*
     * The 'newView' method is used to inflate a new view and return it. No data
     * is bound to the view at this point.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.item_pic, parent, false);
        MyListAdapterViewHolder viewHolder = new MyListAdapterViewHolder(rowView);
        rowView.setTag(viewHolder);

        return rowView;
    }

    /*
     * The 'bindView' method is used to bind all data to a given view
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null) {
            final int position = cursor.getPosition();

            MyListAdapterViewHolder viewHolder = (MyListAdapterViewHolder) view.getTag();

            viewHolder.getmTextViewDate().setText(cursor
                    .getString(cursor.getColumnIndex(MarsPicsContract.PicItemEntry.COLUMN_ITEM_DATE))
            );
            viewHolder.getmTextViewCameraFullName().setText(cursor.
                    getString(cursor.getColumnIndex(MarsPicsContract.PicItemEntry.COLUMN_ITEM_CAMERA_FULL_NAME))
            );
            Picasso.with(context)
                    .load(cursor.getString(cursor.getColumnIndex(MarsPicsContract.PicItemEntry.COLUMN_ITEM_IMAGE_LINK)))
                    .into(viewHolder.getmImageViewImageLink());

            boolean isOddRow = (position % 2) == 0;   // positions are zero-indexed
            if (isOddRow) {
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_odd_key));
            }
        }
    }

    /*
     * Creating a 'ViewHolder' to speed up the performance
     */
    private static class MyListAdapterViewHolder {

        private TextView mTextViewDate;
        private TextView mTextViewCameraFullName;
        private ImageView mImageViewImageLink;

        public TextView getmTextViewDate() {
            return mTextViewDate;
        }

        public TextView getmTextViewCameraFullName() {
            return mTextViewCameraFullName;
        }

        public ImageView getmImageViewImageLink() {
            return mImageViewImageLink;
        }

        MyListAdapterViewHolder(View itemView) {
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_date_item);
            mTextViewCameraFullName = (TextView) itemView.findViewById(R.id.text_camera_full_name_item);
            mImageViewImageLink = (ImageView) itemView.findViewById(R.id.image_item);
        }
    }
}
