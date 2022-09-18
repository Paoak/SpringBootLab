package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    UserEntity create(UserEntity userEntity);

    UserEntity update(UserEntity user);

    void delete(Long userId);

    UserEntity getUserById(Long userId);


}
