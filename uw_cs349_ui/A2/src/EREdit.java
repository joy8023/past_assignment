import javax.swing.*;
import java.awt.*;

/**
 * Created by bwbecker on 2016-10-10.
 */
public class EREdit {

    private DiagramView diagramView;

    public static void main(String[] args) {
        System.out.println("Hello, world!");
        ERModel model = new ERModel();
        DiagramView diagramView = new DiagramView(model);
        ListView listView = new ListView(model);

        JFrame frame = new JFrame("EREdit");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        model.addView(diagramView);
        model.addView(listView);

        frame.add(diagramView, BorderLayout.CENTER);
        frame.add(listView, BorderLayout.WEST);

        frame.pack();
        frame.setPreferredSize(new Dimension(800, 625));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        diagramView.setFocusable(true);
        diagramView.requestFocusInWindow();
    }

}
