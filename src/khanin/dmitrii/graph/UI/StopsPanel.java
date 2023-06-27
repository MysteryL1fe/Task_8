package khanin.dmitrii.graph.UI;

import khanin.dmitrii.graph.logic.Stop;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StopsPanel extends JPanel {
    private final ArrayList<Stop> stops;
    private final MainFrame mainFrame;

    public StopsPanel(ArrayList<Stop> stops, MainFrame mainFrame) {
        this.stops = stops;
        this.mainFrame = mainFrame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        updateStops();
    }

    private void updateStops() {
        this.removeAll();

        JButton addStopBtn = new JButton("Добавить остановку");
        addStopBtn.addActionListener(new AddStopBtnActionListener());
        this.add(addStopBtn);

        for (Stop stop : stops) {
            this.add(new StopBtn(stop));
        }

        this.revalidate();
        this.repaint();
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
                mainFrame.changeSecondPanel(new ChangeStopPanel(stop));
            }
        }
    }

    private class ChangeStopPanel extends JPanel {
        private Stop stop;
        private final JTextField stopName;

        public ChangeStopPanel(Stop stop) {
            this.stop = stop;

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            this.add(new JLabel("Название остановки"));

            stopName = new JTextField(stop == null ? "" : stop.getName());
            this.add(stopName);

            JButton okBtn = new JButton("Готово");
            okBtn.addActionListener(new OkBtnActionListener());
            this.add(okBtn);
        }

        private class OkBtnActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stop == null) {
                    stop = new Stop(stopName.getText());
                    stops.add(stop);
                } else {
                    stop.setName(stopName.getText());
                }
                mainFrame.changeSecondPanel(null);
                updateStops();
            }
        }
    }

    private class AddStopBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainFrame.changeSecondPanel(new ChangeStopPanel(null));
        }
    }
}
