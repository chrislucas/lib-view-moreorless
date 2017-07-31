package appcode.viewmoreorless.custom;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by C_Luc on 31/07/2017.
 */

public class CustomClickablepan extends ClickableSpan {
    public CustomClickablepan() {
        super();
    }

    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
    }

    @Override
    public CharacterStyle getUnderlying() {
        return super.getUnderlying();
    }
}
