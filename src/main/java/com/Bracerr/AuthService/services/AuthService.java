package com.Bracerr.AuthService.services;

import com.Bracerr.AuthService.models.*;
import com.Bracerr.AuthService.payload.request.LoginRequest;
import com.Bracerr.AuthService.payload.request.RecoveryRequest;
import com.Bracerr.AuthService.payload.request.SignupRequest;
import com.Bracerr.AuthService.payload.response.JwtResponse;
import com.Bracerr.AuthService.payload.response.MessageResponse;
import com.Bracerr.AuthService.repository.ConfirmationTokenRepository;
import com.Bracerr.AuthService.repository.PasswordRecoveryTokenRepository;
import com.Bracerr.AuthService.repository.RoleRepository;
import com.Bracerr.AuthService.repository.UserRepository;
import com.Bracerr.AuthService.security.jwt.JwtUtils;
import com.Bracerr.AuthService.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${link.timeout.minutes}")
    private int timeOutEmailMinutes;

    @Value("${link.recovery.password.minutes}")
    private int timeOutPasswordMinutes;

    private final EmailSenderService emailSenderService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       RoleRepository roleRepository, PasswordEncoder encoder,
                       JwtUtils jwtUtils, ConfirmationTokenRepository confirmationTokenRepository,
                       EmailSenderService emailSenderService, PasswordRecoveryTokenRepository passwordRecoveryTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
        this.passwordRecoveryTokenRepository = passwordRecoveryTokenRepository;
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        if (userRepository.findByEmail(loginRequest.getEmail()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: no such email."));
        }

        if (!userRepository.findByEmail(loginRequest.getEmail()).get().isEnabled()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: confirm your account first."));
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(),
                        userDetails.getFirstName(),
                        userDetails.getLastName(),
                        userDetails.getPatronymic(),
                        userDetails.getEmail(), roles));
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            if (!userRepository.findByEmail(signUpRequest.getEmail()).get().isEnabled()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Send this email by mail."));
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use."));
        }

        if (!signUpRequest.getPassword().equals(signUpRequest.getRepeatPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Passwords don't match"));
        }

        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getPatronymic(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(user, timeOutEmailMinutes);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom(emailFrom);
        mailMessage.setText("To confirm your account, please click here: "
                + "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);


        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> recoveryPassword(RecoveryRequest recoveryRequest) {

        Optional<User> userOptional = userRepository.findByEmail(recoveryRequest.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: No such email."));
        }

        User user = userOptional.get();

        if (passwordRecoveryTokenRepository.existsByUserId(user.getId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Try again after " + timeOutPasswordMinutes + " minutes"));
        }

        PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken(user, timeOutPasswordMinutes);
        passwordRecoveryTokenRepository.save(passwordRecoveryToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Recovery password");
        mailMessage.setFrom(emailFrom);
        mailMessage.setText("To continue password recovery on the locallhost:8080 website, follow the link:\n "
                + "http://localhost:8080/recovery-password?token=" + passwordRecoveryToken.getPasswordRecoveryToken() +
                "\n If you didn't request anything, don't pay attention");

        emailSenderService.sendEmail(mailMessage);

        return ResponseEntity.ok(new MessageResponse("The recovery request has been sent."));
    }

}
