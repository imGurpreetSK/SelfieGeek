package gurpreetsk.me.selfiegeek.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Gurpreet on 13/11/16.
 */

public class CustomImageView extends ImageView {

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredWidth();
        int width = Math.round(height * .9f);
        setMeasuredDimension(width, height);
    }
}