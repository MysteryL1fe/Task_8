package khanin.dmitrii.graph.UI;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import khanin.dmitrii.graph.graphs.GraphUtils;
import khanin.dmitrii.graph.logic.*;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.*;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.StringReader;
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
    private final ArrayList<Stop> stopsList = new ArrayList<>();
    private final ArrayList<Road> roadsList = new ArrayList<>();
    private final ArrayList<Route> routesList = new ArrayList<>();
    private final ArrayList<Transport> transportsList = new ArrayList<>();
    private final SvgPanel graphPanelPainter;

    public MainFrame() {
        this.setTitle("Graph 2.0");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(1500, 500);

        firstChangesPanel.setLayout(new BoxLayout(firstChangesPanel, BoxLayout.Y_AXIS));
        secondChangesPanel.setLayout(new BoxLayout(secondChangesPanel, BoxLayout.Y_AXIS));
        thirdChangesPanel.setLayout(new BoxLayout(thirdChangesPanel, BoxLayout.Y_AXIS));
        fourthChangesPanel.setLayout(new BoxLayout(fourthChangesPanel, BoxLayout.Y_AXIS));

        graphPanel.setLayout(new BorderLayout());
        graphPanelPainter = new SvgPanel();
        graphPanel.add(new JScrollPane(graphPanelPainter));

        stopsBtn.addActionListener(new StopsBtnActionListener());
        roadsBtn.addActionListener(new RoadsBtnActionListener());
        routesBtn.addActionListener(new RoutesBtnActionListener());
        transportsBtn.addActionListener(new TransportsBtnActionListener());
        createGraphBtn.addActionListener(new CreateGraphBtnActionListener());
    }

    public void changeSecondPanel(JPanel panel) {
        secondChangesPanel.removeAll();
        thirdChangesPanel.removeAll();
        fourthChangesPanel.removeAll();
        if (panel != null) secondChangesPanel.add(panel);
        revalidate();
        repaint();
    }

    public void changeThirdPanel(JPanel panel) {
        thirdChangesPanel.removeAll();
        fourthChangesPanel.removeAll();
        if (panel != null) thirdChangesPanel.add(panel);
        revalidate();
        repaint();
    }

    public void changeFourthPanel(JPanel panel) {
        fourthChangesPanel.removeAll();
        if (panel != null) fourthChangesPanel.add(panel);
        revalidate();
        repaint();
    }

    private static String dotToSvg(String dotSrc) throws IOException {
        MutableGraph g = new Parser().read(dotSrc);
        return Graphviz.fromGraph(g).render(Format.SVG).toString();
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

    private static class SvgPanel extends JPanel {
        private String svg = null;
        private GraphicsNode svgGraphicsNode = null;

        public void paint(String svg) throws IOException {
            String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
            SVGDocument doc = df.createSVGDocument(null, new StringReader(svg));
            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            GVTBuilder builder = new GVTBuilder();
            svgGraphicsNode = builder.build(ctx, doc);

            this.svg = svg;
            repaint();
        }

        @Override
        public void paintComponent(Graphics gr) {
            super.paintComponent(gr);

            if (svgGraphicsNode == null) {
                return;
            }

            double scaleX = this.getWidth() / svgGraphicsNode.getPrimitiveBounds().getWidth();
            double scaleY = this.getHeight() / svgGraphicsNode.getPrimitiveBounds().getHeight();
            double scale = Math.min(scaleX, scaleY);
            AffineTransform transform = new AffineTransform(scale, 0, 0, scale, 0, 0);
            svgGraphicsNode.setTransform(transform);
            Graphics2D g2d = (Graphics2D) gr;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            svgGraphicsNode.paint(g2d);
        }
    }

    private class StopsBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            firstChangesPanel.removeAll();
            firstChangesPanel.add(new StopsPanel(stopsList, MainFrame.this));
            secondChangesPanel.removeAll();
            thirdChangesPanel.removeAll();
            fourthChangesPanel.removeAll();
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
            MainFrame.this.revalidate();
            MainFrame.this.repaint();
        }
    }

    private class CreateGraphBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                graphPanelPainter.paint(
                        dotToSvg(GraphUtils.toDot(new CityGraph(stopsList, roadsList, routesList, transportsList)))
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
