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

import eresto.co.id.HomeActivity;
import eresto.co.id.R;
import eresto.co.id.model.Eresto;
import eresto.co.id.util.ConvertStreamToString;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ProcessActivity extends Activity {

	private ProcessAdapter adapter;
	private String url;
	private String[][] data;
	private Eresto app;
	private final String PROCESS_URL = "/api/v1/pesananprocess?id=";
	private final String BASE = "/api/v1/cancelpesanan?";
	
	@SuppressWarnings("unused")
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

    final Runnable updateCancel = new Runnable() {
        public void run() {
            suksesCancel();
        }
    };

    final Runnable updateCancel2 = new Runnable() {
        public void run() {
            gagalCancel();
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process);
		this.app = Eresto.findById(Eresto.class, (long) 1);
		this.url = this.app.url()+PROCESS_URL+TabHostActivity.pesanan_id+".json";
		new Thread(new Runnable() {
			public void run() {
				getServe();
			} 		
		}).start();
		Log.v("orm", this.url);
	}
	
	public void onResume(){
		super.onResume();
		
	}
	
	public void getServe(){
		JSONObject object;
		JSONArray arrayobj1;
		
		HttpParams httpParameters = new BasicHttpParams();
	    int timeoutConnection = 5000;
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    int timeoutSocket = 5000;
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	    
    	HttpClient httpclient = new DefaultHttpClient(httpParameters);    
    	HttpGet httppost = new HttpGet(this.url);
    	
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
		adapter = new ProcessAdapter(this, data);
        ListView feature = (ListView)findViewById(R.id.listView1);  
        feature.setAdapter(adapter);
        
        feature.setOnItemLongClickListener(longclicked);
	}
	
	public void suksesCancel(){
		Intent i = new Intent(ProcessActivity.this, HomeActivity.class);		
		Toast.makeText(this, "Sukses cancel order", Toast.LENGTH_SHORT).show();
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("xx", "refresh");
		startActivity(i);
		finish();
	}
	
	public void gagalCancel(){
		Toast.makeText(this, "Cannot reach server, try again", Toast.LENGTH_SHORT).show();
	}
	
	
	OnItemLongClickListener longclicked = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			dialog(arg2);
			Log.v("orm", String.valueOf(arg2));
			return true;
		}
	};
	
	public void gagal(){
		Toast.makeText(this, "Cannot reach server, try again", Toast.LENGTH_SHORT).show();
		finish();
	}
	
	public void dialog(int pos){
		final String menu_id = data[pos][2];
		final String pesanan_id = TabHostActivity.pesanan_id;
		final String qty = data[pos][3];
		
		AlertDialog.Builder alertDialogBuilder = 
				new AlertDialog.Builder(this);
 
		alertDialogBuilder.setTitle("Cancel order");
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
//					menu_id=1&pesanan_id=1&qty=1
					CancelPesanan(pesanan_id, menu_id, qty);
				}
			  })
			.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	public void CancelPesanan(final String pesanan_id, final String menu_id, final String qty){
		new Thread(new Runnable() {
			public void run() {
				DoCancel(pesanan_id, menu_id, qty);
			} 		
		}).start();
	}
	
	public void DoCancel(String pesanan_id, String menu_id, String qty){
		HttpParams httpParameters = new BasicHttpParams();
	    int timeoutConnection = 5000;
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    int timeoutSocket = 5000;
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	    
		HttpClient httpclient = new DefaultHttpClient(httpParameters);  
		String tmp2 = "menu_id="+menu_id+"&pesanan_id="+pesanan_id+"&qty="+qty;

		String tmp = this.app.url()+BASE+ tmp2;
		Log.v("orm", tmp);
    	HttpGet httppost = new HttpGet(tmp);
    	
        try {                         
	         HttpResponse response = httpclient.execute(httppost); 
	         
	         if (response.getStatusLine().getStatusCode() == 200){
		          	 
		       myHandler.post(updateCancel);
	         }else
	        	 myHandler.post(updateCancel2);
         } 
        catch (ClientProtocolException e) {
        	myHandler.post(updateCancel2);
        	
         } 
        catch (IOException e){         
          } 
	}
}
