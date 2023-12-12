package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Usuario;
import com.crisdev.saludservice.service.UsuarioService;
import com.crisdev.saludservice.service.UtilService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    final UsuarioService usuarioService;
    final UtilService utilService;

    public UsuarioController(UsuarioService usuarioService, UtilService utilService) {
        this.usuarioService = usuarioService;
        this.utilService = utilService;
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
    public String editarUsuario(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam(required = false) Long dni, @RequestParam String email, ModelMap model, RedirectAttributes redirectAttributes) {

        try {
            utilService.validarEdit(nombre, apellido, dni, email);
            usuarioService.editarUsuario(id, nombre, dni, apellido, email);
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
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam(required = false) Long dni, @RequestParam String email, ModelMap model, RedirectAttributes redirectAttributes, HttpSession session) {

        model.put("usuario", session);
        try {
            utilService.validarEdit(nombre, apellido, dni, email);

            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setEmail(email);
            session.setAttribute("usuariosession", usuario);

            usuarioService.editarUsuario(id, nombre, dni, apellido, email);
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
}
