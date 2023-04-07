package ua.yakubovskiy.repository;

import org.springframework.stereotype.Repository;
import ua.yakubovskiy.dto.UserDto;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

@Repository
public class UserRepository {

    private NavigableMap<String, UserDto> data = new ConcurrentSkipListMap<>();

    public void save(UserDto user) {
        data.put(user.getUsername(), user);
    }

    public Optional<UserDto> get(String username) {
        return Optional.ofNullable(data.get(username));
    }

    public void deleteAll() {
        data.clear();
    }
}
