package com.example.springbootecommerce.config;

import com.example.springbootecommerce.entity.Country;
import com.example.springbootecommerce.entity.Product;
import com.example.springbootecommerce.entity.ProductCategory;
import com.example.springbootecommerce.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] unSupportedMethods = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};

        disableHttpMethods(Product.class, config, unSupportedMethods);
        disableHttpMethods(ProductCategory.class, config, unSupportedMethods);
        disableHttpMethods(Country.class, config, unSupportedMethods);
        disableHttpMethods(State.class, config, unSupportedMethods);

        // call an internal helper method
        exposeIds(config);
    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] unSupportedMethods) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(unSupportedMethods)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(unSupportedMethods)));
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // create an array of entity types
        List<Class> entityClasses = new ArrayList<>();

        // get the entity types for the entities
        for (EntityType entityType : entities) {
            entityClasses.add(entityType.getJavaType());
        }

        // expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
