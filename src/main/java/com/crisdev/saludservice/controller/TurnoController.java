package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.enums.DiaSemana;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.HorarioLaboral;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.model.Turno;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import com.crisdev.saludservice.service.ProfesionalService;
import com.crisdev.saludservice.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/turno")
public class TurnoController {

    @Autowired
    TurnoService turnoService;

    @Autowired
    ProfesionalService profesionalService;

    @Autowired
    ProfesionalRepository profesionalRepository;

    @GetMapping("/verTurnos")
    public String verTurnos(ModelMap modelMap,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "15") int size) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<Turno> turnos = turnoService.listarTurnos(pageable);

        modelMap.addAttribute("turnos", turnos);
        return "turno_lista";

    }

    @GetMapping("/solicitar/{id}")
    public String solicitarTurno(@PathVariable String id,ModelMap modelMap){

            Turno turno = turnoService.buscarPorId(id);

            System.out.println(turno.getFecha() + " " + turno.getProfesional());
        modelMap.addAttribute("turno",turno);
        return "turno_solicitud";

    }

    @PostMapping("/confirmarTurno/{id}")
    public String confirmarTurno(@PathVariable String id, ModelMap modelMap){

        return "redirect:/paciente/listarCitas";
    }

}
