package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User GetUserByID(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public List<User> GetAllUser() {
        return this.userRepository.findAll();

    }

    public User updateUser(long id, User updateUser) {
        User user = GetUserByID(id);
        user.setId(id);
        user.setEmail(updateUser.getEmail());
        user.setName(updateUser.getName());
        user.setPassword(updateUser.getPassword());
        saveUser(user);
        return user;
    }

    public User GetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

}
