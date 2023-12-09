package com.crisdev.saludservice.controller;

import com.crisdev.saludservice.service.ProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profesional")
public class ProfesionalController {

    @Autowired
    ProfesionalService profesionalService;

    @GetMapping("/listarProfesionales")
    public String listarProfesionales(ModelMap modelMap){

        var profesionales = profesionalService.listarProfesionales();
        modelMap.addAttribute("profesionales",profesionales);

        return "profesional_lista";


    }
}
