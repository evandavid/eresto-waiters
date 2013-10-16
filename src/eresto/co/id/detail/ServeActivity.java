package eresto.co.id.detail;

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

import eresto.co.id.R;
import eresto.co.id.TabHostMenuActivity;
import eresto.co.id.R.layout;
import eresto.co.id.R.menu;
import eresto.co.id.adapter.MenuAdapter;
import eresto.co.id.model.Eresto;
import eresto.co.id.util.ConvertStreamToString;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class ServeActivity extends Activity {
	private ServeAdapter adapter;
	private String type, url;
	private String[][] data;
	private Eresto app;
	private final String SUB_URL = "/api/v1/pesanan?id=";
	
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serve);
		this.app = Eresto.findById(Eresto.class, (long) 1);
		this.url = this.app.url()+SUB_URL+TabHostActivity.pesanan_id+".json";
		type = getIntent().getStringExtra("name");
	}
	
	public void onResume(){
		super.onResume();
		if(type != null){
			if (type.equals("serve")){
				new Thread(new Runnable() {
					public void run() {
						getServe();
					} 		
				}).start();
			}
		}
	}
	
	public void getServe(){
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
						this.data[i][0] = arrayobj1.getJSONObject(i).getString("status_antar");
						this.data[i][1] = arrayobj1.getJSONObject(i).getString("menu_nama");
						this.data[i][2] = arrayobj1.getJSONObject(i).getString("menu_id");
						this.data[i][3] = arrayobj1.getJSONObject(i).getString("qty");;
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
//		TabHostMenuActivity.order = data;
		adapter = new ServeAdapter(this, data, "order");
        ListView feature = (ListView)findViewById(R.id.listView1);  
        feature.setAdapter(adapter);
	}
	
	public void gagal(){
		Toast.makeText(this, "Cannot reach server, try again", Toast.LENGTH_SHORT).show();
		finish();
	}

}
