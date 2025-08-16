
package com.soloval.tech.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "VERIFICATION_CODE")
@NamedQueries({
        @NamedQuery(name = "VerificationCode.validateVerificationCode", query = "SELECT 1 FROM VerificationCode t WHERE t.realmId = :realmId AND t.phoneNumber = :phoneNumber AND t.code = :code AND t.expiresAt >= :now AND t.kind = :kind"),
        @NamedQuery(name = "VerificationCode.findByRealm", query = "FROM VerificationCode t WHERE t.realmId = :realmId")
})
public class VerificationCode {

    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    @Column(name = "REALM_ID", nullable = false)
    private String realmId;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "TRANSACTION_ID", nullable = false)
    private String transactionId;

    @Column(name = "DEVICE_ID", nullable = false)
    private String deviceId;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "EXPIRES_AT")
    private ZonedDateTime expiresAt;

    @Column(name = "CREATED_AT")
    private ZonedDateTime createdAt;

}