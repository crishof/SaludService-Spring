package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, String> {
}
