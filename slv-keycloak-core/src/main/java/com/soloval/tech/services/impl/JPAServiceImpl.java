package com.soloval.tech.services.impl;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.soloval.tech.constants.CommonConstants;
import com.soloval.tech.jpa.VerificationCode;
import com.soloval.tech.services.JPAService;
import com.soloval.tech.utils.KeycloakUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TemporalType;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;

public class JPAServiceImpl implements JPAService {
    private final KeycloakSession session;
    private final EntityManager entityManager;
    public JPAServiceImpl(KeycloakSession session) {
        this.session = session;
        entityManager = session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    @Override
    public Boolean verifyOTPVertificationCode(String realmId, String userId, String otp, String transactionId) {
        //TODO: After verify, disable old token
        try {
            Integer veriCode = entityManager.createNamedQuery("VerificationCode.validateVerificationCode", Integer.class)
                    .setParameter("realmId", realmId)
                    .setParameter("userId", userId)
                    .setParameter("code", otp)
                    .setParameter("now", ZonedDateTime.now(ZoneOffset.UTC))
                    .setParameter("transactionId", transactionId)
                    .getSingleResult();
            if (veriCode == 1) {
                return true;
            }
        }
        catch (Exception err){ }
        return false;
    }

    @Override
    public Boolean createOTPVertificationCode(String userId, String phoneNumber, String otpTtl, String otp, String transactionId) {
        RealmModel realm = KeycloakUtils.getRealm(session);
        if (realm == null) {
            return false;
        }
        VerificationCode existing = entityManager.createQuery(
                        "SELECT v FROM VerificationCode v WHERE v.userId = :userId AND v.realmId = :realmId", VerificationCode.class)
                .setParameter("userId", userId)
                .setParameter("realmId", realm.getId())
                .getResultStream() // avoids NoResultException
                .findFirst()
                .orElse(null);

        entityManager.getTransaction().begin();

        if (existing != null) {
            // Update existing record
            existing.setCode(otp);
            existing.setTransactionId(transactionId);
            existing.setDeviceId("");
            existing.setType(CommonConstants.VerificationCodeType.OTP);
            existing.setExpiresAt(ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(Long.parseLong(otpTtl)));
            existing.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
            entityManager.merge(existing);
        } else {
            // Create new record
            VerificationCode entity = new VerificationCode();
            entity.setId(KeycloakModelUtils.generateId());
            entity.setRealmId(realm.getId());
            entity.setUserId(userId);
            entity.setPhoneNumber(phoneNumber);
            entity.setCode(otp);
            entity.setTransactionId(transactionId);
            entity.setDeviceId("");
            entity.setType(CommonConstants.VerificationCodeType.OTP);
            entity.setExpiresAt(ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(Long.parseLong(otpTtl)));
            entity.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
            entityManager.persist(entity);
        }

        entityManager.getTransaction().commit();
        return true;
    }
}
