/*
 *  This is an application that adds to, tracks and updates inventory.
 */
package factoryinventorysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class has the main method that starts the application.
 * @author Kevin Mock
 */
public class FactoryInventorySystem extends Application {
    /**
     * This method loads the main screen.
     * @param stage sets the stage
     * @throws Exception default exception
     */
    @Override
    public void start(Stage stage) throws Exception {
       
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/MainScreen.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    } 
}
