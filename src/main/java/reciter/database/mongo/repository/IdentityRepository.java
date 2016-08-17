package reciter.database.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import reciter.database.mongo.model.Identity;

public interface IdentityRepository extends MongoRepository<Identity, String> {

	Identity findByCwid(String cwid);
	
    List<Identity> findByCwidRegex(String cwid);
}
