package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.enums.DiaSemana;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.HorarioLaboral;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import com.crisdev.saludservice.service.ProfesionalService;
import com.crisdev.saludservice.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
