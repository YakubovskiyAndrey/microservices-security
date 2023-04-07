package ua.yakubovskiy.controller;


import ua.yakubovskiy.dto.BookSaveDto;
import ua.yakubovskiy.dto.RestResponse;
import ua.yakubovskiy.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse addNewBook(@RequestBody BookSaveDto saveDto) {
        bookService.addNewBook(saveDto);
        return new RestResponse("Successfully");
    }

}
