package khanin.dmitrii.graph.UI;

import khanin.dmitrii.graph.logic.Route;

import javax.swing.*;
import java.util.ArrayList;

public class RoutesPanel extends JPanel {
    private ArrayList<Route> routes;
    private MainFrame mainFrame;

    public RoutesPanel(ArrayList<Route> routes, MainFrame mainFrame) {
        this.routes = routes;
        this.mainFrame = mainFrame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton addRouteBtn = new JButton("Добавить маршрут");
        addRouteBtn.addActionListener(null);
        this.add(addRouteBtn);
    }
}
