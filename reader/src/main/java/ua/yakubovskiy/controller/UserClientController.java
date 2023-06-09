package ua.yakubovskiy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.yakubovskiy.client.FeignUserClient;
import ua.yakubovskiy.dto.UserDto;
import ua.yakubovskiy.service.UserClientService;

@Controller
@RequiredArgsConstructor
public class UserClientController {

    @Autowired
    private FeignUserClient feignUserClient;

    @Autowired
    private final UserClientService userService;

    @GetMapping("/")
    public String showMenu(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            UserDto userDto = userService.getUserDto(user.getUsername());
            model.addAttribute("books",
                    feignUserClient.getListOfBooks(userDto.getToken(), userDto.getId()));
        }
        return "menu";
    }

    @GetMapping("/getBook/{bookId}")
    public String getBook(@PathVariable("bookId") int bookId, Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            UserDto userDto = userService.getUserDto(user.getUsername());
            model.addAttribute("books",
                    feignUserClient.getBook(userDto.getToken(), userDto.getId(), bookId));
        }
        return "book-info";
    }
}
