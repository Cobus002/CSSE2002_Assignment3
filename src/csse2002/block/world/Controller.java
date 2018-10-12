package csse2002.block.world;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * The Controller class for the GUI
 * <p>
 * Used to control the View depending on user input
 */
public class Controller {
    
    /* the view for the canvas application*/
    private View view;
    private Stage primaryStage;
    
    /**
     * Create a new Controller for the given view
     */
    public Controller(View view, Stage primaryStage) {
        this.view = view;
        this.primaryStage = primaryStage;
        view.addButtonHandler(new GameHandler());
        view.addMenuItemHandlers(new MenuBarItemHandler());
    }
    
    /**
     * EventHandler class which deals with user inputs no the buttons in the view
     */
    private class GameHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            
            /* Get the button which was just pressed */
            // TODO: code here
            Button pressedButton = (Button) event.getSource();

            if(pressedButton == view.getNorthButton()){
                //handle the north button stuff
                System.out.println("North");
            }else if(pressedButton == view.getEastButton()){
                System.out.println("East");
            }else if(pressedButton == view.getSouthButton()){
                System.out.println("South");
            }else if(pressedButton == view.getWesthButton()){
                System.out.println("West");
            }
        }
    }

    /**
     * Event handler for the menu bar items
     */
    private class MenuBarItemHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            MenuItem menuItemPressed = (MenuItem) event.getSource();

            if(menuItemPressed == view.getSaveMenuItem()){
                System.out.println("Save");
                FileChooser fc = new FileChooser();
                File selectedFile = fc.showOpenDialog(primaryStage);
                //Pass the file to the appropriate save function

            }else if(menuItemPressed == view.getLoadMenuItem()){
                System.out.println("Load");
                FileChooser fc = new FileChooser();
                File selectedFile = fc.showOpenDialog(primaryStage);
                //Pass the file to the appropriate function
            }
        }
    }
}
