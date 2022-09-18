package com.edu.ulab.app.storage;


import com.edu.ulab.app.entity.BookEntity;

import java.util.List;

public interface BookStorage {

    BookEntity create(BookEntity bookEntity);

    BookEntity update(BookEntity bookEntity);

    void delete(Long bookId);

    BookEntity getBookById(Long bookId);

    List<BookEntity> getBooks();

}
