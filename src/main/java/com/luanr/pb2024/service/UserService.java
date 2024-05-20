package com.luanr.pb2024.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.luanr.pb2024.exception.InvalidArgumentsException;
import com.luanr.pb2024.exception.UserAlreadyExistsException;
import com.luanr.pb2024.exception.UserNotFoundException;
import com.luanr.pb2024.model.User;
import com.luanr.pb2024.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> allUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found.");
        }

        return users;
    }

    public Optional<User> singleUser(ObjectId id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException("No user found with ID: " + id);
        }

        return user;
    }

    public void signup(User user) {
        String userName = user.getName();
        String profilePicture = user.getProfilePicture();

        if (userName == null || profilePicture == null) {
            throw new InvalidArgumentsException("Invalid arguments.");
        }

        if (userNameAlreadyExists(userName)) {
            throw new UserAlreadyExistsException("User with the name " + userName + " already exist.");
        }

        userRepository.save(user);
    }

    public void remove(ObjectId id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException("No user found with ID: " + id);
        }

        userRepository.deleteById(id);
    }

    public void update(ObjectId id, User user) {
        Optional<User> oldUser = userRepository.findById(id);

        String userName = user.getName();
        String profilePicture = user.getProfilePicture();
        String oldName = oldUser.get().getName();

        Query query = new Query().addCriteria(Criteria.where("_id").is(id));
        Update updateDefinition = new Update();

        System.out.println(userName);
        System.out.println(profilePicture);

        if (!userName.equals(oldName) && userNameAlreadyExists(userName)) {
            System.out.println(userName.equals(oldUser.get().getName()));
            throw new UserAlreadyExistsException("User with the name " + userName + " already exist.");
        }

        if (userName != null) {
            updateDefinition.set("name", userName);
        }

        if (userName != null) {
            updateDefinition.set("profilePicture", user.getProfilePicture());
        }

        mongoTemplate.findAndModify(query, updateDefinition, User.class);
    }

    private Boolean userNameAlreadyExists(String userName) {
        Query query = new Query().addCriteria(Criteria.where("name").is(userName));
        User nameFound = mongoTemplate.findOne(query, User.class);

        return nameFound != null ? true : false;
    }
}
