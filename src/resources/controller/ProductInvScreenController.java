
package resources.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import resources.model.Part;
import resources.model.Product;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

/**
 * This class is the controller of the "Add Product" and "Modify Product" UI.
 *
 * @author Kevin Mock
 */
public class ProductInvScreenController implements Initializable {

    @FXML
    private Label productScreenTitle;

    @FXML
    private JFXTextField productIdTextField;

    @FXML
    private JFXTextField productNameTextField;

    @FXML
    private JFXTextField productInvTextField;

    @FXML
    private JFXTextField productPriceDivCostTextField;

    @FXML
    private JFXTextField productMaxTextField;

    @FXML
    private JFXTextField assocPartToProdSearchTextField;

    @FXML
    private JFXTextField productMinTextField;

    @FXML
    private TableView<Part> partsToAddToProdTableView;

    @FXML
    private TableColumn<Part, Integer> partsToAddToProdId;

    @FXML
    private TableColumn<Part, String> partsToAddToProdName;

    @FXML
    private TableColumn<Part, Integer> partsToAddToProdInvLevel;

    @FXML
    private TableColumn<Part, Double> partsToAddToProdPricePerUnit;

    @FXML
    private TableView<Part> partsAddedToProdTableView;

    @FXML
    private TableColumn<Part, Integer> partsAddedToProdId;

    @FXML
    private TableColumn<Part, String> partsAddedToProdName;

    @FXML
    private TableColumn<Part, Integer> partsAddedToProdInvLevel;

    @FXML
    private TableColumn<Part, Double> partsAddedToProdPricePerUnit;

    @FXML
    private JFXButton prodCxlBtn;

    @FXML
    private JFXButton prodSaveBtn;

    @FXML
    private JFXButton prodModSaveBtn;

    @FXML
    private Label maxTextFieldErrorMessageLn1;

    @FXML
    private Label maxTextFieldErrorMessageLn2;

    @FXML
    private Label addPartsToProdMsgLn1;

    @FXML
    private Label addPartsToProdMsgLn2;

    @FXML
    private JFXButton addPartsToNewProductBtn;

    @FXML
    private JFXButton delPartsFromNewProductBtn;

    @FXML
    private JFXButton delPartsFromModProductBtn;

    @FXML
    private JFXButton addPartsToModProductBtn;


    /**
     * When the "Add" or "Change" Products UI is loaded this method sets the particular "Part" object properties to the
     * columns and displays all the observable list objects to the table view.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        NumberFormat dollarFormat = NumberFormat.getCurrencyInstance();

        /* These are the Table View columns of the parts available to add to the current product being modified. 
        Each column is assigned to a specific property in the parts class*/
        partsToAddToProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsToAddToProdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsToAddToProdInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsToAddToProdPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsToAddToProdPricePerUnit.setCellFactory(partTCell -> new TableCell<Part, Double>() {
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

        /* These are the Table View columns of the parts added to the current product being modified. 
        Each column is assigned to a specific property in the parts class*/
        partsAddedToProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsAddedToProdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsAddedToProdInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));

        partsAddedToProdPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsAddedToProdPricePerUnit.setCellFactory(partTCell -> new TableCell<Part, Double>() {
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
    }

    /**
     * This method is called in {@link ProductInvScreenController#saveNewProd(MouseEvent)} and
     * {@link ProductInvScreenController#saveModProd(MouseEvent)} to update or add products to the list of products.
     * Certain parts of this method are used depending on the task required.<br><br>
     *
     * Uses Methods:
     * <ul>
     *      <li>
     *          {@link Inventory#addProduct(Product)}
     *      </li>
     *      <li>
     *          {@link Product#setAssociatedParts(ObservableList)}
     *      </li>
     *      <li>
     *          {@link Inventory#updateProduct(int, Product)}
     *      </li>
     *      <li>
     *          {@link MainScreenController#setNewNextNumInProductPkSequence(ObservableList)}
     *      </li>
     *      <li>
     *          {@link MainScreenController#capFrstLetOfWords(String)}
     *      </li>     
     * </ul>
     *
     * @param methodUseIndicator Indicates to the method to run the code that is only necessary for the task required.
     * @param btn                Which button is being clicked, add or modify.
     */
    private void addOrModProduct(String methodUseIndicator, JFXButton btn) {
        
        int maxNum = Integer.parseInt(productMaxTextField.getText());
        int minNum = Integer.parseInt(productMinTextField.getText());

        /* Making sure the maximum number of parts is larger than the minimum number */
        if (maxNum < minNum) {
            maxTextFieldErrorMessageLn1.setText("Maximum inventory cannot");
            maxTextFieldErrorMessageLn2.setText("be smaller than minimum");
        } else {
            int partId = 0;

            if ("AddProduct".equals(methodUseIndicator)) {
                // Primary key based on the last part id used in the list of products
                partId = MainScreenController.nextNumInProductIdSequence;
            }
            /* Use the same primary key number when modifying a part */
            if ("ModifyProduct".equals(methodUseIndicator)) {
                partId = Integer.parseInt(productIdTextField.getText());
            }

            /* Get the changes, if any, and store them in a varaible with the correct type */
            String prodName = MainScreenController.capFrstLetOfWords(productNameTextField.getText());
            double prodPriceDivCost = Double.parseDouble(productPriceDivCostTextField.getText());
            int prodNumPartsInv = Integer.parseInt(productInvTextField.getText());
            int prodMinInv = Integer.parseInt(productMinTextField.getText());
            int prodMaxInv = Integer.parseInt(productMaxTextField.getText());

            /* The current product being modified from the Observable List of products */
            Product currProd = Inventory.getAllProducts().get(MainScreenController.indexOfProductBeingModified);

            /* If a product is being added, store all the values of the variables in the new instance of the product object */
            if ("AddProduct".equals(methodUseIndicator)) {
                /* Get, of what will be, the index of the new product */
                int newIndexOfAddedProduct = Inventory.getAllProducts().size();
                /* Add the product */
                Inventory.addProduct(new Product(partId, prodName, prodPriceDivCost, prodNumPartsInv, prodMinInv, prodMaxInv));
                /* Get the instance of the new Product object */
                Product newProduct = Inventory.getAllProducts().get(newIndexOfAddedProduct);
                /* Add all the associated parts to the Observable Array List 
                property of the newly added product object instance */
                newProduct.setAssociatedParts(partsAddedToProdTableView.getItems());
            }

            /* Modifying the same instance of the current product */
            if ("ModifyProduct".equals(methodUseIndicator)) {
                if (!(prodName.equals(currProd.getProdName()))) {
                    currProd.setProdName(prodName);
                }
                if (prodPriceDivCost != currProd.getProdPrice()) {
                    currProd.setProdPrice(prodPriceDivCost);
                }
                if (prodNumPartsInv != currProd.getProdStock()) {
                    currProd.setProdStock(prodNumPartsInv);
                }
                if (prodMinInv != currProd.getProdMin()) {
                    currProd.setProdMin(prodMinInv);
                }
                if (prodMaxInv != currProd.getProdMax()) {
                    currProd.setProdMax(prodMaxInv);
                }
                Inventory.updateProduct(MainScreenController.indexOfProductBeingModified, currProd);
            }

            /* Assign the next number that will be used for the next new product to the main UI class variable */
            if ("AddProduct".equals(methodUseIndicator)) {
                MainScreenController.setNewNextNumInProductPkSequence(Inventory.getAllProducts());
            }

            /* Go back to the main screen */
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * This method is called when the addPartToNewProd button is clicked. It adds a part to the associated parts table view.
     * @param event
     */
    @FXML
    void addPartToNewProduct(MouseEvent event) {
        if (!partsToAddToProdTableView.getSelectionModel().isEmpty()) {
            Part selectedPart = partsToAddToProdTableView.getSelectionModel().getSelectedItem();
            partsAddedToProdTableView.getItems().add(selectedPart);
        }
    }

    /**
     * This method is called when the delPartFromNewProd button is clicked. It removes a part from the associated parts table view.
     * @param event
     */
    @FXML
    void delPartFromNewProduct(MouseEvent event) {
        if (!partsToAddToProdTableView.getSelectionModel().isEmpty()) {
            Part selectedPart = partsAddedToProdTableView.getSelectionModel().getSelectedItem();
            partsAddedToProdTableView.getItems().remove(selectedPart);
        }
    }

    /**
     * When the "Add" button is clicked this method adds the part selected, to the associated parts of the product being
     * modified, by the user on the top table view of products window. The user will see the parts they associate with the
     * product in the bottom table view of the products window.<br><br>
     * 
     *  Uses Methods:
     * <ul>
     *      <li>
     *          {@link Product#addAssociatedPart(Part)}
     *      </li>
     * </ul>
     * 
     * @param event mouse click
     */
    @FXML
    void  addPartToModifiedProd(MouseEvent event) {

        if (!partsToAddToProdTableView.getSelectionModel().isEmpty()) {
            Product currProd = Inventory.getAllProducts().get(MainScreenController.indexOfProductBeingModified);
            Part selectedPart = partsToAddToProdTableView.getSelectionModel().getSelectedItem();

            currProd.addAssociatedPart(selectedPart);
            partsAddedToProdTableView.setItems(currProd.getAssociatedParts());
        }
    }

    /**
     * Cancels the window used to add and modify products.<br><br>
     *
     * Uses Method:
     * <ul>
     *     <li>
     *         {@link MainScreenController#confirmActionAlert(String)}
     *     </li>
     * </ul>
     *
     * @param event mouse click
     */
    @FXML
    void cancelWindow(MouseEvent event) {
        if (MainScreenController.confirmActionAlert("cancel from adding or modifying a product")) {
            Stage stage = (Stage) prodCxlBtn.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * When the "Delete" button is clicked, this method deletes the part selected from the associated parts of
     * the product.<br><br>
     * 
     * Uses Method:
     * <ul>
     *     <li>
     *         {@link Product#deleteAssociatedPart(Part)}
     *     </li>
     *      <li>
     *         {@link MainScreenController#confirmActionAlert(String)}
     *     </li> 
     * </ul>
     *
     * @param event mouse click
     */
    @FXML
    void delPartFromModifiedProd(MouseEvent event) {
        if (!partsAddedToProdTableView.getSelectionModel().isEmpty()) {
            if (MainScreenController.confirmActionAlert("delete this part from the product being modified")) {
                Product currentProduct = Inventory.getAllProducts().get(MainScreenController.indexOfProductBeingModified);
                Part selectedPart = partsAddedToProdTableView.getSelectionModel().getSelectedItem();
                if (currentProduct.getAssociatedParts().size() > 1) {
                    currentProduct.deleteAssociatedPart(selectedPart);
                    partsAddedToProdTableView.setItems(currentProduct.getAssociatedParts());
                }
                else {
                    currentProduct.getAssociatedParts().clear();
                    partsAddedToProdTableView.setItems(currentProduct.getAssociatedParts());
                }
            }
        }

    }

    /**
     * Calls {@link ProductInvScreenController#addOrModProduct(String, JFXButton)} to add a new product to the list
     * of products on the click of the save button.<br><br>
     * 
     * Uses Method:<br>
     * <ul>
     *     <li>
     *         {@link ProductInvScreenController#addOrModProduct(String, JFXButton)}
     *     </li>
     * </ul>
     *
     * @param event mouse click
     */
    @FXML
    void saveNewProd(MouseEvent event) {
        String methodUseIndicator = "AddProduct";
        addOrModProduct(methodUseIndicator, prodSaveBtn);
    }

    /**
     * This method calls {@link ProductInvScreenController#addOrModProduct(String, JFXButton)} to modify the product selected.<br><br>
     * 
     * Uses Method:<br>
     * <ul>
     *     <li>
     *         {@link ProductInvScreenController#addOrModProduct(String, JFXButton)}
     *     </li>
     * </ul>
     *
     * @param event mouse click
     */
    @FXML
    void saveModProd(MouseEvent event) {
        String methodUseIndicator = "ModifyProduct";
        addOrModProduct(methodUseIndicator, prodModSaveBtn);
    }

    /**
     * This method looks to see what kind of search is needed by the content in the text field. If numbers are detected in
     * the text field then it looks for the part id and returns the Part. If characters are detected then it looks through the
     * names of the products and returns all products equal to the search<br><br>
     * 
     * Uses Method:<br>
     * <ul>
     *     <li>
     *         {@link Inventory#lookupProduct(int)}
     *     </li>
     *     <li>
     *         {@link Inventory#lookupProduct(String)} 
     *     </li>
     *     <li>
     *         {@link MainScreenController#capFrstLetOfWords(String)}
     *     </li>
     * </ul>
     * @param event mouse click
     */
    @FXML
    void productsSearch(MouseEvent event) {
        String partSearchText = assocPartToProdSearchTextField.getText();
        String upprcaseFrstLetrOfSrchWrds = MainScreenController.capFrstLetOfWords(partSearchText);
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();

        if (partSearchText.isEmpty()) {
            partsToAddToProdTableView.setItems(Inventory.getAllParts());
        } else if (partSearchText.matches("[0-9]+")) {
            int seachedPartById = Integer.parseInt(partSearchText);
            filteredParts.add(Inventory.lookupPart(seachedPartById));
            partsToAddToProdTableView.setItems(filteredParts);
        } else {
            partsToAddToProdTableView.setItems(Inventory.lookupPart(upprcaseFrstLetrOfSrchWrds));
        }
    }

    void setAddProductsSaveBtnVisibility(boolean bool) {
        this.prodSaveBtn.setVisible(bool);
    }

    void setModifyProductsSaveBtnVisibility(boolean bool) {
        this.prodModSaveBtn.setVisible(bool);
    }

    void setProductsTitleText(String title) {
        this.productScreenTitle.setText(title);
    }

    void setProductIdTextField(String title) {
        this.productIdTextField.setText(title);
    }

    void setProductNameTextField(String title) {
        this.productNameTextField.setText(title);
    }

    void setProductInvTextField(String title) {
        this.productInvTextField.setText(title);
    }

    void setProductPriceDivCostTextField(String title) {
        this.productPriceDivCostTextField.setText(title);
    }

    void setProductMinTextField(String title) {
        this.productMinTextField.setText(title);
    }

    void setProductMaxTextField(String title) {
        this.productMaxTextField.setText(title);
    }

    void setPartsAddedToProdTableView (ObservableList<Part> obsListOfParts) {
        partsAddedToProdTableView.setItems(obsListOfParts);
    }

    void setPartsToAddToProdTableView (ObservableList<Part> obsListOfParts) {
        partsToAddToProdTableView.setItems(obsListOfParts);
    }

    void setaddPartsToProdMsgLn1Label(String msg) {
        this.addPartsToProdMsgLn1.setText(msg);
    }
    void setaddPartsToProdMsgLn2Label(String msg) {
        addPartsToProdMsgLn2.setText(msg);
    }

    JFXButton getAddPartsToNewProductBtn() {
        return addPartsToNewProductBtn;
    }

    JFXButton getDelPartsFromNewProductBtn() {
        return delPartsFromNewProductBtn;
    }

    JFXButton getDelPartsFromModProductBtn() {
        return delPartsFromModProductBtn;
    }

    JFXButton getAddPartsToModProductBtn() {
        return addPartsToModProductBtn;
    }


}
