package khanin.dmitrii.graph.UI;

import khanin.dmitrii.graph.logic.Road;
import khanin.dmitrii.graph.logic.Route;
import khanin.dmitrii.graph.logic.Stop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class RoutesPanel extends JPanel {
    private final ArrayList<Route> routes;
    private final MainFrame mainFrame;

    public RoutesPanel(ArrayList<Route> routes, MainFrame mainFrame) {
        this.routes = routes;
        this.mainFrame = mainFrame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        updateRoutes();
    }

    private void updateRoutes() {
        this.removeAll();

        JButton addRouteBtn = new JButton("Добавить маршрут");
        addRouteBtn.addActionListener(new AddRouteBtnActionListener());
        this.add(addRouteBtn);

        for (Route route : routes) {
            this.add(new RouteBtn(route));
        }

        this.revalidate();
        this.repaint();
    }

    private class AddRouteBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainFrame.changeSecondPanel(new ChangeRoutePanel(null));
        }
    }

    private class RouteBtn extends JButton {
        private Route route;

        public RouteBtn(Route route) {
            this.route = route;

            this.setText(route.getNum());
            this.addActionListener(new RouteBtnActionListener());
        }

        private class RouteBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changeSecondPanel(new ChangeRoutePanel(route));
            }
        }
    }

    private class ChangeRoutePanel extends JPanel {
        private final Route route;
        private final ArrayList<Road> roads;
        private final ArrayList<Integer> delayTimes;
        private final JTextField routeNumTextField;

        public ChangeRoutePanel(Route route) {
            this.route = route;
            roads = route == null ? new ArrayList<>() : route.getRoadsList();
            delayTimes = route == null ? new ArrayList<>() : route.getDelayTimesList();

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JButton changeRoadsBtn = new JButton("Изменить путь");
            changeRoadsBtn.addActionListener(new ChangeRoadsBtnActionListener());
            this.add(changeRoadsBtn);

            JButton changeDelayTimesBtn = new JButton("Изменить время стоянок");
            changeDelayTimesBtn.addActionListener(new ChangeDelayTimesBtnActionListener());
            this.add(changeDelayTimesBtn);

            this.add(new JLabel("Номер маршрута"));

            routeNumTextField = new JTextField(route == null ? "" : route.getNum());
            this.add(routeNumTextField);

            JButton okBtn = new JButton("Готово");
            okBtn.addActionListener(new OkBtnActionListener());
            this.add(okBtn);
        }

        private class ChangeRoadsBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changeThirdPanel(new ChangeRoadsPanel());
            }
        }

        private class ChangeDelayTimesBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changeThirdPanel(new ChangeDelayTimesPanel());
            }
        }

        private class OkBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (route == null) {
                    routes.add(new Route(roads, delayTimes, routeNumTextField.getText()));
                } else {
                    route.setRoadsList(roads);
                    route.setDelayTimesList(delayTimes);
                    route.setNum(routeNumTextField.getText());
                }
                mainFrame.changeSecondPanel(null);
                updateRoutes();
            }
        }

        private class ChangeRoadsPanel extends JPanel {
            public ChangeRoadsPanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                updateRoads();
            }

            private void updateRoads() {
                this.removeAll();

                for (Road road : roads) {
                    this.add(new RoadBtn(road));
                }

                JButton addNewRoad = new JButton("Добавить путь");
                addNewRoad.addActionListener(new AddNewRoadBtnActionListener());
                this.add(addNewRoad);

                this.revalidate();
                this.repaint();
            }

            private class AddNewRoadBtnActionListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainFrame.changeFourthPanel(new AddNewRoadPanel());
                }
            }

            private class RoadBtn extends JButton {
                private final Road road;

                public RoadBtn(Road road) {
                    this.road = road;

                    this.setText(String.format(
                            "%s - %s", road.getFirstStop().getName(), road.getSecondStop().getName()
                    ));
                    this.addActionListener(new RoadBtnListener());
                }

                private class RoadBtnListener implements ActionListener {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.changeFourthPanel(new ChangeRoadPanel());
                    }
                }

                private class ChangeRoadPanel extends JPanel {
                    public ChangeRoadPanel() {
                        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                        this.add(new JLabel(String.format(
                                "Путь %s - %s", road.getFirstStop().getName(), road.getSecondStop().getName()
                        )));

                        JButton deleteBtn = new JButton("Убрать путь");
                        deleteBtn.addActionListener(new DeleteRoadBtnActionListener());
                        this.add(deleteBtn);
                    }
                }

                private class DeleteRoadBtnActionListener implements ActionListener {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int index = roads.indexOf(road);
                        delayTimes.subList(index == 0 ? 0 : index + 1, delayTimes.size()).clear();
                        roads.subList(index, roads.size()).clear();
                        updateRoads();
                        mainFrame.changeFourthPanel(null);
                    }
                }
            }

            private class AddNewRoadPanel extends JPanel {
                public AddNewRoadPanel() {
                    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                    ArrayList<Road> roadsForAdding = mainFrame.getRoadsList();
                    if (roads.size() == 0) {
                        for (Road addRoad : roadsForAdding) {
                            this.add(new AddRoadBtn(addRoad));
                            this.add(new AddRoadBtn(
                                    new Road(addRoad.getSecondStop(), addRoad.getFirstStop(), addRoad.getLength())
                            ));
                        }
                    } else {
                        Stop lastStop = roads.get(roads.size() - 1).getSecondStop();
                        for (Road addRoad : roadsForAdding) {
                            if (addRoad.getFirstStop().equals(lastStop)) {
                                this.add(new AddRoadBtn(addRoad));
                            } else if (addRoad.getSecondStop().equals(lastStop)) {
                                this.add(new AddRoadBtn(
                                        new Road(addRoad.getSecondStop(), addRoad.getFirstStop(), addRoad.getLength())
                                ));
                            }
                        }
                    }
                }

                private class AddRoadBtn extends JButton {
                    private final Road addRoad;

                    public AddRoadBtn(Road addRoad) {
                        this.addRoad = addRoad;

                        this.setText(String.format(
                                "%s - %s", addRoad.getFirstStop().getName(), addRoad.getSecondStop().getName()
                        ));
                        this.addActionListener(new AddRoadBtnActionListener());
                    }

                    private class AddRoadBtnActionListener implements ActionListener {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (roads.size() == 0) delayTimes.add(1);
                            roads.add(addRoad);
                            delayTimes.add(1);
                            updateRoads();
                            mainFrame.changeFourthPanel(null);
                        }
                    }
                }
            }
        }

        private class ChangeDelayTimesPanel extends JPanel {
            public ChangeDelayTimesPanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                updateDelayTimes();
            }

            private void updateDelayTimes() {
                this.removeAll();

                if (roads.size() > 0) {
                    int index = 0;
                    this.add(new TimeDelayBtn(delayTimes.get(0), index++, roads.get(0).getFirstStop().getName()));

                    Iterator<Road> roadsIt = roads.iterator();
                    Iterator<Integer> delayTimesIt = delayTimes.iterator();
                    delayTimesIt.next();

                    while (roadsIt.hasNext() && delayTimesIt.hasNext()) {
                        this.add(new TimeDelayBtn(
                                delayTimesIt.next(), index++, roadsIt.next().getSecondStop().getName()
                        ));
                    }
                }
            }

            private class TimeDelayBtn extends JButton {
                private final int delayTime;
                private final int index;
                private final String stopName;

                public TimeDelayBtn(int delayTime, int index, String stopName) {
                    this.delayTime = delayTime;
                    this.index = index;
                    this.stopName = stopName;

                    this.setText(String.format("%s: %s", stopName, delayTime));
                    this.addActionListener(new TimeDelayBtnActionListener());
                }

                private class TimeDelayBtnActionListener implements ActionListener {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.changeFourthPanel(new ChangeDelayTimePanel());
                    }
                }

                private class ChangeDelayTimePanel extends JPanel {
                    private final JSpinner delayTimeSpinner;
                    public ChangeDelayTimePanel() {
                        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                        this.add(new JLabel(String.format("Остановка %s", stopName)));

                        this.add(new JLabel("Время остановки:"));

                        delayTimeSpinner = new JSpinner();
                        delayTimeSpinner.setValue(delayTime);
                        this.add(delayTimeSpinner);

                        JButton okBtn = new JButton("Готово");
                        okBtn.addActionListener(new DelayTimeOkBtnActionListener());
                        this.add(okBtn);
                    }

                    private class DelayTimeOkBtnActionListener implements ActionListener {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            delayTimes.set(index, (Integer) delayTimeSpinner.getValue());
                            updateDelayTimes();
                            mainFrame.changeFourthPanel(null);
                        }
                    }
                }
            }
        }
    }
}
