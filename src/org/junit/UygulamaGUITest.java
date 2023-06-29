package Project;

import org.junit.Assert;
import org.junit.Test;

public class UygulamaGUITest {

    @Test
    public void testAddChild() {
        UygulamaGUI gui = new UygulamaGUI();
        Parent parent = new Parent("admin", "admin");
        gui.addChild(parent);
        User user = gui.getUser("child1");
        Assert.assertNotNull(user);
        Assert.assertTrue(user instanceof Parent);
        Parent parentUser = (Parent) user;
        Assert.assertEquals(1, parentUser.getChildren().size());
    }
    
    @Test
    public void testGetUser() {
        UygulamaGUI gui = new UygulamaGUI();
        User user = gui.getUser("admin");
        Assert.assertNotNull(user);
        Assert.assertEquals("admin", user.getUsername());
    }
    
    @Test
    public void testAuthenticateUser() {
        UygulamaGUI gui = new UygulamaGUI();
        boolean result = gui.authenticateUser("child2", "child2");
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsUsernameTaken() {
        UygulamaGUI gui = new UygulamaGUI();
        boolean result = gui.isUsernameTaken("child3");
        Assert.assertTrue(result);
    }
}

