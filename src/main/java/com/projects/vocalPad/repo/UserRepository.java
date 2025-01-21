package com.projects.vocalPad.repo;

import com.projects.vocalPad.entity.User;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String userName);

    void deleteByUsername(String userName);

    boolean existsByUsername(@NonNull  String username);

    boolean existsByEmail(@NonNull String email);
}
