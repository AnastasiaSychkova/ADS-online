package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.User;

import javax.transaction.Transactional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmailIgnoreCase(String email);
}
