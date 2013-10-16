package eresto.co.id.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView{
	public Typeface type;

	public CustomTextView(Context context) {
		super(context);
		type = Typeface.createFromAsset(getContext().getAssets(),"Nexa.otf");
		this.setTypeface(type);
	}
	public CustomTextView(Context context, AttributeSet attrs) {
		super( context, attrs );
		type = Typeface.createFromAsset(getContext().getAssets(),"Nexa.otf");
		this.setTypeface(type);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		type = Typeface.createFromAsset(getContext().getAssets(),"Nexa.otf");
		this.setTypeface(type);
	}

}