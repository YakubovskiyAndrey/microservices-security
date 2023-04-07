package ua.yakubovskiy.repository;

import ua.yakubovskiy.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserData, Integer> {

    Optional<UserData> findByUsername(String username);

}
