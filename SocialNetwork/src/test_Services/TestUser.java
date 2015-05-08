package test_Services;

import org.junit.Assert;
import org.junit.Test;

import com.FCI.SWE.Services.UserServices;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class TestUser {
	@Test
	public void testRegistrationService1() throws JSONException {

		UserServices obj = new UserServices();
		String result = obj.registrationService("sheref", "AmrDiab@yahoo.com",
				"12345");
		JSONObject object = new JSONObject(result);
		Assert.assertEquals("ok", object.get("status"));

	}

	@Test
	public void testRegistrationService2() throws JSONException {

		UserServices obj = new UserServices();
		String result = obj.registrationService("sheref",
				"sheref_shokry@yahoo.com", "12345");
		JSONObject object = new JSONObject(result);
		Assert.assertEquals("failed", object.get("status"));

	}

	@Test
	public void testRegistrationService3() throws JSONException {

		UserServices obj = new UserServices();
		String result = obj.registrationService("", "AmrDiab@yahoo.com",
				"12345");
		JSONObject object = new JSONObject(result);
		Assert.assertEquals("failed", object.get("status"));

	}

	@Test
	public void testRegistrationService4() throws JSONException {

		UserServices obj = new UserServices();
		String result = obj.registrationService("sheref", "", "12345");
		JSONObject object = new JSONObject(result);
		Assert.assertEquals("failed", object.get("status"));

	}

	@Test
	public void testRegistrationService5() throws JSONException {

		UserServices obj = new UserServices();
		String result = obj.registrationService("sheref", "sheref@yahoo.com",
				"");
		JSONObject object = new JSONObject(result);
		Assert.assertEquals("failed", object.get("status"));

	}

	/********************************************************************/

	@Test
	public void testLoginService1() throws JSONException {

		UserServices obj = new UserServices();
		String result = obj.loginService("", "12345");
		JSONObject object = new JSONObject(result);
		Assert.assertEquals("failed", object.get("status"));

	}

	@Test
	public void testLoginService2() throws JSONException {

		UserServices obj = new UserServices();
		String result = obj.loginService("AmrDiab@yahoo.com", "");
		JSONObject object = new JSONObject(result);
		Assert.assertEquals("failed", object.get("status"));

	}

	@Test
	public void testLoginService3() throws JSONException {

		UserServices obj = new UserServices();
		String result = obj.loginService("sheref_shokry@yahoo.com", "12345");
		JSONObject object = new JSONObject(result);
		Assert.assertEquals("failed", object.get("status"));

	}
}
