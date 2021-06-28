package learn.repository;

import learn.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {

     User findByUserName(String userName);
     User findById(int id);
     List<User> findByEmail(String email);


}
