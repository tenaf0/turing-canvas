package turing;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.util.Random;
import java.util.concurrent.*;

public class MainController {
    private static final int CANVAS_SCALE = 3;
    @FXML
    private Canvas canvas;

    @FXML
    private TextField alphabetNoField;
    @FXML
    private TextField stateNoField;

    private IntegerProperty alphabetNo = new SimpleIntegerProperty(4);
    private IntegerProperty stateNo = new SimpleIntegerProperty(4);

    public void initialize() {
        StringConverter<Number> numberConverter = new StringConverter<>() {
            @Override
            public String toString(Number object) {
                return object.toString();
            }

            @Override
            public Number fromString(String string) {
                return Integer.parseInt(string);
            }
        };
        alphabetNoField.textProperty().bindBidirectional(alphabetNo, numberConverter);
        stateNoField.textProperty().bindBidirectional(stateNo, numberConverter);
    }

    private final Random random = new Random();
    private Thread turingThread = new Thread();

    @FXML
    protected void onNewButtonClick() {
        turingThread.interrupt();

        Phaser phaser = new Phaser(2);

        turingThread = new Thread(() -> {
            Tape tape = new Tape((int) canvas.getWidth() / CANVAS_SCALE, (int) canvas.getHeight() / CANVAS_SCALE);
            Turing turing = new Turing(alphabetNo.get(), stateNo.get(), tape);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    phaser.arriveAndAwaitAdvance();
                } catch (IllegalStateException e) {
                    throw new RuntimeException(e);
                }

                int step = random.nextInt(20) + 500; // To break up unified "update waves"
                for (int i = 0; i < step; i++) {
                    turing.step();
                }

                Platform.runLater(() -> {
                    drawTape(tape);
                    try {
                        phaser.arrive();
                    } catch (IllegalStateException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
        turingThread.setDaemon(true);
        turingThread.start();

        phaser.arrive();
    }

    public void drawTape(Tape tape) {
        GraphicsContext g = canvas.getGraphicsContext2D();

        for (int y = 0; y < tape.height; y++) {
            for (int x = 0; x < tape.width; x++) {
                g.setFill(colorMap(tape.readAt(x, y)));
                g.fillRect(x * CANVAS_SCALE, y * CANVAS_SCALE, CANVAS_SCALE, CANVAS_SCALE);
            }
        }
    }

    private Color colorMap(byte tapeValue) {
        return switch (tapeValue) {
            case 0 -> Color.BLACK;
            case 1 -> Color.CADETBLUE;
            case 2 -> Color.BEIGE;
            case 3 -> Color.DARKSEAGREEN;
            case 4 -> Color.INDIANRED;
            default -> throw new IllegalStateException("Unexpected value: " + tapeValue);
        };
    }
}