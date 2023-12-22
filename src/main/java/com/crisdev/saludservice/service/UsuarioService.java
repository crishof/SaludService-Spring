package com.crisdev.saludservice.service;

import com.crisdev.saludservice.enums.Pais;
import com.crisdev.saludservice.enums.Provincia;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Imagen;
import com.crisdev.saludservice.model.Ubicacion;
import com.crisdev.saludservice.model.Usuario;
import com.crisdev.saludservice.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    final UsuarioRepository usuarioRepository;
    final UtilService utilService;
    final ImagenService imagenService;
    final UbicacionService ubicacionService;

    public UsuarioService(UsuarioRepository usuarioRepository, UtilService utilService, ImagenService imagenService, UbicacionService ubicacionService) {
        this.usuarioRepository = usuarioRepository;
        this.utilService = utilService;
        this.imagenService = imagenService;
        this.ubicacionService = ubicacionService;
    }

    public Usuario buscarUsuario(String id) throws MiException {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isPresent()) {
            return optionalUsuario.get();
        } else {
            // Manejar el caso en que el usuario no existe, por ejemplo, lanzar una excepción o devolver un valor predeterminado
            throw new MiException("Usuario no encontrado con ID: " + id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findUserByEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public void editarUsuario(String id, String nombre, Long dni, String apellido, String email, LocalDate fechaNacimiento) throws MiException {

        Optional<Usuario> respuesta = usuarioRepository.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setEmail(email);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuarioRepository.save(usuario);
        } else {
            throw new MiException("Usuario no encontrado");
        }
    }

    public void modificar(MultipartFile archivo, String idUsuario, String nombre, String apellido, String fechaNacimientoStr, Long dni, String email) throws MiException {
        utilService.validarUsuario(nombre, apellido, fechaNacimientoStr, dni, email);

        Optional<Usuario> respuesta = usuarioRepository.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setEmail(email);

            String idImagen = null;

            if (usuario.getFotoPerfil() != null) {
                idImagen = usuario.getFotoPerfil().getId();
            }
            Imagen imagen = imagenService.actualizar(idImagen, archivo);
            usuario.setFotoPerfil(imagen);

            usuarioRepository.save(usuario);
        }
    }

    public void actualizarUbicacion(String id, Pais pais, Provincia provincia, String localidad, String codigoPostal, String domicilio) throws MiException {

        Optional<Usuario> respuesta = usuarioRepository.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            Ubicacion ubicacion;

            if (usuario.getUbicacion() == null) {
                ubicacion = ubicacionService.crearUbicacion(pais, provincia, localidad, codigoPostal, domicilio);
                usuario.setUbicacion(ubicacion);
            } else {
                String idUbicacion = usuario.getUbicacion().getId();
                ubicacion = ubicacionService.actualizarUbicacion(idUbicacion, pais, provincia, localidad, codigoPostal, domicilio);
                usuario.setUbicacion(ubicacion);
            }
            usuarioRepository.save(usuario);
        }
    }
}
