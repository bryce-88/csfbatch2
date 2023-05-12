package ibf2022.batch2.csf.backend.repositories;


import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.csf.backend.models.User;

@Repository
public class ArchiveRepository {

	@Autowired
	MongoTemplate template;


	//TODO: Task 4
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	/*
	 * db.archives.insert ({
	 * 		bundleId : <bundleId - UUID>,
	 * 		date : <date>,
	 * 		title : <title>,
	 * 		name : <name>,
	 * 		comments : <comments>
	 * 		urls : [
	 * 			{<url1>,
	 * 			 <url2>
	 * 			}
	 * 		]
	 * })
	 */
	public ObjectId recordBundle(User user) {

		Document toInsert = new Document();
		toInsert.put("bundleId", user.getBundleId());
		toInsert.put("date", user.getDate());
		toInsert.put("title", user.getTitle());
		toInsert.put("name", user.getName());
		toInsert.put("comments", user.getComments());
		toInsert.put("urls", user.getUrls());

		Document newDoc = template.insert(toInsert, "archives");
		ObjectId id = newDoc.getObjectId("_id");

		return id;
	}

	//Task 5
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	/*
	 * db.archives.find({
	 * 		"bundleId" : "<bundleId>"
	 * })
	 */
	
	public Document getBundleByBundleId(String bundleId) {
		Criteria criterial = Criteria.where("bundleId").is(bundleId);
		Query query = Query.query(criterial);
		List<Document> result = template.find(query, Document.class, "archives");
		//since bundleId is unique, result should only return list of one document
		Document searchDocument = result.get(0);

		return searchDocument;
	}

	//Task 6
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	/*
	 assume name is the primary identifier...
	 * db.archives.find({
			"name" : "<username>"
	 * }).sort({
	 * 		date : -1,
	 * 		title : 1
	 * })
	 */
	
	public List<Document> getBundles(String username) {
		Criteria criterial = Criteria.where("name").is(username);
		Query query = Query.get

		return null;
	}


}
