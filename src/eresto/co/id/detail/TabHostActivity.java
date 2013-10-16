package eresto.co.id.detail;

import eresto.co.id.HomeActivity;
import eresto.co.id.R;
import eresto.co.id.views.CustomTextView;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.app.TabActivity;
import android.content.Intent;

@SuppressWarnings("deprecation")
public class TabHostActivity extends TabActivity {
	public static String[][] add_menu;
	public static String pesanan_id = "0";
	public static String meja_id = "0";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_host);
		// create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        pesanan_id = getIntent().getStringExtra("pesanan_id");
        meja_id = getIntent().getStringExtra("meja_id");
        
        CustomTextView title = (CustomTextView)findViewById(R.id.title);
        title.setText("eresto waiters - "+meja_id);

        TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabSpec tab3 = tabHost.newTabSpec("Third tab");
        
        
        tab1.setIndicator("Serve");
        Intent intent = new Intent(this, ServeActivity.class);
        intent.putExtra("name", "serve");
//        intent.putExtra("cust_name", name);
//        intent.putExtra("hp", hp);
//        intent.putExtra("meja", meja);
        tab1.setContent(intent);
        
        tab2.setIndicator("Process");
        Intent intent1 = new Intent(this, ServeActivity.class);
        intent1.putExtra("name", "review");
//        intent1.putExtra("cust_name", name);
//        intent1.putExtra("hp", hp);
//        intent1.putExtra("meja", meja);
        tab2.setContent(intent1);
        
        tab3.setIndicator("Add Order");
        Intent intent2 = new Intent(this, ServeActivity.class);
        intent2.putExtra("name", "review");
//        intent2.putExtra("cust_name", name);
//        intent2.putExtra("hp", hp);
//        intent2.putExtra("meja", meja);
        tab3.setContent(intent2);

        
        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, HomeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
	}
	
	public void backHeader(View v){
		Intent i = new Intent(this, HomeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
	}

}
