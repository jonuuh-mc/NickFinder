package net.jonuuh.nickfinder.loggers;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger
{
    private PrintWriter writer = null;

    public FileLogger(String fileName, boolean doAppending)
    {
        String relFilePath = ".\\nickfinder\\" + fileName + ".log";

        try
        {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(relFilePath, doAppending)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        this.writer.close();
    }

    public void write(String log)
    {
        this.writer.println(log);
    }
}
