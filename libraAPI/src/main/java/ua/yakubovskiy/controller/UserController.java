package ua.yakubovskiy.controller;

import ua.yakubovskiy.data.UserData;
import ua.yakubovskiy.dto.BookDetailsDto;
import ua.yakubovskiy.dto.RestResponse;
import ua.yakubovskiy.dto.UserSaveDto;
import ua.yakubovskiy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse createUser(@RequestBody UserSaveDto saveDto) {
        userService.create(saveDto);
        return new RestResponse("user added successfully");

    }

    @PutMapping("/addBookToUser")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    public RestResponse addBookToUser(@RequestParam int userId, @RequestParam int bookId) {
        userService.addBookToUser(userId, bookId);
        return new RestResponse("book added successfully");
    }

    @GetMapping("/getUser")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    public UserData getUser(String username) {
        return userService.getUser(username);
    }

    @GetMapping("/getListOfBooks")
    @PreAuthorize("hasAuthority('READING')")
    public List<BookDetailsDto> getListOfBooks(@RequestParam int userId) {
        return userService.getListOfBooks(userId);
    }

    @GetMapping("/getBook")
    @PreAuthorize("hasAuthority('READING')")
    public BookDetailsDto getBook(@RequestParam int userId, @RequestParam int bookId) {
        return userService.getBook(userId, bookId);
    }

}
