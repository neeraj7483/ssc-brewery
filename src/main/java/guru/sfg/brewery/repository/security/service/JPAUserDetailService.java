package guru.sfg.brewery.repository.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import guru.sfg.brewery.repository.security.UserRespository;

@Service
public class JPAUserDetailService implements UserDetailsService {
	private final UserRespository userRespository;

	public JPAUserDetailService(UserRespository userRespository) {
		this.userRespository = userRespository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRespository.findById(username).orElseThrow(
				() -> new UsernameNotFoundException("Not able to find any user registered with username: " + username));
	}

}
