package eresto.co.id.detail;

import eresto.co.id.R;
import eresto.co.id.views.CustomTextView;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProcessAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[][] data;
    private LayoutInflater inflater=null;
	public String username;
	
    
    public ProcessAdapter(Activity a, String[][] d) {
        this.activity = a;
        this.data = d;
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
	    	check.setVisibility(View.GONE);
	    	
	    	countBox.setBackgroundResource(R.drawable.bluebg);
	    	
	    	menu.setText(this.data[position][1]);
	    	counts.setText(data[position][3]);
	    	counts.setTag(data[position][2]);
	    	
			
        }else {
        }
        return vi;
    }

}
