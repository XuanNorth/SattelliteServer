package com.example.sattelliteserrver.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MockDataTask {

    public static int randomInteger(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    // Random số thực trong khoảng [min, max]
    public static double randomDouble(double min, double max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }
    static  List<Coordinate> coordinates = List.of(new Coordinate(105.853873,21.028502),new Coordinate(105.853742,21.028521),new Coordinate(105.85379,21.028799),new Coordinate(105.855438,21.02895),new Coordinate(105.856533,21.029053),new Coordinate(105.856618,21.029338),new Coordinate(105.856681,21.029664),new Coordinate(105.856699,21.029776),new Coordinate(105.857199,21.029878),new Coordinate(105.85725,21.029767),new Coordinate(105.857405,21.029444),new Coordinate(105.857505,21.029421),new Coordinate(105.857623,21.029202),new Coordinate(105.857682,21.029171),new Coordinate(105.857793,21.029191),new Coordinate(105.8579,21.029211),new Coordinate(105.857993,21.029204),new Coordinate(105.858055,21.029223),new Coordinate(105.85832,21.029328),new Coordinate(105.858719,21.0295),new Coordinate(105.85923,21.029703),new Coordinate(105.859814,21.029961),new Coordinate(105.860574,21.028866),new Coordinate(105.860819,21.028443),new Coordinate(105.861006,21.028063),new Coordinate(105.861124,21.027606),new Coordinate(105.861146,21.027519),new Coordinate(105.861176,21.027404),new Coordinate(105.861485,21.02651),new Coordinate(105.861545,21.026309),new Coordinate(105.861773,21.025698),new Coordinate(105.862038,21.024825),new Coordinate(105.862102,21.024607),new Coordinate(105.862237,21.024095),new Coordinate(105.862264,21.024036),new Coordinate(105.862344,21.023631),new Coordinate(105.862409,21.023309),new Coordinate(105.862702,21.022123),new Coordinate(105.862725,21.022022),new Coordinate(105.862738,21.021965),new Coordinate(105.862832,21.021649),new Coordinate(105.862872,21.021421),new Coordinate(105.862958,21.02111),new Coordinate(105.863037,21.020828),new Coordinate(105.863125,21.020474),new Coordinate(105.863256,21.020076),new Coordinate(105.863554,21.019422),new Coordinate(105.863899,21.018973),new Coordinate(105.863972,21.018947),new Coordinate(105.864077,21.018784),new Coordinate(105.864285,21.018349),new Coordinate(105.864449,21.01794),new Coordinate(105.864535,21.017729),new Coordinate(105.864664,21.017277),new Coordinate(105.86476,21.016954),new Coordinate(105.86482,21.01674),new Coordinate(105.864899,21.016422),new Coordinate(105.86472,21.016363),new Coordinate(105.864615,21.016671),new Coordinate(105.864161,21.016561),new Coordinate(105.864171,21.016496));

    public static void main(String[] args) {

        List<Number> data = new ArrayList<>();
        data.add(coordinates.get(0).latitude);
        data.add(coordinates.get(0).longitude);
        data.add(randomInteger(20, 30));
        data.add(randomInteger(20, 30));
        data.add(randomInteger(20, 30));
        data.add(randomDouble(0,1));
        data.add(randomDouble(0,1));
        data.add(randomDouble(0,1));


        try( Socket clientSocket = new Socket(InetAddress.getByName("localhost"), 8081)) {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            for (int j = 1; j < 80; j++) {
                data.set(0,coordinates.get(j).latitude);
                data.set(1,coordinates.get(j).longitude);
                for (int i  = 2; i < data.size(); i++) {
                    Number current = data.get(i);
                    if (current instanceof Integer) {
                        int newValue = current.intValue() + randomInteger(-1, 1);
                        data.set(i, newValue);
                    } else if (current instanceof Double) {
                        double newValue = current.doubleValue() + randomDouble(-0.1, 0.1);
                        data.set(i, newValue);
                    }
                }
                String modifiedString = data.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("-"));
                out.println(modifiedString);
                System.out.println("message = " + modifiedString);
                Thread.sleep(2000);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}

