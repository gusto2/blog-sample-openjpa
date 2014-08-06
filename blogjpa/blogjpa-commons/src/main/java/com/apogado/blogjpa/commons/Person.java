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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.openjpa.persistence.Persistent;

/**
 * example entity object
 * we will skip out blob part for now because of JSON serialization
 * @author Gabriel Vince
 */
@Entity(name = "Person")
@NamedQueries( {
    @NamedQuery(name = "Person.findById",query = "SELECT p FROM Person p WHERE p.id = :id"),
    @NamedQuery(name = "Person.listAll",query = "SELECT p FROM Person p")}
        )
@XmlRootElement(name = "person")
public class Person implements Serializable {
    
    public static final String QUERY_FIND_BY_ID = "Person.findById";
    public static final String QUERY_FIND_ALL = "Person.listAll";
    
    private Integer id;
    private String name;
    private String address;
    private InputStream personData;

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

    @Persistent(fetch = FetchType.LAZY)
    @XmlTransient
    public InputStream getPersonData() {
        return personData;
    }

    public void setPersonData(InputStream personData) {
        this.personData = personData;
    }

    
   
}
