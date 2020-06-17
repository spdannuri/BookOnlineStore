package com.online.bookstore.convertor;

import com.online.bookstore.dto.request.BookRequestDto;
import com.online.bookstore.dto.response.BookResponseDto;
import com.online.bookstore.model.Book;
import com.online.bookstore.model.User;
import com.online.bookstore.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookConvertor {

    @Autowired
    private UserRepo userRepo;

    public Book convertToBook(BookRequestDto bookRequestDto) {
        String author = bookRequestDto.getAuthor();
        User user = userRepo.getByUserName(author);
        Book book = new Book(bookRequestDto.getISBN(), bookRequestDto.getTitle(),user.getId(),bookRequestDto.getDescription(),true);
        return book;
    }

    public BookResponseDto convertToBookResponseDto(Book book) {
        BookResponseDto bookResponseDto = null;
        if(book != null) {
            User user = userRepo.getUserById(book.getAuthorId());
            bookResponseDto = new BookResponseDto(book.getISBN(),book.getTitle(),user.getName(),book.getDescription());
        }
        return bookResponseDto;
    }
}
