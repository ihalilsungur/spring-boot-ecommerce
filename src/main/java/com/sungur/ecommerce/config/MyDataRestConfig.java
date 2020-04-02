package com.sungur.ecommerce.config;

import com.sungur.ecommerce.entity.Product;
import com.sungur.ecommerce.entity.ProductCategory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;
    public MyDataRestConfig(EntityManager theEntityManager){
        entityManager=theEntityManager;
    }


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        HttpMethod [] theUnsupportedActions={HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};
             //disable HTTP methods for product : PUT , POST and DELETE
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        //disable HTTP methods for ProductCategory : PUT , POST and DELETE
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
        //call an internal helper method
        exposeIds(config);

    }

    private void exposeIds(RepositoryRestConfiguration config) {

        //get all a list of call entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //create an array of the entityTypes
        List<Class> entityClasses = new ArrayList<>();

        //get the entity types fro the entities
        for (EntityType tempEntityType:entities){
            entityClasses.add(tempEntityType.getJavaType());
        }
        // expose the entity ids fro the array of entity / domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
