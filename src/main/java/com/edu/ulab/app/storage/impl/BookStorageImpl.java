package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.storage.BookStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BookStorageImpl implements BookStorage {

    private Map<Long, BookEntity> books;
    private Long currentId;

    public BookStorageImpl() {
        books = new HashMap<>();
        currentId = 0L;
    }

    @Override
    public BookEntity create(BookEntity bookEntity) {
        bookEntity.setId(++currentId);
        books.put(bookEntity.getId(), bookEntity);
        return bookEntity;
    }

    @Override
    public BookEntity update(BookEntity bookEntity) {
        if (!books.containsKey(bookEntity.getId())) {
            throw new NotFoundException("Book with ID=" + bookEntity.getId() + " not found!");
        }
        books.put(bookEntity.getId(), bookEntity);
        return bookEntity;
    }

    @Override
    public void delete(Long bookId) {
        if (!books.containsKey(bookId)) {
            throw new NotFoundException("Book with ID=" + bookId + " not found!");
        }
        books.remove(bookId);
    }

    @Override
    public BookEntity getById(Long bookId) {
        if (!books.containsKey(bookId)) {
            throw new NotFoundException("Book with ID=" + bookId + " not found!");
        }
        return books.get(bookId);
    }

    @Override
    public List<BookEntity> getBooks() {
        return new ArrayList<>(books.values());
    }
}