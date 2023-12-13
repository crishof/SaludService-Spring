package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion,String> {
}
