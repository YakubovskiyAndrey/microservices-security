package ua.yakubovskiy.service;

import ua.yakubovskiy.data.Book;
import ua.yakubovskiy.dto.BookSaveDto;
import ua.yakubovskiy.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void addNewBook(BookSaveDto saveDto) {
        bookRepository.save(Book.builder()
                .contents(saveDto.getContents())
                .name(saveDto.getName())
                .build());
    }

}
