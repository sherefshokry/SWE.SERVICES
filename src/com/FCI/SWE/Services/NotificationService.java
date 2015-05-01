package com.FCI.SWE.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.labs.repackaged.com.google.common.base.Pair;
import com.google.apphosting.datastore.EntityV4.Key;
//import com.google.appengine.repackaged.com.google.api.client.util.Key;
//import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.gson.Gson;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


public class NotificationService {


@POST
@Path("/notifyMessage")
public void messageNotify(@FormParam("Esource") String Esource)
{
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	Transaction txn = datastore.beginTransaction();
	Query gaeQuery = new Query("Notifications"); 
	PreparedQuery pq = datastore.prepare(gaeQuery);
	List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

	for (Entity entity : pq.asIterable()) 
	{
		if(entity.getProperty("email").toString().equals(Esource))
		{
			int counter = Integer.parseInt(entity.getProperty("messages").toString())+1;
			entity.setProperty("messages", counter);
			return;
		}
	}
	Entity entity = new Entity("Notifications", list.size() + 1);
	newNotificationRecord(entity);
	entity.setProperty("email", Esource);
}

public void newNotificationRecord(Entity entity)
{
	entity.setProperty("messages", 0);		
	entity.setProperty("acceptedReq", 0);		
	entity.setProperty("recievedReq", 0);
}


@POST
@Path("/addMemberNotify")
public void addMemberNotify(@FormParam("Esource") String Esource ,
		@FormParam("ENewMember") String newMember ,@FormParam("groupID") int groupID  )
{
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	Transaction txn = datastore.beginTransaction();
	Query gaeQuery = new Query("chat"); 
	PreparedQuery pq = datastore.prepare(gaeQuery);
	

	for (Entity entity : pq.asIterable()) 
	{
		String id = Long.toString(entity.getKey().getId());
		if(id.equals(groupID))
		{
			ArrayList<String> users = new ArrayList<String>();
		users = (ArrayList<String>)entity.getProperty("Edestination");
		users.add(newMember);
		
		entity.setProperty("Edestination", users);
			return;
		}
	}
	
	 gaeQuery = new Query("users"); 
	 pq = datastore.prepare(gaeQuery);
	 
		for (Entity entity : pq.asIterable()) 
		{
			
			if(entity.getProperty("email").equals(newMember))
			{
				ArrayList<String> Ids = new ArrayList<String>();
			Ids = (ArrayList<String>)entity.getProperty("GroupChatID");
			String id = Integer.toString(groupID);
			Ids.add(id);
			
			entity.setProperty("GroupChatID", Ids);
				return;
			}
		}
		
String source = searchUser(Esource) , newMem = searchUser(newMember);

 gaeQuery = new Query("chat"); 
 pq = datastore.prepare(gaeQuery);

for (Entity entity : pq.asIterable()) 
{
	String id = Long.toString(entity.getKey().getId());
	if(id.equals(groupID))
	{
	String allMessage = entity.getProperty("Message").toString();
	allMessage+=source+" added " + newMem;
	entity.setProperty("Message", allMessage);
		return;
	}
}
}

public String searchUser(String Email) {
	DatastoreService datastore = DatastoreServiceFactory
			.getDatastoreService();

	Query gaeQuery = new Query("users");
	PreparedQuery pq = datastore.prepare(gaeQuery);

	for (Entity entity : pq.asIterable())
		if (entity.getProperty("email").toString().equals(Email))
			return entity.getProperty("name").toString();
	return null;
}

}
