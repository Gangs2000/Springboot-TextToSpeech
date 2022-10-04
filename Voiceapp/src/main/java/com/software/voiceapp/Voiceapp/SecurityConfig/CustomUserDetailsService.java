package com.software.voiceapp.Voiceapp.SecurityConfig;

import com.software.voiceapp.Voiceapp.Document.Users;
import com.software.voiceapp.Voiceapp.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user=usersRepository.findById(username);
        if(user.isEmpty())
            throw new UsernameNotFoundException("User doesn't exist..");
        return new User(user.get().get_id(),user.get().getPassword(),new LinkedList<>());
    }
}
