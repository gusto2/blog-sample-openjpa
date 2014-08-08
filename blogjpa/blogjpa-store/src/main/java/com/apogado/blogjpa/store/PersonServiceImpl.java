package com.apogado.blogjpa.store;


import com.apogado.blogjpa.commons.Person;
import com.apogado.blogjpa.commons.PersonService;
import java.util.ArrayList;
import java.util.List;
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
        if(p.getName()==null || "".equals(p.getName()))
            throw new RuntimeException("Name is mandatory");
        EntityManager em = null;
        try
        {
            em = this.entityManagerFactory.createEntityManager();
            em.getTransaction().begin();

//            InputStream in = new ByteArrayInputStream("Example person blob".getBytes("UTF-8"));
//            p.setPersonData(in);
            em.persist(p);
            
            if(logger.isLoggable(Level.INFO))
                logger.log(Level.INFO, "Created Person id {0}",p.getId());
            
            em.flush();
            em.getTransaction().commit();
//            in.close();
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "createPerson", ex);
            if(em!=null)
            {
                if(em.getTransaction().isActive())
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
            em.getTransaction().begin();
            p =  em
                    .find(Person.class, id);
            // seee if there are lob data
//            InputStream in = p.getPersonData();
//            if(in==null)
//            {
//                logger.info("No person blob data");
//            }
//            else
//            {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
//                String personBlob = reader.readLine();
//                reader.close();
//                logger.log(Level.INFO, "Person blob: {0}",personBlob);
//            }
            em.getTransaction().commit();
            logger.log(Level.INFO, "Person found with name: {0}",p.getName());
        }
        catch(javax.persistence.EntityNotFoundException nex)
        {
            logger.log(Level.INFO, "Person not found with id: {0}",id);
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "getPerson", ex);
        }
        finally
        {
            if(em!=null)
            {
                if(em.getTransaction().isActive())
                    em.getTransaction().rollback();
                em.close();
            }
        }
        return p;    
    }
    
    @Override
    public List<Person> getPersonRecords() {
        EntityManager em = null;
        try
        {
            em = this.entityManagerFactory.createEntityManager();
            List l = em.createNamedQuery(Person.QUERY_FIND_ALL).getResultList();
            List<Person> resultList = new ArrayList<Person>(l.size());
            resultList.addAll(l);
            
            return resultList;
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        finally {
            if(em!=null)
                em.close();
            
        }
    }
    
    @Override
    public void deletePerson(Integer id) {
        EntityManager em = null;
        try
        {
            em = this.entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            Person p = em.find(Person.class, id);
            em.remove(p);
            em.getTransaction().commit();
            
            logger.log(Level.INFO, "Person deleted {0}",id);
        } catch(Exception ex) {
            logger.log(Level.SEVERE, "deletePerson");
            throw new RuntimeException(ex);
        }
        finally {
            if(em!=null)
                em.close();
        }
    }
    
        
    
    /**
     * test method to test if it all works
     */
    public void test()
    {
        try
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
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "test",ex);
        }
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    
    
}
