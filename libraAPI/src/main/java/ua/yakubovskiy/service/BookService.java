package ua.yakubovskiy.service;

import ua.yakubovskiy.dto.BookSaveDto;

public interface BookService {

    void addNewBook(BookSaveDto saveDto);

}
