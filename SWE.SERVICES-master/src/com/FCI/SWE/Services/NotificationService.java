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
	public String messageNotify(@FormParam("Esource") String Esource) {
		JSONObject Jobj = new JSONObject();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Notifications");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Jobj.put("status", "ok");

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(Esource)) {
				int counter = Integer.parseInt(entity.getProperty("messages")
						.toString()) + 1;
				entity.setProperty("messages", counter);

				break;

			}
		}

		Entity entity = new Entity("Notifications", list.size() + 1);

		entity.setProperty("messages", 1);
		entity.setProperty("acceptedReq", 0);
		entity.setProperty("recievedReq", 0);
		entity.setProperty("email", Esource);
		return Jobj.toString();
	}

	@POST
	@Path("/notifyAccFriend")
	public String accFriendNotify(@FormParam("Esource") String Esource) {
		JSONObject Jobj = new JSONObject();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Notifications");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Jobj.put("status", "ok");
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(Esource)) {
				int counter = Integer.parseInt(entity
						.getProperty("acceptedReq").toString()) + 1;
				entity.setProperty("acceptedReq", counter);

				break;
			}
		}
		Entity entity = new Entity("Notifications", list.size() + 1);
		entity.setProperty("messages", 0);
		entity.setProperty("acceptedReq", 1);
		entity.setProperty("recievedReq", 0);

		entity.setProperty("email", Esource);

		return Jobj.toString();
	}

	@POST
	@Path("/notifyFriendReq")
	public String friendReqNotify(@FormParam("Esource") String Esource) {
		JSONObject Jobj = new JSONObject();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Notifications");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(Esource)) {
				int counter = Integer.parseInt(entity
						.getProperty("recievedReq").toString()) + 1;
				entity.setProperty("recievedReq", counter);
				break;
			}
		}
		Entity entity = new Entity("Notifications", list.size() + 1);

		entity.setProperty("messages", 0);
		entity.setProperty("acceptedReq", 0);
		entity.setProperty("recievedReq", 1);

		entity.setProperty("email", Esource);
		Jobj.put("status", "ok");
		return Jobj.toString();
	}

	
}
