package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.storage.UserStorage;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserStorageImpl implements UserStorage {

    private Map<Long, UserEntity> users;
    private Long currentId;

    public UserStorageImpl() {
        users = new HashMap<>();
        currentId = 0L;
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        userEntity.setId(++currentId);
        users.put(userEntity.getId(), userEntity);
        return userEntity;
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        if (!users.containsKey(userEntity.getId())) {
            throw new NotFoundException("User with ID = " + userEntity.getId() + " not found");
        }
        users.put(userEntity.getId(), userEntity);
        return userEntity;
    }

    @Override
    public void delete(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User with ID = " + userId + " not found");
        }
        users.remove(userId);
    }

    @Override
    public UserEntity getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User with ID = " + userId + " not found");
        }
        return users.get(userId);
    }
}
