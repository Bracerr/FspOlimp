package com.Bracerr.AuthService.controllers;


import com.Bracerr.AuthService.services.ConfirmEmailService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConfirmEmailController {

    private final ConfirmEmailService confirmEmailService;

    @Autowired
    public ConfirmEmailController(ConfirmEmailService confirmEmailService){
        this.confirmEmailService = confirmEmailService;
    }

    @Transactional
    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        return confirmEmailService.confirmUserAccount(confirmationToken);
    }
}
