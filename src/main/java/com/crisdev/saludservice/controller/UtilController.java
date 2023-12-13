package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.enums.DiaSemana;
import com.crisdev.saludservice.enums.Especialidad;
import com.crisdev.saludservice.enums.Pais;
import com.crisdev.saludservice.enums.Provincia;
import com.crisdev.saludservice.exception.MiException;
import com.crisdev.saludservice.model.Paciente;
import com.crisdev.saludservice.model.Ubicacion;
import com.crisdev.saludservice.repository.ProfesionalRepository;
import com.crisdev.saludservice.service.PacienteService;
import com.crisdev.saludservice.service.ProfesionalService;
import com.crisdev.saludservice.service.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @GetMapping("/profesionalRandom")
    public String cargarDataBase() throws MiException, ParseException {

        String[] nombres = {"Juan", "José", "Antonio", "Manuel", "David", "Carlos", "Francisco", "Jesús", "Pedro", "Pablo", "Luis", "Diego", "Andrés", "Alejandro", "Miguel", "Fernando", "Jorge", "Óscar", "María", "Ana", "Laura", "Carmen", "Elena", "Isabel", "Marta", "Julia", "Sara", "Paula", "Inés", "Teresa", "Cristina", "Blanca", "Rocío", "Alba", "Daniela", "Andrea"};
        String[] apellidos = {"García", "López", "García", "López", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero", "Gómez", "Fernández", "Hernández", "Díaz", "Muñoz", "Castro", "Jiménez", "Moreno", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero"};
        String[] localidades = {"La Plata", "Mar del Plata", "Quilmes", "San Fernando del Valle de Catamarca", "San Isidro", "Andalgalá", "Resistencia", "Barranqueras ", "Quitilipi"};
        String[] direcciones = {"9 de Julio 1995", "Rivadavia 110", "San Martín 44", "Belgrano 5962", "Sarmiento 74", "Mitre 998", "Independencia 785", "Pueyrredón 404", "Córdoba 753", "Santa Fe 620"};

        Random random = new Random();

        String nombre = nombres[random.nextInt(nombres.length)];

        String apellido = apellidos[random.nextInt(apellidos.length)];

        Long dni = random.nextLong(5000000, 50000000);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, random.nextInt(2024 - 2000) + 2000);
        calendar.set(Calendar.MONTH, random.nextInt(12));
        calendar.set(Calendar.DAY_OF_MONTH, random.nextInt(31));
        Date fechaNacimiento = calendar.getTime();
        // Formatear la fecha como una cadena de texto
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaNacimientoStr = dateFormat.format(fechaNacimiento);

        String email = nombre.toLowerCase() + apellido.substring(0, 1).toLowerCase() + "@mail.com";

        String password = "123123";

        Long matricula = random.nextLong(1000, 99999);

        Especialidad especialidad = Especialidad.values()[random.nextInt(Especialidad.values().length)];

        Pais pais = Pais.values()[random.nextInt(Pais.values().length)];
        Provincia provincia = Provincia.values()[random.nextInt(Provincia.values().length)];
        String localidad = localidades[random.nextInt(localidades.length)];
        String direccion = direcciones[random.nextInt(direcciones.length)];
        String codigoPostal = String.valueOf(random.nextInt(1000, 9999));

        Ubicacion ubicacion = ubicacionService.crearUbicacion(pais, provincia, localidad, direccion, codigoPostal);

        List<DiaSemana> diasDisponibles = new ArrayList<>();
        DiaSemana[] diasSemana = DiaSemana.values();
        for (int i = 0; i < 3; i++) {
            DiaSemana diaAleatorio = diasSemana[random.nextInt(diasSemana.length)];
            diasDisponibles.add(diaAleatorio);
        }

        LocalTime[] horariosEntrada = new LocalTime[17];
        for (int i = 0; i < 17; i++) {
            horariosEntrada[i] = LocalTime.of(8 + i / 2, (i % 2) * 30);
        }

        LocalTime[] horariosSalida = new LocalTime[17];
        for (int i = 0; i < 17; i++) {
            horariosSalida[i] = LocalTime.of(12 + i / 2, (i % 2) * 30);
        }

        LocalTime horarioEntrada = horariosEntrada[random.nextInt(horariosEntrada.length)];
        LocalTime horarioSalida = horariosSalida[random.nextInt(horariosSalida.length)];

        int valorAleatorio = random.nextInt(36) + 15;
        int precioConsulta = Math.round(valorAleatorio * 100.0f / 100) * 100;

        profesionalService.crearProfesional(nombre, apellido, dni, fechaNacimientoStr, null, matricula, null, especialidad, email, password, password, ubicacion);

        var registrado = profesionalRepository.buscarPorEmail(email);

        return "index";
    }

    @GetMapping("/pacienteRandom")
    public String cargarDataBasePaciente() throws MiException, ParseException {

        String[] nombres = {"Juan", "José", "Antonio", "Manuel", "David", "Carlos", "Francisco", "Jesús", "Pedro", "Pablo", "Luis", "Diego", "Andrés", "Alejandro", "Miguel", "Fernando", "Jorge", "Óscar", "María", "Ana", "Laura", "Carmen", "Elena", "Isabel", "Marta", "Julia", "Sara", "Paula", "Inés", "Teresa", "Cristina", "Blanca", "Rocío", "Alba", "Daniela", "Andrea"};
        String[] apellidos = {"García", "López", "García", "López", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero", "Gómez", "Fernández", "Hernández", "Díaz", "Muñoz", "Castro", "Jiménez", "Moreno", "Martínez", "Rodríguez", "Sánchez", "González", "Pérez", "Álvarez", "Romero"};
        String[] localidades = {"La Plata", "Mar del Plata", "Quilmes", "San Fernando del Valle de Catamarca", "San Isidro", "Andalgalá", "Resistencia", "Barranqueras ", "Quitilipi"};
        String[] direcciones = {"9 de Julio 1995", "Rivadavia 110", "San Martín 44", "Belgrano 5962", "Sarmiento 74", "Mitre 998", "Independencia 785", "Pueyrredón 404", "Córdoba 753", "Santa Fe 620"};

        Random random = new Random();

        String nombre = nombres[random.nextInt(nombres.length)];

        String apellido = apellidos[random.nextInt(apellidos.length)];

        Long dni = random.nextLong(5000000, 50000000);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, random.nextInt(2024 - 2000) + 2000);
        calendar.set(Calendar.MONTH, random.nextInt(12));
        calendar.set(Calendar.DAY_OF_MONTH, random.nextInt(31));
        Date fechaNacimiento = calendar.getTime();
        // Formatear la fecha como una cadena de texto
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaNacimientoStr = dateFormat.format(fechaNacimiento);

        String email = nombre.toLowerCase() + apellido.substring(0, 1).toLowerCase() + "@mail.com";

        String password = "123123";

        Pais pais = Pais.values()[random.nextInt(Pais.values().length)];
        Provincia provincia = Provincia.values()[random.nextInt(Provincia.values().length)];
        String localidad = localidades[random.nextInt(localidades.length)];
        String direccion = direcciones[random.nextInt(direcciones.length)];
        String codigoPostal = String.valueOf(random.nextInt(1000, 9999));

        Ubicacion ubicacion = ubicacionService.crearUbicacion(pais, provincia, localidad, direccion, codigoPostal);

        pacienteService.crearPaciente(nombre, apellido, dni, fechaNacimientoStr, null, email, password, password, ubicacion);

        return "index";
    }
}
