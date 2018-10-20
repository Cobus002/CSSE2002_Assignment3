package csse2002.block.world;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

/**
 * The Controller class for the GUI
 * <p>
 * Used to control the View depending on user input
 */
public class Controller {

    //Private constants used for the exits
    private static final String NORTH = "north";
    private static final String EAST = "east";
    private static final String SOUTH = "south";
    private static final String WEST = "west";

    //List for the directions to make loops easier
    private static final List<String> DIRECTION_LIST = Arrays.asList(NORTH,
            EAST, SOUTH, WEST);

    /* the view for the application*/
    private View view;
    private Stage primaryStage;
    //The world map
    private WorldMap gameMap;
    //boolean to say if map has been loaded
    private boolean gameActive = false;
    //Current commbobox selected action
    private String actionType = null;
    //The builder position
    private Position builderPos = null;
    //Map to store <Position, Tile> info
    Map<Position, Tile> gameMapWithCoords;

    /**
     * Create a new Controller for the given view
     */
    public Controller(View view, Stage primaryStage) {
        this.view = view;
        this.primaryStage = primaryStage;
        view.addButtonHandler(new GameHandler());
        view.addMenuItemHandlers(new MenuBarItemHandler());
        view.addActionComboHandler(new ActionsComboBoxHandler());
    }

    /**
     * showWarning() method is used in the controller to show error and
     * warning messages to the user.
     *
     * @param msg
     */
    protected static void showWarning(String msg) {
        Alert myAlert = new Alert(Alert.AlertType.WARNING, msg);
        myAlert.show();
    }

    /**
     * generateGameMapWithCoords() function is used to generate Map with
     * positions and tiles mapped together, this is used to make drawing
     * easier since we don't have access to the already generated tileMap in
     * the sparseTileArray class(which would have been easy to implement with
     * to getter functions, see Piazza for my discussion).
     * @param startingTile
     * @param startingX
     * @param startingY
     * @return
     */
    private Map<Position, Tile> generateGameMapWithCoords(Tile startingTile,
                                                          int startingX,
                                                          int startingY){
        //Resulting map
        Map<Position, Tile> resultMap = new LinkedHashMap<>();
        //Set up the Queue structure used in the breadth first search
        Queue<Position> toVisitQueue = new LinkedList<>();
        Set<Position> visited = new LinkedHashSet<>();
        Map<Position, Tile> toVisitMap = new HashMap<>();
        Map<String, Tile> exits;
        Tile tileAtPos;
        Position currPos = new Position(startingX, startingY);
        toVisitMap.put(currPos, startingTile);
        toVisitQueue.add(currPos);
        /************* Start Breadth-First Search Algorithm ***************/
        while(toVisitQueue.size()!=0){
            currPos = toVisitQueue.remove();
            tileAtPos = toVisitMap.get(currPos);

            if(!visited.contains(currPos)){
                //First visit
                visited.add(currPos);
                resultMap.put(currPos, tileAtPos);
                exits=tileAtPos.getExits();
                Iterator dirIterator = DIRECTION_LIST.iterator();
                int currPosX = currPos.getX();
                int currPosY = currPos.getY();
                while(dirIterator.hasNext()){
                    Position newPos = null;
                    String direction = (String)dirIterator.next();

                    switch(direction){
                        case NORTH:
                            newPos = new Position(currPosX, currPosY-1);
                            break;
                        case EAST:
                            newPos = new Position(currPosX + 1, currPosY);
                            break;
                        case SOUTH:
                            newPos = new Position(currPosX, currPosY + 1);
                            break;
                        case WEST:
                            newPos = new Position(currPosX - 1, currPosY);
                            break;
                    }
                    if(exits.containsKey(direction)){
                        toVisitQueue.add(newPos);
                        toVisitMap.put(newPos, exits.get(direction));
                    }

                }
            }

        }
        /************* End Breadth-First Search Algorithm ***************/

        return resultMap;
    }

    /**
     * Draw the top tile in the world map of the tiles
     */
    protected void drawMap() {
        Iterator mapIterator = gameMapWithCoords.entrySet().iterator();
        Position shiftPosition = new Position(4-builderPos.getX(),
                4-builderPos.getY());
        view.resetMapView();
        while (mapIterator.hasNext()) {
            Map.Entry pair = (Map.Entry) mapIterator.next();
            Position currPos = (Position) pair.getKey();
            Tile currTile = (Tile) pair.getValue();

            try {
                Position modifiedPos =
                        new Position(currPos.getX()+shiftPosition.getX(),
                                currPos.getY()+shiftPosition.getY());
                view.drawTileOnMap(modifiedPos, currTile,
                        shiftPosition.getX()+builderPos.getX(),
                        shiftPosition.getY()+builderPos.getY());
            } catch (TooLowException e) {
                System.out.println("TooLowException error");
            }
        }
        //Update the inventory label of the view
        int inventorySize = gameMap.getBuilder().getInventory().size();
        List<Block> inventoryList = gameMap.getBuilder().getInventory();
        if(inventorySize==0){
            view.setInventoryLabel("[ ]");
        }else {
            String inventoryString = new String();
            inventoryString+="[ ";
            int i = 0;
            for( i = 0 ; i < inventorySize-1; i++){
                inventoryString+=inventoryList.get(i).getBlockType();
                inventoryString+=", ";
            }
            inventoryString += inventoryList.get(i).getBlockType();
            inventoryString += " ]";
            view.setInventoryLabel(inventoryString);
        }
    }

    /**
     * EventHandler class which deals with user inputs no the buttons in the view
     */
    private class GameHandler implements EventHandler<ActionEvent> {

        /**
         * own handle action function since the given Action class
         * processAction() function does not throw
         * any exceptions to use for processing
         * @param primaryAction
         * @param secondary
         * @throws NoExitException
         * @throws TooHighException
         * @throws InvalidBlockException
         * @throws TooLowException
         */
        void handleAction(int primaryAction, String secondary) throws
                NoExitException, TooHighException, InvalidBlockException,
                TooLowException{

            if(primaryAction==Action.MOVE_BUILDER){
                Tile moveTo =
                        gameMap.getBuilder().getCurrentTile().getExits().
                                get(secondary);
                gameMap.getBuilder().moveTo(moveTo);
                switch (secondary){
                    case "north":
                        builderPos = new Position(builderPos.getX(),
                                builderPos.getY()-1);
                        break;
                    case "east":
                        builderPos = new Position(builderPos.getX()+1,
                                builderPos.getY());
                        break;
                    case "south":
                        builderPos = new Position(builderPos.getX(),
                                builderPos.getY()+1);
                        break;
                    case "west":
                        builderPos = new Position(builderPos.getX()-1,
                                builderPos.getY());
                        break;
                }
            }else if(primaryAction == Action.MOVE_BLOCK){
                gameMap.getBuilder().getCurrentTile().moveBlock(
                        secondary);
            }else if(primaryAction == Action.DIG){
                gameMap.getBuilder().digOnCurrentTile();
            }else if(primaryAction == Action.DROP){
                int index = Integer.parseInt(secondary);
                gameMap.getBuilder().dropFromInventory(index);
            }
        }

        @Override
        public void handle(ActionEvent event) {

            /* Get the button which was just pressed */
            if (!gameActive) {
                showWarning("Load map first");
                return;
            }
            if(actionType == null){
                showWarning("Select action type with combobox");
                return;
            }
            int primaryAction = 99;
            if(actionType.equals("block")){
                primaryAction = Action.MOVE_BLOCK;
            }else{
                primaryAction = Action.MOVE_BUILDER;
            }
            Button pressedButton = (Button) event.getSource();

            try {
                if (pressedButton == view.getNorthButton()) {
                    //handle the north button stuff
                    System.out.println("North");
                    handleAction(primaryAction, "north");

                } else if (pressedButton == view.getEastButton()) {
                    System.out.println("East");
                    handleAction(primaryAction, "east");

                } else if (pressedButton == view.getSouthButton()) {
                    System.out.println("South");
                    handleAction(primaryAction, "south");

                } else if (pressedButton == view.getWestButton()) {
                    System.out.println("West");
                    handleAction(primaryAction, "west");

                } else if (pressedButton == view.getDigButton()) {
                    System.out.println("Dig");
                    handleAction(Action.DIG, null);

                } else if (pressedButton == view.getDropButton()) {
                    //Todo: fix this drop implementation
                    System.out.println("Drop");
                    handleAction(Action.DROP, view.getIndexTextFieldText());
                }
                drawMap();
            } catch(Exception e){
                //NoExitException, TooHighException, InvalidBlockException,
                //                TooLowException
                if(e instanceof NoExitException){
                    showWarning("NoExitException");
                }else if(e instanceof TooHighException){
                    showWarning("TooHighException");
                }else if(e instanceof InvalidBlockException){
                    showWarning("InvalidBlockException");
                }else if(e instanceof TooLowException){
                    showWarning("TooLowException");
                }

            }






        }
    }

    /**
     * Event handler for the menu bar items. Used to Load and save the world
     * maps from text files.
     */
    private class MenuBarItemHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            MenuItem menuItemPressed = (MenuItem) event.getSource();
            //If the save menubar item was pressed handle below
            if (menuItemPressed == view.getSaveMenuItem()) {
                System.out.println("Save");
                FileChooser fc = new FileChooser();
                File selectedFile = fc.showSaveDialog(primaryStage);
                //Pass the file to the appropriate save function
                try {
                    gameMap.saveMap(selectedFile.toString());
                } catch (Exception e) {
                    if (e instanceof IOException) {
                        showWarning("IOException error");
                    }
                }
                //If the Load menubar item was pressed
            } else if (menuItemPressed == view.getLoadMenuItem()) {
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
                    builderPos = gameMap.getStartPosition();
                    gameMapWithCoords =
                            generateGameMapWithCoords(gameMap.getTile(builderPos),
                            builderPos.getX(), builderPos.getY());
                    drawMap();
                } catch (Exception e) {
                    //The file was not found
                    if (e instanceof FileNotFoundException) {
                        System.out.println("File not found");
                        showWarning("File not found");
                    } else if (e instanceof WorldMapFormatException) {
                        System.out.println("WorldFormatMapException");
                        showWarning("WorldFormatMapException error occurred");
                    } else if (e instanceof WorldMapInconsistentException) {
                        System.out.println("WorldMapInconsistent");
                        showWarning("WorldMapInconsistent error occurred");
                    }else{
                        showWarning("Other error occured");
                    }
                }
            }
        }
    }

    public class ActionsComboBoxHandler implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observable,
                            String oldValue, String newValue) {
            actionType = newValue;

        }
    }
}
