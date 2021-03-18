package telegram.currencyexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram.currencyexchange.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
