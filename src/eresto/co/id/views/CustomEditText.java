package eresto.co.id.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText{
	public Typeface type;

	public CustomEditText(Context context) {
		super(context);
		type = Typeface.createFromAsset(getContext().getAssets(),"Nexa.otf");
		this.setTypeface(type);
	}
	public CustomEditText(Context context, AttributeSet attrs) {
		super( context, attrs );
		type = Typeface.createFromAsset(getContext().getAssets(),"Nexa.otf");
		this.setTypeface(type);
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		type = Typeface.createFromAsset(getContext().getAssets(),"Nexa.otf");
		this.setTypeface(type);
	}

}