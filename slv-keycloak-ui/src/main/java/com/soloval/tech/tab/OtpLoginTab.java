package com.soloval.tech.tab;

import com.google.auto.service.AutoService;
import com.soloval.tech.constants.KeycloakAttributesConstant;
import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.services.ui.extend.UiTabProvider;
import org.keycloak.services.ui.extend.UiTabProviderFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(UiTabProviderFactory.class)
public class OtpLoginTab implements UiTabProvider, UiTabProviderFactory<ComponentModel> {
    static public final String PROVIDER_ID = "OTP Login";

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getHelpText() {
        return PROVIDER_ID;
    }

    @Override
    public String getPath() {
        return "/:realm/realm-settings/:tab?";
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("tab", "otp-login-ui-tab");
        return params;
    }

    @Override
    public void init(Config.Scope scope) {
    }

    @Override
    public void postInit(KeycloakSessionFactory ksf) {
    }

    @Override
    public void close() {
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        final ProviderConfigurationBuilder builder = ProviderConfigurationBuilder.create();
        builder.property()
                .name(KeycloakAttributesConstant.OTP_DIGITS_ATTRIBUTE)
                .label("OTP Digits")
                .type(ProviderConfigProperty.LIST_TYPE)
                .options(List.of("4", "6", "8"))
                .defaultValue("6")
                .helpText("Number of digits in the OTP code.")
                .add();
        builder.property()
                .name(KeycloakAttributesConstant.OTP_TTL_ATTRIBUTE)
                .label("Life Time (in seconds)")
                .type(ProviderConfigProperty.NUMBER_TYPE)
                .defaultValue("300")
                .helpText("Life Time (in seconds)")
                .add();
        builder.property()
                .name(KeycloakAttributesConstant.OTP_IS_SIMULATION_ATTRIBUTE)
                .label("Simulation Mode")
                .type(ProviderConfigProperty.BOOLEAN_TYPE)
                .defaultValue(true)
                .helpText("OTP code will be logged in simulation mode. This mode is used for testing purposes only.")
                .add();

        return builder.build();
    }

    @Override
    public void onCreate(KeycloakSession session, RealmModel realm, ComponentModel model) {
        UiTabProviderFactory.super.onCreate(session, realm, model);
    }

    @Override
    public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {

        UiTabProviderFactory.super.onUpdate(session, realm, oldModel, newModel);
    }

}