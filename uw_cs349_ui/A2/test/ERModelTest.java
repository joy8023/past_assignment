import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bwbecker on 2016-10-10.
 */
public class ERModelTest {

    @Test  //interaction 1&2
    public void addEntity() throws Exception {
        ERModel m = new ERModel();
        m.addEntity("model",10,10);
        m.addEntity("view",10,100);
        m.addEntity("controller",100,100);
        assertEquals("Should be model","model",m.entities.get(0).name);  //test name&model
        assertEquals("X should be 10",10,m.entities.get(1).x);  //test view&position
        assertFalse("Should be false",m.entities.get(2).isHighlighted);  //test controller&status
    }

    @Test  //interaction 3
    public void addArrow() throws Exception {
        ERModel m = new ERModel();
        m.addArrow(0,1);
        m.addArrow(1,2);
        assertEquals(0,m.arrows.get(0).sp);
        assertEquals(1,m.arrows.get(0).tp);
        assertFalse(m.arrows.get(1).isHighlighted);
    }

    @Test  //interaction 4
    public void moveEntity() throws Exception {
        ERModel m = new ERModel();
        m.addEntity("model",10,10);
        m.moveEntity(0,100,100);
        assertEquals("model",m.entities.get(0).name);
        assertEquals(100,m.entities.get(0).x);
        assertEquals(100,m.entities.get(0).y);

    }

    @Test  //interaction 5&6
    public void selectEntity() throws Exception {
        ERModel m = new ERModel();
        m.addEntity("model",10,10);
        m.addEntity("view",10,100);
        m.addArrow(0,1);
        m.addArrow(1,0);
        assertFalse("Entity should not be highlighted",m.entities.get(0).isHighlighted);
        assertFalse("Arrow should not be highlighted",m.arrows.get(0).isHighlighted);
        assertFalse("Arrow should not be highlighted",m.arrows.get(1).isHighlighted);
        m.selectEntity(0);
        assertTrue("Entity should be highlighted",m.entities.get(0).isHighlighted);
        assertTrue("Arrow should be highlighted",m.arrows.get(0).isHighlighted);
        assertTrue("Arrow should be highlighted",m.arrows.get(1).isHighlighted);
    }

    @Test  //interaction 7
    public void selectArrow() throws Exception {
        ERModel m = new ERModel();
        m.addEntity("model",10,10);
        m.addEntity("view",10,100);
        m.addArrow(0,1);
        assertFalse("Model should not be highlighted",m.entities.get(0).isHighlighted);
        assertFalse("View should not be highlighted",m.entities.get(1).isHighlighted);
        assertFalse("Arrow should not be highlighted",m.arrows.get(0).isHighlighted);
        m.selectArrow(0);
        assertTrue("Model should be highlighted",m.entities.get(0).isHighlighted);
        assertTrue("View should be highlighted",m.entities.get(1).isHighlighted);
        assertTrue("Arrow should be highlighted",m.arrows.get(0).isHighlighted);

    }

}