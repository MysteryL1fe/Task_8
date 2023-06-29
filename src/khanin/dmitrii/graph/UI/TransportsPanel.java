package khanin.dmitrii.graph.UI;

import khanin.dmitrii.graph.logic.Route;
import khanin.dmitrii.graph.logic.Transport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TransportsPanel extends JPanel {
    private ArrayList<Transport> transports;
    private MainFrame mainFrame;

    public TransportsPanel(ArrayList<Transport> transports, MainFrame mainFrame) {
        this.transports = transports;
        this.mainFrame = mainFrame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        updateTransports();
    }

    private void updateTransports() {
        this.removeAll();

        JButton addTransportBtn = new JButton("Добавить транспорт");
        addTransportBtn.addActionListener(new AddTransportBtnActionListener());
        this.add(addTransportBtn);

        for (Transport transport : transports) {
            this.add(new TransportBtn(transport));
        }
    }

    private class AddTransportBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainFrame.changeSecondPanel(new ChangeTransportPanel(null));
        }
    }

    private class TransportBtn extends JButton {
        private final Transport transport;

        public TransportBtn(Transport transport) {
            this.transport = transport;

            this.setText(String.format("%s", transport.getNum()));
            this.addActionListener(new TransportBtnActionListener());
        }

        private class TransportBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changeSecondPanel(new ChangeTransportPanel(transport));
                mainFrame.paintRoute(transport.getRoute());
            }
        }
    }

    private class ChangeTransportPanel extends JPanel {
        private Transport transport;
        private final JSpinner startTimeSpinner;
        private final JSpinner speedSpinner;
        private final JSpinner costSpinner;
        private final JTextField numTextField;
        private Route route;
        private JButton routeBtn;

        public ChangeTransportPanel(Transport transport) {
            this.transport = transport;
            this.route = transport == null ? null : transport.getRoute();

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            this.add(new JLabel("Маршрут транспорта"));

            routeBtn = new JButton(route == null ? "" : route.getNum());
            routeBtn.addActionListener(new RouteBtnActionListener());
            this.add(routeBtn);

            this.add(new JLabel("Время начала движения"));

            startTimeSpinner = new JSpinner();
            startTimeSpinner.setValue(transport == null ? 0 : transport.getStartTime());
            this.add(startTimeSpinner);

            this.add(new JLabel("Скорость (расстояние за единицу времени)"));

            speedSpinner = new JSpinner();
            speedSpinner.setValue(transport == null ? 1 : transport.getSpeed());
            this.add(speedSpinner);

            this.add(new JLabel("Стоимость поездки за единицу расстояния"));

            costSpinner = new JSpinner();
            costSpinner.setValue(transport == null ? 0 : transport.getCost());
            this.add(costSpinner);

            this.add(new JLabel("Номер транспорта"));

            numTextField = new JTextField(transport == null ? "" : String.format("%s", transport.getNum()));
            this.add(numTextField);

            JButton okBtn = new JButton("Готово");
            okBtn.addActionListener(new OkBtnActionListener());
            this.add(okBtn);
        }

        private void updateRoute() {
            routeBtn.setText(route.getNum());
        }

        private class OkBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (transport == null) {
                    transport = new Transport(
                            route, Math.max(0, (Integer) startTimeSpinner.getValue()),
                            Math.max(1, (Integer) speedSpinner.getValue()),
                            Math.max(0, (Integer) costSpinner.getValue()), numTextField.getText()
                    );
                    transports.add(transport);
                } else {
                    transport.setRoute(route);
                    transport.setStartTime(Math.max(0, (Integer) startTimeSpinner.getValue()));
                    transport.setSpeed(Math.max(1, (Integer) speedSpinner.getValue()));
                    transport.setCost(Math.max(0, (Integer) costSpinner.getValue()));
                    transport.setNum(numTextField.getText());
                }
                updateTransports();
                mainFrame.changeSecondPanel(null);
            }
        }

        private class RouteBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changeThirdPanel(new ChooseRoutePanel());
                mainFrame.paintRoute(route);
            }
        }

        private class ChooseRoutePanel extends JPanel {
            public ChooseRoutePanel() {
                this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                for (Route chooseRoute : mainFrame.getRoutesList()) {
                    this.add(new ChooseRouteBtn(chooseRoute));
                }
            }
        }

        private class ChooseRouteBtn extends JButton {
            private Route chooseRoute;

            public ChooseRouteBtn(Route chooseRoute) {
                this.chooseRoute = chooseRoute;
                this.setText(chooseRoute.getNum());
                this.addActionListener(new ChooseRouteBtnActionListener());
            }

            private class ChooseRouteBtnActionListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    route = chooseRoute;
                    updateRoute();
                    mainFrame.changeThirdPanel(null);
                    mainFrame.paintRoute(route);
                }
            }
        }
    }
}
