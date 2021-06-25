package guru.sfg.brewery.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import guru.sfg.brewery.web.model.security.Authority;

public interface AthorityRepository extends JpaRepository<Authority, String> {

}
