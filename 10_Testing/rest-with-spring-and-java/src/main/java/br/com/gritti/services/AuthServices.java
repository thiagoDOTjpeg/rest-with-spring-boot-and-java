package br.com.gritti.services;

import br.com.gritti.data.vo.v1.security.AccountCredentialsVO;
import br.com.gritti.data.vo.v1.security.TokenVO;
import br.com.gritti.repositories.UserRepository;
import br.com.gritti.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final UserRepository userRepository;

  @Autowired
  public AuthServices(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.userRepository = userRepository;
  }

  @SuppressWarnings("rawtypes")
  public ResponseEntity signin(AccountCredentialsVO data) {
    try {
      var username = data.getUserName();
      var password = data.getPassword();
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

      var user = userRepository.findByUsername(username);

      var tokenResponse = new TokenVO();
      if(user != null) {
        tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
      } else {
        throw new UsernameNotFoundException("Username " + username + " not found!");
      }
      return ResponseEntity.ok(tokenResponse);
    } catch(Exception e) {
      throw new BadCredentialsException("Invalid username/password supplied!");
    }
  }

}
