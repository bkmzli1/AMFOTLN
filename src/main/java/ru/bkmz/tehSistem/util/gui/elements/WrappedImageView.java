package ru.bkmz.tehSistem.util.gui.elements;

import javafx.application.Platform;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.net.URL;


public class WrappedImageView extends ImageView {
    public static final Logger logger = LogManager.getLogger();
    private Thread off;
    private Thread on;
    private int time = 50;

    public WrappedImageView(Image image, String url) {
        super(image);


        setOnMouseEntered(event -> {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            try {
                                on.join();
                                off.join();
                            } catch (Exception ignored) {
                            }
                            on = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (double i = 0d; i < 1d; i += 0.2d) {
                                        double finalI = i;
                                        try {
                                            Thread.sleep(time);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                setEffect(new Glow(finalI));
                                            }
                                        });


                                    }
                                }
                            });


                            on.start();
                        } catch (Exception ignored) {
                        }
                    }
                }).start();
            } catch (Exception ignored) {
            }
        });
        setOnMouseExited(event -> {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            try {
                                on.join();
                                off.join();
                            } catch (Exception ignored) {
                            }
                            off = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (double i = 1d; i > 0d; i -= 0.2d) {
                                        try {
                                            Thread.sleep(time);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        double finalI = i;

                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                setEffect(new Glow(finalI));
                                            }
                                        });

                                    }
                                }
                            });


                            off.start();
                        } catch (Exception ignored) {
                        }
                    }
                }).start();
            } catch (Exception ignored) {
            }
        });
        setOnMouseClicked(event -> {
            openWebpage(url);
        });
        setPreserveRatio(true);

    }


    @Override
    public double minWidth(double height) {
        return 40;
    }

    @Override
    public double prefWidth(double height) {
        Image I = getImage();
        if (I == null) return minWidth(height);
        return I.getWidth();
    }

    @Override
    public double maxWidth(double height) {
        return 16384;
    }

    @Override
    public double minHeight(double width) {
        return 40;
    }

    @Override
    public double prefHeight(double width) {
        Image I = getImage();
        if (I == null) return minHeight(width);
        return I.getHeight();
    }

    @Override
    public double maxHeight(double width) {
        return 16384;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {

        setFitWidth(width);
        setFitHeight(height);
    }

    private static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}