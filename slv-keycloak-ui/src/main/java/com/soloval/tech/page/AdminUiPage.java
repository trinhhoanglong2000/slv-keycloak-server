package com.soloval.tech.page;
import com.google.auto.service.AutoService;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.services.ui.extend.UiPageProvider;
import org.keycloak.services.ui.extend.UiPageProviderFactory;

import java.util.List;

/**
 * Implements UiPageProvider so it will be a master detail view in the admin ui of TODO items
 */
@AutoService(UiPageProviderFactory.class)
public class AdminUiPage implements UiPageProvider, UiPageProviderFactory<ComponentModel> {

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "Todo";
    }

    @Override
    public String getHelpText() {
        return "Here you can store your Todo items";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return ProviderConfigurationBuilder.create()
                .property()
                .name("name")
                .label("Name")
                .helpText("Short name of the task")
                .type(ProviderConfigProperty.STRING_TYPE)
                .add().property()
                .name("description")
                .label("Description")
                .helpText("Description of what needs to be done")
                .type(ProviderConfigProperty.TEXT_TYPE)
                .add().property()
                .name("prio")
                .label("Priority")
                .type(ProviderConfigProperty.LIST_TYPE)
                .options("critical", "high priority", "neutral", "low priority", "unknown")
                .add().build();
    }
}
