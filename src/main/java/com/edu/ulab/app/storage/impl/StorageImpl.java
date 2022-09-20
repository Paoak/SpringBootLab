package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.NotValidationException;
import com.edu.ulab.app.storage.Storage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StorageImpl implements Storage {
        private Map<Long, Object> entityMap;

        private Long currId;

        public StorageImpl() {
            currId = 0L;
            entityMap = new HashMap<>();
        }

        public Long getCurrId() {
            return currId;
        }

        @Override
        public <T> T create(T t) {
            entityMap.put(currId++, t);
            return t;
        }

        @Override
        public <T> T update(T t) {
            if (t instanceof UserEntity userEntity) {
                if (!entityMap.containsKey(userEntity.getId())) {
                    throw new NotFoundException("User with ID = " + userEntity.getId() + " not found!");
                }
                if (!(entityMap.get(userEntity.getId()) instanceof UserEntity)) {
                    throw new NotValidationException("Object with ID = " + userEntity.getId() + " is not a user!");
                }
                entityMap.put(userEntity.getId(), t);

            } else if (t instanceof BookEntity bookEntity) {
                if (!entityMap.containsKey(bookEntity.getId())) {
                    throw new NotFoundException("Book with ID = " + bookEntity.getId() + " not found!");
                }
                if (!(entityMap.get(bookEntity.getId()) instanceof BookEntity)) {
                    throw new NotValidationException("Object with ID = " + bookEntity.getId() + " is not a book!");
                }
                entityMap.put(bookEntity.getId(), bookEntity);
            }

            return t;
        }

        @Override
        public void delete(Long id) {
            if (!entityMap.containsKey(id)) {
                throw new NotFoundException("Object with ID = " + id + " not found!");
            }
            entityMap.remove(id);
        }

        @Override
        public Object getById(Long id) {
            if (!entityMap.containsKey(id)) {
                throw new NotFoundException("Object with ID=" + id + " not found!");
            }
            return entityMap.get(id);
        }


        @Override
        public List<BookEntity> getBooks() {
            return entityMap.values()
                    .stream()
                    .filter(obj -> obj instanceof BookEntity)
                    .map(this::toBook)
                    .toList();
        }

        private BookEntity toBook(Object object) {
            return (BookEntity) object;
        }
    }

