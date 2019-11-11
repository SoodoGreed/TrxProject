package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.scene.chart.BarChart;

import javax.swing.*;


public class Main extends Application {

    @Override public void start(Stage stage) throws SQLException, ClassNotFoundException, ParseException {

        trx Trx = new trx();
        Trx.inputInSqlite();

        LineChart<String, Number> chart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        final XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("y/M/d HH:mm:ss");

        HashMap<String, Integer> pointsMap = Trx.outputFromSqlite();
        for (HashMap.Entry point : pointsMap.entrySet()) {
                Date pointDate = dateFormat.parse(point.getKey().toString());
                series1.getData().add(new XYChart.Data(pointDate.toString(), point.getValue()));
            Collections.sort(series1.getData(),new Comparator<XYChart.Data>() {
                @Override
                public int compare(XYChart.Data o1, XYChart.Data o2) {
                    SimpleDateFormat format=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
                    try {
                        Date xValue1 = format.parse(o1.getXValue().toString());
                        Date xValue2 = format.parse(o2.getXValue().toString());

                        return xValue1.compareTo(xValue2);

                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });

        }

        chart.getData().addAll(series1);
     Scene scene  = new Scene(chart,800,600);

     stage.setScene(scene);
       stage.show();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        launch(args);
    }
}