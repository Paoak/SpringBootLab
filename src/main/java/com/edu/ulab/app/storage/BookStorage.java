package com.edu.ulab.app.storage;


import com.edu.ulab.app.entity.BookEntity;

import java.util.List;

public interface BookStorage extends Storage<BookEntity>{
    List<BookEntity> getBooks();
}