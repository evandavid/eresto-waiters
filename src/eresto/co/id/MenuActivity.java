package eresto.co.id;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import eresto.co.id.adapter.MenuAdapter;
import eresto.co.id.model.Eresto;
import eresto.co.id.model.Users;
import eresto.co.id.util.ConvertStreamToString;
import eresto.co.id.views.CustomEditText;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("CutPasteId")
public class MenuActivity extends Activity {
	private MenuAdapter adapter;
	private CustomEditText filter;
	public ListView feature;
	public String[][] data;
	public String type, url, url_save;
	private final String SUB_URL = "/api/v1/menu.json";
	private Eresto app;
	private final String SAVE_URL = "/api/pesanan/index?params=";
	private ProgressDialog dialog;
	
	private final Handler myHandler = new Handler();
    final Runnable updateRunnable = new Runnable() {
        public void run() {
            sukses();
        }
    };
    
    final Runnable updateRunnable2 = new Runnable() {
        public void run() {
            gagal();
        }
    };
    
    final Runnable updateGagalSave = new Runnable() {
        public void run() {
            gagalSave();
        }
    };
    
    final Runnable updateSuksesSave = new Runnable() {
        public void run() {
            suksesSave();
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		this.app = Eresto.findById(Eresto.class, (long) 1);
		this.url = this.app.url()+SUB_URL;
		this.url_save = this.app.url()+SAVE_URL;
		
		this.type = getIntent().getStringExtra("name");
		if(type != null){
			if (type.equals("order")){
				ImageButton img = (ImageButton)findViewById(R.id.imageButton1);
				LinearLayout ll = (LinearLayout)findViewById(R.id.line);
				img.setVisibility(View.GONE);
				ll.setVisibility(View.GONE);
				
				new Thread(new Runnable() {
					public void run() {
						getMenu();
					} 		
				}).start();
			}else {
				ImageView im = (ImageView)findViewById(R.id.imageView1);
				CustomEditText et = (CustomEditText)findViewById(R.id.editText1);
				im.setVisibility(View.GONE);
				et.setVisibility(View.GONE);
			}
		}
		
		this.filter = (CustomEditText)findViewById(R.id.editText1);
		
		this.filter.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				MenuActivity.this.filter(cs);  
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void getMenu(){
		JSONObject object;
		JSONArray arrayobj1;
    	HttpClient httpclient = new DefaultHttpClient();    
    	HttpGet httppost = new HttpGet(this.url);
    	HttpParams httpParameters = new BasicHttpParams();
	    int timeoutConnection = 5000;
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    int timeoutSocket = 5000;
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        try {                         
	         HttpResponse response = httpclient.execute(httppost); 
	         
	         if (response.getStatusLine().getStatusCode() == 200){
		         HttpEntity entity = response.getEntity();
		         if (entity != null) {
		        	 InputStream instream = entity.getContent();
		        	 ConvertStreamToString convert = new ConvertStreamToString();
		        	 String result = convert.doConvert(instream);
		        	 instream.close();
		        	 //parse data
		        	 object= new JSONObject(result);
		        	 
		        	 arrayobj1 = object.getJSONArray("data");
		        	 
		        	 this.data = new String[arrayobj1.length()][4];
		        	 for (int i = 0; i < arrayobj1.length(); i++) {
						this.data[i][0] = arrayobj1.getJSONObject(i).getString("menu_nama");
						this.data[i][1] = arrayobj1.getJSONObject(i).getString("menu_id");
						this.data[i][2] = arrayobj1.getJSONObject(i).getString("stok");
						this.data[i][3] = "0";
		        	 }
		        	 
		        	 myHandler.post(updateRunnable);
		         }
	         }
         } 
        catch (ClientProtocolException e) {
        	myHandler.post(updateRunnable2);
        	
         } 
        catch (IOException e){         
          } catch (JSONException e) {
			e.printStackTrace();
		}
	
	}
	
	public void sukses(){
		TabHostMenuActivity.order = data;
		adapter = new MenuAdapter(this, data, "order");
        feature = (ListView)findViewById(R.id.listView1);  
        feature.setAdapter(adapter);
	}
	
	public void gagal(){
		Toast.makeText(this, "Cannot reach server, try again", Toast.LENGTH_SHORT).show();
		finish();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(type != null){
			if (!type.equals("order")){
				if (TabHostMenuActivity.review != null){
					adapter = new MenuAdapter(this, TabHostMenuActivity.review, "review");
			        feature = (ListView)findViewById(R.id.listView1);  
			        feature.setAdapter(adapter);
				}
			}else{
				if (TabHostMenuActivity.order != null){
					data = TabHostMenuActivity.order;
					adapter = new MenuAdapter(this, data, "order");
					feature = (ListView)findViewById(R.id.listView1);  
			        feature.setAdapter(adapter);
				}
			}
		}
	}
	
	
	//search in listview
	@SuppressLint("DefaultLocale")
	public void filter(CharSequence cs){
		String sc = String.valueOf(cs);
		Log.e("dav", sc);
		outerloop:
		for (int i = 0; i < data.length; i++) {
		    if (data[i][0].toLowerCase().contains(sc.toLowerCase())){
		    	feature.setSelection(i);
		    	break outerloop;
		    }
		}
	}

	public void backHeader(View v){
		Intent i = new Intent(this, HomeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
	}
	
	public void SavePesanan(View view){
		if (TabHostMenuActivity.review != null){
			dialog = ProgressDialog.show(this, "Save Pesanan", 
					"Please wait", true);
			new Thread(new Runnable() {
				public void run() {
					String tmp = MenuActivity.constructJson(
							TabHostMenuActivity.review, TabHostMenuActivity.name,
							TabHostMenuActivity.meja,TabHostMenuActivity.hp);
					SavePesanan(tmp);
				} 		
			}).start();
		}
	}
	
	public void SavePesanan(String tmp){
				
		HttpClient httpclient = new DefaultHttpClient(); 
		String tmps = null;
		try {
			tmps = this.url_save + URLEncoder.encode(tmp,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpGet httppost = new HttpGet(tmps);
    	HttpParams httpParameters = new BasicHttpParams();
	    int timeoutConnection = 5000;
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    int timeoutSocket = 5000;
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        try {                         
	         HttpResponse response = httpclient.execute(httppost); 
	         
	         if (response.getStatusLine().getStatusCode() == 200){
		        	 myHandler.post(updateSuksesSave);
	         }
         } 
        catch (ClientProtocolException e) {
        	myHandler.post(updateGagalSave);
         } 
        catch (IOException e){         
          } 
	}
	
	public void suksesSave(){
		dialog.dismiss();
		Intent i = new Intent(MenuActivity.this, HomeActivity.class);
	    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
	
	public void gagalSave(){
		dialog.dismiss();
		Toast.makeText(this, "Failed to save, try again", Toast.LENGTH_SHORT).show();
	}
	
	public static String constructJson(String[][] a, String name, String table, String hp){
		Users u = Users.findById(Users.class, (long) 1);
		String tmp = "{\"data\":[";
		for (int i = 0; i < a.length; i++) {
			tmp = tmp+ "{\"menu_id\":\""+a[i][1]+"\",\"qty\":\""+a[i][3]+"\"},";
		}
		
		tmp = tmp.substring(0, tmp.length()-1);
		tmp = tmp+"],\"customer\":\""+name+"\",\"table\":\""+table+"\",\"telp\":\""+hp+"\",\"waiter\":\""+u.user_id+"\"}";
		
		return tmp;
	}

}
