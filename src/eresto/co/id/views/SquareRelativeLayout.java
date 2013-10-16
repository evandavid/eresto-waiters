package eresto.co.id.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

@SuppressLint("ViewConstructor")
public class SquareRelativeLayout extends RelativeLayout{
	public int viewWidth = 0;
    public int viewHeight = 0;

	public SquareRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public SquareRelativeLayout(Context context, AttributeSet attrs) {
		super( context, attrs );
	}

	@SuppressLint("NewApi")
	public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super( context, attrs, defStyle );
	}

	@Override 
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
	    setMeasuredDimension(height, width);
	}
	
	@Override
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
	     super.onSizeChanged(xNew, yNew, xOld, yOld);

	     viewWidth = xNew;
	     viewHeight = yNew;
	}
}
