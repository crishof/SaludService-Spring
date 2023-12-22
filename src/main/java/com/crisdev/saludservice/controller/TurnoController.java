package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import com.crisdev.saludservice.service.PacienteService;
import com.crisdev.saludservice.service.ProfesionalService;
import com.crisdev.saludservice.service.TurnoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/turno")
public class TurnoController {

    final TurnoService turnoService;
    final ProfesionalService profesionalService;
    final ProfesionalRepository profesionalRepository;
    final PacienteService pacienteService;

    public TurnoController(TurnoService turnoService, ProfesionalService profesionalService, ProfesionalRepository profesionalRepository, PacienteService pacienteService) {
        this.turnoService = turnoService;
        this.profesionalService = profesionalService;
        this.profesionalRepository = profesionalRepository;
        this.pacienteService = pacienteService;
    }


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
    public String confirmarTurno(@PathVariable String id, HttpSession session) throws MiException {

        Paciente paciente = (Paciente) session.getAttribute("usuariosession");
        turnoService.confirmarTurno(id, paciente);

        return "redirect:/paciente/listarCitas";
    }

    @GetMapping("/cancelarTurno/{id}")
    public String cancelarTurno(@PathVariable String id) {

        turnoService.cancelarTurno(id);

        return "redirect:/paciente/listarCitas";
    }

}
