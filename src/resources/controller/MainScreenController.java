
package resources.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.text.NumberFormat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import resources.model.InHouse;
import resources.model.Outsourced;
import resources.model.Part;
import resources.model.Product;

/**
 * This class takes executes of all the actions initiated from the Main UI
 * @author Kevin Mock
 */
public class MainScreenController implements Initializable {

    /**
     * Holds the next number in the primary key sequence for parts.
     */
    static int nextNumInPartIdSequence;
    /**
     * Holds the next number in the primary key sequence for products.
     */
    static int nextNumInProductIdSequence;
    /**
     * This holds the index number of the current product being modified.
     */
    static int indexOfProductBeingModified;
    /**
     * This holds the index number of the current part being modified.
     */
    static int indexOfPartModified;
    @FXML
    private TableView<Part> mainScreenPartsTableView;
    @FXML
    private TableColumn<Part, Integer> partId;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partsInvLevel;
    @FXML
    private TableColumn<Part, Double> priceDivCostPerUnit;
    @FXML
    private JFXTextField mainScreenPartsSearchTextField;
    @FXML
    private JFXButton mainScreenPartsAddButton;
    @FXML
    private JFXButton mainScreenPartsModifyButton;
    @FXML
    private TableView<Product> mainScreenProductsTableView;
    @FXML
    private TableColumn<Product, Integer> prodId;
    @FXML
    private TableColumn<Product, String> prodName;
    @FXML
    private TableColumn<Product, Integer> prodInvLevel;
    @FXML
    private TableColumn<Product, Double> prodPricePerUnit;
    @FXML
    private JFXTextField mainScreenProductsSearchTextField;
    @FXML
    private JFXButton mainScreenProductsAddButton;
    @FXML
    private JFXButton mainScreenProductsModifyButton;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mainScreenPartsSearchTextField.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> mainScreenPartsSearchTextField.setText(""));
        mainScreenProductsSearchTextField.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> mainScreenProductsSearchTextField.setText(""));

        /* Setting the column values to the particular product properties  */
        prodId.setCellValueFactory(new PropertyValueFactory<>("prodId"));
        prodName.setCellValueFactory(new PropertyValueFactory<>("prodName"));
        prodInvLevel.setCellValueFactory(new PropertyValueFactory<>("prodStock"));
        prodPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("prodPrice"));
        NumberFormat dollarFormat = NumberFormat.getCurrencyInstance();
        prodPricePerUnit.setCellFactory(currencyFrmt -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double prodPrice, boolean noVal) {
                super.updateItem(prodPrice, noVal);
                if (noVal) {
                    setText(null);
                } else {
                    setText(dollarFormat.format(prodPrice));
                }
            }
        });

        /* Sample information for demo purposes */
        Inventory.addProduct(new Product(3, "Car", 4999.95, 8, 2, 10));
        Inventory.addProduct(new Product(1, "Sailboat", 29999.95, 5, 2, 12));
        Inventory.addProduct(new Product(2, "Computer", 1999.95, 12, 3, 20));       

        Inventory.addPart(new InHouse(3, "Carburetor", 198.00, 10, 5, 20, 1));
        Inventory.addPart(new InHouse(2, "Hubcap", 21.99, 12, 4, 48, 2));
        Inventory.addPart(new Outsourced(1, "CPU", 493.00, 3, 2, 10, "IniTech"));
        Inventory.addPart(new Outsourced(4, "8GB RAM Module", 239.99, 5, 3, 20, "IniTech"));
        Inventory.addPart(new Outsourced(5, "Sail", 349.95, 3, 2, 5, "Americas Cup Company"));
        Inventory.addPart(new Outsourced(6, "Mast", 169.95, 2, 1, 5, "Americas Cup Company"));

        /* Set all the products to the products table view */
        mainScreenProductsTableView.setItems(Inventory.getAllProducts());

        /* Setting the part table view column values to the particular part properties  */
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceDivCostPerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceDivCostPerUnit.setCellFactory(partTCell -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double prodPrice, boolean isVal) {
                super.updateItem(prodPrice, isVal);
                if (isVal) {
                    setText(null);
                } else {
                    setText(dollarFormat.format(prodPrice));
                }
            }
        });

        /* set all the parts to the parts table view */
        mainScreenPartsTableView.setItems(Inventory.getAllParts());

        /* sort all the information by id column */
        mainScreenPartsTableView.getSortOrder().add(partId);
        mainScreenProductsTableView.getSortOrder().add(prodId);

        /* Get the next number in the sequence of primary keys 
        for products and parts and set it to a class variable */
        setNewNextNumInPartPkSequence(Inventory.getAllParts());
        setNewNextNumInProductPkSequence(Inventory.getAllProducts());

    }

    /**
     * Sets the primary key number sequence based on the last element's Part ID
     * by adding one and storing it to a class variable
     *
     * @param partObsList the updated Observable List of Parts
     */
    static void setNewNextNumInPartPkSequence(ObservableList<Part> partObsList) {
        int obsListSize = partObsList.size();
        nextNumInPartIdSequence = partObsList.get(obsListSize - 1).getId() + 1;
    }

    /**
     * Sets the primary key number sequence based on the last element's Product
     * ID by adding one and storing it to a class variable
     *
     * @param prodObsList the updated Observable List of Products
     */
    static void setNewNextNumInProductPkSequence(ObservableList<Product> prodObsList) {
        int obsListSize = prodObsList.size();
        nextNumInProductIdSequence = prodObsList.get(obsListSize - 1).getProdId() + 1;
    }

    /**
     * Capitalizes the first letter of each word.
     *
     * @param wrds The word(s) that need their first letter capitalized
     * @return The string of words with the first letters of each word being
     * capital
     */
    static String capFrstLetOfWords(String wrds) {
        if (wrds == null || wrds.isEmpty()) {
            return null;
        } else {
            String[] words = wrds.split("\\s");
            StringBuilder capWord = new StringBuilder();
            for (String wrd : words) {
                String firstWrd = wrd.substring(0, 1);
                String remainingWrds = wrd.substring(1);
                capWord.append(firstWrd.toUpperCase().concat(remainingWrds + " "));

            }
            return capWord.toString().trim();
        }
    }

    /**
     * A confirmation dialog to get the users feedback to confirm the action
     * they want to execute.
     *
     * @param confirmationAction The message to the user identifying what type
     * of confirmation action is required
     * @return the feedback from the user
     */
    static boolean confirmActionAlert(String confirmationAction) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Please Confirm");

        Label label = new Label("\n"
                + "Confirm you wish to " + confirmationAction);

        label.setFont(new Font("San Sarif", 15));

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);

        alert.getDialogPane().setContent(expContent);

        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType confirm = new ButtonType("Confirm");
        alert.getButtonTypes().setAll(confirm, cancel);
        Optional<ButtonType> action = alert.showAndWait();

        return action.get() == confirm;
    }

    /**
     * This method is used by
     * {@link MainScreenController#addPart(MouseEvent)} and
     * {@link MainScreenController#modifyPart(MouseEvent)} to
     * start the window to edit and add parts to the Observable List Array of
     * Parts.<br><br>
     *
     * Uses Method:
     * <ul>
     *      <li>
     *          {@link MainScreenController#newPopUpWindowConfig(JFXButton, String, FXMLLoader)}
     *      </li>
     *      <li> 
     *          {@link MainScreenController#getFxmlLoader(String)}
     *      </li>
     * </ul>
     *
     * @param clickedJFXButton Which button was clicked, new part button or
     * modify the part button
     * @param fxmlLocationString the PATH of the FXML file
     * @param windowTitleString The title of the window and screen
     * @param methodUseIndicator The way in which the method is used activates
     * certain code withing the method to run
     */
    private void newOrModPartStageLoader(JFXButton clickedJFXButton, String fxmlLocationString, String windowTitleString, String methodUseIndicator) {

        FXMLLoader loader = getFxmlLoader(fxmlLocationString);

        PartInvScreenController modifyPartsInvDisplay = loader.getController();

        modifyPartsInvDisplay.setPartsTitleText(windowTitleString);

        if ("ModifyPart".equals(methodUseIndicator)) {
            modifyPartsInvDisplay.setAddPartsSaveBtnVisibility(false);
            modifyPartsInvDisplay.setModifyPartsSaveBtnVisibility(true);

            /* Getting the part selected from the table view */
            Part selectedPart = mainScreenPartsTableView.getSelectionModel().getSelectedItem();
            /* Now that we have the part object we find what index it 
            is in the Observable Array List and store the location */
            indexOfPartModified = Inventory.getAllParts().indexOf(selectedPart);

            /* Using the accessor methods we populate the text fields with the information from the aprt object */
            modifyPartsInvDisplay.setPartIdTextField(Integer.toString(selectedPart.getId()));
            modifyPartsInvDisplay.setPartNameTextField(selectedPart.getName());
            modifyPartsInvDisplay.setPartPriceDivCostTextField(Double.toString(selectedPart.getPrice()));
            modifyPartsInvDisplay.setPartInvTextField(Integer.toString(selectedPart.getStock()));
            modifyPartsInvDisplay.setPartMinTextField(Integer.toString(selectedPart.getMin()));
            modifyPartsInvDisplay.setPartMaxTextField(Integer.toString(selectedPart.getMax()));

            /* The two "if" statements below finds which object it is and executes the required actions to 
            populate the field, set the radio button and label the text field appropriately */
            if (selectedPart instanceof Outsourced) {
                modifyPartsInvDisplay.setPrtsOutsrcdRadBtn(true);
                modifyPartsInvDisplay.setPrtsInhouseRadBtn(false);
                modifyPartsInvDisplay.setPrtsCompNameOrMachIdLabel("Company Name");
                modifyPartsInvDisplay.setPrtsCompNameOrMachIdTxtField("Company Name");
                modifyPartsInvDisplay.setPartCompNameOrMachIdTextField(((Outsourced) selectedPart).getCompanyName());
            }

            if (selectedPart instanceof InHouse) {
                modifyPartsInvDisplay.setPrtsInhouseRadBtn(true);
                modifyPartsInvDisplay.setPrtsOutsrcdRadBtn(false);
                modifyPartsInvDisplay.setPrtsCompNameOrMachIdLabel("Machine ID");
                modifyPartsInvDisplay.setPrtsCompNameOrMachIdTxtField("Machine ID");
                modifyPartsInvDisplay.setPartCompNameOrMachIdTextField(Integer.toString(((InHouse) selectedPart).getMachineId()));
            }
        }
        if ("AddPart".equals(methodUseIndicator)) {
            modifyPartsInvDisplay.setAddPartsSaveBtnVisibility(true);
            modifyPartsInvDisplay.setModifyPartsSaveBtnVisibility(false);
        }

        /* Complete the rest of the window configuration */
        newPopUpWindowConfig(clickedJFXButton, windowTitleString, loader);
    }

    /**
     * This method is used in {@link MainScreenController#newOrModPartStageLoader(JFXButton, String, String, String)}
     * and {@link MainScreenController#newOrModProductsStageLoader(JFXButton, String, String, String)} to get the FXML loader instance
     *
     * @param fxmlLocationString the PATH of the FXML file
     * @return FXML loader instance
     */
    private FXMLLoader getFxmlLoader(String fxmlLocationString) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlLocationString));

        try {
            loader.load();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainScreenController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return loader;
    }

    /**
     * This method is used by {@link MainScreenController#addProducts(MouseEvent)} and {@link MainScreenController#modifyProducts(MouseEvent)}
     * to start the window, edit and/or add parts to the Observable List Array of Parts.<br><br>
     *
     * Uses Method:
     * <ul>
     *     <li>
     *          {@link MainScreenController#newPopUpWindowConfig(JFXButton, String, FXMLLoader)}
     *     </li>
     *     <li> 
     *          {@link MainScreenController#getFxmlLoader(String)}
     *     </li>
     * </ul>
     *
     * @param clickedJFXButton Which button was clicked, new products button or modify the product button
     * @param fxmlLocationString The PATH of the FXML file
     * @param windowTitleString The title of the window and screen
     * @param methodUseIndicator The way in which the method is used activates certain code withing the method to run
     */
    private void newOrModProductsStageLoader(JFXButton clickedJFXButton, String fxmlLocationString, String windowTitleString, String methodUseIndicator) {

        FXMLLoader loader = getFxmlLoader(fxmlLocationString);

        ProductInvScreenController modifyProductsInvDisplay = loader.getController();
        modifyProductsInvDisplay.setProductsTitleText(windowTitleString);

        /* Make sure the correct buttons are displayed */
        if ("AddProduct".equals(methodUseIndicator)) {
            modifyProductsInvDisplay.setAddProductsSaveBtnVisibility(true);
            modifyProductsInvDisplay.setModifyProductsSaveBtnVisibility(false);
            modifyProductsInvDisplay.setPartsToAddToProdTableView(Inventory.getAllParts());

            /* set the buttons add and delete parts to a new product visible */
            modifyProductsInvDisplay.getAddPartsToNewProductBtn().setVisible(true);
            modifyProductsInvDisplay.getDelPartsFromNewProductBtn().setVisible(true);

            /* set the buttons to add and delete parts to products being modified to NOT visible */
            modifyProductsInvDisplay.getAddPartsToModProductBtn().setVisible(false);
            modifyProductsInvDisplay.getDelPartsFromModProductBtn().setVisible(false);
        }

        if ("ModifyProduct".equals(methodUseIndicator)) {

            /* set the buttons to add and delete parts to NEW products to NOT visible */
            modifyProductsInvDisplay.getAddPartsToNewProductBtn().setVisible(false);
            modifyProductsInvDisplay.getDelPartsFromNewProductBtn().setVisible(false);

            /* set the buttons to add and delete parts to products being modified to visible */
            modifyProductsInvDisplay.getAddPartsToModProductBtn().setVisible(true);
            modifyProductsInvDisplay.getDelPartsFromModProductBtn().setVisible(true);

            /* Set the appropriate button to modify a product */
            modifyProductsInvDisplay.setAddProductsSaveBtnVisibility(false);
            modifyProductsInvDisplay.setModifyProductsSaveBtnVisibility(true);

            /* Get the product selected */
            Product selectedProd = mainScreenProductsTableView.getSelectionModel().getSelectedItem();
            /* Get the index of the product selected and store it */
            indexOfProductBeingModified = Inventory.getAllProducts().indexOf(selectedProd);

            /* populate the text fields */
            modifyProductsInvDisplay.setProductIdTextField(Integer.toString(selectedProd.getProdId()));
            modifyProductsInvDisplay.setProductNameTextField(selectedProd.getProdName());
            modifyProductsInvDisplay.setProductPriceDivCostTextField(Double.toString(selectedProd.getProdPrice()));
            modifyProductsInvDisplay.setProductInvTextField(Integer.toString(selectedProd.getProdStock()));
            modifyProductsInvDisplay.setProductMinTextField(Integer.toString(selectedProd.getProdMin()));
            modifyProductsInvDisplay.setProductMaxTextField(Integer.toString(selectedProd.getProdMax()));

            modifyProductsInvDisplay.setPartsToAddToProdTableView(Inventory.getAllParts());

            modifyProductsInvDisplay.setPartsAddedToProdTableView(selectedProd.getAssociatedParts());

            modifyProductsInvDisplay.setaddPartsToProdMsgLn1Label("Add part(s) below to the product");
            modifyProductsInvDisplay.setaddPartsToProdMsgLn2Label("being modified from the selection above");
        }

        newPopUpWindowConfig(clickedJFXButton, windowTitleString, loader);
    }

    /**
     * This method is used in {@link MainScreenController#newOrModProductsStageLoader(JFXButton, String, String, String)}
     * and {@link MainScreenController#newOrModPartStageLoader(JFXButton, String, String, String)}
     * to create the final configuration of the new window that adds or modifies a part or product.
     *
     * @param clickedJFXButton Which button was clicked, new or modify part or product button
     * @param windowTitleString The title of the window and screen
     * @param loader FXML loader instance
     */
    private void newPopUpWindowConfig(JFXButton clickedJFXButton, String windowTitleString, FXMLLoader loader) {
        Parent main = loader.getRoot();

        Stage mainWindow = (Stage) clickedJFXButton.getScene().getWindow();
        Stage secondaryStage = new Stage();

        secondaryStage.setTitle(windowTitleString);
        secondaryStage.setScene(new Scene(main));
        secondaryStage.setResizable(false);
        secondaryStage.initModality(Modality.WINDOW_MODAL);
        secondaryStage.initOwner(mainWindow);
        secondaryStage.show();
    }

    /**
     * This method uses {@link MainScreenController#newOrModPartStageLoader(JFXButton, String, String, String)}
     * to add a part to the Part Table View List
     *
     * @param event mouse click
     */
    @FXML
    void addPart(MouseEvent event) {
        newOrModPartStageLoader(mainScreenPartsAddButton, "/resources/fxml/PartInvScreen.fxml", "Add Part", "AddPart");
    }

    /**
     * This method uses {@link MainScreenController#newOrModPartStageLoader(JFXButton, String, String, String)}
     * to modify the part selected in the Part Table View
     *
     * @param event mouse click
     */
    @FXML
    void modifyPart(MouseEvent event) {
        if (!mainScreenPartsTableView.getSelectionModel().isEmpty()) {
            newOrModPartStageLoader(mainScreenPartsModifyButton, "/resources/fxml/PartInvScreen.fxml", "Modify Part", "ModifyPart");
        }

    }

    /**
     * Delete the part selected on the Part Table View
     *
     * @param event mouse click
     */
    @FXML
    void deletePart(MouseEvent event) {
        if (!mainScreenPartsTableView.getSelectionModel().isEmpty()) {
            Part selectedPart = mainScreenPartsTableView.getSelectionModel().getSelectedItem();
            if (confirmActionAlert("delete the selected part " + selectedPart.getName())) {
                Inventory.deletePart(selectedPart);
            }
            setNewNextNumInPartPkSequence(Inventory.getAllParts());
        }
    }

    /**
     * This method calls {@link MainScreenController#newOrModProductsStageLoader(JFXButton, String, String, String)}
     * to add a product to the Products Table View
     *
     * @param event mouse click
     */
    @FXML
    void addProducts(MouseEvent event) {
        newOrModProductsStageLoader(mainScreenProductsAddButton, "/resources/fxml/ProductInvScreen.fxml", "Add Product", "AddProduct");

    }

    /**
     * This method calls {@link MainScreenController#newOrModProductsStageLoader(JFXButton, String, String, String)}
     * to modify the product that was selected in the Products Table View
     *
     * @param event mouse click
     */
    @FXML
    void modifyProducts(MouseEvent event) {
        if (!mainScreenProductsTableView.getSelectionModel().isEmpty()) {
            newOrModProductsStageLoader(mainScreenProductsModifyButton, "/resources/fxml/ProductInvScreen.fxml", "Modify Product", "ModifyProduct");
        }

    }

    /**
     * This deletes the product selected in the Product Table View <br><br>
     *
     * Uses Method:
     * <ul>
     *      <li>
     *          {@link MainScreenController#setNewNextNumInProductPkSequence(ObservableList)}
     *      </li>
     * </ul>
     *
     * @param event mouse click
     */
    @FXML
    void deleteProducts(MouseEvent event) {
        if (!mainScreenProductsTableView.getSelectionModel().isEmpty()) {
            Product selectedProduct = mainScreenProductsTableView.getSelectionModel().getSelectedItem();
            if (confirmActionAlert("delete the selected product " + selectedProduct.getProdName())) {
                Inventory.deleteProduct(selectedProduct);
            }
            setNewNextNumInProductPkSequence(Inventory.getAllProducts());
        }
    }

    /**
     * This method exits the entire application when clicked
     *
     * @param event mouse click
     */
    @FXML
    void exitApp(MouseEvent event) {
        Platform.exit();
    }

    /**
     * This method looks to see what kind of search is needed by the content in
     * the text field. If numbers are detected in the text field then it looks
     * for the part associated with the part id and displays it in the Table
     * View. If characters are detected then it looks through the names of the
     * products and displays the products associated with the name.<br><br>
     *
     * Uses methods:
     * <ul>
     *      <li>
     *          {@link Inventory#getAllParts()}
     *      </li>
     *      <li>
     *          {@link MainScreenController#capFrstLetOfWords(String)}
     *      </li>
     *      <li>
     *          {@link Inventory#lookupPart(int)}
     *      </li>
     *      <li>
     *          {@link Inventory#lookupPart(String)}
     *      </li>
     * </ul>
     *
     * @param event
     */
    @FXML
    void partsSearch(MouseEvent event) {

        String partSearchText = mainScreenPartsSearchTextField.getText();
        String upprcaseFrstLetrOfWrds = capFrstLetOfWords(partSearchText);
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();

        if (partSearchText.isEmpty()) {
            mainScreenPartsTableView.setItems(Inventory.getAllParts());
        } else if (partSearchText.matches("[0-9]+")) {
            int seachedPartById = Integer.parseInt(partSearchText);
            filteredParts.add(Inventory.lookupPart(seachedPartById));
            mainScreenPartsTableView.setItems(filteredParts);
        } else {
            mainScreenPartsTableView.setItems(Inventory.lookupPart(upprcaseFrstLetrOfWrds));
        }
    }

    /**
     * This method looks to see what kind of search is needed by the content in
     * the text field. If numbers are detected in the text field then it looks
     * for the product id and displays the product associated with the product
     * id. If characters are detected then it looks through the names of the
     * products and displays all products associated with the search.<br><br>
     *
     * Uses methods:
     * <ul>
     *      <li>
     *          {@link Inventory#getAllProducts()}
     *      </li>
     *      <li>
     *          {@link MainScreenController#capFrstLetOfWords(String)}
     *      </li>
     *      <li>
     *          {@link Inventory#lookupProduct(int)}
     *      </li>
     *      <li>
     *          {@link Inventory#lookupProduct(String)}
     *      </li>     
     * </ul>
     *
     * @param event
     */
    @FXML
    void productsSearch(MouseEvent event) {
        String productSearchText = mainScreenProductsSearchTextField.getText();
        String upprcaseFrstLetrOfWrds = capFrstLetOfWords(productSearchText);
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

        if (productSearchText.isEmpty()) {
            mainScreenProductsTableView.setItems(Inventory.getAllProducts());
        } else if (productSearchText.matches("[0-9]+")) {
            int searchProductsById = Integer.parseInt(productSearchText);
            filteredProducts.add(Inventory.lookupProduct(searchProductsById));
            mainScreenProductsTableView.setItems(filteredProducts);
        } else {
            mainScreenProductsTableView.setItems(Inventory.lookupProduct(upprcaseFrstLetrOfWrds));
        }
    }

}
