package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotValidationException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.Storage;
import com.edu.ulab.app.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private Storage storage;
    UserMapper userMapper;

    @Autowired
    public UserServiceImpl(Storage storage, UserMapper userMapper) {
        this.storage = storage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id
        UserEntity userEntity;
        if (UserValidation.isValidUser(userDto)) {
            userDto.setId(storage.getCurrId());
            userEntity = storage.create(userMapper.userDtoToUserEntity(userDto));
        } else {
            throw new NotValidationException("Not valid data: " + userDto);
        }
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {

        UserDto User = getUserById(userDto.getId());

        if (userDto.getFullName() != null) {
            User.setFullName(userDto.getFullName());
        }

        if (userDto.getTitle() != null) {
            User.setTitle(userDto.getTitle());
        }

        if (userDto.getAge() != 0) {
            User.setAge(userDto.getAge());
        }

        if (UserValidation.isValidUser(User)) {
            User = userMapper.userEntityToUserDto(storage.update(userMapper.userDtoToUserEntity(User)));
        }
        return User;
    }

    @Override
    public UserDto getUserById(Long id) {
        Object object = storage.getById(id);
        if (object instanceof UserEntity) {
            return userMapper.userEntityToUserDto((UserEntity) object);
        } else {
            throw new NotValidationException("Object with ID = " + id + " is not a user!");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        Object object = storage.getById(id);
        if (object instanceof UserEntity) {
            storage.delete(id);
        } else {
            throw new NotValidationException("Object with ID = " + id + " is not a user!");
        }
    }
}
