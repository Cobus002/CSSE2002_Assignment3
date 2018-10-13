package csse2002.block.world;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Controller class for the GUI
 * <p>
 * Used to control the View depending on user input
 */
public class Controller {
    
    /* the view for the canvas application*/
    private View view;
    private Stage primaryStage;

    private WorldMap gameMap;
    private boolean gameActive = false;
    
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
     * showWarning() method is used in the controller to show error and
     * warning messages to the user.
     * @param msg
     */
    protected static void showWarning(String msg){
        Alert myAlert = new Alert(Alert.AlertType.WARNING, msg);
        myAlert.show();
    }

    /**
     * Draw the top tile in the world map of the tiles
     */
    protected void drawMap(){

        Map<Position, Tile> loadMap = gameMap.getWorldMap();
        Iterator mapIterator = loadMap.entrySet().iterator();
        while(mapIterator.hasNext()){
            Map.Entry pair = (Map.Entry)mapIterator.next();
            Position currPos = (Position) pair.getKey();
            Tile currTile = (Tile)pair.getValue();
            String colour = new String("none");
            try{
                colour = currTile.getTopBlock().getColour();
            }catch(TooLowException e){
                //Too Low there is no top tile just use the default color
            }
            view.addRectangleToMap(colour, currPos.getX(),currPos.getY());

        }


    }

    /**
     * EventHandler class which deals with user inputs no the buttons in the view
     */
    private class GameHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            
            /* Get the button which was just pressed */
            // TODO: code here
            if(!gameActive){
                showWarning("Load map first");
                return;
            }
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
     * Event handler for the menu bar items. Used to Load and save the world
     * maps from text files.
     */
    private class MenuBarItemHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            MenuItem menuItemPressed = (MenuItem) event.getSource();

            if(menuItemPressed == view.getSaveMenuItem()){
                System.out.println("Save");
                FileChooser fc = new FileChooser();
                File selectedFile = fc.showSaveDialog(primaryStage);
                //Pass the file to the appropriate save function
                try{
                    gameMap.saveMap(selectedFile.toString());
                }catch(Exception e){
                    if(e instanceof IOException){
                        showWarning("IOException error");
                    }
                }
            }else if(menuItemPressed == view.getLoadMenuItem()){
                System.out.println("Load");
                FileChooser fc = new FileChooser();
                File selectedFile = fc.showOpenDialog(primaryStage);
                //Pass the file to the appropriate function
                try {
                    BufferedReader bufReader =
                            new BufferedReader(new FileReader(selectedFile));
                    gameMap = new WorldMap(selectedFile.toString());
                    //Let the controller know that the map was loaded
                    // successfully
                    gameActive = true;
                    drawMap();
                }catch(Exception e){
                    //The file was not found
                    if(e instanceof FileNotFoundException){
                        System.out.println("File not found");
                        showWarning("File not found");
                    }else if(e instanceof WorldMapFormatException){
                        System.out.println("WorldFormatMapException");
                        showWarning("WorldFormatMapException error occurred");
                    }else if(e instanceof WorldMapInconsistentException){
                        System.out.println("WorldMapInconsistent");
                        showWarning("WorldMapInconsistent error occurred");
                    }
                }
            }
        }
    }
}
