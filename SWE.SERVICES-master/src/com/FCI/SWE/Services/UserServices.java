package com.FCI.SWE.Services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.ServicesModels.UserEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * This class contains REST services, also contains action function for web
 * application
 * 
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 *
 */


@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class UserServices {
	

	/**
	 * Registration Rest service, this service will be called to make
	 * registration. This function will store user data in data store
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided password
	 * @return Status json
	 */
	@POST
	@Path("/RegistrationService")
	public String registrationService(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
	
		JSONObject object = new JSONObject();
		object.put("email",email);
		
		FriendsServices obj=new FriendsServices ();
		
	if(obj.searchUser(email) != null)
	{
		object.put("Status", "Failed");
	}
		else if (uname == null) {
			object.put("Status", "Failed");
		}
		else	if (email == null) {
			object.put("Status", "Failed");
		}
		else if(pass == null){
			object.put("Status", "Failed");
		}
		else{
			object.put("Status", "ok");
			return object.toString();
		}
		UserEntity user = new UserEntity(uname, email, pass);
		user.saveUser();
	
		return object.toString();
	}
	
	
	/**
	 * Login Rest Service, this service will be called to make login process
	 * also will check user data and returns new user from datastore
	 * @param uname provided user name
	 * @param pass provided user password
	 * @return user in json format
	 */
	
	@POST
	@Path("/LoginService/")
	public String loginService(@FormParam("email") String email,
			@FormParam("password") String pass) {
	
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.getUser(email, pass);
		if (email == null) {
			object.put("Status", "Failed");
		}
		else if(pass == null){
			object.put("Status", "Failed");
		}
		else {
			object.put("Status", "OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			object.put("password", user.getPass());
			object.put("id", user.getId());
		}
		return object.toString();
	}
}