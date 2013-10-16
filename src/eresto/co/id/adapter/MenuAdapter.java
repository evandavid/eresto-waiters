package eresto.co.id.adapter;

import java.util.ArrayList;

import eresto.co.id.R;
import eresto.co.id.TabHostMenuActivity;
import eresto.co.id.views.CustomTextView;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[][] data;
    private LayoutInflater inflater=null;
	public String username, type;
	
    
    public MenuAdapter(Activity a, String[][] d, String type) {
        this.activity = a;
        this.data = d;
        this.type = type;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
    	View vi=convertView;
        if(convertView==null)
	            vi = inflater.inflate(R.layout.item_list_mene, null);
	    if(data[position][1] != null){
	    	CustomTextView menu = (CustomTextView)vi.findViewById(R.id.textView1);
	    	final RelativeLayout countBox = (RelativeLayout)vi.findViewById(R.id.xx);
	    	final TextView counts = (TextView)vi.findViewById(R.id.textView2);
	    	ImageButton min = (ImageButton)vi.findViewById(R.id.imageView1);
	    	
	    	menu.setText(this.data[position][0]);
	    	
	    	final int stok = Integer.parseInt(data[position][2]);
	    	
	    	if ( Integer.parseInt(data[position][2]) > 0 ){
	    		countBox.setBackgroundResource(R.drawable.greenbg);
	    		if (Integer.parseInt(data[position][3]) > 0)
	    			countBox.setBackgroundResource(R.drawable.bluebg);
	    	} else {
	    		countBox.setBackgroundResource(R.drawable.orangebg);
	    	}
	    	
	    	counts.setText(data[position][3]);
	    	
			OnClickListener tambah = new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if (Integer.parseInt(data[position][3]) < stok){
						int tmp = Integer.parseInt(counts.getText().toString());
						counts.setText(String.valueOf(tmp+1));
						int qty = Integer.parseInt(data[position][3]);
						data[position][3] = String.valueOf(qty+1);
						countBox.setBackgroundResource(R.drawable.bluebg);
						constructReview(data);
						if (type.equals("review")){
							for (int i = 0; i < TabHostMenuActivity.order.length; i++) {
								if (TabHostMenuActivity.order[i][1].equals(data[position][1])){
									TabHostMenuActivity.order[i][3] = data[position][3];
								}
							}
						}
					}
				}
			};
			
			OnClickListener kurang = new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if (Integer.parseInt(data[position][3]) > 0){
						int tmp = Integer.parseInt(counts.getText().toString());
						if (Integer.parseInt(data[position][3]) == 1){
							countBox.setBackgroundResource(R.drawable.greenbg);
						}
						counts.setText(String.valueOf(tmp-1));
						int qty = Integer.parseInt(data[position][3]);
						data[position][3] = String.valueOf(qty-1);
						constructReview(data);
						if (type.equals("review")){
							for (int i = 0; i < TabHostMenuActivity.order.length; i++) {
								if (TabHostMenuActivity.order[i][1].equals(data[position][1])){
									TabHostMenuActivity.order[i][3] = data[position][3];
								}
							}
						}
					}
				}
			};
			
			if (  Integer.parseInt(data[position][2]) > 0 ){
				min.setOnClickListener(kurang);
				countBox.setOnClickListener(tambah);
			} else {
				min.setClickable(false);
				countBox.setClickable(false);
			}
        }else {
        }
        return vi;
    }
    
    public void constructReview(String[][] data){
    	ArrayList<String[]> list = new ArrayList<String[]>();
	    for (String[] s : data){
	        if (!s[3].equals("0"))
	            list.add(s);
	    }
	    TabHostMenuActivity.review = list.toArray(new String[list.size()][]);
    }

}
