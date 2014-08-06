/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apogado.blogjpa.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * test DS connection at the deploy time
 * @author Gabriel
 */
public class DsTest {
    
    private static final Logger logger = Logger.getLogger(DsTest.class.getName());
    
    private DataSource datasource;
    private String testQuery;

    public DataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    public String getTestQuery() {
        return testQuery;
    }

    public void setTestQuery(String testQuery) {
        this.testQuery = testQuery;
    }
    
    
    
    public void test() throws SQLException {
        Connection c = null;
        try {
            c = this.datasource.getConnection();
            logger.info("testing DS, connection acquired");
            if(this.testQuery!=null) {
                Statement stmt = c.createStatement();
                stmt.executeQuery(this.testQuery);
                logger.info("testing DS, statement executed");
            }
        }
        catch(Exception ex)
        {
            logger.log(Level.SEVERE, "testing DS: ",ex);
            throw new RuntimeException(ex);
        }
        finally {
            if(c!=null)
                c.close();
        }
    }
}
