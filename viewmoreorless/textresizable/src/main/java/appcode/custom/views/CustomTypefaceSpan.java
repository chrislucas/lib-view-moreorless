package appcode.custom.views;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by r028367 on 28/07/2017.
 */

public class CustomTypefaceSpan extends TypefaceSpan {

    private Typeface mNewType;

    /**
     * @param family The font family for this typeface.  Examples include
     *               "monospace", "serif", and "sans-serif".
     */
    public CustomTypefaceSpan(String family) {
        super(family);
    }

    public CustomTypefaceSpan(Parcel src) {
        super(src);
    }


    /**
     * @param family The font family for this typeface.  Examples include
     *               "monospace", "serif", and "sans-serif".
     */
    public CustomTypefaceSpan(String family, Typeface mNewType) {
        super(family);
        this.mNewType = mNewType;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        //super.updateDrawState(ds);
        applyCustomTypeface(paint, mNewType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        //super.updateMeasureState(paint);
        applyCustomTypeface(paint, mNewType);
    }


    private void applyCustomTypeface(Paint paint, Typeface typeface) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~typeface.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(typeface);
    }


    public static final Creator CREATOR = new Creator () {
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Object createFromParcel(Parcel source) {
            return new CustomTypefaceSpan(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };
}
