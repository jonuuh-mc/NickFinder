package net.jonuuh.nickfinder.utils;

import java.io.*;

public class FileLogger {
    private PrintWriter writer = null;

    public FileLogger(String fileName, boolean doAppending)
    {
        String relFilePath = ".\\nickfinder\\" + fileName + ".log";

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(relFilePath, doAppending)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        writer.close();
    }

    public void addLog(String log)
    {
        writer.print(log);
    }

    public void addLogLn(String log)
    {
        writer.println(log);
    }
}
