package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.enums.DiaSemana;
import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.model.Usuario;
import com.crisdev.saludservice.service.HorarioLaboralService;
import com.crisdev.saludservice.service.ProfesionalService;
import com.crisdev.saludservice.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/profesional")
public class ProfesionalController {

    @Autowired
    ProfesionalService profesionalService;

    @Autowired
    HorarioLaboralService horarioLaboralService;

    @Autowired
    UtilService utilService;

    @GetMapping("/listarProfesionales")
    public String listarProfesionales(@Param("especialidad") String especialidad, @Param("columna") String columna, ModelMap modelo) {


        List<Profesional> profesionales = profesionalService.listarProfesionales(especialidad, columna);
        modelo.addAttribute("profesionales", profesionales);
        Especialidad[] especialidades = Especialidad.values();
        modelo.addAttribute("especialidades", especialidades);
        modelo.addAttribute("valorSeleccionado", especialidad);
        modelo.addAttribute("ordenSeleccionado", columna);
        return "profesional_lista";
    }

    @PreAuthorize("hasAnyRole('PROFESIONAL','ADMIN')")
    @GetMapping("/dashboard")
    public String panel() {
        return "dashboard_profesional";
    }


    @GetMapping("/horario")
    public String crearHorario(ModelMap model, @RequestParam(required = false) String error, @RequestParam(required = false) String exito) {

        if (error != null) {
            model.put("error", error);
        }
        if (exito != null) {
            model.put("exito", exito);
        }
        DiaSemana[] dias = DiaSemana.values();
        model.addAttribute("dias", dias);

        return "profesional_horario";
    }

    @PostMapping("/horario/{id}")
    public String crearHorario(@PathVariable String id, String dia, String horaEntrada, String horaSalida, ModelMap model, RedirectAttributes redirectAttributes, HttpSession session) {

        System.out.println("TEST POST ENTRADA");
        try {
            utilService.validarHorario(dia, horaEntrada, horaSalida);

            Usuario usuario = (Usuario) session.getAttribute("usuariosession");

            var horario = horarioLaboralService.crearHorario(id, dia, horaEntrada, horaSalida);

            profesionalService.agregarHorario(id, horario);


            redirectAttributes.addAttribute("exito", "Usuario modificado con éxito");
        } catch (MiException e) {
            redirectAttributes.addAttribute("error", e.getMessage());

            return "redirect:/profesional/horario";
        }
        return "redirect:/profesional/horario";
    }
}
