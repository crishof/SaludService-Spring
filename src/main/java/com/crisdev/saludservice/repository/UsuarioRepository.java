package com.crisdev.saludservice.repository;

import com.crisdev.saludservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,String> {
}
