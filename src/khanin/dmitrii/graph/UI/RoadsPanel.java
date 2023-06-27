package khanin.dmitrii.graph.UI;

import khanin.dmitrii.graph.logic.Road;
import khanin.dmitrii.graph.logic.Stop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RoadsPanel extends JPanel {
    private ArrayList<Road> roads;
    private MainFrame mainFrame;

    public RoadsPanel(ArrayList<Road> roads, MainFrame mainFrame) {
        this.roads = roads;
        this.mainFrame = mainFrame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        updateRoads();
    }

    private void updateRoads() {
        this.removeAll();

        JButton addRoadBtn = new JButton("Добавить дорогу");
        addRoadBtn.addActionListener(new AddRoadBtnActionListener());
        this.add(addRoadBtn);

        for (Road road : roads) {
            this.add(new RoadPanel(road));
        }

        this.revalidate();
        this.repaint();
    }

    private class RoadPanel extends JPanel {
        private Road road;

        public RoadPanel(Road road) {
            this.road = road;

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JButton roadBtn = new JButton(String.format(
                    "%s - %s", road.getFirstStop().getName(), road.getSecondStop().getName()
            ));
            roadBtn.addActionListener(new RoadBtnActionListener());
            this.add(roadBtn);
        }

        private class RoadBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changeSecondPanel(new ChangeRoadPanel(road));
            }
        }
    }

    private class ChangeRoadPanel extends JPanel {
        private Road road;
        private Stop firstStop;
        private Stop secondStop;
        private JSpinner lengthSpinner;

        public ChangeRoadPanel(Road road) {
            if (road != null) {
                this.road = road;
                firstStop = road.getFirstStop();
                secondStop = road.getSecondStop();
            }

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            updateChangeRoadPanel();
        }

        private void updateChangeRoadPanel() {
            this.removeAll();

            this.add(new JLabel("Первая остановка"));

            JButton firstStopBtn = new JButton(firstStop == null ? "" : firstStop.getName());
            firstStopBtn.addActionListener(new ChooseStopBtnActionListener(true));
            this.add(firstStopBtn);

            this.add(new JLabel("Вторая остановка"));

            JButton secondStopBtn = new JButton(secondStop == null ? "" : secondStop.getName());
            secondStopBtn.addActionListener(new ChooseStopBtnActionListener(false));
            this.add(secondStopBtn);

            this.add(new JLabel("Длина дороги"));

            lengthSpinner = new JSpinner();
            lengthSpinner.setValue(road == null ? 0 : road.getLength());
            this.add(lengthSpinner);

            JButton okBtn = new JButton("Готово");
            okBtn.addActionListener(new OkBtnActionListener());
            this.add(okBtn);

            this.revalidate();
            this.repaint();
        }

        private class ChooseStopBtnActionListener implements ActionListener {
            private boolean isFirstStop;

            public ChooseStopBtnActionListener(boolean isFirstStop) {
                this.isFirstStop = isFirstStop;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changeThirdPanel(new ChooseStopPanel(isFirstStop));
            }
        }

        private class OkBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (road == null) {
                    road = new Road(firstStop, secondStop, Math.max(0, (Integer) lengthSpinner.getValue()));
                    roads.add(road);
                } else {
                    road.setFirstStop(firstStop);
                    road.setSecondStop(secondStop);
                    road.setLength((Integer) lengthSpinner.getValue());
                }
                mainFrame.changeSecondPanel(null);
                updateRoads();
            }
        }

        private class ChooseStopPanel extends JPanel {
            private boolean isFirstStop;

            public ChooseStopPanel(boolean isFirstStop) {
                this.isFirstStop = isFirstStop;

                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                for (Stop stop : mainFrame.getStopsList()) {
                    this.add(new StopPanel(stop));
                }
            }

            private class StopPanel extends JPanel {
                private Stop stop;

                public StopPanel(Stop stop) {
                    this.stop = stop;

                    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                    JButton stopBtn = new JButton(stop.getName());
                    stopBtn.addActionListener(new StopBtnActionListener());
                    this.add(stopBtn);
                }

                private class StopBtnActionListener implements ActionListener {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isFirstStop) firstStop = stop;
                        else secondStop = stop;
                        updateChangeRoadPanel();
                        mainFrame.changeThirdPanel(null);
                    }
                }
            }
        }
    }

    private class AddRoadBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainFrame.changeSecondPanel(new ChangeRoadPanel(null));
        }
    }
}
