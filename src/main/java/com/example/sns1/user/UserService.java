package com.example.sns1.user;

import org.springframework.stereotype.Service;

import com.example.sns1.DataNotFoundException;
import com.example.sns1.answer.AnswerRepository;
import com.example.sns1.post.PostRepository;

import java.time.LocalDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;

    public UserData create(String username, String email, String password) {
        UserData user = new UserData();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreateDate(LocalDateTime.now());
        this.userRepository.save(user);
        return user;
    }

    public UserData getUser(String email) {
        Optional<UserData> userData = this.userRepository.findByEmail(email);
        if (userData.isPresent()) {
            return userData.get();
        } else {
            throw new DataNotFoundException("회원정보를 찾을 수 없습니다.");
        }
    }

    public void changeUsername(String email, String newUsername) {
        if (userRepository.findByUsername(newUsername).isPresent()) {
            throw new DataIntegrityViolationException("이미 존재하는 사용자명입니다.");
        }
        
        UserData userData = this.getUser(email);
        userData.setUsername(newUsername);
        this.userRepository.save(userData);
    }

    public void changePassword(String email, String currentPassword, String newPassword) {
        UserData userData = this.getUser(email);
        
        if (!passwordEncoder.matches(currentPassword, userData.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }
        
        userData.setPassword(passwordEncoder.encode(newPassword));
        this.userRepository.save(userData);
    }

    @Transactional
    public void withdrawal(String email, String password) {
        UserData userData = this.getUser(email);
        if (!passwordEncoder.matches(password, userData.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        postRepository.updateAuthorToNull(userData);
        answerRepository.updateAuthorToNull(userData);
        this.userRepository.delete(userData);
    }
}
