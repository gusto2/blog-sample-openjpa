package com.apogado.blogjpa.commons;

import java.io.InputStream;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;

/**
 * example entity object
 * @author Gabriel Vince
 */
@Entity(name = "Person")
@NamedQuery(name = "Person.findById",query = "SELECT p FROM Person p WHERE p.id = :id")
public class Person implements Serializable {
    
    public static final String QUERY_FIND_BY_ID = "Person.findById";
    
    private Integer id;
    private String name;
    private String address;
    private FileBlob personData;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "personname")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Lob @Basic(fetch = FetchType.LAZY)
    public FileBlob getPersonData() {
        return personData;
    }

    public void setPersonData(FileBlob personData) {
        this.personData = personData;
    }
   
}
