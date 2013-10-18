package eresto.co.id;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import eresto.co.id.model.Eresto;
import eresto.co.id.model.Users;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.util.Log;
import android.view.Window;

public class SplashActivity extends Activity {
	private static int SPLASH_TIME_OUT = 800;
	private Eresto app;
	private boolean test;
	private String url;
	
	private final Handler myHandler = new Handler();
    final Runnable updateRunnable = new Runnable() {
        public void run() {
            testConnectionDOne();
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		this.app = Eresto.findById(Eresto.class, (long) 1);
		
		if (app == null){		
			new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	Intent i;
                	i = new Intent(SplashActivity.this, OpsActivity.class);
                	startActivity(i);	 
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
			
		}else{
			url = this.app.url()+"/api/v1/test";
			new Thread(new Runnable() {
    			public void run() {
    				
    				isConnectedToServer(url, 5000);
    			} 		
    		}).start();
		}
	}
	
public void isConnectedToServer(String url, int timeout) {
		
		boolean isReachable = false;
		try { 
	    
	    	HttpGet httpPost = new HttpGet(url);

	    	HttpParams httpParameters = new BasicHttpParams();
		    int timeoutConnection = timeout;
		    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		    int timeoutSocket = 3000;
		    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		    DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		    BasicHttpResponse httpResponse = null;
		
			httpResponse = (BasicHttpResponse)  httpClient.execute(httpPost);
		    isReachable = (httpResponse.getStatusLine().getStatusCode() == 200);
		} catch (ClientProtocolException e) {
			isReachable = false;
		} catch (IOException e) {
			isReachable = false;
		}

	    test  = isReachable;
	    myHandler.post(updateRunnable);
	}
	
	public void testConnectionDOne(){
		Intent i;
    	if (test){
    		Users usr = Users.findById(Users.class, (long) 1);
    		if ((usr == null) || usr.user_id == null)
    			i = new Intent(SplashActivity.this, LoginActivity.class);
    		else
    			i = new Intent(SplashActivity.this, HomeActivity.class);
    	}
    	else{
    		i = new Intent(SplashActivity.this, OpsActivity.class);
    		i.putExtra("status", "not connected");
    	}
    		startActivity(i);	 
        finish();
	}
}
