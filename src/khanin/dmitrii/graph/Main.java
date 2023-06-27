package khanin.dmitrii.graph;

import khanin.dmitrii.graph.UI.MainFrame;

public class Main {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        }).run();
    }
}