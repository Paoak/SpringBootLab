package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.NotValidException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
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
    private BookStorage bookStorage;
    private BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookStorage bookStorage, BookMapper bookMapper) {
        this.bookStorage = bookStorage;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        BookEntity bookEntity;
        if (BookValidation.isValidBook(bookDto)) {
            bookEntity = bookStorage.create(bookMapper.bookDtoToBookEntity(bookDto));
        } else {
            throw new NotValidException("Not valid data: " + bookDto);
        }
        return bookMapper.bookEntityToBookDto(bookEntity);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        BookDto Book = bookMapper.bookEntityToBookDto(bookStorage.getById(bookDto.getId()));

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
            Book = bookMapper.bookEntityToBookDto(bookStorage.update(bookMapper.bookDtoToBookEntity(Book)));
        }
        return Book;
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.bookEntityToBookDto(bookStorage.getById(id));
    }

    @Override
    public void deleteBookById(Long id) {
        bookStorage.delete(id);
    }

    @Override
    public List<BookDto> getBooksByUserId(Long userId) {
        return bookStorage.getBooks()
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
                .map(book -> book.getId())
                .forEach(bookStorage::delete);
    }
}