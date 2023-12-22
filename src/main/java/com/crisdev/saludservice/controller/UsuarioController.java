package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.enums.Pais;
import com.crisdev.saludservice.enums.Provincia;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Usuario;
import com.crisdev.saludservice.service.UbicacionService;
import com.crisdev.saludservice.service.UsuarioService;
import com.crisdev.saludservice.service.UtilService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    final UsuarioService usuarioService;
    final UtilService utilService;
    final UbicacionService ubicacionService;

    public UsuarioController(UsuarioService usuarioService, UtilService utilService, UbicacionService ubicacionService) {
        this.usuarioService = usuarioService;
        this.utilService = utilService;
        this.ubicacionService = ubicacionService;
    }

    @GetMapping("/listarUsuarios")
    public String listarUsuarios(ModelMap modelo, @RequestParam(required = false) String exito) {

        List<Usuario> usuarios = usuarioService.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        if (exito != null) {
            modelo.addAttribute("exito", "Usuario modificado con éxito");
        }
        return "usuario_lista";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable String id, @RequestParam(required = false) String error, @RequestParam(required = false) String exito, ModelMap model) throws MiException {

        Usuario usuario = usuarioService.buscarUsuario(id);
        model.addAttribute("usuario", usuario);
        if (error != null) {
            model.put("error", error);
        }
        if (exito != null) {
            model.put("exito", exito);
        }
        return "usuario_editar";
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, String fechaNacimientoStr, @RequestParam(required = false) Long dni, @RequestParam String email, ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            utilService.validarUsuario(nombre, apellido, fechaNacimientoStr, dni, email);
            LocalDate fechaNacimiento = utilService.formatearFecha(fechaNacimientoStr);
            usuarioService.editarUsuario(id, nombre, dni, apellido, email, fechaNacimiento);
            redirectAttributes.addAttribute("exito", "Usuario modificado con éxito");
        } catch (MiException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            model.addAttribute("nombre", nombre);
            model.addAttribute("apellido", apellido);
            model.addAttribute("dni", dni);
            model.addAttribute("email", email);

            return "redirect:/usuario/editar/{id}";
        }
        return "redirect:/usuario/listarUsuarios";
    }

    @GetMapping("/perfil")
    public String perfil(ModelMap model, @RequestParam(required = false) String error, @RequestParam(required = false) String exito) {
        if (error != null) {
            model.put("error", error);
        }
        if (exito != null) {
            model.put("exito", exito);
        }
        return "usuario_modificar";
    }

    @PostMapping("/perfil/{id}")
    public String actualizar(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, String fechaNacimientoStr, @RequestParam(required = false) Long dni, @RequestParam String email, ModelMap model, RedirectAttributes redirectAttributes, HttpSession session) {

        model.put("usuario", session);
        try {
            utilService.validarUsuario(nombre, apellido, fechaNacimientoStr, dni, email);

            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);

            LocalDate fechaNacimiento = utilService.formatearFecha(fechaNacimientoStr);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setDni(dni);
            usuario.setEmail(email);
            session.setAttribute("usuariosession", usuario);

            usuarioService.editarUsuario(id, nombre, dni, apellido, email, fechaNacimiento);
            redirectAttributes.addAttribute("exito", "Usuario modificado con éxito");
        } catch (MiException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            model.addAttribute("nombre", nombre);
            model.addAttribute("apellido", apellido);
            model.addAttribute("dni", dni);
            model.addAttribute("email", email);

            return "redirect:/usuario/perfil";
        }
        return "redirect:/usuario/perfil";
    }

    @GetMapping("/ubicacion")
    public String actualizarUbicacion(ModelMap model, @RequestParam(required = false) String error, @RequestParam(required = false) String exito) {

        if (error != null) {
            model.put("error", error);
        }
        if (exito != null) {
            model.put("exito", exito);
        }
        Pais[] paises = Pais.values();
        Provincia[] provincias = Provincia.values();
        model.addAttribute("paises", paises);
        model.addAttribute("provincias", provincias);

        return "ubicacion_modificar";
    }

    @PostMapping("/ubicacion/{id}")
    public String actualizarUbicacion(@PathVariable String id, @RequestParam(required = false) Pais pais, @RequestParam(required = false) Provincia provincia, @RequestParam String localidad, @RequestParam String codigoPostal, @RequestParam String domicilio, ModelMap model, RedirectAttributes redirectAttributes, HttpSession session) {

        model.put("usuario", session);
        try {
            utilService.validarUbicacion(pais, provincia, localidad, codigoPostal, domicilio);

            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            usuario.getUbicacion().setPais(pais);
            usuario.getUbicacion().setProvincia(provincia);
            usuario.getUbicacion().setLocalidad(localidad);
            usuario.getUbicacion().setCodigoPostal(codigoPostal);
            usuario.getUbicacion().setDomicilio(domicilio);
            session.setAttribute("usuariosession", usuario);
            usuarioService.actualizarUbicacion(id, pais, provincia, localidad, codigoPostal, domicilio);
            redirectAttributes.addAttribute("exito", "Datos modificados con éxito");

        } catch (MiException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            model.addAttribute("pais", pais);
            model.addAttribute("provinicia", provincia);
            model.addAttribute("localidad", localidad);
            model.addAttribute("codigoPostal", codigoPostal);
            model.addAttribute("domicilio", domicilio);

            return "redirect:/usuario/ubicacion";
        }
        return "redirect:/usuario/ubicacion";
    }
}
