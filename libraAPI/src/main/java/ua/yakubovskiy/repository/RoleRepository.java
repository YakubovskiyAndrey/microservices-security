package ua.yakubovskiy.repository;

import org.springframework.stereotype.Repository;
import ua.yakubovskiy.data.Role;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a repository for roles.
 */
@Repository
public class RoleRepository {

  private static final Map<String, Role> ROLES = List.of(
      Role.builder()
          .id("USER")
          .privileges(List.of("READING"))
          .build(),
      Role.builder()
          .id("ADMIN")
          .privileges(List.of("USER_MANAGEMENT"))
          .build())
      .stream()
      .collect(Collectors.toUnmodifiableMap(
          Role::getId,
          Function.identity())
      );

  public Optional<Role> getRole(String id) {
    return Optional.ofNullable(ROLES.get(id));
  }

}
