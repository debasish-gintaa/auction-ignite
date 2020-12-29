package com.asconsoft.gintaa.sequence;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

public class IdGenerator implements IdentifierGenerator, Configurable {
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {

    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UUID.randomUUID().toString();
    }
}
