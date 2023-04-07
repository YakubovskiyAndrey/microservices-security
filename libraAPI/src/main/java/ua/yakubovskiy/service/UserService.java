package ua.yakubovskiy.service;

import ua.yakubovskiy.data.UserData;
import ua.yakubovskiy.dto.BookDetailsDto;
import ua.yakubovskiy.dto.UserSaveDto;

import java.util.List;

public interface UserService {

    void create(UserSaveDto saveDto);

    UserData getUser(String username);

    void addBookToUser(int userId, int bookId);

    List<BookDetailsDto> getListOfBooks(int userId);

    BookDetailsDto getBook(int userId, int bookId);

}
