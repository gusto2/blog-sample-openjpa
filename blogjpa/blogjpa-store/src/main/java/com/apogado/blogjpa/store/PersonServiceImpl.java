package com.apogado.blogjpa.store;


import com.apogado.blogjpa.commons.Person;
import com.apogado.blogjpa.commons.PersonService;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Example data access service implementation
 * @author Gabriel Vince
 */
public class PersonServiceImpl implements PersonService {

    private static final Logger logger = Logger.getLogger(PersonServiceImpl.class.getName());
    
    private EntityManagerFactory entityManagerFactory;
    
    public Person createPerson(Person p) {
        EntityManager em = null;
        try
        {
            em = this.entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            em.persist(p);
            
            if(logger.isLoggable(Level.INFO))
                logger.log(Level.INFO, "Created Person id {0}",p.getId());
            
            em.flush();
            em.getTransaction().commit();
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "createPerson", ex);
            if(em!=null)
            {
                em.getTransaction().rollback();
                
            }
        }
        finally
        {
            if(em!=null)
                em.close();
        }
        return p;
    }

    public Person getPerson(Integer id) {
        EntityManager em = null;
        Person p = null;
        try
        {
            em = this.entityManagerFactory.createEntityManager();
            p = (Person) em
                    .createNamedQuery(Person.QUERY_FIND_BY_ID)
                    .setParameter("id", id)
                    .getSingleResult();
            logger.log(Level.INFO, "Person found with name: {0}",p.getName());
        }
        catch(javax.persistence.EntityNotFoundException nex)
        {
            logger.log(Level.INFO, "Person not found with id: {0}",id);
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "createPerson", ex);
        }
        finally
        {
            if(em!=null)
                em.close();
        }
        return p;    }
    
    /**
     * test method to test if it all works
     */
    public void test()
    {
        logger.info("running");
        if(this.entityManagerFactory!=null)
        {
            logger.info("entity manager factory available");
            Person p = new Person();
            p.setName(UUID.randomUUID().toString());
            Integer id = this.createPerson(p).getId();
            logger.log(Level.INFO, "Person persisted with id: {0}",id);
            p = this.getPerson(id);
            if(p!=null)
                logger.log(Level.INFO, "Person found with name: {0}",p.getName());
            else
                logger.log(Level.INFO, "Person not found with id: {0}",id);
        }
        else
        {
            logger.info("entityManagerFactory is NULL !");
        }
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    
    
}
