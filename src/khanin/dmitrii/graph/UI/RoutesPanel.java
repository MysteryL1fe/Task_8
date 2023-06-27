package khanin.dmitrii.graph.UI;

import khanin.dmitrii.graph.logic.Road;
import khanin.dmitrii.graph.logic.Route;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
                mainFrame.changeThirdPanel(new ChangeRoadsPanel(roads));
            }
        }

        private class ChangeDelayTimesBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changeThirdPanel(null);
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
    }

    private class ChangeRoadsPanel extends JPanel {
        private final ArrayList<Road> roads;

        public ChangeRoadsPanel(ArrayList<Road> roads) {
            this.roads = roads;

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
        }

        private class ChangeRoadPanel extends JPanel {

        }

        private class AddNewRoadPanel extends JPanel {
            public AddNewRoadPanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                for (Road addRoad : mainFrame.getRoadsList()) {
                    this.add(new AddRoadBtn(addRoad));
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
                        roads.add(addRoad);
                        updateRoads();
                        mainFrame.changeFifthPanel(null);
                    }
                }
            }
        }
    }
}
