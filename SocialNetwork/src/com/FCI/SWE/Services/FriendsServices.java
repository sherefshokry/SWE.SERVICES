
package com.FCI.SWE.Services;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.FCI.SWE.ServicesModels.FriendEntity;
import com.FCI.SWE.ServicesModels.UserEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;



/*
Query gaeQuery = new Query("Friends");
PreparedQuery pq = datastore.prepare(gaeQuery);
JSONObject json = new JSONObject();
*/

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class FriendsServices {

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
	
	@POST
	@Path("/SearchFriendRequest")
	public String searchFriendReqs(@FormParam("Esource") String Esource) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		JSONObject json = new JSONObject();

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("Edestination").toString().equals(Esource)
					&& entity.getProperty("flag").toString().equals("0")) {
								
				json.put(entity.getProperty("Esource"),
						searchUser(entity.getProperty("Esource")
								.toString()));
			}
		}
		return json.toJSONString();
	}

	
	@POST
	@Path("/AddFriend")
	public String AddFriend(@FormParam("Esource") String Esource,
			@FormParam("Edestination") String Edestination) {

		JSONObject object = new JSONObject();		
		
		FriendEntity user = new FriendEntity(Esource, Edestination);
		Boolean state = user.AddFriendService(Esource,Edestination);
		if (state == false) {
			object.put("Status", "Failed");
		} else {
			object.put("Status", "Ok");
		}
		return object.toString();
	}	

	@POST
	@Path("/FriendRequest")
	public String FriendRequest(@FormParam("Esource") String Esource,
			@FormParam("Edestination") String Edestination) {
		FriendEntity user = new FriendEntity (Esource, Edestination);
		user.SaveFriendRequest();
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}	
}