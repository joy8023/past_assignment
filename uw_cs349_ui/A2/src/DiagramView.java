import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Joy on 16/10/14.
 */
public class DiagramView extends JPanel implements IView{

    ERModel model;
    int EntityWidth = 150;
    int EntityHeight = 100;
    double zoom  = 1;
    boolean ctrl = false;
    boolean openBracket = false;
    boolean closeBracktet = false;
    JLabel tip;
    JLabel zoomLable;
    JButton addEntity,addArrow,zoomIn,zoomOut;

    //for moving
    boolean move = false;
    int moveEntity;
    int xoff;   // offset of the cursor from the left
    int yoff;  //offset of the cursor from the up

    //for adding
    boolean add1 = false;  //in add entity mode;
    boolean add2 = false;  //in add arrow mode;
    boolean add3 = false;  //has chosen one entity;
    String addName;
    int startp;
    int terminalp;

    //for drawing
    Font f = new Font("Helvetica",Font.PLAIN,(int)(20*zoom));
    FontMetrics fm;
    int textWidth;
    int textHeight;


    public DiagramView(ERModel m) {
        this.model = m;
        setBackground(Color.white);
        setBorder(BorderFactory.createTitledBorder("Diagram"));
        setPreferredSize(new Dimension(600, 625));
        setLayout(new BorderLayout());

        addEntity = new JButton("Add Entity");
        addArrow = new JButton("Add Arrow");
        zoomIn = new JButton("Zoom In");
        zoomOut = new JButton("Zoom Out");
        tip = new JLabel();
        tip.setForeground(Color.red);
        zoomLable = new JLabel();
        Box box = new Box(0);
        box.add(addEntity);
        box.add(addArrow);
        box.add(zoomIn);
        box.add(zoomOut);
        box.add(zoomLable);
        box.add(tip);
        add(box, BorderLayout.SOUTH);

        addEntity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(null,"Enter the name and click on diagram to put:","Add entity",1);
                if((name != null) && (name.length()>0)) {
                    add1 = true;
                    addName = name;
                }
            }
        });

        addArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("add arrow");
                if(model.entities.size()<2)
                    JOptionPane.showMessageDialog(null,"Error! You need more entities!");
                else{
                    add2 = true;
                    JOptionPane.showMessageDialog(null,"Please choose two entities orderly.");
                }
            }
        });

        zoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomIn();
            }
        });

        zoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int width = (int)(zoom*EntityWidth);
                int height = (int)(zoom*EntityHeight);

                int x = e.getX();
                int y = e.getY();

                if (add1) {
                    model.addEntity(addName,Math.min((int)(x/zoom),600-width),Math.min((int)(y/zoom),600-height));
                    add1 = false;
                }

                model.cleanHighlight();
                for (Entity entity : model.entities) {
                    int ex = (int)(zoom*entity.x);
                    int ey = (int)(zoom*entity.y);
                    if ((x >= ex) && (x <= ex + width) && (y >= ey) && (y <= ey + height)) {
                        //rename
                        if(e.getClickCount()==2){
                            String rename = JOptionPane.showInputDialog(null,"Enter the new name","Renama entity",1);
                            if((rename!= null)&&(rename.length()>0))
                                entity.setName(rename);
                            else
                                JOptionPane.showMessageDialog(null,"Error! Fail to rename!");
                        }
                        //select
                        model.selectEntity(entity.No);
                        //add arrow
                        if(add2){
                            startp = entity.No;
                            add2 = false;
                            add3 = true;
                            break;
                        }
                        if(add3){
                            if(startp != entity.No) {
                                terminalp = entity.No;
                                model.addArrow(startp, terminalp);
                                add3 = false;
                                break;
                            }
                            else
                                JOptionPane.showMessageDialog(null,"Error! This entity has been chosen.Please choose others!");
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

                int width = (int)(zoom*EntityWidth);
                int height = (int)(zoom*EntityHeight);

                model.cleanHighlight();
                for (Entity entity : model.entities) {

                    xoff = e.getX() - (int)(zoom*entity.x);
                    yoff = e.getY() - (int)(zoom*entity.y);
                    if ((xoff >= 0) && (xoff <= width) && (yoff >= 0) && (yoff <= height)) {
                        move = true;
                        moveEntity = entity.No;
                        model.highlightEntity(entity.No);
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                move = false;
                moveEntity = -1;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                int width = (int)(zoom*EntityWidth);
                int height = (int)(zoom*EntityHeight);

                if(move){
                    int c = e.getX() - xoff;
                    int d = e.getY() - yoff;
                    if((c>0)&&(c<600-width)&&(d>0)&&(d<600-height))
                        model.moveEntity(model.entities.get(moveEntity).No,(int)(c/zoom),(int)(d/zoom));
                }
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation()>0)
                    zoomIn();
                else if(e.getWheelRotation()<0)
                    zoomOut();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("press");
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL: {
                        ctrl = true;
                        System.out.println(ctrl);
                        break;
                    }
                    case KeyEvent.VK_OPEN_BRACKET:
                        openBracket = true;
                        break;
                    case KeyEvent.VK_CLOSE_BRACKET:
                        closeBracktet = true;
                        break;
                    default:
                        break;
                }
                if (ctrl && openBracket) {
                    zoomOut();
                } else if (ctrl && closeBracktet) {
                    zoomIn();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("release");
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        ctrl = false;
                        break;
                    case KeyEvent.VK_OPEN_BRACKET:
                        openBracket = false;
                        break;
                    case KeyEvent.VK_CLOSE_BRACKET:
                        closeBracktet = false;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void zoomIn(){
        this.zoom += 0.1000000000000000;
        if(zoom>2)
            this.zoom = 2;
        updateView();
    }

    public void zoomOut(){
        this.zoom -=0.1000000000000000;
        if(zoom<0.1)
            this.zoom = 0.1;
        updateView();
    }

    public void updateView(){
        repaint();
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D)graphics;
        g.setFont(f);
        fm = g.getFontMetrics(f);

        int width = (int)(zoom*EntityWidth);
        int height = (int)(zoom*EntityHeight);

        for(Entity e : model.entities){
            int x = Math.min((int)(e.x*zoom),600-width);
            int y = Math.min((int)(zoom*e.y),600-height);
            if(((int)(zoom*e.x)+width>600)||((int)(zoom*e.y)+height>600)){
                e.setPosition((int)(x/zoom),(int)(y/zoom));
                System.out.println(e.x+","+e.y);
            }
            textWidth = fm.stringWidth(e.name);
            textHeight = fm.getDescent();

            if(e.isHighlighted == true) {
                g.setColor(Color.pink);
                g.fillRect(x,y,width,height);
                g.setColor(Color.black);
                g.drawString(e.name, x+width/2-textWidth/2, y+height/2+textHeight);
            }
            else{
                g.setColor(Color.black);
                g.drawRect(x, y, width, height);
                g.drawString(e.name,x+width/2-textWidth/2,y+height/2+textHeight);
            }
        }

        for(Arrow a: model.arrows){
            Entity sp = model.entities.get(a.sp);
            Entity tp = model.entities.get(a.tp);
            drawArrow(g,(int)(zoom*sp.x),(int)(zoom*sp.y),(int)(zoom*tp.x),(int)(zoom*tp.y));
        }

        if(add1) {
            this.addEntity.setVisible(false);
            this.addArrow.setVisible(false);
            this.zoomIn.setVisible(false);
            this.zoomOut.setVisible(false);
            this.zoomLable.setVisible(false);
            this.tip.setText("Click on the diagram to put this new entity.");
        }
        else if (add2) {
            this.addEntity.setVisible(false);
            this.addArrow.setVisible(false);
            this.zoomIn.setVisible(false);
            this.zoomOut.setVisible(false);
            this.zoomLable.setVisible(false);
            this.tip.setText("Please choose the first entiity.");
        } else if (add3) {
            this.addEntity.setVisible(false);
            this.addArrow.setVisible(false);
            this.zoomIn.setVisible(false);
            this.zoomOut.setVisible(false);
            this.zoomLable.setVisible(false);
            this.tip.setText("Please choose the second entity.");
        } else {
            this.zoomLable.setText("Zoom = "+(int)(zoom*100+0.1)+"% ");
            this.addEntity.setVisible(true);
            this.addArrow.setVisible(true);
            this.zoomIn.setVisible(true);
            this.zoomOut.setVisible(true);
            this.zoomLable.setVisible(true);
            this.tip.setText("(10%~200%)");
        }
    }

    public void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2){
        int spx,spy,tpx,tpy,tmpx,tmpy;
        int width = (int)(zoom*EntityWidth);
        int height = (int)(zoom*EntityHeight);
        boolean jog = true;

        if(x1+width<x2){
            spx=x1+width;
            tpx=x2;
            tmpx=spx;
        }else if(x1>x2+width){
            spx=x1;
            tpx=x2+width;
            tmpx=spx;
        }else {
            spx=(x1+x2+width)/2;
            tpx=spx;
            tmpx=spx;
            jog=false;
        }

        if(y1+height<y2){
            spy=y1+height;
            tpy=y2;
            tmpy=tpy;
        }else if(y1>y2+height){
            spy=y1;
            tpy=y2+height;
            tmpy=tpy;
        }else {
            spy=(y1+y2+height)/2;
            tpy=spy;
            tmpy=spy;
            jog=false;
        }

        if(jog) {
            g.drawLine(spx, spy, tmpx, tmpy);
            spx = tmpx;
            spy = tmpy;
        }

        int x = spx-tpx;
        int y = tpy-spy;

        double length = Math.sqrt(x*x+y*y);
        if (length == 0) length = 1;

        double cos = x/length;
        double sin = y/length;

        g.setColor(Color.black);
        g.drawLine(spx,spy,tpx,tpy);
        g.drawLine((int)(tpx+7*cos-5*sin),(int)(tpy-7*sin-5*cos),tpx,tpy);
        g.drawLine((int)(tpx+7*cos+5*sin),(int)(tpy-7*sin+5*cos),tpx,tpy);

    }
}
