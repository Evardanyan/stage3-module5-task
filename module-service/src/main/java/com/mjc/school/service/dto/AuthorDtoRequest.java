package com.mjc.school.service.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record AuthorDtoRequest(
        @Positive(message = "Author ID must be a positive number")
        @Digits(integer = 10, fraction = 0, message = "ID must be a whole number with a maximum of 10 digits")
        Long id,
        @NotBlank(message = "Author Name cannot be blank")
        @Size(min = 3, max = 15, message = "Author Name must be between 3 and 15 characters")
        String name) {


    public AuthorDtoRequest(String name) {
        this(null, name);
    }

    public AuthorDtoRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

