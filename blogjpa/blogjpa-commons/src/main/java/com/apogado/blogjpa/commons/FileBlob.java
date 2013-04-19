/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apogado.blogjpa.commons;

import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * storing stream data into a temporary file
 * @author Gabriel Vince
 */
public class FileBlob implements Externalizable{
    private static final Logger logger = Logger.getLogger(FileBlob.class.getName());
    
    private static final int BUFF_SIZE = 32000;
    
    private File tempFile;
    private InputStream inputStream;

    public InputStream getInputStream() throws FileNotFoundException {
        // we will read data, store the content into tem file and return 
        // a cleanup input stream
        synchronized(this)
        {
            if(inputStream==null)
            {
                inputStream = new CleanupFileInputstream(tempFile);
            }
        }
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
//         we will store the data
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        byte[] buff = new byte[BUFF_SIZE];
        for (
                int read = inputStream.read(buff);
                read >= 0;
                read = inputStream.read(buff))
        {
            out.write(buff,0, read);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       // store the data into a temp file
        synchronized(this)
        {
            this.tempFile = File.createTempFile("openjpa", "blob");
            byte[] buff = new byte[BUFF_SIZE];
            OutputStream out = new FileOutputStream(tempFile);
            for(
                    int read = in.read(buff);
                    read >= 0;
                    read = in.read(buff)
                )
            {
                out.write(buff, 0, read);
            }
            out.flush();
            out.close();
            tempFile.deleteOnExit();
        }
    }
    
    
   
}
