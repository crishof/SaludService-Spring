package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.HorarioLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioLaboralRepository extends JpaRepository<HorarioLaboral, String> {
}
