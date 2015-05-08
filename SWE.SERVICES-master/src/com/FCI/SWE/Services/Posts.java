package com.FCI.SWE.Services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.FCI.SWE.ServicesModels.GroupEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.labs.repackaged.com.google.common.base.Pair;
import com.google.appengine.labs.repackaged.org.json.JSONArray;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class Posts {

	@POST
	@Path("/CreatePublicPost")
	public String createPublicPost(@FormParam("ESource") String Esource,
			@FormParam("EDest") String EDest, @FormParam("post") String post  , @FormParam("hashTags") ArrayList<String> hashTags) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity entity = new Entity("Posts", list.size() + 1);

		entity.setProperty("ESource", Esource);
		entity.setProperty("EDest", EDest);
		entity.setProperty("CustomMails", null);
		entity.setProperty("Privacy", 0);
		entity.setProperty("Likes", 0);
		entity.setProperty("Seen", 0);
		entity.setProperty("postID", Long.toString(entity.getKey().getId()));
		entity.setProperty("post", post);
		datastore.put(entity);

		txn.commit();
      String postID = Long.toString(entity.getKey().getId()); 
      for(int i=0;i<hashTags.size();i++)
         addHashTag(hashTags.get(i) ,postID);
        
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	@POST
	@Path("/CreatePrivatePost")
	public String createPrivatePost(@FormParam("ESource") String Esource,
			@FormParam("EDest") String EDest, @FormParam("post") String post , @FormParam("hashTags") ArrayList<String> hashTags) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity entity = new Entity("Posts", list.size() + 1);

		entity.setProperty("ESource", Esource);
		entity.setProperty("EDest", EDest);
		entity.setProperty("CustomMails", null);
		entity.setProperty("Privacy", 1);
		entity.setProperty("Likes", 0);
		entity.setProperty("Seen", 0);
		entity.setProperty("ostID", Long.toString(entity.getKey().getId()));
		entity.setProperty("post", post);
		datastore.put(entity);

		txn.commit();
      
      String postID = Long.toString(entity.getKey().getId()); 
      for(int i=0;i<hashTags.size();i++)
         addHashTag(hashTags.get(i) ,postID);
      
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	@POST
	@Path("/CreateCustomPost")
	public String createCustomPost(@FormParam("ESource") String Esource,
			@FormParam("EDest") String EDest, @FormParam("post") String post,
			@FormParam("SpecificUsers") ArrayList<String> specificUsers , @FormParam("hashTags") ArrayList<String> hashTags) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity entity = new Entity("Posts", list.size() + 1);

		entity.setProperty("ESource", Esource);
		entity.setProperty("EDest", EDest);
		entity.setProperty("CustomMails", specificUsers);
		entity.setProperty("Privacy", 2);
		entity.setProperty("Likes", 0);
		entity.setProperty("Seen", 0);
		entity.setProperty("postID", Long.toString(entity.getKey().getId()));
		entity.setProperty("post", post);
		datastore.put(entity);

		txn.commit();

      String postID = Long.toString(entity.getKey().getId()); 
      for(int i=0;i<hashTags.size();i++)
         addHashTag(hashTags.get(i) ,postID);
      
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	@POST
	@Path("/SeenPost")
	public String seenPost(@FormParam("postID") String ID) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		// Entity entity = new Entity("Posts", list.size() + 1);

		for (Entity entity : pq.asIterable()) {

			String id = Long.toString(entity.getKey().getId());
			if (id.equals(ID)) {
				String numSeen = entity.getProperty("Seen").toString();
				int nSeen = Integer.parseInt(numSeen);
				entity.setProperty("Likes", nSeen);
				break;
			}
		}

		txn.commit();

		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	@POST
	@Path("/LikePost")
	public String likePost(@FormParam("postID") String ID) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		// Entity entity = new Entity("Posts", list.size() + 1);

		for (Entity entity : pq.asIterable()) {

			String id = Long.toString(entity.getKey().getId());
			if (id.equals(ID)) {
				String numLikes = entity.getProperty("Likes").toString();
				int nLikes = Integer.parseInt(numLikes);
				entity.setProperty("Likes", nLikes);
				break;
			}
		}

		txn.commit();

		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	// ***********************************************************************************************************************

	public void addHashTag(String hashTag, String postID) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("HashTag");

		PreparedQuery pq = datastore.prepare(gaeQuery);
		Transaction txn = datastore.beginTransaction();
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

      	for (Entity entity : pq.asIterable()) {
          if(entity.getProperty("hashTag").equals(hashTag))
          {
				ArrayList<String> postIDs = (ArrayList<String>) entity
						.getProperty("postID");
				String name = entity.getProperty("hashTag").toString();				
				int nHashTag = Integer.parseInt(entity.getProperty("nHashTag").toString());
				nHashTag++;
            
				postIDs.add(postID);
				txn = datastore.beginTransaction();
				
				datastore.delete(entity.getKey());

            	Entity tag = new Entity("HashTag", list.size() + 1);
                tag.setProperty("postID", postIDs);
                tag.setProperty("hashTag", hashTag);
                tag.setProperty("nHashTag", nHashTag);

                datastore.put(tag);
                txn.commit();
	
            	return;
          }
        }
      
	      ArrayList<String> postIDs = new ArrayList<String>();
    	  postIDs.add(postID);
      
            	Entity tag = new Entity("HashTag", list.size() + 1);
                tag.setProperty("postID", postIDs);
                tag.setProperty("hashTag", hashTag);
                tag.setProperty("nHashTag", "1");

		datastore.put(tag);
		txn.commit();

	}
	@POST
	@Path("/ReadPostService/")
	public String readPostService(@FormParam("Esource") String Esource,
			@FormParam("Edestination") String Edestination) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		JSONArray posts = new JSONArray();
		String post = "", likes = "", seen = "", Esour = "", Edest = "";
		Query gaeQuery = new Query("Posts");

		PreparedQuery pq = datastore.prepare(gaeQuery);
		// int numPosts = 0;
		for (Entity entity : pq.asIterable()) {
			JSONObject object = new JSONObject();

			if (entity.getProperty("Edestination").toString()
					.equals(Edestination)) {
				if (entity.getProperty("Esource").toString().equals(Esource)) {

					post = entity.getProperty("post").toString();
					likes = entity.getProperty("Likes").toString();
					seen = entity.getProperty("Seen").toString();
					Esour = entity.getProperty("Esource").toString();
					Edest = entity.getProperty("Edestination").toString();
					object.put("post", post);
					object.put("likes", likes);
					object.put("seen", seen);
					object.put("Esource", Esour);
					object.put("Edestination", Edest);
					posts.put(object);
				}

				else if (entity.getProperty("Privacy").toString().equals("0")) {

					post = entity.getProperty("post").toString();
					likes = entity.getProperty("Likes").toString();
					seen = entity.getProperty("Seen").toString();
					Esour = entity.getProperty("Esource").toString();
					Edest = entity.getProperty("Edestination").toString();
					object.put("post", post);
					object.put("likes", likes);
					object.put("seen", seen);
					object.put("Esource", Esour);
					object.put("Edestination", Edest);
					posts.put(object);
				}
			}

			else if (entity.getProperty("Privacy").toString().equals("1")) {

				int check = checkFreind(Esource, Edestination);
				if (check == 1) {
					post = entity.getProperty("post").toString();
					likes = entity.getProperty("Likes").toString();
					seen = entity.getProperty("Seen").toString();
					Esour = entity.getProperty("Esource").toString();
					Edest = entity.getProperty("Edestination").toString();
					object.put("post", post);
					object.put("likes", likes);
					object.put("seen", seen);
					object.put("Esource", Esour);
					object.put("Edestination", Edest);
					posts.put(object);
				}
			}
          else if (entity.getProperty("Privacy").toString().equals("2")) {

				ArrayList<String> Emails = (ArrayList<String>) entity
						.getProperty("CustomMails");
				for (int index = 0; index < Emails.size(); index++) {
					if (Emails.get(index) == Esource) {

						post = entity.getProperty("post").toString();
						likes = entity.getProperty("Likes").toString();
						seen = entity.getProperty("Seen").toString();
						Esour = entity.getProperty("Esource").toString();
						Edest = entity.getProperty("Edestination").toString();
						object.put("post", post);
						object.put("likes", likes);
						object.put("seen", seen);
						object.put("Esource", Esour);
						object.put("Edestination", Edest);
						posts.put(object);
	                    break;                      
					}
				}

			}

		}

		return posts.toString();
	}


	@POST
	@Path("/sharePostService/")
	public String sharePostService(@FormParam("postID") String postID,
			@FormParam("Esource") String Esource) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("Posts");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {

			if (entity.getProperty("postID").toString().equals(postID)) {

				String post = entity.getProperty("post").toString();
				ArrayList<String> cMails = (ArrayList<String>) entity
						.getProperty("CustomMails");
				String Privacy = entity.getProperty("Privacy").toString();
				List<Entity> list = pq.asList(FetchOptions.Builder
						.withDefaults());
				Entity newEntity = new Entity("Posts", list.size() + 2);

				newEntity.setProperty("Esource", Esource);
				newEntity.setProperty("Edestination", Esource);
				newEntity.setProperty("post", post);
				newEntity.setProperty("CustomMails", cMails);
				newEntity.setProperty("Privacy", Privacy);
				newEntity.setProperty("postID",
						Long.toString(newEntity.getKey().getId()));
				newEntity.setProperty("Likes", 0);
				newEntity.setProperty("seen", 0);

				datastore.put(newEntity);
				return new JSONObject().put("Status", "OK").toString();
			}

		}
		return new JSONObject().put("Status", "FAILED").toString();
	}

	int checkFreind(String Esrc, String Edest) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		JSONObject json = new JSONObject();

		for (Entity entity : pq.asIterable()) {
			if ((entity.getProperty("Edestination").toString().equals(Esrc) && entity
					.getProperty("Esource").toString().equals(Edest))
					|| (entity.getProperty("Edestination").toString()
							.equals(Edest) && entity.getProperty("Esource")
							.toString().equals(Esrc))) {

				if (entity.getProperty("flag").toString().equals("1")) {
					int flag = Integer.parseInt(entity.getProperty("flag")
							.toString());
					return flag;
				}
			}
		}
		return 0;
	}

}