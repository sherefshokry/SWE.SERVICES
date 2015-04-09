package com.FCI.SWE.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.com.google.common.base.Pair;
//import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.gson.Gson;

@Path("/")
public class Messages {

	@POST
	@Path("/SendSingleMessage")
	public boolean sendSingleMessage(@FormParam("Esource") String Esource,
			@FormParam("Edestination") String Edest,
			@FormParam("Message") String msg) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("Chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {

			if (entity.getProperty("Group").equals("0"))

			{
				if (entity.getProperty("Esource").toString().equals(Esource)
						&& entity.getProperty("Edestination").toString()
								.equals(Edest)) {
					entity.setProperty("Message", entity.getProperty("Message")
							.toString() + msg);
					datastore.put(entity);
					return true;
				}

				else if (entity.getProperty("Esource").toString().equals(Edest)
						&& entity.getProperty("Edestination").toString()
								.equals(Esource)) {
					FriendsServices obj = new FriendsServices();
					String uName = obj.searchUser(Esource);

					if (uName == null)
						return false;
					else
						msg = "/n" + uName + ": " + msg;

					entity.setProperty("Message", entity.getProperty("Message")
							.toString() + msg);
					datastore.put(entity);
					return true;
				}
			}
		}

		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		Entity entity = new Entity("Chat", list.size() + 1);

		FriendsServices obj = new FriendsServices();
		String uName = obj.searchUser(Esource);

		if (uName == null)
			return false;
		else
			msg = "/n" + uName + ": " + msg;

		Date now = Calendar.getInstance(TimeZone.getTimeZone("Egypt/Cairo"),
				Locale.US).getTime();
		String dateNow = now.toString();
		entity.setProperty("Esource", Esource);
		entity.setProperty("Edestination", Edest);
		entity.setProperty("Message", msg + "/n" + dateNow);
		entity.setProperty("Group", "0");
		datastore.put(entity);
		return true;
	}

	@POST
	@Path("/SendGroupMessage")
	public boolean sendGroupMessage(@FormParam("Esource") String Esource,
			@FormParam("ID") String ID, @FormParam("Message") String msg) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("Chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {

			if (entity.getProperty("Group").equals("1")) {
				Date now = Calendar.getInstance(
						TimeZone.getTimeZone("Egypt/Cairo"), Locale.US)
						.getTime();

				if (entity.getProperty("ID/Name").equals(ID)) {
					FriendsServices obj = new FriendsServices();
					String uName = obj.searchUser(Esource);

					if (uName == null)
						return false;
					else
						msg = "/n" + uName + ": " + msg;

					entity.setProperty("Message", entity.getProperty("Message")
							.toString() + msg + "\n" + now.toString());
					datastore.put(entity);

					return true;
				}
			}
		}
		return false;
	}

	@POST
	@Path("/AddMemberInChatGroup")
	public String AddMemberInChatGroup(
			@FormParam("memEmail") String memberToAdd,
			@FormParam("ChatID") String ID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity user : pq.asIterable()) {
			if (user.getProperty("email").toString().equals(memberToAdd)) {
				Object obj = user.getProperty("GroupChatID");
				ArrayList<String> IDs = (ArrayList<String>) obj;
				IDs.add(ID);
				user.setProperty("GroupChatID", IDs);
			}
		}
		return ID;
	}

	
	
	
	@POST
	@Path("/GetMyGroupMsgs")
	public String readMyGroupMsgs(@FormParam("Esource") String Esource) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity user : pq.asIterable()) {
			if (user.getProperty("email").toString().equals(Esource)) {
				return new Gson().toJson(user.getProperty("GroupChatID"));
			}
		}
		return null;
	}

	
	
	
	@POST
	@Path("/GetMySingleMsgs")
	public String readMySingleMsgs(@FormParam("Esource") String Esource) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		String res = "";
		
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("Group").equals("0")) {
				if (entity.getProperty("Esource").toString().equals(Esource)) 
					res += entity.getProperty("Edestination").toString() + "#";					

				else if(entity.getProperty("Edestination").toString().equals(Esource))
					res += entity.getProperty("Esource").toString() + "#";
			}
		}
		return res;
	}

	
	
	@POST
	@Path("/ReadGroupMessage")
	public String ReadGroupMessage(@FormParam("ChatID") String ID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		String msg = "";

		for (Entity user : pq.asIterable()) {
			if (user.getProperty("ID/Name").toString().equals(ID)) {
				msg = user.getProperty("Message").toString();
			}
		}
		return msg;
	}

	
	
	@POST
	@Path("/ReadSingleMessage")
	public String ReadSingleMessage(@FormParam("Esource") String Esource,
			@FormParam("Edestination") String Edest) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("Chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		String msg = "";

		for (Entity entity : pq.asIterable()) {

			if (entity.getProperty("Group").equals("0")) {
				if (entity.getProperty("Esource").toString().equals(Esource)
						&& entity.getProperty("Edestination").toString()
								.equals(Edest)) {
					msg = entity.getProperty("Message").toString();
					return msg;
				} else if (entity.getProperty("Esource").toString()
						.equals(Edest)
						&& entity.getProperty("Edestination").toString()
								.equals(Esource)) {

					msg = entity.getProperty("Message").toString();
					return msg;
				}
			}

		}
		return null;
	}
	
	
	@POST
	@Path("/createChatGroup")
	public boolean createChatGroup(@FormParam("Esource") String Esource,
			@FormParam("GrName") String groupName) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("Chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		Entity entity = new Entity("Chat", list.size() + 1);
		
		
		  entity.setProperty("Esource", Esource);
		entity.setProperty("GroupName", groupName);
		entity.setProperty("Group", "1");
		String id = entity.getProperty("ID/Name").toString();
		datastore.put(entity);


		 gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity user : pq.asIterable()) {
			if (user.getProperty("email").toString().equals(memberToAdd)) {
				Object obj = user.getProperty("GroupChatID");
				ArrayList<String> IDs = (ArrayList<String>) obj;
				IDs.add(ID);
				user.setProperty("GroupChatID", IDs);
			}
		
		
		
		
		
		
		return true;
	}
	
}