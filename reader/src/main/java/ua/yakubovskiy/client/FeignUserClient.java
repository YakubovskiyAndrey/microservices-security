package ua.yakubovskiy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ua.yakubovskiy.dto.BookDto;
import ua.yakubovskiy.dto.UserDto;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(value = "libraAPI", url = "http://localhost:1000/api/users")
public interface FeignUserClient {

    @GetMapping(value = "/getListOfBooks", produces = APPLICATION_JSON_VALUE)
    List<BookDto> getListOfBooks(@RequestHeader("Authorization") String header,
                                 @RequestParam int userId);

    @GetMapping(value = "/getUser", produces = APPLICATION_JSON_VALUE)
    UserDto getUser(@RequestHeader("Authorization") String header,
                    @RequestParam String username);

    @GetMapping(value ="/getBook", produces = APPLICATION_JSON_VALUE)
    BookDto getBook(@RequestHeader("Authorization") String header,
                    @RequestParam int userId, @RequestParam int bookId);

}
