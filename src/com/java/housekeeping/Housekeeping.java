package com.java.housekeeping;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;

public class Housekeeping {
    public static void main(String args[]) throws IOException, ParseException {

        String properties_filepath = "config.properties";
        Properties files_properties;
        files_properties = new Properties();
        FileInputStream config_properties =new FileInputStream(properties_filepath);
        files_properties.load(config_properties);

        String[] path_files = files_properties.getProperty("properties.Path").split("\\|");
        String[] type_files = files_properties.getProperty("properties.FileType").split("\\|");
        String age_files = files_properties.getProperty("properties.Duration");
        String path_log = files_properties.getProperty("properties.LogPath");
        String type_log = files_properties.getProperty("properties.LogType");

        String format_date = "yyyyMMdd";
        String format_time = "HHmmss";
        SimpleDateFormat time_formatter= new SimpleDateFormat(format_time);
        SimpleDateFormat formatter= new SimpleDateFormat(format_date);
        Date date = new Date(System.currentTimeMillis());

        FileWriter logWriter = new FileWriter(path_log+"log_"+formatter.format(date)+"_"+time_formatter.format(date)+"."+type_log);

        try {

            for (String path_properties : path_files){

                File directoryPath = new File(path_properties);

                System.out.println("       =====================================================================================================================================");
                System.out.println("\n       # Directory : "+directoryPath.getCanonicalPath()+"\n");

                logWriter.write("       =====================================================================================================================================\n");
                logWriter.write("\n       # Directory : "+directoryPath.getCanonicalPath()+"\n\n");

                if (directoryPath.isDirectory()){
                    File[] files = directoryPath.listFiles();

                    int numFiles = files.length;
                    int successFiles = 0;
                    int failedFiles = 0;
                    int Subfiles = 0;

                    System.out.print("       Summary          ");
                    System.out.format("%-86s%-16s%-16s", "File location and filename", " Size", "Time(HHmmss)");
                    System.out.println("\n");

                    logWriter.write("       Summary          ");
                    logWriter.write(String.format("%-86s%-16s%-16s", "File location and filename", " Size", "Time(HHmmss)"));
                    logWriter.write("\n\n");

                    for (File file : files){
                        final DateTimeFormatter formatters = DateTimeFormatter.ofPattern(format_date);
                        final String firstInput = formatter.format(new Date(file.lastModified()));
                        final String secondInput = formatter.format((date));
                        final LocalDate firstDate = LocalDate.parse(firstInput, formatters);
                        final LocalDate secondDate = LocalDate.parse(secondInput, formatters);
                        final long days = ChronoUnit.DAYS.between(firstDate, secondDate);

                        if (file.isFile()){
                            for (String typefiles_properties : type_files){
                                if (typefiles_properties.equals("*")){
                                    if (days >= Integer.parseInt(age_files)){
                                        System.out.print("       Delete Files   : ");
                                        System.out.format("%-86s%-16s%-16s", file.getName(), " "+file.length()/1024 + " kb", time_formatter.format(date));
                                        System.out.println(" ");

                                        logWriter.write("       Delete Files   : ");
                                        logWriter.write(String.format("%-86s%-16s%-16s", file.getName(), " "+file.length()/1024 + " kb", time_formatter.format(date)));
                                        logWriter.write("\n");

                                        successFiles++;

                                        if(!file.delete()) throw new IOException("Not able to delete file: " + file.getAbsolutePath());
                                    }
                                    else {
                                        System.out.print("       Keep Files     : ");
                                        System.out.format("%-86s%-16s%-16s", file.getName(), " "+file.length()/1024 + " kb", time_formatter.format(date));
                                        System.out.println(" ");

                                        logWriter.write("       Keep Files     : ");
                                        logWriter.write(String.format("%-86s%-16s%-16s", file.getName(), " "+file.length()/1024 + " kb", time_formatter.format(date)));
                                        logWriter.write("\n");

                                        failedFiles++;
                                    }
                                }
                                else {
                                    if (file.getPath().endsWith("."+typefiles_properties)){
                                        if (days >= Integer.parseInt(age_files)){
                                            System.out.print("       Delete Files   : ");
                                            System.out.format("%-86s%-16s%-16s", file.getName(), " "+file.length()/1024 + " kb", time_formatter.format(date));
                                            System.out.println(" ");

                                            logWriter.write("       Delete Files   : ");
                                            logWriter.write(String.format("%-86s%-16s%-16s", file.getName(), " "+file.length()/1024 + " kb", time_formatter.format(date)));
                                            logWriter.write("\n");

                                            successFiles++;

                                            if(!file.delete()) throw new IOException("Not able to delete file: " + file.getAbsolutePath());
                                        }
                                        else {
                                            System.out.print("       Keep Files     : ");
                                            System.out.format("%-86s%-16s%-16s", file.getName(), " "+file.length()/1024 + " kb", time_formatter.format(date));
                                            System.out.println(" ");

                                            logWriter.write("       Keep Files     : ");
                                            logWriter.write(String.format("%-86s%-16s%-16s", file.getName(), " "+file.length()/1024 + " kb", time_formatter.format(date)));
                                            logWriter.write("\n");

                                            failedFiles++;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            Subfiles++;
                        }
                    }

                    int deleteFailed = 0;
                    int successdelete = 0;

                    if (successFiles  >= 0){
                        successdelete = successFiles;
                    }

                    if(failedFiles >= 0){
                        deleteFailed = failedFiles;
                    }
                    int items_total = Math.abs(successdelete)+Math.abs(deleteFailed);

                    System.out.println("\n       _____________________________________________________________________________________________________________________________________\n");
                    System.out.print("       Delete Files  : "+Math.abs(successdelete));
                    System.out.print("       Keep Files   : "+Math.abs(deleteFailed));
                    System.out.println("       Total Items    : "+items_total);
                    System.out.println("\n");

                    logWriter.write("\n       _____________________________________________________________________________________________________________________________________\n\n");
                    logWriter.write("       Delete Files  : "+Math.abs(successdelete));
                    logWriter.write("       Keep Files   : "+Math.abs(deleteFailed));
                    logWriter.write("       Total Items    : "+items_total+"\n");
                    logWriter.write("\n");
                }
                else {
                    System.out.println("       -------------------------------------------       ");
                    System.out.println("       !      Sorry, directory is not found      !       ");
                    System.out.println("       -------------------------------------------       ");

                    logWriter.write("       -------------------------------------------       \n");
                    logWriter.write("       !      Sorry, directory is not found      !       \n");
                    logWriter.write("       -------------------------------------------       \n");
                }
            }
            logWriter.close();
        }
        catch (IOException e) {
            System.out.println("Sorry, an error occurred");
            e.printStackTrace();
        }
    }
}
