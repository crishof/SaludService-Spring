package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.service.ProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profesional")
public class ProfesionalController {

    @Autowired
    ProfesionalService profesionalService;

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
}
