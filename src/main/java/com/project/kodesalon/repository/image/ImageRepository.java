package com.project.kodesalon.repository.image;

import com.project.kodesalon.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
