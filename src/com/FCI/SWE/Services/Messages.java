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

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class Messages {

	@POST
	@Path("/SendSingleMessage/")
	public String sendSingleMessage(@FormParam("Esource") String Esource,
			@FormParam("Edestination") String Edest,
			@FormParam("Message") String msg) {

		JSONObject Jobj = new JSONObject();
		Jobj.put("message", msg);
		Jobj.put("status", "ok");
	
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
	
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {

			if (entity.getProperty("Group").equals("0")) {
				if (entity.getProperty("Esource").toString().equals(Esource)
						&& entity.getProperty("Edestination").toString()
								.equals(Edest)) {
					entity.setProperty("Message", entity.getProperty("Message")
							.toString() + msg);
					datastore.put(entity);
					return Jobj.toString();
				} else if (entity.getProperty("Esource").toString()
						.equals(Edest)
						&& entity.getProperty("Edestination").toString()
								.equals(Esource)) {
					FriendsServices obj = new FriendsServices();
					String uName = obj.searchUser(Esource);

					if (uName == null)
						return Jobj.toString();
					else
						msg = "/n" + uName + ": " + msg;

					entity.setProperty("Message", entity.getProperty("Message")
							.toString() + msg);
					datastore.put(entity);
					txn.commit();
					return Jobj.toString();
				}
			}
		
		}

		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		Entity entity = new Entity("chat", list.size() + 1);

		FriendsServices obj = new FriendsServices();
		String uName = obj.searchUser(Esource);

		if (uName == null)
			return Jobj.toString();
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
		txn.commit();
		return Jobj.toString();

	}

	@POST
	  @Path("/SendGroupMessage") public String
	  sendGroupMessage(@FormParam("Esource") String Esource,
	  @FormParam("ID") String ID, @FormParam("Message") String msg)
	  {
		  
	  JSONObject obj= new JSONObject ();
	 
	  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	  Query gaeQuery = new Query("chat");
	  PreparedQuery pq =datastore.prepare(gaeQuery);
	  
	  for (Entity entity : pq.asIterable()) {
		  
	  if ( entity.getProperty("Group").equals("1") ) {
       Date now = Calendar.getInstance( TimeZone.getTimeZone("Egypt/Cairo"), Locale.US).getTime();	  
	  if (Long.toString(entity.getKey().getId()).equals(ID)) { 
	     ArrayList <String> chatUsers =new ArrayList<String> ();
	     chatUsers=(ArrayList<String>) entity.getProperty("chatUsers");
		FriendsServices Friendobj = new FriendsServices(); 
		
		String uName = Friendobj.searchUser(Esource);
	  
	  if (uName == null) return obj.put("status", "failed").toString();
	
	  else msg = "/n" + uName + ": " + msg;
	  entity.setProperty("Message", entity.getProperty("Message") .toString() +msg + "\n" + now.toString()); 
	  datastore.put(entity);
	  
	  return obj.put("emails", chatUsers).toString();
	  
	  }
	  } 
	  } 
	  
	  return obj.put("status", "failed").toString(); 
	  
	  }

	@POST
	@Path("/AddMemberInChatGroup")
	public String AddMemberInChatGroup(
			@FormParam("memEmail") String memberToAdd,
			@FormParam("ChatID") String ID,
			@FormParam("groupName") String groupName) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		JSONObject obj = new JSONObject();
		Query gaeQuery = new Query("chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		for (Entity user : pq.asIterable()) {

			if (user.getProperty("ID").toString().equals(ID)) {
				Transaction txn = datastore.beginTransaction();
				String id = user.getProperty("ID").toString();
				String message = user.getProperty("Message").toString();

				ArrayList<String> emails = new ArrayList<String>();
				emails = (ArrayList<String>) user.getProperty("chatUsers");
				String sourceEmail = user.getProperty("Esource").toString();
				emails.add(memberToAdd);

				datastore.delete(user.getKey());

				Entity chatUser = new Entity("chat", list.size() + 2);

				chatUser.setProperty("Esource", sourceEmail);
				chatUser.setProperty("GroupName", groupName);
				chatUser.setProperty("Group", "1");
				chatUser.setProperty("ID", id);
				chatUser.setProperty("chatUsers", emails);

				datastore.put(chatUser);
				txn.commit();
				break;

			}
		}
		gaeQuery = new Query("users");
		pq = datastore.prepare(gaeQuery);

		for (Entity user : pq.asIterable()) {

			if (user.getProperty("email").toString().equals(memberToAdd)) {

				ArrayList<String> objID = new ArrayList<String>();
				objID = (ArrayList<String>) user.getProperty("GroupChatID");
				String name = user.getProperty("name").toString();
				String email = user.getProperty("email").toString();
				String password = user.getProperty("password").toString();

				// ArrayList<String> IDs = (ArrayList<String>) obj;

				String mix = groupName + "#" + ID;
				objID.add(mix);
				// object.put("id", ID);

				datastore = DatastoreServiceFactory.getDatastoreService();
				gaeQuery = new Query("users");
				pq = datastore.prepare(gaeQuery);
				Transaction txn = datastore.beginTransaction();
				txn = datastore.beginTransaction();
				list = pq.asList(FetchOptions.Builder.withDefaults());
				datastore.delete(user.getKey());

				Entity newRow = new Entity("users", list.size() + 2);

				newRow.setProperty("GroupChatID", objID);

				newRow.setProperty("name", name);
				newRow.setProperty("email", email);
				newRow.setProperty("password", password);
				datastore.put(newRow);
				txn.commit();
				return obj.put("status", "ok").toString();

				// user.setProperty("GroupChatID", objID);
			}
		}
		return obj.put("status", "failed").toString();
	}

	@POST
	@Path("/ReadGroupMessage")
	public String ReadGroupMessage(@FormParam("ChatID") String ID) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		String msg = "";
		for (Entity user : pq.asIterable()) {
			if (Long.toString(user.getKey().getId()).equals(ID)) {
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
		Query gaeQuery = new Query("chat");
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

				else if (entity.getProperty("Edestination").toString()
						.equals(Esource))
					res += entity.getProperty("Esource").toString() + "#";
			}
		}
		return res;
	}

	@POST
	@Path("/GetMyGroupMsgs")
	public String getMyMsgs(@FormParam("Esource") String Esource) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		JSONObject object = new JSONObject();
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity user : pq.asIterable()) {
			if (user.getProperty("email").toString().equals(Esource)) {
				object.put("IDs", user.getProperty("GroupChatID"));
				// return new Gson().toJson(user.getProperty("GroupChatID"));
			}
		}
		return object.toString();

	}

	@POST
	@Path("/createChatGroup")
	public String createChatGroup(@FormParam("Esource") String Esource,
			@FormParam("GrName") String groupName) {

		JSONObject object = new JSONObject();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("chat");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		ArrayList<String> lst = new ArrayList<String>();
		lst.add(Esource);
		Entity chatUser = new Entity("chat", list.size() + 2);

		chatUser.setProperty("Esource", Esource);
		chatUser.setProperty("GroupName", groupName);
		chatUser.setProperty("Group", "1");
		chatUser.setProperty("ID", Long.toString(chatUser.getKey().getId()));
		chatUser.setProperty("chatUsers", lst);
		datastore.put(chatUser);
		txn.commit();
		String id = Long.toString(chatUser.getKey().getId());

		Query query = new Query("users");
		pq = datastore.prepare(query);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(Esource)) {

				object.put("email", entity.getProperty("email"));
				ArrayList<String> ID = (ArrayList<String>) entity
						.getProperty("GroupChatID");
				String name = entity.getProperty("name").toString();
				String email = entity.getProperty("email").toString();
				String password = entity.getProperty("password").toString();

				String mix = groupName + "#" + id;
				ID.add(mix);
				object.put("id", ID);

				datastore = DatastoreServiceFactory.getDatastoreService();
				query = new Query("users");
				pq = datastore.prepare(query);
				txn = datastore.beginTransaction();
				list = pq.asList(FetchOptions.Builder.withDefaults());
				datastore.delete(entity.getKey());

				Entity newRow = new Entity("users", list.size() + 2);

				newRow.setProperty("GroupChatID", ID);

				newRow.setProperty("name", name);
				newRow.setProperty("email", email);
				newRow.setProperty("password", password);
				datastore.put(newRow);
				txn.commit();

				break;
			}
		}
		object.put("Status", "ok");
		return object.toString();
	}
}