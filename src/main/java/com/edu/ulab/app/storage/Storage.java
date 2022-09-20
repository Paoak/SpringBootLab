package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BookEntity;

import java.awt.print.Book;
import java.util.List;

public interface Storage {
    //todo создать хранилище в котором будут содержаться данные
    // сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
    // продумать что у узера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции

    <T> T create(T t);

    <T> T update(T t);

    void delete(Long id);

    Object getById(Long id);

    List<BookEntity> getBooks();

    Long getCurrId();
}
