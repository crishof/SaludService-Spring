package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.enums.DiaSemana;
import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.HorarioLaboral;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Profesional;
import com.crisdev.saludservice.service.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/profesional")
public class ProfesionalController {

    final ProfesionalService profesionalService;
    final HorarioLaboralService horarioLaboralService;
    final UtilService utilService;
    final TurnoService turnoService;
    final ConsultaService consultaService;

    public ProfesionalController(ProfesionalService profesionalService, HorarioLaboralService horarioLaboralService, UtilService utilService, TurnoService turnoService, ConsultaService consultaService) {
        this.profesionalService = profesionalService;
        this.horarioLaboralService = horarioLaboralService;
        this.utilService = utilService;
        this.turnoService = turnoService;
        this.consultaService = consultaService;
    }

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

    @GetMapping("/buscarProfesional")
    public String buscarProfesional(@RequestParam String texto, ModelMap modelo) {


        List<Profesional> profesionales = profesionalService.buscarProfesionales(texto);
        modelo.addAttribute("profesionales", profesionales);
        Especialidad[] especialidades = Especialidad.values();
        modelo.addAttribute("especialidades", especialidades);
        return "profesional_lista";
    }


    @PreAuthorize("hasAnyRole('PROFESIONAL','ADMIN')")
    @GetMapping("/dashboard")
    public String panel() {
        return "dashboard_profesional";
    }

    @GetMapping("/horario")
    public String crearHorario(ModelMap model, @RequestParam(required = false) String error, @RequestParam(required = false) String exito) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Profesional profesional = profesionalService.buscarPorEmail(email);
        model.addAttribute("profesional", profesional);

        List<HorarioLaboral> horarios = horarioLaboralService.listarHorariosProfesional(profesional.getId());
        model.addAttribute("horarios", horarios);

        if (profesional != null) {
            if (error != null) {
                model.put("error", error);
            }
            if (exito != null) {
                model.put("exito", exito);
            }

            DiaSemana[] dias = DiaSemana.values();
            model.addAttribute("dias", dias);

            return "profesional_horario";
        } else {
            model.put("error", "No se encontró el perfil de profesional para el usuario logueado.");
            return "profesional_horario";
        }
    }

    @PostMapping("/horario/{id}")
    public String crearHorario(@PathVariable String id, DiaSemana dia, String horaEntrada, String horaSalida, RedirectAttributes redirectAttributes) {

        try {
            utilService.validarHorario(dia, horaEntrada, horaSalida);

            Profesional profesional = profesionalService.buscarPorId(id);

            utilService.validarHorarioProfesional(profesional, dia, horaEntrada, horaSalida);

            var horario = horarioLaboralService.crearHorario(dia, horaEntrada, horaSalida);

            profesionalService.agregarHorario(profesional, horario);

            turnoService.generarTurnos(profesional.getId(), horario);


            redirectAttributes.addAttribute("exito", "Horario agregado con éxito");

        } catch (MiException e) {
            redirectAttributes.addAttribute("error", e.getMessage());

            return "redirect:/profesional/horario";
        }
        return "redirect:/profesional/horario";
    }

    @PostMapping("/precioConsulta/{id}")
    public String modificarPrecioConsulta(@PathVariable String id, double precio, RedirectAttributes redirectAttributes) {

        try {
            utilService.validarPrecio(precio);
            profesionalService.actualizarPrecioConsulta(id, precio);
            redirectAttributes.addAttribute("exito", "Precio actualizado con éxito");

        } catch (MiException e) {
            redirectAttributes.addAttribute("error", e.getMessage());

            return "redirect:/profesional/horario";
        }
        return "redirect:/profesional/horario";
    }

    @GetMapping("/listarCitas")
    public String listarCitas(HttpSession session, ModelMap modelMap) {

        Profesional profesional = (Profesional) session.getAttribute("usuariosession");

        var turnos = turnoService.listarTurnosReservados(profesional.getId());
        modelMap.addAttribute("turnos", turnos);

        return "profesional_citas";
    }

    @GetMapping("/listarPacientes")
    public String listarPacientes(HttpSession session, ModelMap modelMap) {

        Profesional profesional = (Profesional) session.getAttribute("usuariosession");

        List<Paciente> pacientes = profesionalService.listarPacientesDelProfesional(profesional.getId());
        modelMap.addAttribute("pacientes", pacientes);

        return "profesional_listaPacientes";
    }
}
