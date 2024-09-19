package com.programming.keycloak.model.token;

import com.programming.keycloak.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;
    private LocalDateTime createdAt;

    private static final int EXPIRATION_MINUTE = 15;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public VerificationToken(String token,  User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.tokenExpirationTime();
        this.createdAt = LocalDateTime.now();
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.tokenExpirationTime();
    }

    private Date tokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_MINUTE);

        return new Date(calendar.getTime().getTime());
    }
}
