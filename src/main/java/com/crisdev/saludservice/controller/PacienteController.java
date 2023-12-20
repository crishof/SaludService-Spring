package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.service.PacienteService;
import com.crisdev.saludservice.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    TurnoService turnoService;
    @Autowired
    PacienteService pacienteService;

    @GetMapping("/listarCitas")
    public String listarCitas(HttpSession session, ModelMap modelMap) throws MiException {

        Paciente paciente = (Paciente) session.getAttribute("usuariosession");

            var turnos = turnoService.listarTurnosPaciente(paciente.getId());
            modelMap.addAttribute("turnos",turnos);

        return "paciente_citas";
    }
}
