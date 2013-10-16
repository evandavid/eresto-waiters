package eresto.co.id.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button{
	public Typeface type;

	public CustomButton(Context context) {
		super(context);
		type = Typeface.createFromAsset(getContext().getAssets(),"Nexa.otf");
		this.setTypeface(type);
	}
	public CustomButton(Context context, AttributeSet attrs) {
		super( context, attrs );
		type = Typeface.createFromAsset(getContext().getAssets(),"Nexa.otf");
		this.setTypeface(type);
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		type = Typeface.createFromAsset(getContext().getAssets(),"Nexa.otf");
		this.setTypeface(type);
	}

}