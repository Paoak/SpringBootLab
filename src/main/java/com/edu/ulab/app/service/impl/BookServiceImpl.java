package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.NotValidationException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.Storage;
import com.edu.ulab.app.validation.BookValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private Storage storage;
    private BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(Storage storage, BookMapper bookMapper) {
        this.storage = storage;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        BookEntity bookEntity;
        if (BookValidation.isValidBook(bookDto)) {
            bookEntity = storage.create(bookMapper.bookDtoToBookEntity(bookDto));
        } else {
            throw new NotValidationException("Not valid data: " + bookDto);
        }
        return bookMapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        BookDto Book = getBookById(bookDto.getId());

        if (bookDto.getTitle() != null) {
            Book.setTitle(bookDto.getTitle());
        }
        if (bookDto.getAuthor() != null) {
            Book.setAuthor(bookDto.getAuthor());
        }
        if (bookDto.getPageCount() != 0) {
            Book.setPageCount(bookDto.getPageCount());
        }
        if (BookValidation.isValidBook(Book)) {
            Book = bookMapper.bookEntityToBookDto(storage.update(bookMapper.bookDtoToBookEntity(Book)));
        }
        return Book;
    }

    @Override
    public BookDto getBookById(Long id) {
        Object object = storage.getById(id);
        if (object instanceof BookEntity) {
            return bookMapper.bookEntityToBookDto((BookEntity) object);
        } else {
            throw new NotValidationException("Object with ID = ," + id + " is not a book!");
        }
    }

    @Override
    public void deleteBookById(Long id) {
        Object object = storage.getById(id);
        if (object instanceof BookEntity) {
            storage.delete(id);
        } else {
            throw new NotValidationException("Object with ID=" + id + " is not a book!");
        }
    }

    @Override
    public List<BookDto> getBooksByUserId(Long userId) {
        return storage.getBooks()
                .stream()
                .filter(Objects::nonNull)
                .filter(book -> book.getUserId().equals(userId))
                .map(bookMapper::bookEntityToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBooksByUserId(Long userId) {
        getBooksByUserId(userId)
                .stream()
                .filter(Objects::nonNull)
                .map(BookDto::getId)
                .forEach(storage::delete);
    }
}
