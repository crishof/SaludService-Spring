package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepository extends JpaRepository<Imagen, String> {
}
