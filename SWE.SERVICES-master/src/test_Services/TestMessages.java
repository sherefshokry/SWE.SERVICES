
	 package test_Services;

	 import org.testng.Assert;
import org.testng.annotations.Test;

import com.FCI.SWE.Services.Messages;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

	 public class TestMessages {
	 Messages obj = new Messages();
	   @Test
	   public void testSendSingleMessage1() throws JSONException {
	   
	  	String result = obj.sendSingleMessage("sheref_shokry@yahoo.com","shrio@yahoo.com","Hello");
	       JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("ok", object.get("status"));
	   }

	 	@Test
	 	  public void testSendSingleMessage2() throws JSONException {
	 	 
	 	 	String result = obj.sendSingleMessage("sheref_shokry@yahoo.com","shrio@yahoo.com","");
	 	      JSONObject object = new JSONObject(result);  
	 	      Assert.assertEquals("failed", object.get("status"));
	 	  }
	   @Test
	 	  public void testSendSingleMessage3() throws JSONException {
	 	 
	 	 	String result = obj.sendSingleMessage("sheref_shokry@yahoo.com","shankoty@yahoo.com","Hello");
	 	      JSONObject object = new JSONObject(result);  
	 	      Assert.assertEquals("failed", object.get("status"));
	 	  }
	 /***********************************************//*************************************************************************************************************/
	  
	    @Test
	   public void testSendGroupMessage1()throws JSONException {
	   
	  String result = obj.sendGroupMessage("sheref_shokry@yahoo.com","1","Hello");
	       JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("ok", object.get("status"));  

	 }

	    @Test
	   public void testSendGroupMessage2()throws JSONException {
	   
	  String result = obj.sendGroupMessage("sheref_shokry@yahoo.com","1","");
	       JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("failed", object.get("status"));  

	 } 
	  
	   /******************************************************************************************************/
	   
	    @Test
	   public void testAddMemeberInChatGroup1()throws JSONException {
	   
	       String result = obj.AddMemberInChatGroup("sheref_shokry@yahoo.com","1","sheref");
	       JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("ok", object.get("status"));  
	   }
	   
	     @Test
	   public void testAddMemeberInChatGroup2()throws JSONException {
	   
	       String result = obj.AddMemberInChatGroup("sheref@yahoo.com","1","sheref");
	       JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("failed", object.get("status"));  
	   }
	   
	   
	     @Test
	   public void testAddMemeberInChatGroup3()throws JSONException {
	   
	       String result = obj.AddMemberInChatGroup("sheref_shokry@yahoo.com","1","sheref");
	       JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("failed", object.get("status"));  
	   }
	   
	   /**
	 * @throws JSONException ***************************************************************************/
	  @Test
	   public void testReadGroupMessage1() throws JSONException
	   {
	     String result = obj.ReadGroupMessage("1");
	     JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("ok", object.get("status"));
	   }
	   
	    @Test
	   public void testReadGroupMessage2() throws JSONException
	   {
	     String result = obj.ReadGroupMessage("");
	     JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("failed", object.get("status"));
	   }
	    @Test
	   public void testReadGroupMessage3() throws JSONException
	   {
	     String result = obj.ReadGroupMessage("508");
	     JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("failed", object.get("status"));
	   }
	  /**
	 * @throws JSONException *******************************************************************************/
	   @Test
	   public void testReadSingleMessage1() throws JSONException
	   {
	     String result = obj.ReadSingleMessage("sheref_shokry@yahoo.com" , "shrio@yahoo.com");
	     JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("ok", object.get("status")); 
	   }
	   
	   
	   @Test
	   public void testReadSingleMessage2() throws JSONException
	   {
	     String result = obj.ReadSingleMessage("sheref@yahoo.com" , "shrio@yahoo.com");
	     JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("falied", object.get("status")); 
	   }
	   
	   @Test
	   
	   public void testReadSingleMessage3() throws JSONException
	   {
	     String result = obj.ReadSingleMessage("sheref_shokry@yahoo.com" , "nabila@yahoo.com");
	     JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("falied", object.get("status")); 
	   }
	   
	   @Test
	   
	   public void testReadSingleMessage4() throws JSONException
	   {
	     String result = obj.ReadSingleMessage("shokry@yahoo.com" , "abbas@yahoo.com");
	     JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("falied", object.get("status")); 
	   }
	   
	   /**
	 * @throws JSONException ****************************************************************************************/
	   
	   @Test
	   public void testGetGroupMsgs() throws JSONException
	   {
	     String result = obj.getMyMsgs("sheref_shokry@yahoo.com");
	     JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("ok", object.get("status")); 
	   }
	   /**
	 * @throws JSONException ************************************************************************/
	   
	   @Test
	   public void testCreateChatGroup1() throws JSONException
	   {
	     String result = obj.createChatGroup("sheref_shokry@yahoo.com" , "Hello");
	         JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("ok", object.get("status"));
	     
	   }
	   
	   
	   @Test
	   public void testCreateChatGroup2() throws JSONException
	   {
	     String result = obj.createChatGroup("sheref_shokry@yahoo.com" , "");
	         JSONObject object = new JSONObject(result);  
	       Assert.assertEquals("falied", object.get("status"));
	     
	   }
	      
	   

}
