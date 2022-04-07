import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addItem() {
        Inventory i = new Inventory();
        assertNull( i.retrieveItem("Sword"), "No Sword or anything in inventory yet");
        i.addItem(new Sword());
        assertEquals("Sword", i.retrieveItem("Sword").getName());
    }

    @Test
    void retrieveAllOfType() {
        Inventory i = new Inventory();
        i.addItem(new Sword());
        assertEquals(1, i.retrieveAllOfType(Sword.class).size(), "Did not retrieve Sword");
    }
    @Test
    void retrieveAllOfType2() {
        Inventory i = new Inventory();
        i.addItem(new Sword());
        assertEquals(1, i.retrieveAllOfType(AttackItem.class).size(), "Did not retrieve AttackItem");
    }
    @Test
    void retrieveAllOfType3() {
        Inventory i = new Inventory();
        i.addItem(new Sword());
        i.addItem(new Sword());
        assertEquals(2, i.retrieveAllOfType(AttackItem.class).size(), "Did not retrieve AttackItem");
    }

    @Test
    void retrieveAllOfType4() {
        Inventory i = new Inventory();
        i.addItem(new Sword());
        i.addItem(new Sword());
        i.addItem(new Shield());
        assertEquals(2, i.retrieveAllOfType(AttackItem.class).size(), "Did not retrieve AttackItem");
    }
}