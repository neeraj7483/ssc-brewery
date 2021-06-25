package guru.sfg.brewery.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import guru.sfg.brewery.web.model.security.User;

public interface UserRespository extends JpaRepository<User, String> {

}
