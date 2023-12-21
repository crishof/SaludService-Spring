package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.enums.*;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.*;
import com.crisdev.saludservice.repository.HorarioLaboralRepository;
import com.crisdev.saludservice.repository.PacienteRepository;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import com.crisdev.saludservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/util")
public class UtilController {

    @Autowired
    ProfesionalService profesionalService;

    @Autowired
    PacienteService pacienteService;

    @Autowired
    ProfesionalRepository profesionalRepository;

    @Autowired
    UbicacionService ubicacionService;

    @Autowired
    UtilService utilService;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    ImagenService imagenService;
    @Autowired
    HorarioLaboralRepository horarioLaboralRepository;

    @Autowired
    TurnoService turnoService;
    Random random = new Random();

    @GetMapping("/profesionalRandom")
    public String crearProfesional() throws MiException {

        Usuario usuario = crearUsuario();
        Profesional profesional = new Profesional();

        profesional.setNombre(usuario.getNombre());
        profesional.setApellido(usuario.getApellido());
        profesional.setDni(usuario.getDni());
        profesional.setFechaNacimiento(usuario.getFechaNacimiento());
        profesional.setEmail(usuario.getEmail());
        profesional.setPassword(usuario.getPassword());
        profesional.setFotoPerfil(usuario.getFotoPerfil());
        profesional.setFechaAlta(usuario.getFechaAlta());
        profesional.setUbicacion(usuario.getUbicacion());


        long matricula = random.nextLong(1000, 99999);
        Especialidad especialidad = Especialidad.values()[random.nextInt(Especialidad.values().length)];

        profesional.setMatricula(matricula);
        profesional.setEspecialidad(especialidad);
        profesional.setDiploma(null);
        profesional.setRol(Rol.PROFESIONAL);

        profesional.setHorarioLaboral(crearHorario());


        Profesional profe = profesionalRepository.save(profesional);

        turnoService.generarTurnos(profe.getId(), profesional.getHorarioLaboral().get(1));
        turnoService.generarTurnos(profe.getId(), profesional.getHorarioLaboral().get(0));

        return "redirect:/profesional/listarProfesionales";
    }
    @GetMapping("/pacienteRandom")
    public String crearPaciente() throws MiException {

        Usuario usuario = crearUsuario();
        Paciente paciente = new Paciente();

        paciente.setNombre(usuario.getNombre());
        paciente.setApellido(usuario.getApellido());
        paciente.setDni(usuario.getDni());
        paciente.setFechaNacimiento(usuario.getFechaNacimiento());
        paciente.setEmail(usuario.getEmail());
        paciente.setPassword(usuario.getPassword());
        paciente.setFotoPerfil(usuario.getFotoPerfil());
        paciente.setFechaAlta(usuario.getFechaAlta());
        paciente.setUbicacion(usuario.getUbicacion());

        paciente.setRol(Rol.PACIENTE);
        pacienteRepository.save(paciente);

        return "redirect:/";
    }

    public Usuario crearUsuario() throws MiException {

        String[] nombres = {"Juan", "José", "Antonio", "Manuel", "David", "Carlos", "Francisco", "Jesús", "Pedro", "Pablo", "Luis", "Diego", "Andrés", "Alejandro", "Miguel", "Fernando", "Jorge", "Óscar", "María", "Ana", "Laura", "Carmen", "Elena", "Isabel", "Marta", "Julia", "Sara", "Paula", "Inés", "Teresa", "Cristina", "Blanca", "Rocío", "Alba", "Daniela", "Andrea"};
        String[] apellidos = {"García", "López", "García", "López", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero", "Gómez", "Fernández", "Hernández", "Díaz", "Muñoz", "Castro", "Jiménez", "Moreno", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero"};

        String nombre = nombres[random.nextInt(nombres.length)];
        String apellido = apellidos[random.nextInt(apellidos.length)];
        long dni = random.nextLong(5000000, 50000000);
        String email = nombre.toLowerCase() + apellido.substring(0, 1).toLowerCase() + "@mail.com";
        String password = new BCryptPasswordEncoder().encode("123123");

        Usuario usuario = new Usuario();

        Imagen imagen = imagenService.guardar(null);

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setFechaNacimiento(crearfecha());
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setFotoPerfil(imagen);
        usuario.setFechaAlta(LocalDate.now());
        usuario.setUbicacion(crearUbicacion());

        return usuario;
    }

    public LocalDate crearfecha() throws MiException {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, random.nextInt(2024 - 2000) + 2000);
        calendar.set(Calendar.MONTH, random.nextInt(12));
        calendar.set(Calendar.DAY_OF_MONTH, random.nextInt(31));
        Date fechaNacimiento = calendar.getTime();
        // Formatear la fecha como una cadena de texto
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaNacimientoStr = dateFormat.format(fechaNacimiento);
        return utilService.formatearFecha(fechaNacimientoStr);
    }

    public Ubicacion crearUbicacion() {

        String[] localidades = {"La Plata", "Mar del Plata", "Quilmes", "San Fernando del Valle de Catamarca", "San Isidro", "Andalgalá", "Resistencia", "Barranqueras ", "Quitilipi"};
        String[] direcciones = {"9 de Julio 1995", "Rivadavia 110", "San Martín 44", "Belgrano 5962", "Sarmiento 74", "Mitre 998", "Independencia 785", "Pueyrredón 404", "Córdoba 753", "Santa Fe 620"};

        Pais pais = Pais.values()[random.nextInt(Pais.values().length)];
        Provincia provincia = Provincia.values()[random.nextInt(Provincia.values().length)];
        String localidad = localidades[random.nextInt(localidades.length)];
        String direccion = direcciones[random.nextInt(direcciones.length)];
        String codigoPostal = String.valueOf(random.nextInt(1000, 9999));

        return ubicacionService.crearUbicacion(pais, provincia, localidad, direccion, codigoPostal);
    }

    public List<HorarioLaboral> crearHorario() {

        List<HorarioLaboral> horarioLista = new ArrayList<>();

        DiaSemana dia = DiaSemana.values()[random.nextInt(DiaSemana.values().length)];
        DiaSemana dia2;
        do {
            dia2 = DiaSemana.values()[random.nextInt(DiaSemana.values().length)];
        } while (dia2 == dia);

        LocalTime[] horariosEntrada = new LocalTime[17];
        LocalTime[] horariosSalida = new LocalTime[17];

        for (int i = 0; i < 17; i++) {
            horariosEntrada[i] = LocalTime.of(8 + i / 2, (i % 2) * 30);
            horariosSalida[i] = LocalTime.of(12 + i / 2, (i % 2) * 30);
        }

        LocalTime horarioEntrada = horariosEntrada[random.nextInt(horariosEntrada.length)];
        LocalTime horarioSalida;

        do {
            horarioSalida = horariosSalida[random.nextInt(horariosSalida.length)];
        } while (horarioSalida.getHour() <= horarioEntrada.getHour());

        HorarioLaboral horario = new HorarioLaboral();
        HorarioLaboral horario2 = new HorarioLaboral();

        horario.setDiaSemana(dia);
        horario.setHoraEntrada(horarioEntrada);
        horario.setHoraSalida(horarioSalida);
        horarioLista.add(horarioLaboralRepository.save(horario));

        horario2.setDiaSemana(dia2);
        horario2.setHoraEntrada(horarioEntrada);
        horario2.setHoraSalida(horarioSalida);
        horarioLista.add(horarioLaboralRepository.save(horario2));

        return horarioLista;

    }
//        int valorAleatorio = random.nextInt(36) + 15;
//        int precioConsulta = Math.round(valorAleatorio * 100.0f / 100) * 100;

}
