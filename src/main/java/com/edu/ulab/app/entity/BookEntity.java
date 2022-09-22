package com.edu.ulab.app.entity;

import lombok.Data;

import java.util.List;

@Data
public class BookEntity extends Entity {
    private Long id;
    private Long userId;
    private String title;
    private String author;
    private long pageCount;
}
