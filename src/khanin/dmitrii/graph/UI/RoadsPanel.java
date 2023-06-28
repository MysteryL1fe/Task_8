package khanin.dmitrii.graph.UI;

import khanin.dmitrii.graph.logic.Road;
import khanin.dmitrii.graph.logic.Stop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RoadsPanel extends JPanel {
    private final ArrayList<Road> roads;
    private final MainFrame mainFrame;

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
            this.add(new RoadBtn(road));
        }

        this.revalidate();
        this.repaint();
    }

    private class RoadBtn extends JButton {
        private final Road road;

        public RoadBtn(Road road) {
            this.road = road;

            this.setText(String.format(
                    "%s - %s", road.getFirstStop().getName(), road.getSecondStop().getName()
            ));
            this.addActionListener(new RoadBtnActionListener());
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
            lengthSpinner.setValue(road == null ? 1 : road.getLength());
            this.add(lengthSpinner);

            JButton okBtn = new JButton("Готово");
            okBtn.addActionListener(new OkBtnActionListener());
            this.add(okBtn);

            this.revalidate();
            this.repaint();
        }

        private class ChooseStopBtnActionListener implements ActionListener {
            private final boolean isFirstStop;

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
                    road = new Road(firstStop, secondStop, Math.max(1, (Integer) lengthSpinner.getValue()));
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
            private final boolean isFirstStop;

            public ChooseStopPanel(boolean isFirstStop) {
                this.isFirstStop = isFirstStop;

                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                for (Stop stop : mainFrame.getStopsList()) {
                    if (isFirstStop && stop != secondStop || !isFirstStop && stop != firstStop) {
                        this.add(new StopBtn(stop));
                    }
                }
            }

            private class StopBtn extends JButton {
                private final Stop stop;

                public StopBtn(Stop stop) {
                    this.stop = stop;

                    this.setText(stop.getName());
                    this.addActionListener(new StopBtnActionListener());
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
