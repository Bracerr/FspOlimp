package com.Bracerr.AuthService.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class PasswordRecoveryToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private Long tokenid;

    @Column(name="passwordRecoveryToken")
    private String passwordRecoveryToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public PasswordRecoveryToken(User user, int timeoutMinutes) {
        this.user = user;
        this.createdDate = new Date();
        this.passwordRecoveryToken = UUID.randomUUID().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, timeoutMinutes);
        this.expiryDate = calendar.getTime();
    }

    public PasswordRecoveryToken() {

    }
}
