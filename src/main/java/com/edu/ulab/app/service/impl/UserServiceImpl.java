package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotValidException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import com.edu.ulab.app.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    UserStorage userStorage;
    UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id
        UserEntity userEntity;
        if (UserValidation.isValidUser(userDto)){
            userEntity = userStorage.create(userMapper.userDtoToUserEntity(userDto));
        } else {
            throw new NotValidException("Not valid data: " + userDto);
        }
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserDto User = userMapper.userEntityToUserDto(userStorage.getById(userDto.getId()));

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
            User = userMapper.userEntityToUserDto(userStorage.update(userMapper.userDtoToUserEntity(User)));
        }
        return User;
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.userEntityToUserDto(userStorage.getById(id));
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.delete(id);
    }
}