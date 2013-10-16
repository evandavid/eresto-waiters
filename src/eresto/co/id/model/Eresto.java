package eresto.co.id.model;

import android.content.Context;

import com.orm.SugarRecord;

public class Eresto extends SugarRecord<Eresto> {
	  public String name;
	  public String ip;
	  public String namespace;
	  
	  public Eresto(Context ctx){
	    super(ctx);
	  }

	  public Eresto(Context ctx, String name, String ip, String namespace){
	    super(ctx);
	    this.name = name;
	    this.ip = ip;
	    this.namespace = namespace;
	  }
	  
	  public String url() {
		  String url;
		  if (this.namespace == null)
			  url = "http://"+this.ip;
		  else
			  url = "http://"+this.ip+"/"+this.namespace;
		  return url;
	  }
	  
}