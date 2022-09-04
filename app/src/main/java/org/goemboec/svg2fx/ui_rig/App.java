package org.goemboec.svg2fx.ui_rig;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.goemboec.svg2fx.Document;
import org.goemboec.svg2fx.TreeBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        Group group = null;
        try {
            group = createTree(example1);
        }
        catch(Exception ex)
        {
            System.out.println("foo");
        }


        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(group, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    public static Group createTree(String svg) throws IOException
    {
        Reader initialReader = new StringReader("With Java");

        InputStream targetStream = new ByteArrayInputStream(svg.getBytes(StandardCharsets.UTF_8));

        var document = new Document(targetStream);
        var treeBuilder = new TreeBuilder();
        document.visit(treeBuilder);
        var group = treeBuilder.getRoot();

        initialReader.close();
        targetStream.close();

        return group;
    }

    private final static String example1 = """
            <?xml version="1.0" encoding="UTF-8" standalone="no"?>
            <svg xmlns="http://www.w3.org/2000/svg" width="320" height="320">
              <path d="M 10 315
                       L 110 215
                       A 36 60 0 0 1 150.71 170.29
                       L 172.55 152.45
                       A 30 50 -45 0 1 215.1 109.9
                       L 315 10" stroke="black" fill="green" stroke-width="2" fill-opacity="0.5"/>
              <circle cx="150.71" cy="170.29" r="2" fill="red"/>
              <circle cx="110" cy="215" r="2" fill="red"/>
              <ellipse cx="144.931" cy="229.512" rx="36" ry="60" fill="transparent" stroke="blue"/>
              <ellipse cx="115.779" cy="155.778" rx="36" ry="60" fill="transparent" stroke="blue"/>
            </svg>
            """;
}