package eresto.co.id.detail;

import java.util.ArrayList;

import eresto.co.id.R;
import eresto.co.id.views.CustomTextView;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ServeAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[][] data;
    private LayoutInflater inflater=null;
	public String username, type;
	
    
    public ServeAdapter(Activity a, String[][] d, String type) {
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
	            vi = inflater.inflate(R.layout.item_list_serve, null);
	    if(data[position][1] != null){
	    	CustomTextView menu = (CustomTextView)vi.findViewById(R.id.textView1);
	    	final RelativeLayout countBox = (RelativeLayout)vi.findViewById(R.id.xx);
	    	final TextView counts = (TextView)vi.findViewById(R.id.textView2);
	    	CheckBox check = (CheckBox)vi.findViewById(R.id.imageView1);
	    	
	    	if (data[position][0].equals("1")){
	    		check.setChecked(true);
	    		check.setClickable(false);
	    		countBox.setBackgroundResource(R.drawable.greenbg);
	    	}else{
	    		countBox.setBackgroundResource(R.drawable.bluebg);
	    	}
	    	
	    	check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					Log.v("orm", String.valueOf(arg1));
					if (arg1)
						data[position][0] = "2";
					else
						data[position][0] = "0";
					constructReview(data);
				}
			});
	    	
	    	menu.setText(this.data[position][1]);
	    	counts.setText(data[position][3]);
	    	
			
        }else {
        }
        return vi;
    }
    
    public void constructReview(String[][] data){
    	ArrayList<String[]> list = new ArrayList<String[]>();
	    for (String[] s : data){
	        if (s[0].equals("2"))
	            list.add(s);
	    }
	    TabHostActivity.antar = list.toArray(new String[list.size()][]);
    }

}
