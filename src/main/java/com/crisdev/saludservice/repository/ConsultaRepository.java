package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.controller.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta,String> {
}
