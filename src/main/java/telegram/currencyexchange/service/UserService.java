package telegram.currencyexchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telegram.currencyexchange.model.User;
import telegram.currencyexchange.repository.UserRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void addUser(User user){
        userRepository.save(user);
    }
    @Transactional
    public void updateUser(User user){
        userRepository.save(user);
    }
}
