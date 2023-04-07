package ua.yakubovskiy.service;

import jakarta.annotation.PostConstruct;
import ua.yakubovskiy.data.Book;
import ua.yakubovskiy.data.Role;
import ua.yakubovskiy.data.UserData;
import ua.yakubovskiy.dto.BookDetailsDto;
import ua.yakubovskiy.dto.UserSaveDto;
import ua.yakubovskiy.exception.AlreadyExistException;
import ua.yakubovskiy.exception.NotFoundException;
import ua.yakubovskiy.repository.BookRepository;
import ua.yakubovskiy.repository.RoleRepository;
import ua.yakubovskiy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories
            .createDelegatingPasswordEncoder();

    @PostConstruct
    @Transactional
    public void init() {
       Optional<UserData> userDataOptional = userRepository.findByUsername("admin");
       if (userDataOptional.isEmpty()) {
           userRepository.save(UserData.builder()
                   .username("admin")
                   .password(passwordEncoder.encode("admin"))
                   .decPassword("admin")
                   .role("ADMIN")
                   .enabled(true)
                   .build()
           );
       }
    }

    @Override
    @Transactional
    public void create(UserSaveDto saveDto) {
        Optional<UserData> userDataOptional = userRepository.findByUsername(saveDto.getUsername());
        if (userDataOptional.isEmpty()) {
            userRepository.save(UserData.builder()
                    .username(saveDto.getUsername())
                    .password(passwordEncoder.encode(saveDto.getPassword()))
                    .decPassword(saveDto.getPassword())
                    .role(saveDto.getRole())
                    .enabled(true)
                    .build());
        } else {
            throw new AlreadyExistException("User with login '%s' already exists"
                    .formatted(saveDto.getUsername()));
        }
    }

    @Override
    public UserData getUser(String username) {
        Optional<UserData> userDataOptional = userRepository.findByUsername(username);
        return userDataOptional.orElse(null);
    }

    @Override
    @Transactional
    public void addBookToUser(int userId, int bookId) {
        UserData user = getUserOrThrow(userId);
        Book book = getBookOrThrow(bookId);
        user.getBooks().add(book);
        userRepository.save(user);
    }

    @Override
    public List<BookDetailsDto> getListOfBooks(int userId) {
        UserData user = getUserOrThrow(userId);
        List<BookDetailsDto> bookDetails = new ArrayList<>();
        user.getBooks().forEach(book -> bookDetails.add(BookDetailsDto.builder()
                .id(book.getId())
                .name(book.getName())
                .contents(book.getContents())
                .build()));

        return bookDetails;
    }

    @Override
    public BookDetailsDto getBook(int userId, int bookId) {
        UserData user = getUserOrThrow(userId);
        Optional<Book> bookOptional = user.getBooks().stream()
                .filter(book -> book.getId() == bookId)
                .findFirst();

        return bookOptional.map(book -> BookDetailsDto.builder()
                .id(book.getId())
                .name(book.getName())
                .contents(book.getContents())
                .build()).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::toUserDetails)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with name '%s' not found".formatted(username)));
    }

    private UserDetails toUserDetails(UserData user) {
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(collectAuthorities(user.getRole()))
                .disabled(!user.isEnabled())
                .build();
    }

    private UserData getUserOrThrow(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(id)));
    }

    private Book getBookOrThrow(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
    }

    private List<GrantedAuthority> collectAuthorities(String role) {
        return roleRepository.getRole(role)
                .map(Role::getPrivileges)
                .stream().flatMap(Collection::stream)
                .map(privileges -> (GrantedAuthority)new SimpleGrantedAuthority(privileges))
                .toList();
    }
}
