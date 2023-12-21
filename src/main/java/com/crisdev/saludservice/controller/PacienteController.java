package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.service.ConsultaService;
import com.crisdev.saludservice.service.PacienteService;
import com.crisdev.saludservice.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/paciente")
public class PacienteController {
//
    @Autowired
    TurnoService turnoService;
    @Autowired
    ConsultaService consultaService;

    @PreAuthorize("hasAnyRole('PACIENTE','ADMIN')")
    @GetMapping("/dashboard")
    public String panel() {
        return "dashboard_paciente";
    }

    @GetMapping("/listarCitas")
    public String listarCitas(HttpSession session, ModelMap modelMap) {

        Paciente paciente = (Paciente) session.getAttribute("usuariosession");

        var turnos = turnoService.listarTurnosPaciente(paciente);
        modelMap.addAttribute("turnos", turnos);

        return "paciente_citas";
    }

    @GetMapping("/listarConsultas")
    public String listarConsultas(HttpSession session, ModelMap modelMap) {

        Paciente paciente = (Paciente) session.getAttribute("usuariosession");

        var consultas = consultaService.listarConsultasPaciente(paciente);
        modelMap.addAttribute("consultas", consultas);

        return "paciente_consultas";
    }
}
