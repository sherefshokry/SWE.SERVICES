package test_Services;

import org.json.simple.JSONObject;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.FCI.SWE.Services.Messages;
import com.google.appengine.labs.repackaged.org.json.JSONException;

import com.FCI.SWE.Services.NotificationService;
import com.google.appengine.labs.repackaged.org.json.JSONException;

public class TestNotifications {
	
	NotificationService obj = new NotificationService();
  @Test
  public void testMessageNotify()throws JSONException {
	  
	  String result = obj.messageNotify("sheref_shokry@yahoo.com");
	   JSONObject object = new JSONObject(result);
      Assert.assertEquals("ok", object.get("status"));
  }
  
  
  @Test
  public void testAccFriendNotify()throws JSONException {
	  
	  String result = obj.accFriendNotify("sheref_shokry@yahoo.com");
	  JSONObject object = new   JSONObject(result);  
      Assert.assertEquals("ok", object.get("status"));
  }
  
  @Test
  public void testFriendReqNotify()throws JSONException {
	  
	  String result = obj.FriendReqNotify("sheref_shokry@yahoo.com");
      JSONObject object = new JSONObject(result);  
      Assert.assertEquals("ok", object.get("status"));
  }
  
}






