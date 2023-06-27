package khanin.dmitrii.graph.UI;

import khanin.dmitrii.graph.logic.Transport;

import javax.swing.*;
import java.util.ArrayList;

public class TransportsPanel extends JPanel {
    private ArrayList<Transport> transports;
    private MainFrame mainFrame;

    public TransportsPanel(ArrayList<Transport> transports, MainFrame mainFrame) {
        this.transports = transports;
        this.mainFrame = mainFrame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton addTransportBtn = new JButton("Добавить транспорт");
        addTransportBtn.addActionListener(null);
        this.add(addTransportBtn);
    }
}
