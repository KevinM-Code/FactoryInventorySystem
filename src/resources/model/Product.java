
package resources.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product class
 * @author Kevin Mock
 */
public class Product {    
    
    private int prodId;
    private String prodName;
    private double prodPrice;
    private int prodStock;
    private int prodMin;
    private int prodMax;



    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Product(int prodId, String prodName, double prodPrice, int prodStock, int prodMin, int prodMax) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.prodStock = prodStock;
        this.prodMin = prodMin;
        this.prodMax = prodMax;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public double getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(double prodPrice) {
        this.prodPrice = prodPrice;
    }

    public int getProdStock() {
        return prodStock;
    }

    public void setProdStock(int prodStock) {
        this.prodStock = prodStock;
    }

    public int getProdMin() {
        return prodMin;
    }

    public void setProdMin(int prodMin) {
        this.prodMin = prodMin;
    }

    public int getProdMax() {
        return prodMax;
    }

    public void setProdMax(int prodMax) {
        this.prodMax = prodMax;
    }

    /**
     * This method returns all associated parts
     * @return Associated parts to a product
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    /**
     * Sets associatedPart
     * @param associatedParts all associated parts with a product
     */
    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    /**
     * Adds a part associated with a product
     * @param part part to associate with a product
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * This method deletes a part associated with a product
     * @param selectedAsPart A selected part associated with a product
     * @return weather the part was removed successfully
     */
    public boolean deleteAssociatedPart(Part selectedAsPart) {
        return associatedParts.remove(selectedAsPart);
    }

    /**
     *
     * @return all Associated Parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }   
    
}
