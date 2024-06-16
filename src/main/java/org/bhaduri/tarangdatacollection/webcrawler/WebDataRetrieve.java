/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bhaduri.tarangdatacollection.webcrawler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author bhaduri
 */
public class WebDataRetrieve implements Job{

    static Logger logger = Logger.getLogger(WebDataRetrieve.class.getName());
    private static String FILE_PATH = "/home/bhaduri/Downloads/";
    
    public void gheu() {
        System.out.println("Ore Gheu Gheu Gheu");
    }

    public void webDownload(String[] args) {
        if (args.length != 0) {
            String dirPath = args[0];
            FILE_PATH = dirPath;
        }
        logger.setLevel(Level.FINE);
        logger.addHandler(new ConsoleHandler());

        try {
            Handler fileHandler = new FileHandler(FILE_PATH + "logger.log", 2000, 5);
            logger.addHandler(fileHandler);
        } catch (SecurityException | IOException e) {
            System.out.println("Cannot open log file");
        }

        //String url = "https://economictimes.indiatimes.com/indices/nifty_50_companies";
        String url = "https://www.moneycontrol.com/markets/indian-indices/";
        //String url = "https://in.investing.com/equities/adani-enterprises";
        //String url = "https://www.nseindia.com/market-data/live-equity-market";
        String downFileName = createFileName();
        
        DownloadWebPage(url, downFileName);
    }

    private  String createFileName() {
        String dirPath = FILE_PATH;
        String prefix = "nifty50_";
        Date currDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String timeCreate = formatter.format(currDate);
        String suffix = "_.txt";
        String totalFileName = dirPath + prefix + timeCreate + suffix;
        return totalFileName;
    }

    public  void DownloadWebPage(String webpage, String downFileName) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000)
                .setConnectionRequestTimeout(1000)
                .setSocketTimeout(1000)
                .build();
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        //CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(webpage);
            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try {
                    InputStream instream = entity.getContent();
                    OutputStream outstream = new FileOutputStream(downFileName);

                    byte[] buffer = new byte[4096];
                    int len;

                    while ((len = instream.read(buffer)) != -1) {
                        outstream.write(buffer, 0, len);
                    }

                    System.out.println("File downloaded successfully");
                    EntityUtils.consume(entity);
                } catch (IOException | UnsupportedOperationException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                } finally {
                    response.close();
                }
            }
        } catch (IOException ie) {
            logger.log(Level.SEVERE, ie.toString());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                Logger.getLogger(WebDataRetrieve.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        gheu();
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
