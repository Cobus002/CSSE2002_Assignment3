package csse2002.block.world;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * The View class for the Canvas Example
 * <p>
 * This class creates the GUI view and has methods which can update to the
 * created view
 */
public class View {

    /* The root node of the scene graph, to add all the GUI elements to */
    private VBox rootBox;
    private Label inventoryLabel;
    private GridPane worldMap;
    //Preferred dimensions of the map view
    private static final int MAP_WIDTH = 600;
    private static final int MAP_HEIGHT = 600;
    //Text used for the buttons
    private static final String NORTH_MSG = "north";
    private static final String SOUTH_MSG = "south";
    private static final String EAST_MSG = "east";
    private static final String WEST_MSG = "west";
    //Dig and Drop Messages
    private static final String DIG_MSG = "dig";
    private static final String DROP_MSG = "drop";
    //Action messages for the combobox
    private static final String BUILDER_ACTION_MSG = "builder";
    private static final String BLOCK_ACTION_MSG = "block";
    //Specific index of each button in the buttons array
    private static final int NORTH_BTN_INDEX = 0;
    private static final int SOUTH_BTN_INDEX = 2;
    private static final int EAST_BTN_INDEX = 1;
    private static final int WEST_BTN_INDEX = 3;
    private static final int DIG_BTN_INDEX = 4;
    private static final int DROP_BTN_INDEX = 5;
    //Specific index of the menu items in the file menu
    private static final int LOAD_MENU_ITEM_INDEX = 0;
    private static final int SAVE_MENU_ITEM_INDEX = 1;

    private static final int BLOCK_WIDTH = 50;
    private static final int BLOCK_HEIGHT = 50;
    //Array to store all the game buttons
    private Button[] buttons;
    //Array to store all the menu items
    private MenuItem[] menuItems;
    //Combobox
    ComboBox actionSelectionBox;
    //TextField for the index selection
    TextField indexSelectionTextField;

    /*
     * text field to enter the x and y coord and previous pressed button/coords
     */
    private TextField xInput, yInput;

    /**
     * Constructor
     */
    public View() {
        rootBox = new VBox();
        addComponents();
    }

    /**
     * Get the Scene of the GUI with the scene graph
     *
     * @return the current scene
     */
    public Scene getScene() {

        return new Scene(rootBox);
    }

    /**
     * Get the number of buttons in the GUI
     *
     * @return the number of buttons
     */
    public int numberOfButtons() {
        return buttons.length;
    }

    /**
     * Get the buttons array
     */
    public Button[] getButtons() {

        return this.buttons;
    }

    /**
     * Get the north button
     */
    public Button getNorthButton() {
        return this.buttons[NORTH_BTN_INDEX];
    }

    /**
     * Get the east button
     */
    public Button getEastButton() {
        return this.buttons[EAST_BTN_INDEX];
    }

    /**
     * Get the south button
     */
    public Button getSouthButton() {
        return this.buttons[SOUTH_BTN_INDEX];
    }

    /**
     * Get the west button
     */
    public Button getWestButton() {
        return this.buttons[WEST_BTN_INDEX];
    }

    public String getActionComboText() {
        return this.actionSelectionBox.getId();
    }

    /**
     * Get the dig button instance
     *
     * @return
     */
    public Button getDigButton() {
        return this.buttons[DIG_BTN_INDEX];
    }

    /**
     * Get the drop button instance
     *
     * @return
     */
    public Button getDropButton() {
        return this.buttons[DROP_BTN_INDEX];
    }

    /**
     * Set the handler for the buttons
     */
    public String getIndexTextFieldText() {
        return this.indexSelectionTextField.getText();
    }

    public void setInventoryLabel(String text) {
        this.inventoryLabel.setText(text);
    }

    public void addButtonHandler(EventHandler<ActionEvent> eventHandler) {
        for (Button currButton : buttons) {
            currButton.setOnAction(eventHandler);
        }
    }

    /**
     * Add the handler for the actionsCombobox
     *
     * @param actionsComboBoxHandler
     */
    public void addActionComboHandler(
            Controller.ActionsComboBoxHandler actionsComboBoxHandler) {
        this.actionSelectionBox.valueProperty().
                addListener(actionsComboBoxHandler);
    }

    public MenuItem getLoadMenuItem() {
        return this.menuItems[LOAD_MENU_ITEM_INDEX];
    }

    /**
     * Get the dave menu item. Can be used to check what event caused event
     * handler to trigger
     *
     * @return
     */
    public MenuItem getSaveMenuItem() {
        return this.menuItems[SAVE_MENU_ITEM_INDEX];
    }

    /**
     * Add menu item handlers
     */
    public void addMenuItemHandlers(EventHandler eventHandler) {
        for (MenuItem currItem : menuItems) {
            currItem.setOnAction(eventHandler);
        }
    }


    /**
     * Adds all the GUI elements to the root layout
     * <p>
     * These is where the scene graph is created
     */
    private void addComponents() {
        VBox leftBox = new VBox();

        /* Add padding, colour to the left side */
        leftBox.setPadding(new Insets(10, 10, 10, 10));
        leftBox.setStyle("-fx-background-color: white");

        /* add all the leftside components to this leftBox */
        addLeftSideComponents(leftBox);

        /* Another layout node for the left side of the GUI */
        VBox rightBox = new VBox();

        /* add colour and padding to the right layout */
        rightBox.setSpacing(10);
        rightBox.setPadding(new Insets(10, 10, 10, 10));
        rightBox.setStyle("-fx-background-color: #336699");

        /* add all the right side components to this rightBox */
        addRightSideComponents(rightBox);

        /* add both layouts to the root HBox layout */
        rootBox = new VBox();
        HBox innerView = new HBox();

        //Add a menu
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        menuBar.getMenus().add(file);

        MenuItem load = new MenuItem("Load");
        MenuItem save = new MenuItem("Save");

        //Set the menu items in the menu items array
        menuItems = new MenuItem[2];
        menuItems[0] = load;
        menuItems[1] = save;
        //Add the menu items to the menu
        file.getItems().addAll(load, save);

        innerView.getChildren().addAll(leftBox, rightBox);
        rootBox.getChildren().addAll(menuBar, innerView);

    }

    /**
     * Add all the Gui elements to the left container
     *
     * @param box the container to add the elements to
     */
    private void addLeftSideComponents(VBox box) {

        /*
         * This is where all the components which are on the left side such as
         * Canvas, TextFields and Labels will be added
         */

        /*
         * add the canvas inside a HBox the HBox (canvasContainer) is used so
         * that border can be added around the canvas
         */
        HBox mapContainer = new HBox();
        worldMap = new GridPane();
        worldMap.setPrefSize(MAP_WIDTH, MAP_HEIGHT);
        worldMap.setGridLinesVisible(true);
        mapContainer.getChildren().add(worldMap);
        worldMap.setStyle("-fx-border-color: black");



        /* Create another HBox and add textInputs and Labels inside it */
        VBox inventoryBox = new VBox();
        inventoryBox.setPadding(new Insets(10, 10, 10,
                10));
        inventoryBox.setSpacing(15);
        inventoryLabel = new Label("empty");
        inventoryBox.getChildren().addAll(new Label("Builder Inventory"),
                inventoryLabel);

        box.getChildren().addAll(mapContainer, inventoryBox);

    }

    /**
     * Add all the Gui elements to the right container
     *
     * @param box the container to add the elements to
     */
    private void addRightSideComponents(VBox box) {

        /* Add some text to indicate what buttons are for */
        Text drawText = new Text("Actions");
        drawText.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
        drawText.setFill(Color.WHITE);
        box.getChildren().add(drawText);
        BorderPane buttonLayout = new BorderPane();
        buttons = new Button[6]; // loop over this and add new Button to the
        int buttonCount = 0;
        for (int i = 0; i < 6; i++) {
            switch (buttonCount) {
                case 0:
                    buttons[buttonCount] = new Button(NORTH_MSG);
                    break;
                case 1:
                    buttons[buttonCount] = new Button(EAST_MSG);
                    break;
                case 2:
                    buttons[buttonCount] = new Button(SOUTH_MSG);
                    break;
                case 3:
                    buttons[buttonCount] = new Button(WEST_MSG);
                    break;
                case 4:
                    buttons[buttonCount] = new Button(DIG_MSG);
                    break;
                case 5:
                    buttons[buttonCount] = new Button(DROP_MSG);
                    break;
            }
            buttonCount++;
        }

        //Add all the buttons
        buttonLayout.setTop(buttons[0]);
        buttonLayout.setRight(buttons[1]);
        buttonLayout.setBottom(buttons[2]);
        buttonLayout.setLeft(buttons[3]);
        //Center all the buttons
        buttonLayout.setAlignment(buttons[0], Pos.CENTER);
        buttonLayout.setAlignment(buttons[1], Pos.CENTER);
        buttonLayout.setAlignment(buttons[2], Pos.CENTER);
        buttonLayout.setAlignment(buttons[3], Pos.CENTER);
        buttonLayout.setMargin(buttons[0], new Insets(10, 10,
                10, 10));
        buttonLayout.setMargin(buttons[1], new Insets(10, 10,
                10, 10));
        buttonLayout.setMargin(buttons[2], new Insets(10, 10,
                10, 10));
        buttonLayout.setMargin(buttons[3], new Insets(10, 10,
                10, 10));
        buttons[0].setPrefWidth(80);
        buttons[1].setPrefWidth(80);
        buttons[2].setPrefWidth(80);
        buttons[3].setPrefWidth(80);
        buttonLayout.setPadding(new Insets(10, 10, 10,
                10));
        //Add the button layout to the box
        box.getChildren().add(buttonLayout);
        //Add the combobox and the dig and drop buttons to the right
        VBox buttonLayout2 = new VBox();
        buttonLayout2.setAlignment(Pos.CENTER);
        actionSelectionBox = new ComboBox();
        actionSelectionBox.getItems().addAll(BUILDER_ACTION_MSG,
                BLOCK_ACTION_MSG);
        actionSelectionBox.setPrefWidth(80);
        buttonLayout.setCenter(actionSelectionBox);
        //Layout to store the Drop button
        HBox buttonLayout3 = new HBox();
        buttonLayout3.setPadding(new Insets(10, 10, 10,
                10));
        buttonLayout3.setSpacing(15);
        HBox buttonLayout4 = new HBox();
        buttonLayout4.setAlignment(Pos.CENTER_LEFT);
        buttonLayout4.setPadding(new Insets(10, 10, 10,
                10));
        buttonLayout4.getChildren().add(buttons[DIG_BTN_INDEX]);
        indexSelectionTextField = new TextField();
        indexSelectionTextField.setPrefWidth(80);
        buttonLayout3.getChildren().addAll(buttons[DROP_BTN_INDEX],
                indexSelectionTextField);

        buttonLayout2.getChildren().addAll(buttonLayout4,
                buttonLayout3);

        buttons[4].setPrefWidth(80);
        buttons[5].setPrefWidth(80);
        //Add the inventory selection stuff to right side box
        box.getChildren().add(buttonLayout2);
    }

    /**
     * addRightArrowToGroup draws a right arrow on top of a group
     *
     * @param group
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void addRightArrowToGroup(Group group, int startX, int startY,
                                      int endX, int endY) {
        //Create the lines
        Line line1 = new Line(startX, startY, endX, endY);
        Line line2 = new Line(startX + 5, startY + 10, endX, endY);
        Line line3 = new Line(startX + 5, startY - 10, endX, endY);

        line1.setStroke(Color.WHITE);
        line2.setStroke(Color.WHITE);
        line3.setStroke(Color.WHITE);

        group.getChildren().addAll(line1, line2, line3);
    }

    /**
     * addLeftArrowToGroup function is used to add a left arrow to a Group
     * object. Used to indicate valid exit east bound
     *
     * @param group
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void addLeftArrowToGroup(Group group, int startX, int startY,
                                     int endX, int endY) {
        //Create the lines
        Line line1 = new Line(startX, startY, endX, endY);
        Line line2 = new Line(startX - 5, startY + 10, endX, endY);
        Line line3 = new Line(startX - 5, startY - 10, endX, endY);
        line1.setStroke(Color.WHITE);
        line2.setStroke(Color.WHITE);
        line3.setStroke(Color.WHITE);
        group.getChildren().addAll(line1, line2, line3);
    }

    /**
     * addUpArrowToGroup function is used to add up arrow to a Group object
     * to indicate that there is a valid exit north bound.
     *
     * @param group
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void addUpArrowToGroup(Group group, int startX, int startY,
                                   int endX, int endY) {
        //Create the lines
        Line line1 = new Line(startX, startY, endX, endY);
        Line line2 = new Line(startX - 10, startY - 5, endX, endY);
        Line line3 = new Line(startX + 10, startY - 5, endX, endY);
        line1.setStroke(Color.WHITE);
        line2.setStroke(Color.WHITE);
        line3.setStroke(Color.WHITE);
        group.getChildren().addAll(line1, line2, line3);
    }

    /**
     * addDownArrowToGroup function is used to add a down arrow to a Group
     * object that is passed to it. Used to indicate valid exit south bound.
     *
     * @param group
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void addDownArrowToGroup(Group group, int startX, int startY,
                                     int endX, int endY) {
        //Create the lines
        Line line1 = new Line(startX, startY, endX, endY);
        Line line2 = new Line(startX - 10, startY + 5, endX, endY);
        Line line3 = new Line(startX + 10, startY + 5, endX, endY);
        line1.setStroke(Color.WHITE);
        line2.setStroke(Color.WHITE);
        line3.setStroke(Color.WHITE);
        group.getChildren().addAll(line1, line2, line3);
    }

    /**
     * addTextToGroup function is used to add text to a Group object that is
     * passed to the function.
     *
     * @param group
     * @param text
     * @param x
     * @param y
     */
    private void addTextToGroup(Group group, String text, int x, int y) {
        Text textLabel = new Text(x - 5, y + 5, text);
        textLabel.setStroke(Color.BLUEVIOLET);
        group.getChildren().add(textLabel);

    }


    /**
     * addRectangleToGroup function is used to add a rectangle to a Group
     * object that is passed in.
     *
     * @param group
     * @param colour
     */
    private void addRectangleToGroup(Group group, String colour) {

        Color rectColour = null;

        switch (colour) {
            case "green":
                rectColour = Color.GREEN;
                break;
            case "brown":
                rectColour = Color.BROWN;
                break;
            case "black":
                rectColour = Color.BLACK;
                break;
            case "gray":
                rectColour = Color.GRAY;
                break;
            case "white":
                rectColour = Color.WHITE;
        }

        Rectangle rect = new Rectangle();
        rect.setWidth(BLOCK_WIDTH);
        rect.setHeight(BLOCK_HEIGHT);
        rect.setFill(rectColour);
        group.getChildren().add(rect);
    }

    /**
     * Add a circle to the group at position
     *
     * @param group
     * @param x
     * @param y
     */
    private void addCircleToGroup(Group group, int x, int y) {
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(5);
        circle.setFill(Color.YELLOW);
        group.getChildren().add(circle);
    }

    /**
     * Draw the tile on the map view using position, tile and builder
     *
     * @param pos
     * @param tile
     * @throws TooLowException
     */
    public void drawTileOnMap(Position pos, Tile tile, int builderX,
                              int builderY) throws TooLowException {
        Group blockGroup = new Group();
        try {
            Block topBlock = tile.getTopBlock();
            //Add the coloured rectangle for the block
            addRectangleToGroup(blockGroup, topBlock.getColour());
        } catch (TooLowException e) {
            //The tile is empty
            addRectangleToGroup(blockGroup, "white");

        }

        Map<String, Tile> exitsMap = tile.getExits();
        Iterator it = exitsMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String exitDir = (String) pair.getKey();
            switch (exitDir) {
                case "north":
                    addUpArrowToGroup(blockGroup, 25, 10,
                            25, 0);
                    break;
                case "south":
                    addDownArrowToGroup(blockGroup, 25, 40,
                            25, 50);
                    break;
                case "east":
                    addRightArrowToGroup(blockGroup, 40, 25,
                            50, 25);
                    break;
                case "west":
                    addLeftArrowToGroup(blockGroup, 10, 25,
                            0, 25);
                    break;
            }
        }
        if ((builderX == pos.getX()) && (builderY == pos.getY())) {
            //Add the builder as a circle to the tile
            addCircleToGroup(blockGroup, 40, 10);
        }
        addTextToGroup(blockGroup, Integer.toString(tile.getBlocks().size()),
                25, 25);
        worldMap.add(blockGroup, pos.getX() * BLOCK_WIDTH,
                pos.getY() * BLOCK_HEIGHT);

    }


    /**
     * Reset all the tiles back to
     */
    public void resetMapView() {
        int rows = 9;
        worldMap.getChildren().clear();
        for (int i = 0; i < rows; i++) {
            Group emptyGroup = new Group();
            addRectangleToGroup(emptyGroup, "white");
            worldMap.add(emptyGroup, i * BLOCK_WIDTH,
                    i * BLOCK_HEIGHT);

        }
    }
}
