package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class App {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Количество байт на прием: " + countOfBytesPerTransmission());
        System.out.println("Количество байт на передачу: " + countOfBytesPerAdmission());
        System.out.println("Количество циклов успешных обменов: " + countOfSuccessfulTrade());
        System.out.println("Время первого успешного обмена: " + theFirstSuccessfulTradeTime("ServerSocket1.txt"));
        System.out.println("Время последнего успешного обмена: " + theLastSuccessfulTradeTime("ServerSocket1.txt"));
        System.out.println("Время застоя в миллисекундах: "+theTimeOfStagnation("ServerSocket1.txt"));
    }


    public static List<String> readFileByFilter(String fileName, String filter) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        ArrayList<String> list = new ArrayList<>();
        while (reader.ready()) {
            String str = reader.readLine();
            if (str.contains(filter)) {
                list.add(str);
            }
        }
        reader.close();
        return list;
    }

    static int countOfSuccessfulTrade() throws IOException {
        List<String> stringList = new ArrayList<>(readFileByFilter("ServerSocket1.txt", "Принято"));
        int count = 0;
        for (int i = 0; i < stringList.size(); i++) {
            count++;
        }
        return count;
    }

    public static String theFirstSuccessfulTradeTime(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        ArrayList<String> list = new ArrayList<>();
        String string = reader.readLine();
        int i = 0;
        while (reader.ready()) {
            list.add(string);
            if (string.contains("Принято")) {
                String time = list.get(i - 2).substring(11);
                return time;
            }
            string = reader.readLine();
            i++;
        }
        reader.close();
        return null;
    }

    static int countOfBytesPerTransmission() throws IOException {
        int count = 0;
        List<String> stringList = new ArrayList<>(readFileByFilter("ServerSocket1.txt", "Socket->ReceiveBuf()"));
        for (int i = 0; i < stringList.size(); i++) {
            String str = stringList.get(i).substring(31, 34);
            count += Integer.parseInt(str.trim());
        }
        return count;
    }

    static int countOfBytesPerAdmission() throws IOException {
        List<String> stringList = new ArrayList<>(readFileByFilter("ServerSocket1.txt", "Передача"));
        int count = 0;
        for (int i = 0; i < stringList.size(); i++) {
            String str = stringList.get(i).substring(9, 12);
            count += Integer.parseInt(str.trim());

        }
        return count;
    }

    public static String theLastSuccessfulTradeTime(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        ArrayList<String> list = new ArrayList<>();
        String string = reader.readLine();
        String time = "";
        int i = 0;
        while (reader.ready()) {
            list.add(string);
            if (string.contains("Принято")) {
                time = list.get(i - 2).substring(11);
            }
            string = reader.readLine();
            i++;
        }
        reader.close();
        return time;
    }


    public static long theTimeOfStagnation(String fileName) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Long> list2 = new ArrayList<>();
        String string = reader.readLine();
        String time="";
        long time3=0;
        DateFormat format=new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        int i = 0;
        while (reader.ready()) {
            list.add(string);
            if (string.contains("ssWaitAnswer")) {
                time = list.get(i - 2);
                Date date=format.parse(time);
                list2.add(date.getTime());
            }
            if (string.contains("ssIdle")) {
                time = list.get(i - 2);
                Date date=format.parse(time);
                list2.add(date.getTime());
            }
            string = reader.readLine();
            i++;
        }
        list2.remove(0);
        for(int j=1; j<list2.size(); j+=2){
            long time2= list2.get(j)-list2.get(j-1);
            time3+=time2;
        }
        reader.close();
        return time3;
    }


}