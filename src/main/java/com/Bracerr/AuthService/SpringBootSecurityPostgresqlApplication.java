package com.Bracerr.AuthService;

import com.Bracerr.AuthService.services.ConfirmationTokenService;
import com.Bracerr.AuthService.services.PasswordRecoveryTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class SpringBootSecurityPostgresqlApplication {

	private final ConfirmationTokenService confirmationTokenService;
	private final PasswordRecoveryTokenService passwordRecoveryTokenService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityPostgresqlApplication.class, args);
	}

	@Autowired
	public SpringBootSecurityPostgresqlApplication(ConfirmationTokenService confirmationTokenService,
												   PasswordRecoveryTokenService passwordRecoveryTokenService){
		this.confirmationTokenService = confirmationTokenService;
		this.passwordRecoveryTokenService = passwordRecoveryTokenService;
	}

	@Scheduled(fixedRate = 60000)
	public void removeExpiredTokens() {
		confirmationTokenService.removeExpiredTokens();
		passwordRecoveryTokenService.removeExpiredTokens();
	}

}
