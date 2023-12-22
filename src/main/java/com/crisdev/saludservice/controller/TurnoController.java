package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import com.crisdev.saludservice.service.PacienteService;
import com.crisdev.saludservice.service.ProfesionalService;
import com.crisdev.saludservice.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/turno")
public class TurnoController {

    @Autowired
    TurnoService turnoService;
    @Autowired
    ProfesionalService profesionalService;
    @Autowired
    ProfesionalRepository profesionalRepository;
    @Autowired
    PacienteService pacienteService;


    @GetMapping("/verTurnos")
    public String verTurnos(ModelMap modelMap, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Turno> turnos = turnoService.listarTurnosDisponibles(pageable);

        modelMap.addAttribute("turnos", turnos);
        return "turno_lista";

    }

    @GetMapping("/solicitar/{id}")
    public String solicitarTurno(@PathVariable String id, ModelMap modelMap) {

        Turno turno = turnoService.buscarPorId(id);

        modelMap.addAttribute("turno", turno);
        return "turno_solicitud";

    }

    @PostMapping("/confirmarTurno/{id}")
    public String confirmarTurno(@PathVariable String id, HttpSession session, ModelMap modelMap) throws MiException {

        Paciente paciente = (Paciente) session.getAttribute("usuariosession");
        turnoService.confirmarTurno(id, paciente);

        return "redirect:/paciente/listarCitas";
    }

    @GetMapping("/cancelarTurno/{id}")
    public String cancelarTurno(@PathVariable String id){

        turnoService.cancelarTurno(id);

        return "redirect:/paciente/listarCitas";
    }

}
