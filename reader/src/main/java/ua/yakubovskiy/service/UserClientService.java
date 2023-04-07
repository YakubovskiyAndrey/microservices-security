package ua.yakubovskiy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.yakubovskiy.client.FeignUserClient;
import ua.yakubovskiy.dto.UserDto;
import ua.yakubovskiy.repository.UserRepository;

import java.util.Base64;
import java.util.Optional;

@Service
public class UserClientService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeignUserClient feignReaderClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String user = "admin";
        String password = "admin";

        byte[] encodedBytes = Base64.getEncoder().encode((user + ":" + password).getBytes());
        String authHeader = "Basic " + new String(encodedBytes);

        UserDto userDto = feignReaderClient.getUser(authHeader, username);
        if (userDto != null) {
            userRepository.save(userDto);
            return toUserDetails(userDto);
        }
        return null;
    }

    public UserDto getUserDto(String username){
        Optional<UserDto> userDtoOptional = userRepository.get(username);
        return userDtoOptional.orElse(null);
    }

    private UserDetails toUserDetails(UserDto user) {
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .disabled(!user.isEnabled())
                .build();
    }
}
