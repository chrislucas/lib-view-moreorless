package appcode.custom.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;


/**
 * Created by r028367 on 27/07/2017.
 */

public class CustomTextView extends AppCompatTextView {
    
    public CustomTextView(Context context) {
        super(context);
        loadDefault();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadDefault();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadDefault();
    }

    private void loadDefault() {
        try {
            Typeface typeface =  Typeface.createFromAsset(getContext().getAssets(), "fonts/minhafonte.ttf");
            setTypeface(typeface, 1);
        } catch (Exception e) {
            Log.e("EXCP_LOADING_FONTS", e.getMessage());
        }
    }




}
