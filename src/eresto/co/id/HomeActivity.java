package eresto.co.id;

import java.io.IOException;
import java.io.InputStream;

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

import eresto.co.id.adapter.TableAdapter;
import eresto.co.id.model.Eresto;
import eresto.co.id.model.Users;
import eresto.co.id.util.ConvertStreamToString;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private TableAdapter adapter, adapter2;
	private final String AVAILABLE = "available";
	private final String BUSY = "busy";
	private FrameLayout layout_MainMenu;
	private final String SUB_URL = "/api/v1/meja.json";
	private Eresto app;
	private String url;
	private String[][] busy_data, available_data;
	
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
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		layout_MainMenu = (FrameLayout) findViewById( R.id.mainmenu);
		layout_MainMenu.getForeground().setAlpha( 0);
		
		this.app = Eresto.findById(Eresto.class, (long) 1);
		this.url = this.app.url()+SUB_URL;
		  
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		new Thread(new Runnable() {
			public void run() {
				getMeja();
			} 		
		}).start();
		
	}
	
	public void getMeja(){
		JSONObject object;
		JSONArray arrayobj1, arrayobj2;
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
		        	 
		        	 arrayobj1 = object.getJSONArray("available");
		        	 arrayobj2 = object.getJSONArray("busy");
		        	 
		        	 this.available_data = new String[arrayobj1.length()][2];
		        	 for (int i = 0; i < arrayobj1.length(); i++) {
						this.available_data[i][0] = arrayobj1.getJSONObject(i).getString("meja_id");
						this.available_data[i][1] = arrayobj1.getJSONObject(i).getString("status");
		        	 }
		        	 
		        	 this.busy_data = new String[arrayobj2.length()][3];
		        	 for (int i = 0; i < arrayobj2.length(); i++) {
						this.busy_data[i][0] = arrayobj2.getJSONObject(i).getString("meja_id");
						this.busy_data[i][1] = arrayobj2.getJSONObject(i).getString("status");
						this.busy_data[i][2] = arrayobj2.getJSONObject(i).getString("pesanan");
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
		adapter = new TableAdapter(this, this.busy_data, BUSY, layout_MainMenu);
        GridView feature = (GridView)findViewById(R.id.gridView1);  
        feature.setAdapter(adapter);
        
        adapter2 = new TableAdapter(this, this.available_data, AVAILABLE, layout_MainMenu);
        GridView feature2 = (GridView)findViewById(R.id.gridView2);  
        feature2.setAdapter(adapter2);
	}
	
	public void gagal(){
		Toast.makeText(this, "Cannot reach server, try again", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(HomeActivity.this, SettingActivity.class);
	    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}
	
	public void doLogouts(View view){
		Log.v("orm", "clicked");
		Users usr = Users.findById(Users.class, (long) 1);
		usr.user_id = null;
		usr.save();
		Intent i = new Intent(HomeActivity.this, LoginActivity.class);
	    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

}
