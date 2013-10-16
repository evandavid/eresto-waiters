package eresto.co.id;

import eresto.co.id.views.CustomTextView;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabHostMenuActivity extends TabActivity {
	public static String name, hp, meja;
	public static String[][] review, order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_host_menu);
		
		// create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        name = getIntent().getStringExtra("name");
        hp = getIntent().getStringExtra("hp");
        meja = getIntent().getStringExtra("meja_id");
        
        CustomTextView title = (CustomTextView)findViewById(R.id.title);
        title.setText(name+" - "+meja);
        
        hp = hp == null ? "" : hp;

        TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabSpec tab2 = tabHost.newTabSpec("Second Tab");
//        TabSpec tab3 = tabHost.newTabSpec("Third tab");

       // Set the Tab name and Activity
       // that will be opened when particular Tab will be selected
        
        
        tab1.setIndicator("Order");
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("name", "order");
        intent.putExtra("cust_name", name);
        intent.putExtra("hp", hp);
        intent.putExtra("meja", meja);
        tab1.setContent(intent);
        
        tab2.setIndicator("Review Order");
        Intent intent1 = new Intent(this, MenuActivity.class);
        intent1.putExtra("name", "review");
        intent.putExtra("cust_name", name);
        intent.putExtra("hp", hp);
        intent.putExtra("meja", meja);
        tab2.setContent(intent1);
//
//        tab3.setIndicator("Tab3");
//        tab3.setContent(new Intent(this,MenuActivity.class));
        
        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
//        tabHost.addTab(tab3);
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
