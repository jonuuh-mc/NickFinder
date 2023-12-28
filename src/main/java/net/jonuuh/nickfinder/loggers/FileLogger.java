package net.jonuuh.nickfinder.loggers;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A PrintWriter wrapper.
 */
public class FileLogger
{
    private PrintWriter writer = null;

    /**
     * Instantiates a new FileLogger.
     *
     * @param fileName    the file name
     * @param doAppending whether to append to the file or create a new one
     */
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

    /**
     * Close the FileLogger.
     */
    public void close()
    {
        this.writer.close();
    }

    /**
     * Add a log to the FileLogger.
     *
     * @param log the log
     */
    public void addLog(String log)
    {
        this.writer.print(log);
    }

    /**
     * Add a log + newline to the FileLogger.
     *
     * @param log the log
     */
    public void addLogLn(String log)
    {
        this.writer.println(log);
    }
}
