package com.soloval.tech.jpa;

import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;

import java.util.Collections;
import java.util.List;

public class VerificationCodeJpaEntityProvider implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        return Collections.<Class<?>>singletonList(VerificationCode.class);
    }

    @Override
    public String getChangelogLocation() {
        return "liquidbase/db.changelog-master.xml";
    }

    @Override
    public void close() {}

    @Override
    public String getFactoryId() {
        return VerificationCodeJpaEntityProviderFactory.ID;
    }
}
