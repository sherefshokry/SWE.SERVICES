package com.FCI.SWE.ServicesModels;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class FriendEntity {

	private String Esource;
	private String Edestination;
	private String flag;
	private String Messages;

	// private long id;

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public FriendEntity(String Esource, String Edestination) {

		this.Esource = Esource;
		this.Edestination = Edestination;
	}

	public FriendEntity(String Esource, String Edestination, String msg) {

		this.Esource = Esource;
		this.Edestination = Edestination;
		this.Messages = msg;
	}

	/*
	 * private void setId(long id){ this.id = id; }
	 * 
	 * public long getId(){ return id; }
	 * 
	 * public String getName() { return name; }
	 * 
	 * public String getEmail() { return email; }
	 * 
	 * public String getPass() { return password; }
	 */

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

	/*
	 * @POST
	 * 
	 * @Path("/SearchFriendRequest/") public String
	 * searchFriendReqs(@FormParam("Esource") String Esource) { DatastoreService
	 * datastore = DatastoreServiceFactory .getDatastoreService();
	 * 
	 * Transaction txn = datastore.beginTransaction(); Query gaeQuery = new
	 * Query("Friends"); PreparedQuery pq = datastore.prepare(gaeQuery);
	 * JSONObject json = new JSONObject();
	 * 
	 * for (Entity entity : pq.asIterable()) { if
	 * (entity.getProperty("Edestination").toString().equals(Esource) &&
	 * entity.getProperty("flag").toString().equals("0")) {
	 * json.put(entity.getProperty("Esource"),
	 * searchUser(entity.getProperty("Esource") .toString())); } } txn.commit();
	 * 
	 * return json.toJSONString(); }
	 */

	/**
	 * 
	 * This static method will form UserEntity class using user name and
	 * password This method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static Boolean AddFriendService(String Esource, String Edestination) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("Esource").toString().equals(Esource)
					&& entity.getProperty("Edestination").toString()
							.equals(Edestination)
					&& entity.getProperty("flag").equals("0")) 
			{
				entity.setProperty("flag", "1");

				return true;
			}
		}
		return false;
	}

	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */

	public Boolean SaveFriendRequest() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		System.out.println("Size = " + list.size());
		try {

			Entity friend = new Entity("Friends", list.size() + 2);

			friend.setProperty("Esource", this.Esource);
			friend.setProperty("Edestination", this.Edestination);
			friend.setProperty("flag", "0");
			datastore.put(friend);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		return true;
	}
}