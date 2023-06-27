package khanin.dmitrii.graph.UI;

import khanin.dmitrii.graph.logic.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel graphPanel;
    private JButton shortestPathBtn;
    private JButton cheapestPathBtn;
    private JButton stopsBtn;
    private JButton roadsBtn;
    private JButton routesBtn;
    private JButton transportsBtn;
    private JPanel firstChangesPanel;
    private JPanel secondChangesPanel;
    private JPanel thirdChangesPanel;
    private JPanel fourthChangesPanel;
    private JButton createGraphBtn;
    private JPanel fifthChangesPanel;
    private ArrayList<Stop> stopsList = new ArrayList<>();
    private ArrayList<Road> roadsList = new ArrayList<>();
    private ArrayList<Route> routesList = new ArrayList<>();
    private ArrayList<Transport> transportsList = new ArrayList<>();

    public MainFrame() {
        this.setTitle("Graph 2.0");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(1500, 500);

        firstChangesPanel.setLayout(new BoxLayout(firstChangesPanel, BoxLayout.Y_AXIS));
        secondChangesPanel.setLayout(new BoxLayout(secondChangesPanel, BoxLayout.Y_AXIS));
        thirdChangesPanel.setLayout(new BoxLayout(thirdChangesPanel, BoxLayout.Y_AXIS));
        fourthChangesPanel.setLayout(new BoxLayout(fourthChangesPanel, BoxLayout.Y_AXIS));
        fifthChangesPanel.setLayout(new BoxLayout(fifthChangesPanel, BoxLayout.Y_AXIS));


        stopsBtn.addActionListener(new StopsBtnActionListener());
        roadsBtn.addActionListener(new RoadsBtnActionListener());
        routesBtn.addActionListener(new RoutesBtnActionListener());
        transportsBtn.addActionListener(new TransportsBtnActionListener());
    }

    public void changeSecondPanel(JPanel panel) {
        secondChangesPanel.removeAll();
        thirdChangesPanel.removeAll();
        fourthChangesPanel.removeAll();
        fifthChangesPanel.removeAll();
        if (panel != null) secondChangesPanel.add(panel);
        revalidate();
        repaint();
    }

    public void changeThirdPanel(JPanel panel) {
        thirdChangesPanel.removeAll();
        fourthChangesPanel.removeAll();
        fifthChangesPanel.removeAll();
        if (panel != null) thirdChangesPanel.add(panel);
        revalidate();
        repaint();
    }

    public void changeFourthPanel(JPanel panel) {
        fourthChangesPanel.removeAll();
        fifthChangesPanel.removeAll();
        if (panel != null) fourthChangesPanel.add(panel);
        revalidate();
        repaint();
    }

    public void changeFifthPanel(JPanel panel) {
        fifthChangesPanel.removeAll();
        if (panel != null) fifthChangesPanel.add(panel);
        revalidate();
        repaint();
    }

    public ArrayList<Stop> getStopsList() {
        return (ArrayList<Stop>) stopsList.clone();
    }

    public ArrayList<Road> getRoadsList() {
        return (ArrayList<Road>) roadsList.clone();
    }

    public ArrayList<Route> getRoutesList() {
        return (ArrayList<Route>) routesList.clone();
    }

    public ArrayList<Transport> getTransportsList() {
        return (ArrayList<Transport>) transportsList.clone();
    }

    private class StopsBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            firstChangesPanel.removeAll();
            firstChangesPanel.add(new StopsPanel(stopsList, MainFrame.this));
            secondChangesPanel.removeAll();
            thirdChangesPanel.removeAll();
            fourthChangesPanel.removeAll();
            fifthChangesPanel.removeAll();
            MainFrame.this.revalidate();
            MainFrame.this.repaint();
        }
    }

    private class RoadsBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            firstChangesPanel.removeAll();
            firstChangesPanel.add(new RoadsPanel(roadsList, MainFrame.this));
            secondChangesPanel.removeAll();
            thirdChangesPanel.removeAll();
            fourthChangesPanel.removeAll();
            fifthChangesPanel.removeAll();
            MainFrame.this.revalidate();
            MainFrame.this.repaint();
        }
    }

    private class RoutesBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            firstChangesPanel.removeAll();
            firstChangesPanel.add(new RoutesPanel(routesList, MainFrame.this));
            secondChangesPanel.removeAll();
            thirdChangesPanel.removeAll();
            fourthChangesPanel.removeAll();
            fifthChangesPanel.removeAll();
            MainFrame.this.revalidate();
            MainFrame.this.repaint();
        }
    }

    private class TransportsBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            firstChangesPanel.removeAll();
            firstChangesPanel.add(new TransportsPanel(transportsList, MainFrame.this));
            secondChangesPanel.removeAll();
            thirdChangesPanel.removeAll();
            fourthChangesPanel.removeAll();
            fifthChangesPanel.removeAll();
            MainFrame.this.revalidate();
            MainFrame.this.repaint();
        }
    }
}
