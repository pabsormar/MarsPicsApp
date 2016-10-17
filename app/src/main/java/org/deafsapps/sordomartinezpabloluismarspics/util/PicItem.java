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

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.deafsapps.sordomartinezpabloluismarspics.BuildConfig;

/**
 * Created by ${USER} on ${DATE}.
 *
 * This class defines the object which will mainly compose the application
 */
public class PicItem implements Parcelable {

    private static final String TAG_PIC_ITEM = PicItem.class.getSimpleName();

    private String mPicDate;
    private String mPicCameraFullName;
    private String mPicImageLink;

    public PicItem() {}

    // This constructor relates to the 'Parcelable' condition of this class
    public PicItem(Parcel pc) {
        mPicDate = pc.readString();
        mPicCameraFullName = pc.readString();
        mPicImageLink = pc.readString();
    }

    public String getmPicDate() {
        return mPicDate;
    }
    public void setmPicDate(String mPicDate) {
        this.mPicDate = mPicDate;
    }

    public String getmPicCameraFullName() {
        return mPicCameraFullName;
    }
    public void setmPicCameraFullName(String mPicCameraFullName) {
        this.mPicCameraFullName = mPicCameraFullName;
    }

    public String getmPicImageLink() {
        return mPicImageLink;
    }
    public void setmPicImageLink(String mPicImageLink) {
        this.mPicImageLink = mPicImageLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (BuildConfig.DEBUG) Log.d(TAG_PIC_ITEM, "writeToParcel... " + flags);

        dest.writeString(mPicDate);
        dest.writeString(mPicCameraFullName);
        dest.writeString(mPicImageLink);
    }

    /**
     * Static field used to regenerate object, individually or as arrays. Its name must be 'CREATOR'
     */
    public static final Creator<PicItem> CREATOR = new Creator<PicItem>() {
        @Override
        public PicItem createFromParcel(Parcel source) {
            return new PicItem(source);
        }

        @Override
        public PicItem[] newArray(int size) {
            return new PicItem[size];
        }
    };
}
