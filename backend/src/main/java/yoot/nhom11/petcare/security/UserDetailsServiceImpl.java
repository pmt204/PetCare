package yoot.nhom11.petcare.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoot.nhom11.petcare.entity.AppUser;
import yoot.nhom11.petcare.repository.AppUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private AppUserRepository appUserRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser user = appUserRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

  @Transactional
  public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
    AppUser user = appUserRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

    return UserDetailsImpl.build(user);
  }
}
