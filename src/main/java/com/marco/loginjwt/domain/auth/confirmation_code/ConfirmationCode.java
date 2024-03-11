package com.marco.loginjwt.domain.auth.confirmation_code;

import com.marco.loginjwt.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "confirmation_code")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfirmationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String code;
    @Column
    private Instant expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
