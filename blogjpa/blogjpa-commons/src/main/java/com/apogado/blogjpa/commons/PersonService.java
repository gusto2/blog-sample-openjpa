package com.apogado.blogjpa.commons;


/**
 * an example data access service, just show it works
 * @author Gabriel Vince
 */
public interface PersonService {
    public Person createPerson(Person p);
    
    public Person getPerson(Integer id);
    
}
