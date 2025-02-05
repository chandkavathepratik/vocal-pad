package com.projects.vocalpad.repo;

import com.projects.vocalpad.entity.User;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);

    void deleteByUsername(String userName);

    boolean existsByUsername(@NonNull  String username);

    boolean existsByEmail(@NonNull String email);
}
