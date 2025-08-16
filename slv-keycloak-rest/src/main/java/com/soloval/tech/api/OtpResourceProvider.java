package com.soloval.tech.api;

import com.soloval.tech.constants.KeycloakAttributesConstant;
import com.soloval.tech.services.JPAService;
import com.soloval.tech.services.UserService;
import com.soloval.tech.services.impl.JPAServiceImpl;
import com.soloval.tech.services.impl.UserServiceImpl;
import com.soloval.tech.tab.OtpLoginTab;
import com.soloval.tech.utils.Authenticator;
import com.soloval.tech.utils.KeycloakUtils;
import com.soloval.tech.utils.SecurityUtils;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.soloval.tech.constants.CommonConstants.DEFAULT_PASSWORD;
import static com.soloval.tech.constants.CommonConstants.ResponseFields.OTP;
import static com.soloval.tech.constants.CommonConstants.ResponseFields.TRANSACTION_ID;
import static com.soloval.tech.constants.KeycloakAttributesConstant.PHONE_ATTRIBUTE;

@Slf4j
public class OtpResourceProvider implements RealmResourceProvider {
    private final KeycloakSession session;
    private final JPAService jpaService;
    private final UserService userService;

    public OtpResourceProvider(KeycloakSession session) {
        this.session = session;
        jpaService = new JPAServiceImpl(session);
        userService = new UserServiceImpl(session);
    }

    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {
    }

    @GET
    @Path("/generate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response Generate(@QueryParam("phoneNumber") String phoneNumber) {
        Authenticator.INSTANCE.authenticate(session);

        // CHECK IF PHONE NUMBER EXIST
        var listUserByUserAttribute = KeycloakUtils.getListUserByUserAttribute(session, PHONE_ATTRIBUTE, phoneNumber);
        UserModel userModel;
        if (listUserByUserAttribute.isEmpty()) {
            log.info("User with phone number {} not found. Creating new user...", phoneNumber);
            userModel = userService.createNewUser("user-" + UUID.randomUUID(), DEFAULT_PASSWORD, "", "", null, Map.of(PHONE_ATTRIBUTE, List.of(phoneNumber)));
        } else {
            userModel = listUserByUserAttribute.getFirst();
        }

        //GET OTP CONFIGURATION
        var configuration = KeycloakUtils.getFirstComponentConfig(KeycloakUtils.getListComponentByProviderId(session, OtpLoginTab.PROVIDER_ID));

        String isSimulation = configuration.getFirst(KeycloakAttributesConstant.OTP_IS_SIMULATION_ATTRIBUTE);
        String otpTTL = configuration.getFirst(KeycloakAttributesConstant.OTP_TTL_ATTRIBUTE);
        String otpDigitsNum = configuration.getFirst(KeycloakAttributesConstant.OTP_DIGITS_ATTRIBUTE);
        String transactionId = UUID.randomUUID().toString();
        String otp = SecurityUtils.generateOTP(Integer.parseInt(otpDigitsNum));
        // store OTP to DB
        jpaService.createOTPVertificationCode(userModel.getId(), phoneNumber, otpTTL, otp, transactionId);

        Map<String, String> response = new HashMap<>();
        response.put(TRANSACTION_ID, transactionId);
        if (Boolean.parseBoolean(isSimulation)) {
            response.put(OTP, otp);
            log.warn("OTP code {}", otp);
        }

        return Response.ok(response).build();
    }

    @POST
    @Path("/verify")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verifyOtp(Map<String, String> payload) {
        String transactionId = payload.get("transactionId");
        String otp = payload.get("otp");

        // Clean up
        // Set session flag
        UserSessionModel userSession = session.getContext().getUserSession();
        if (userSession != null) {
            userSession.setNote("device-owner-verified", "true");
        }

        return Response.ok(Map.of("status", "verified")).build();
    }
}
