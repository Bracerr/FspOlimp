package com.Bracerr.AuthService.controllers;


import com.Bracerr.AuthService.services.ConfirmEmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfirmEmailController {

    private final ConfirmEmailService confirmEmailService;

    @Autowired
    public ConfirmEmailController(ConfirmEmailService confirmEmailService){
        this.confirmEmailService = confirmEmailService;
    }

    @Transactional
    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        return confirmEmailService.confirmUserAccount(confirmationToken);
    }
}
