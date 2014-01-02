package br.com.efinancas.android.widget;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Typeface;
import br.com.efinancas.android.R;

/**
 * @author: Misael Ferreira
 * Date: 16/11/13
 * Time: 12:51
 */
public class RobotoTextView extends TextView {

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(isInEditMode()){
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.RobotoTextView);
        String fontName = styledAttrs.getString(R.styleable.RobotoTextView_typeface);
        styledAttrs.recycle();

        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            setTypeface(typeface);
        }
    }
}
