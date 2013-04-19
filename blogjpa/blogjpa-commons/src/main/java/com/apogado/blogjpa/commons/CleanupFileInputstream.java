/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apogado.blogjpa.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * file input stream - when the stream is over, delete the file
 *
 * @author Gabriel Vince
 */
public class CleanupFileInputstream extends InputStream {

    private File file;
    private InputStream originalInputStream;

    public CleanupFileInputstream(File file) throws FileNotFoundException {
        this.file = file;
        this.originalInputStream = new FileInputStream(file);
    }

    @Override
    public int read() throws IOException {
        int read = -1;
        if(this.originalInputStream!=null)
        {
            read = this.originalInputStream.read();
            if(read<0)
                close();
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int read = -1;
        if(this.originalInputStream!=null)
        {
            read = this.originalInputStream.read(b);
            if(read<0)
                close();
        }
        return read;    
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = -1;
        if(this.originalInputStream!=null)
        {
            read = this.originalInputStream.read(b,off,len);
            if(read<0)
                close();
        }
        return read;    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (this.originalInputStream != null) {
                this.originalInputStream.close();
                this.originalInputStream = null;
            }
        }
        if(this.file!=null)
        {
            if(this.file.exists())
                this.file.delete();
        }
    }
}
