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
import org.json.JSONException;
import org.json.JSONObject;

import eresto.co.id.model.Eresto;
import eresto.co.id.model.Users;
import eresto.co.id.util.ConvertStreamToString;
import eresto.co.id.views.CustomEditText;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private CustomEditText usernameBox, passwordBox;
	private String username, password, url;
	private ProgressDialog dialog;
	private Eresto app;
	@SuppressWarnings("unused")
	private ConvertStreamToString convert;
	
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
		setContentView(R.layout.activity_login);
		
		this.initialize();
	}
	
	private void initialize(){
		this.app = Eresto.findById(Eresto.class, (long) 1);
		this.usernameBox = (CustomEditText)findViewById(R.id.username);
		this.passwordBox = (CustomEditText)findViewById(R.id.password);
	}
	
	public void doneHeader(View view){
		this.username = this.usernameBox.getText().toString();
		this.password = this.passwordBox.getText().toString();
		
		if (this.username.equals("") || this.password.equals("")){
			Toast.makeText(this, "Username and Password cannot be blank", Toast.LENGTH_SHORT).show();
		}else{
			dialog = ProgressDialog.show(this, "Logging in", 
					"Please wait", true);
			url = app.url()+"/api/v1/login.json?username="+this.username+"&password="+this.password+"";
			Log.v("orm", url);
			new Thread(new Runnable() {
    			public void run() {
    				doLogin();
    			} 		
    		}).start();
		}       
		
	}
	
	public void doLogin(){
		JSONObject object;
    	  
    	HttpGet httppost = new HttpGet(url);

    	HttpParams httpParameters = new BasicHttpParams();
	    int timeoutConnection = 5000;
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    int timeoutSocket = 5000;
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
	    HttpClient httpclient = new DefaultHttpClient(httpParameters);  
        try {               
//	         List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);        
//	
//	         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));               
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
		        	 
		        	 String name = object.getString("username");
		        	 String user_id = object.getString("user_id");
		        	 
		        	 Users user = Users.findById(Users.class, Long.parseLong(user_id));
		             if (user == null){
		             	user = new Users(this, name, user_id);
		             	user.save();
		             }else{
		             	user.username = name;
		             	user.user_id = user_id;
		             	user.save();
		             }
		             
		        	 myHandler.post(updateRunnable);
		         }
	         }else{
	        	 myHandler.post(updateRunnable2); 
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
		dialog.dismiss();
		Intent i = new Intent(LoginActivity.this, HomeActivity.class);
	    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
	
	public void gagal(){
		dialog.dismiss();
		Toast.makeText(this, "Username or Password wrong", Toast.LENGTH_SHORT).show();
	}

}
