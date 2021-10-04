
package resources.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import resources.model.InHouse;
import resources.model.Outsourced;
import resources.model.Part;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class is the controller of the "Add Part" and "Modify Part" UI.
 *
 * @author Kevin Mock
 */
public class PartInvScreenController implements Initializable {


    @FXML
    private JFXRadioButton partInHouseRadBtn;

    @FXML
    private JFXRadioButton partOutsrcRadBtn;

    @FXML
    private Label partScreenTitle;

    @FXML
    private Label partCompNameOrMachIdLabel;

    @FXML
    private JFXTextField partIdTextField;

    @FXML
    private JFXTextField partNameTextField;

    @FXML
    private JFXTextField partInvTextField;

    @FXML
    private JFXTextField partPriceDivCostTextField;

    @FXML
    private JFXTextField partMaxTextField;

    @FXML
    private JFXTextField partCompNameOrMachIdTextField;

    @FXML
    private JFXTextField partMinTextField;

    @FXML
    private JFXButton addPartSaveBtn;

    @FXML
    private JFXButton partCxlBtn;

    @FXML
    private JFXButton modPartSaveBtn;

    @FXML
    private Label maxTextFieldErrorMessage;

    /**
     * This method makes sure only one radio button is selected in the add or modify parts display and initializes
     * the controller class
     *
     * @param url default
     * @param rb default
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /* These event handlers are to ensure that only one radio button is selected and the text 
        field labels describe the information needed according to the type of part */
        partInHouseRadBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (partInHouseRadBtn.isSelected()) {
                partOutsrcRadBtn.setSelected(false);
                partCompNameOrMachIdLabel.setText("Machine ID");
                partCompNameOrMachIdTextField.setPromptText("Machine ID");
            }
        });
        partOutsrcRadBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (partOutsrcRadBtn.isSelected()) {
                partInHouseRadBtn.setSelected(false);
                partCompNameOrMachIdLabel.setText("Company Name");
                partCompNameOrMachIdTextField.setPromptText("Company Name");
            }
        });

    }

    /**
     * This method is used to cancel the window that allows the user to add or modify parts
     * @param event mouse click<br><br>
     *
     * Uses Methods:
     * <ul>
     *     <li>
     *         {@link MainScreenController#confirmActionAlert(String)}
     *     </li>     
     * </ul>
     */
    @FXML
    void cxlPartWin(MouseEvent event) {
        if (MainScreenController.confirmActionAlert("cancel from adding or modifying a part")) {
            Stage stage = (Stage) partCxlBtn.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * This method is called in {@link PartInvScreenController#addPart(MouseEvent)} and
     * {@link PartInvScreenController#modifyPart(MouseEvent)} to update or add parts to the list of parts.
     * Certain parts of this method are used depending on the task required.<br><br>  
     *
     * Uses Methods:
     * <ul>
     *     <li>
     *         {@link Inventory#addPart(Part)}
     *     </li>
     *     <li>
     *         {@link Inventory#updatePart(int, Part)}
     *     </li>
     *     <li>
     *         {@link MainScreenController#setNewNextNumInPartPkSequence(ObservableList)}
     *     </li>
     *     <li>
     *         {@link MainScreenController#capFrstLetOfWords(String)}
     *     </li>
     * </ul>
     *
     * @param methodUseIndicator Indicates to the method to run the code that is only necessary for the task required.
     * @param btn                Which button is being clicked
     */
    private void addOrModPart(String methodUseIndicator, JFXButton btn) {
        // Primary key based on the last part id used in the list of parts
        int maxNum = Integer.parseInt(partMaxTextField.getText());
        int minNum = Integer.parseInt(partMinTextField.getText());

        if (maxNum < minNum) {
            maxTextFieldErrorMessage.setText("Maximum inventory cannot be smaller than minimum");
        } else {
            
            int partId = 0;

            if ("AddPart".equals(methodUseIndicator)) {
                /* need the next number in the primary key sequence */
                partId = MainScreenController.nextNumInPartIdSequence;
            }
            if ("ModifyPart".equals(methodUseIndicator)) {
                /* Use the same id */
                partId = Integer.parseInt(partIdTextField.getText());
            }

            /* Get all the information from the text fields, convert them to the correct type and store
            them in a variable */
            String partName = MainScreenController.capFrstLetOfWords(partNameTextField.getText());
            double priceDivCost = Double.parseDouble(partPriceDivCostTextField.getText());
            int numPartsInv = Integer.parseInt(partInvTextField.getText());
            int minInv = Integer.parseInt(partMinTextField.getText());
            int maxInv = Integer.parseInt(partMaxTextField.getText());

            /* Attaining the same instance of the "Part" object in the Observable List */
            Part currPart = Inventory.getAllParts().get(MainScreenController.indexOfPartModified);

            /* If In-House radio button is selected, run the code based on whether you are adding
            or modifying an In-House part indicated by which radio button is selected and the 
            methodUseIndicator parameter */
            if (partInHouseRadBtn.isSelected()) {
                int machId = Integer.parseInt(partCompNameOrMachIdTextField.getText());
                if ("AddPart".equals(methodUseIndicator)) {
                    Inventory.addPart(new InHouse(partId, partName, priceDivCost, numPartsInv, minInv, maxInv, machId));
                }
                /* Modifying the same instance of the Inhouse object */
                if ("ModifyPart".equals(methodUseIndicator)) {
                    if (!(partName.equals(currPart.getName()))) {
                        currPart.setName(partName);
                    }
                    if (priceDivCost != currPart.getPrice()) {
                        currPart.setPrice(priceDivCost);
                    }
                    if (numPartsInv != currPart.getStock()) {
                        currPart.setStock(numPartsInv);
                    }
                    if (minInv != currPart.getMin()) {
                        currPart.setMin(minInv);
                    }
                    if (maxInv != currPart.getMax()) {
                        currPart.setMax(maxInv);
                    }
                    if (currPart instanceof InHouse) {
                        if (machId != ((InHouse) currPart).getMachineId()) {
                            ((InHouse) currPart).setMachineId(machId);
                        }
                    }

                    Inventory.updatePart(MainScreenController.indexOfPartModified, currPart);
                }
            }

            /* If Outsourced radio button is selected, run the code based on whether you are adding
            or modifying an Outsourced part indicated by which radio button is selected and the 
            methodUseIndicator parameter */
            if (partOutsrcRadBtn.isSelected()) {
                String comName = MainScreenController.capFrstLetOfWords(partCompNameOrMachIdTextField.getText());
                if ("AddPart".equals(methodUseIndicator)) {
                    Inventory.addPart(new Outsourced(partId, partName, priceDivCost, numPartsInv, minInv, maxInv, comName));
                }
                /* Modifying the same instance of the Outsouced object */
                if ("ModifyPart".equals(methodUseIndicator)) {
                    if (!(partName.equals(currPart.getName()))) {
                        currPart.setName(partName);
                    }
                    if (priceDivCost != currPart.getPrice()) {
                        currPart.setPrice(priceDivCost);
                    }
                    if (numPartsInv != currPart.getStock()) {
                        currPart.setStock(numPartsInv);
                    }
                    if (minInv != currPart.getMin()) {
                        currPart.setMin(minInv);
                    }
                    if (maxInv != currPart.getMax()) {
                        currPart.setMax(maxInv);
                    }
                    if (currPart instanceof Outsourced) {
                        if (comName.equals(((Outsourced) currPart).getCompanyName())) {
                            ((Outsourced) currPart).setCompanyName(comName);
                        }
                    }
                    Inventory.updatePart(MainScreenController.indexOfPartModified, currPart);
                }             
            }

            /* If you added a part, run the method that gets the number used and assigns 1 higher to the variable */
            if ("AddPart".equals(methodUseIndicator)) {
                MainScreenController.setNewNextNumInPartPkSequence(Inventory.getAllParts());
            }
            /* Once the part has been updated close the window */
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * This method calls {@link PartInvScreenController#addOrModPart(String, JFXButton)} to add the part.
     * @param event mouse click
     */
    @FXML
    void addPart(MouseEvent event) {
        String methodUseIndicator = "AddPart";
        addOrModPart(methodUseIndicator, addPartSaveBtn);
    }

    /**
     * This method calls {@link PartInvScreenController#addOrModPart(String, JFXButton)} to modify the part selected
     * @param event mouse click
     */    
    @FXML
    void modifyPart(MouseEvent event) {
        String methodUseIndicator = "ModifyPart";
        addOrModPart(methodUseIndicator, modPartSaveBtn);
    }

    void setAddPartsSaveBtnVisibility(boolean bool) {
        this.addPartSaveBtn.setVisible(bool);
    }

    void setModifyPartsSaveBtnVisibility(boolean bool) {
        this.modPartSaveBtn.setVisible(bool);
    }

    void setPartsTitleText(String title) {
        this.partScreenTitle.setText(title);
    }

    /* These text fields will be populated with the modified part information*/
    void setPrtsOutsrcdRadBtn(boolean condition) {
        partOutsrcRadBtn.setSelected(condition);
    }

    void setPrtsInhouseRadBtn(boolean condition) {
        partInHouseRadBtn.setSelected(condition);
    }

    void setPrtsCompNameOrMachIdLabel(String text) {
        partCompNameOrMachIdLabel.setText(text);
    }

    void setPrtsCompNameOrMachIdTxtField(String text) {
        partCompNameOrMachIdTextField.setPromptText(text);
    }

    void setPartIdTextField(String title) {
        this.partIdTextField.setText(title);
    }

    void setPartNameTextField(String title) {
        this.partNameTextField.setText(title);
    }

    void setPartInvTextField(String title) {
        this.partInvTextField.setText(title);
    }

    void setPartPriceDivCostTextField(String title) {
        this.partPriceDivCostTextField.setText(title);
    }

    void setPartMaxTextField(String title) {
        this.partMaxTextField.setText(title);
    }

    void setPartMinTextField(String title) {
        this.partMinTextField.setText(title);
    }

    void setPartCompNameOrMachIdTextField(String title) {
        this.partCompNameOrMachIdTextField.setText(title);
    }

}
