package com.soloval.tech.exception;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import static com.soloval.tech.utils.RestCommonUtils.buildBaseResponse;

@Provider
public class AppExceptionHandler implements ExceptionMapper<AppException> {

    private static final Logger logger = Logger.getLogger(org.keycloak.services.error.KeycloakErrorHandler.class);

    @Override
    public Response toResponse(AppException throwable) {

        return Response.status(throwable.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE.toString())
                .entity(buildBaseResponse(throwable.getStatusCode(), "", throwable.getMessageCode(), throwable.getMessage(),throwable.getDetailMessage()))
                .build();
    }
}
