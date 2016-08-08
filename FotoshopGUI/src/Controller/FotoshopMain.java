/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Images.ColorImage;
import Images.EditableImage;
import Images.Filters;
import Interface.Command;
import Interface.Editor;
import Locale.Language;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;

/**
 *
 * @author James
 */
public class FotoshopMain extends Application {

    // Declare data members - graphical objects
    // Main panels
    private BorderPane root;
    private TilePane toolBar;
    private VBox filterPanel;
    private StackPane editWindow;
    private StackPane editBox;
    private StackPane messageBox;

    // Toolbar Buttons
    private Button btnQuit;
    private Button btnOpen;
    private Button btnSave;
    private Button btnLook;
    private Button btnUndo;

    // Filter Buttons
    private Button btnMono;
    private Button btnRot90;
    
    // Cache buttons
    private Button btnPut;
    private Button btnGet;

    // Messages Label
    private Label messageLabel;

    // Image view
    private ImageView imageView;

    //image
    private Image image;
    private String absolutePath;
    private String url;
    private String newUrl;
    private String filters;

    //editor object
    private final Editor editor = new Editor();

    //command object
    private Command command;

    //file chooser
    private FileChooser fc;
    
    //popup window
    private Stage popWin;

    // locale
    private final ResourceBundle messages = Language.getBundle();

    @Override
    public void start(Stage primaryStage) {
        
        //instantiate filters
        Filters filts = new Filters();
        filters = filts.returnFilterList();

        // Construct graphical features
        //root panel - border pane
        root = new BorderPane();
        root.setPadding(new Insets(20, 20, 20, 20));
        Scene scene = new Scene(root, 1800, 900);
        scene.getStylesheets().add("Controller/FotoshopStylesheet.css");

        // Construct buttons
        btnOpen = new Button("Open");
        btnOpen.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnSave = new Button("Save");
        btnSave.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnLook = new Button("Look");
        btnLook.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnUndo = new Button("Undo");
        btnUndo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnQuit = new Button("Exit Fotoshop");
        btnQuit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnMono = new Button("Mono");
        btnMono.setMaxWidth(Double.MAX_VALUE);
        btnRot90 = new Button("Rot 90");
        btnRot90.setMaxWidth(Double.MAX_VALUE);
        btnPut = new Button("Put");
        btnPut.setMaxWidth(Double.MAX_VALUE);
        btnGet = new Button("Get");
        btnGet.setMaxWidth(Double.MAX_VALUE);

        // Set button actions
        
        //exit program
        btnQuit.setOnAction(e -> {
            System.out.println(messages.getString("thank_you"));
            Platform.exit();
                    });

        //open an image from file
        btnOpen.setOnAction(e -> {
            fc = new FileChooser();
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg")
            );
            fc.setTitle("Open File");
            File file = fc.showOpenDialog(primaryStage);
            url = "";
            try {
                url = file.toURI().toURL().toString();
            } catch (MalformedURLException ex) {
                System.out.println(ex.getMessage());
            }
            absolutePath = file.getPath();
            command = new Command("open", absolutePath);
            editor.open(command);
            getImage(url);
            imageView.setImage(image);
            messageLabel.setText("Loaded " + absolutePath + "\nFilters active: " + filters);
            // fade image in
            FadeTransition ft = new FadeTransition(Duration.millis(3000), imageView);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setCycleCount(1);
            ft.setAutoReverse(false);
            ft.play();
        });

        // Retrieve information abotu current image
        btnLook.setOnAction(e -> {
            //list out current image and all the applied filters
            editor.getCurrentImage();
            filters = editor.getFilters().returnFilterList();
            if(filters == null){
                filters = messages.getString("no_filters_active");
            }
            messageLabel.setText(messages.getString("current_image") + absolutePath + "\n" + messages.getString("filters_applied") + filters);
        });

        //save current image to file
        btnSave.setOnAction(e -> {
            // make a copy of the current image and save it
            if (url != null) {
                fc = new FileChooser();
                fc.setTitle("Save Image To");
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
                File file = fc.showSaveDialog(primaryStage);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(imageView.getImage(), null), "png", file);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                newUrl = file.getPath();
                messageLabel.setText("Copy of image " + absolutePath + " saved to " + newUrl);
                url = newUrl;
                newUrl = null;
            } else {
                messageLabel.setText("Unable to save as no image open!");
            }
        });
        
        //undo button
        btnUndo.setOnAction(e -> {
            if(url != null){
                if(editor.isUndoable(filters)){
                    editor.undoLastFilter();
                    filters = editor.getFilters().returnFilterList();
                    System.out.println(filters);
                    try {
                        image = convertToImage(editor.returnCurrentImage());
                        imageView.setImage(image);
                        editor.getCurrentImage();
                        filters = editor.getFilters().returnFilterList();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    editor.getCurrentImage();
                    filters = editor.getFilters().returnFilterList();
                    messageLabel.setText("Filter rot90 undone.  Filters now cleared. " + absolutePath + "\n" + messages.getString("current_image") + absolutePath + "\n" + messages.getString("filters_applied") + filters);
                    
                }
                else{
                    editor.getCurrentImage();
                    filters = editor.getFilters().returnFilterList();
                    messageLabel.setText("No undoable filter found. " + absolutePath + "\n" + messages.getString("current_image") + absolutePath + "\n" + messages.getString("filters_applied") + filters);
                }
            }
            else {
                messageLabel.setText("Unable to undo last filter operation as no image open.");
            }
        });
        
        //mono button
        btnMono.setOnAction(e -> {
            if(url != null){
                editor.mono();
                try {
                    image = convertToImage(editor.returnCurrentImage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                imageView.setImage(image);
                filters = editor.getFilters().returnFilterList();
                if(editor.canApplyFilter(editor.returnImgName(), editor.getFilters())){
                    messageLabel.setText("Mono filter applied to image " + absolutePath + "\n" + messages.getString("current_image") + absolutePath + "\n" + messages.getString("filters_applied") + filters);
                }
                else{
                    messageLabel.setText("Filter pipeline full. Cannot currently apply further filters.\n" + messages.getString("current_image") + absolutePath + "\n" + messages.getString("filters_applied") + filters);
                }
            }
            else{
                messageLabel.setText("Unable to apply filter operation as no image open.");
            }
        });
        
        //rot90 button
        btnRot90.setOnAction(e -> {
            if(editor.canApplyFilter(editor.returnImgName(), editor.getFilters())){
                editor.rot90();
                try {
                    image = convertToImage(editor.returnCurrentImage());
                    imageView.setImage(image);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                editor.getCurrentImage();
                filters = editor.getFilters().returnFilterList();
                messageLabel.setText("Rot90 filter applied to image " + absolutePath + "\n" + messages.getString("current_image") + absolutePath + "\n" + messages.getString("filters_applied") + filters);
            }
            else if(url == null){
                messageLabel.setText("Unable to apply filter operation as no image open.");
            }
            else {
                messageLabel.setText("Filter pipeline full. Cannot currently apply further filters.\n" + messages.getString("current_image") + absolutePath + "\n" + messages.getString("filters_applied") + filters);
            }
        });
        
        // put button
        btnPut.setOnAction(e -> {
            if(editor.returnImgName() != null){
                
                editor.setEdtImg(new EditableImage(editor.returnCurrentImage(), editor.getFilters()));
                
                editor.returnCache().addToCache(absolutePath, editor.returnEdtImg());
                
                messageLabel.setText("Image " + absolutePath + " added to Cache of images.");                
            }
            else {
                messageLabel.setText("No image open.  Cannot complete cache operation.");
            }
        });        
        
        //get button
        btnGet.setOnAction(e -> {
            //accquire image url from user
            popWin = new Stage();
            popWin.setTitle("Cache");
            VBox vBox = new VBox();
            vBox.setPadding(new Insets(10,10,10,10));
            Scene popupScene = new Scene(vBox, 300, 400);
            StackPane stack = new StackPane();
            stack.setPadding(new Insets(10, 0, 0, 0));
            TilePane buttonPane = new TilePane();
            buttonPane.setAlignment(Pos.CENTER);
            buttonPane.setHgap(40);
            buttonPane.setVgap(20);
            Button btnLoad = new Button("Load");
            btnLoad.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            Button btnCancel = new Button("Cancel");
            
            btnCancel.setOnAction(s -> {popWin.close();});
            
            btnCancel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            buttonPane.getChildren().addAll(btnLoad, btnCancel);
            ListView cacheList = new ListView();
            cacheList.setEditable(true);
            cacheList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            stack.getChildren().addAll(buttonPane);
            vBox.getChildren().addAll(cacheList, stack);
            
            btnLoad.setOnAction(a -> {
                absolutePath = cacheList.getSelectionModel().getSelectedItem().toString();
                command = new Command("get", absolutePath);
                ColorImage currImage = editor.getFromCache(command).returnColImage();
                filters = editor.getFromCache(command).returnFilters().returnFilterList();
                try {
                    imageView.setImage(convertToImage(currImage));
                    absolutePath = editor.returnImgName();
                    filters = editor.returnEdtImg().returnFilters().returnFilterList();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
                popWin.close();
            });            
            
            popWin.setScene(popupScene);
            popWin.show();
            
            if(!editor.getCache().isEmpty()){
                ObservableList<String> oList = FXCollections.observableArrayList();
                oList.addAll(editor.getCache().returnCacheArray());
                System.out.println(editor.getCache().returnCacheArray());
                cacheList.setItems(oList);
                System.out.println("added items to cachlist");
            }
            else{
                System.out.println("cache was empty");
            }
            
        });        

        // add buttons to scene
        // Toolbar to hold open, look, quit, save etc commands
        toolBar = new TilePane(Orientation.HORIZONTAL);
        toolBar.getStyleClass().add("toolBar");
        toolBar.setPadding(new Insets(10, 10, 10, 10));
        toolBar.setHgap(100);
        toolBar.setVgap(10);
        toolBar.setAlignment(Pos.CENTER);

        toolBar.getChildren().addAll(btnOpen, btnSave, btnLook, btnUndo, btnQuit);

        // filters panel to hold filter buttons
        filterPanel = new VBox();
        filterPanel.getStyleClass().add("filtPanel");
        filterPanel.setSpacing(100);
        filterPanel.setPadding(new Insets(5, 10, 10, 10));
        filterPanel.setAlignment(Pos.CENTER);

        filterPanel.getChildren().addAll(btnMono, btnRot90, btnPut, btnGet);

        // Edit window to load images into
        editBox = new StackPane();
        editBox.getStyleClass().add("editBox");
        editBox.setPadding(new Insets(80, 70, 80, 80));

        editWindow = new StackPane();
        editWindow.getStyleClass().add("editWindow");
        editWindow.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        imageView = new ImageView();
        imageView.getStyleClass().add("imageView");
        imageView.setFitHeight(600);
        imageView.setFitWidth(475);

        editBox.getChildren().add(editWindow);
        editWindow.getChildren().add(imageView);

        // Message box to display text in
        messageBox = new StackPane();
        messageBox.setPadding(new Insets(10, 10, 10, 10));
        messageBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        messageBox.getStyleClass().add("messageBox");

        messageLabel = new Label();
        messageLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        messageLabel.getStyleClass().add("messageLabel");
        messageLabel.setPadding(new Insets(20, 10, 20, 10));

        messageLabel.setText((messages.getString("welcome1")) + "\n" + (messages.getString("welcome2")));

        messageBox.getChildren().add(messageLabel);

        // Add elements to root border pane
        root.setTop(toolBar);
        root.setLeft(filterPanel);
        root.setCenter(editBox);
        root.setBottom(messageBox);

        //Initialise primaryStage
        primaryStage.setTitle("Fotoshop");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Language.setLocale();
        Language.selectBundle("Locale.MessagesBundle");
        Language.setBundle();
        Editor editorInstance = new Editor();
        launch(args);
        //editorInstance.edit();
    }

    private void getImage(String localUrl) {
        image = new Image(localUrl, false);
    }

    private Image convertToImage(ColorImage colImage) throws IOException {
        File outputFile = new File("C:\\temp\\tempImg.jpg");
        try{
            ImageIO.write(colImage, "jpg", outputFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }        
        String path = outputFile.toURI().toURL().toString();       
        Image returnImage = new Image(path, false);
        return returnImage;
    }

}
