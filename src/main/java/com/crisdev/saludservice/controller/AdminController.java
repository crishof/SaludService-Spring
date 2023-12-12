package com.crisdev.saludservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
//SE puede agregar seguridad a toda la clase
public class AdminController {

    @GetMapping("/dashboard")
    public String panel() {
        return "dashboard_admin";
    }

//    /usuarios para ver la lista de users
}
