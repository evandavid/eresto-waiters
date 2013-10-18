package eresto.co.id.adapter;

import eresto.co.id.R;
import eresto.co.id.TabHostMenuActivity;
import eresto.co.id.detail.TabHostActivity;
import eresto.co.id.views.CustomEditText;
import eresto.co.id.views.CustomTextView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.view.View.OnLongClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class TableAdapter extends BaseAdapter {
	private Context context;
	private String[][] data, dim;
	private String type, meja_id;
	private FrameLayout layout_MainMenu;
	private Animation anim;
	
	public eresto.co.id.views.CustomEditText name, phone;

	public TableAdapter(Context context, String[][] data, String type, FrameLayout f, String[][] dim) {
		this.context = context;
		this.data = data;
		this.dim = dim;
		this.type = type;
		this.layout_MainMenu = f;
	}


	public View getView(final int position, View convertView, ViewGroup parent) {
 
		View gridView;
		
		if (convertView == null) {  // if it's not recycled, initialize some attributes
	        LayoutInflater inflater = (LayoutInflater) context.getSystemService(     Context.LAYOUT_INFLATER_SERVICE );
	        gridView = inflater.inflate(R.layout.item_table, parent, false);
	    } else {
	    	gridView = (View) convertView;
	    }
			anim = new AlphaAnimation(0.5f, 1.0f);
			anim.setDuration(500); 
			anim.setStartOffset(20);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(Animation.INFINITE);
 
			if (this.data != null){
				RelativeLayout rl = (RelativeLayout)gridView.findViewById(R.id.bgTable);
				
				CustomTextView tb = (CustomTextView)gridView.findViewById(R.id.textView1);
				tb.setText(data[position][0]);
				rl.setTag(data[position][0]);
				
				OnLongClickListener listener = new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						selectMenu(position);
						return true;
					}
				};
				
				OnClickListener clicked = new OnClickListener() {
					@Override
					public void onClick(View v) {
						initiatePopupWindow(position);
					}
				};
				
				OnClickListener busyclicked = new OnClickListener() {
					@Override
					public void onClick(View v) {
						String pesanan_id = "0";
						if (type.equals("busy")){
							pesanan_id = data[position][2];
						}
						Intent i = new Intent(context, TabHostActivity.class);
						i.putExtra("pesanan_id", pesanan_id);
						i.putExtra("meja_id", data[position][0]);
//						i.putExtra("meja_id", meja_id);
			            context.startActivity(i);
					}
				};
				
				if (type.equals("available")){
					rl.setBackgroundResource(R.drawable.bluebg);
					rl.setOnClickListener(clicked);
				}else{
					for (int i = 0; i < dim.length; i++) {
						if (dim[i][0].equals(data[position][0]) && dim[0][1].equals("1")){
							Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
							int dot = 200;      // Length of a Morse Code "dot" in milliseconds
							int dash = 500;     // Length of a Morse Code "dash" in milliseconds
							int short_gap = 200;    // Length of Gap Between dots/dashes
							int medium_gap = 500;   // Length of Gap Between Letters
							int long_gap = 1000;    // Length of Gap Between Words
							long[] pattern = {
							    0,  // Start immediately
							    dot, short_gap, dot, short_gap, dot,    // s
							    medium_gap,
							    dash, short_gap, dash, short_gap, dash, // o
							    medium_gap,
							    dot, short_gap, dot, short_gap, dot,    // s
							    long_gap
							};
							v.vibrate(pattern, -1);
							rl.startAnimation(anim);
						}
					}
					rl.setOnLongClickListener(listener);
					rl.setOnClickListener(busyclicked);
				}				
				
			}
 
		return gridView;
	}
	
	public void selectMenu(int pos){
		final int tmp = pos;
		this.meja_id = data[pos][0];
    	String[] list_menu = new String[]{
    	        "New Costumer",
    	    };
    	
    	android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);  
	     builder.setTitle("");  
	     builder.setItems(list_menu, new DialogInterface.OnClickListener() {  
	       public void onClick(DialogInterface dialog, int which) {
	    	   if (which == 0)
	    		   initiatePopupWindow(tmp);
	    	   dialog.dismiss();  
	       }  
	     });  
	     builder.setNegativeButton("", null);  
	     AlertDialog alert = builder.create();  
	     alert.show();  
    }


	public int getCount() {
		return data.length;
	}
 
	public Object getItem(int position) {
		return null;
	}
 
	public long getItemId(int position) {
		return 0;
	}
	
	private PopupWindow pwindo;

	private void initiatePopupWindow(int pos) {
			this.meja_id = data[pos][0];
			// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View layout = inflater.inflate(R.layout.popup_newcostumer, null);
			
			
			pwindo = new PopupWindow(layout, 450, 370, true);
			pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
			
			View vi = pwindo.getContentView();
		
			Button cancel = (Button)layout.findViewById(R.id.button1);
			Button yes = (Button)layout.findViewById(R.id.button2);
			name = (CustomEditText)vi.findViewById(R.id.name);
			phone = (CustomEditText)layout.findViewById(R.id.hp);
			
			cancel.setOnClickListener(cancelButton);
			yes.setOnClickListener(yesButton);
			layout_MainMenu.getForeground().setAlpha( 220);
	}
	
	OnClickListener cancelButton = new OnClickListener() {
		@Override
		public void onClick(View v) {
			pwindo.dismiss();
			layout_MainMenu.getForeground().setAlpha( 0 );
		}
	};
	
	OnClickListener yesButton = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String cust_name = name.getText().toString();
			String cust_hp = phone.getText().toString();
			
			if (cust_name.equals("")){
				cust_name = "No Name";
			}
			
			Intent i = new Intent(context, TabHostMenuActivity.class);
			i.putExtra("name", cust_name);
			i.putExtra("hp", cust_hp);
			i.putExtra("meja_id", meja_id);
            context.startActivity(i);
		}
	};
 
}