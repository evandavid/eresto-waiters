package eresto.co.id.model;

import android.content.Context;
import com.orm.SugarRecord;

public class Users extends SugarRecord<Users> {
	  public String username;
	  public String user_id;
	  
	  public Users(Context ctx){
	    super(ctx);
	  }

	  public Users(Context ctx, String name, String id){
	    super(ctx);
	    this.username = name;
	    this.user_id = id;
	  }
}
