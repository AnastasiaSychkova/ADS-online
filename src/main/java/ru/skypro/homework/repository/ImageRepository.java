package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Image;

import javax.transaction.Transactional;
@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findImageById(Long id);
}
