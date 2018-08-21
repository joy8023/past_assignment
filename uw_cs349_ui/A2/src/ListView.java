import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Joy on 16/10/25.
 */
public class ListView extends JPanel implements IView {

    ERModel model;

    DefaultListModel eName = new DefaultListModel();
    JList eList = new JList(eName);

    DefaultListModel aName = new DefaultListModel();
    JList aList = new JList(aName);

    Font font = new Font("Dialog",Font.BOLD,26);
    Font font1 = new Font("Dialog",Font.PLAIN,11);

    JLabel instr = new JLabel();

    public ListView(ERModel model) {

        this.model = model;
        setLayout(new GridLayout(3,1));
        setPreferredSize(new Dimension(200,625));
        setBackground(Color.PINK);
        setFocusable(false);

        instr.setFont(font1);
        instr.setBorder(BorderFactory.createTitledBorder("Instructions"));
        instr.setText("<html>" +
                "Add Entity:<br>" +
                "--Enter the name,<br>" +
                "--Choose the location.<br><br>"+
                "Add Arrow<br>"+
                "--Choose the first entity,<br>"+
                "--Choose the second entity.<br><br>"+
                "Click on entity to select.<br>" +
                "Click on blank to unselect.<br>"+
                "Double click on entity to rename.<br>"+
                "Drag entity to relocate."+
                "</html>");

        eList.setBackground(Color.cyan);
        eList.setFont(font);
        eList.setBorder(BorderFactory.createTitledBorder("Entity list"));
        eList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane ePane = new JScrollPane(eList);

        aList.setBackground(Color.YELLOW);
        aList.setFont(font);
        aList.setBorder(BorderFactory.createTitledBorder("Arrow list"));
        aList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane aPane = new JScrollPane(aList);

        add(instr);
        add(ePane);
        add(aPane);

        eList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.selectEntity(eList.getSelectedIndex());

            }
            @Override
            public void mouseReleased(MouseEvent e){
                model.selectEntity(eList.getSelectedIndex());
            }
        });

        aList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.selectArrow(aList.getSelectedIndex());
            }
        });
    }

    @Override
    public void updateView () {
        int[] eselect = new int[10];
        for (int a = 0; a < 10; a++) {
            eselect[a] = -1;
        }
        int i = 0;
        eName.clear();
        for (Entity e : model.entities) {
            eName.addElement(e.name);       //display name
            if (e.isHighlighted)         // is highlighted
                eselect[i++] = e.No;
        }
        eList.clearSelection();
        eList.setSelectedIndices(eselect);

        int[] aselect = new int[10];
        for(int b = 0; b<10; b++){
            aselect[b] = -1;
        }
        i = 0;
        aName.clear();
        for(Arrow a : model.arrows){
            aName.addElement(model.entities.get(a.sp).name+"->"+model.entities.get(a.tp).name);
            if (a.isHighlighted)
                aselect[i++] = a.No;
        }
        aList.clearSelection();
        aList.setSelectedIndices(aselect);
    }
}
