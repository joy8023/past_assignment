import java.util.ArrayList;

interface IView {
    public void updateView();
}

public class ERModel {

    public ERModel() {

    }
    ArrayList<IView> views = new ArrayList<>();
    ArrayList<Entity> entities = new ArrayList<>();
    ArrayList<Arrow> arrows = new ArrayList<>();

    public void addView (IView view){
        views.add(view);
        view.updateView();
    }

    public void notifyView(){
        for(IView v : this.views)
            v.updateView();
    }

    public Entity getEntity(int no){
        return entities.get(no);
    }

    public Arrow getArrow(int no){
        return arrows.get(no);
    }

    public void addEntity(String s, int x, int y){  //group1 1&2
        Entity e = new Entity(s,x,y);
        entities.add(e);
        e.setNo(entities.indexOf(e));
        e.setHighlighted(false);
        notifyView();
    }

    public void addArrow(int sp, int tp){  //group1 3
        Arrow a = new Arrow(sp,tp);
        arrows.add(a);
        a.setNo(arrows.indexOf(a));
        a.setHighlighted(false);
        notifyView();
    }

    public void moveEntity(int no, int x, int y){  //group1 4
        entities.get(no).setPosition(x,y);
        notifyView();
    }

    public void highlightEntity(int no){
        entities.get(no).setHighlighted(true);
    }

    public void highlightArrow(int no){
        arrows.get(no).setHighlighted(true);
    }

    public void selectEntity(int no){  //group1 5&6
        cleanHighlight();
        highlightEntity(no);
        for(Arrow a : this.arrows){
            if((a.sp == no) || (a.tp == no))
                a.setHighlighted(true);
        }
        notifyView();
    }

    public void selectArrow(int no){  //group1 7
        cleanHighlight();
        highlightArrow(no);
        highlightEntity(arrows.get(no).sp);
        highlightEntity(arrows.get(no).tp);
        notifyView();
    }

    public void cleanHighlight(){
        for(Entity e : this.entities)
            e.setHighlighted(false);
        for(Arrow a : this.arrows)
            a.setHighlighted(false);
        notifyView();
    }

}

class Entity{
    int No;
    String name;
    int x;  //position x
    int y;  //position y
    boolean isHighlighted;

    public Entity(String s, int x, int y){
        this.name = s;
        this.x = x;
        this.y = y;

    }

    public void setNo (int i){
        this.No = i;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHighlighted(boolean b){
        this.isHighlighted = b;
    }
}

class Arrow {
    int No;  //Number of arrows
    int sp;  //entity's no of the starting point
    int tp;  //entity's no of the terminal point
    Boolean isHighlighted;

    public Arrow(int sp, int tp){
        this.sp = sp;
        this.tp = tp;
    }

    public void setNo(int n){
        this.No = n;
    }

    public void setHighlighted(boolean b){
        this.isHighlighted = b;
    }

}
