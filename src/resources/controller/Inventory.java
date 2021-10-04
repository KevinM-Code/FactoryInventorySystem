package resources.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import resources.model.Part;
import resources.model.Product;

/**
 * This class makes all the modification and additions to the Parts and Products inventory.
 * @author Kevin Mock
 */
class Inventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Add a new "Part" object to the Observable Array List.
     * @param newPart the Part object being added
     */
    static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Add a new "Product" object to the Observable Array List.
     * @param newProduct the Product object being added
     */
    static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * This method takes the part id and returns the part associated with the part id.
     * @param partId id of the part being searched
     * @return the part that is associated with the part id that was searched
     */
    static Part lookupPart(int partId) {
        FilteredList<Part> filteredParts = new FilteredList<>(allParts);
        String searchedPart = Integer.toString(partId);
        filteredParts.setPredicate(srch -> Integer.toString(srch.getId()).contains(searchedPart));
        
        return filteredParts.get(0);
    }

    /**
     * This method takes the primary key product id and returns the "Product" object associated with the product id.
     * @param productId the id of the product that is being searched.
     * @return The Product associated with the product id
     */
    static Product lookupProduct (int productId) {
        FilteredList<Product> filteredProducts = new FilteredList<>(allProducts);
        String searchedPart = Integer.toString(productId);
        filteredProducts.setPredicate(srch -> Integer.toString(srch.getProdId()).contains(searchedPart));
        return filteredProducts.get(0);
    }

    /**
     * This method takes the name of the part being searched and returns an Observable List of "Part" object(s) that are a match
     * with name of the part being searched.
     * @param partName the name of the part that is being searched
     * @return the array of Parts that are an exact match with the name of the part searched
     */
    static ObservableList<Part> lookupPart(String partName) {
        FilteredList<Part> filteredParts = new FilteredList<>(allParts);        
        filteredParts.setPredicate(srch -> srch.getName().contains(partName));
        return filteredParts;
    }

    /**
     * This method takes the name of the product being searched and returns an Observable List of "Product" object(s)
     * that are a match with name of the product being searched.
     * @param productName the name of the product being search
     * @return Filter List array of Products matching the search
     */
    static ObservableList<Product> lookupProduct (String productName) {
        FilteredList<Product> filteredProducts = new FilteredList<>(allProducts);        
        filteredProducts.setPredicate(srch -> srch.getProdName().contains(productName));
        return filteredProducts;
    }

    /**
     * This method updates the properties of the Part that reside in the particular index in the Observable Array
     * List of Parts.
     * @param index the position the Part object is in the array of Parts
     * @param selectedPart the Part being selected in the Table View
     */
    static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * This method updates the properties of the Product that reside in the particular index in the Observable Array
     * List of Products.
     * @param index The index where the Product resides in the Observable Array List.
     * @param newProduct The new modified part
     */
    static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * The Part being removed from the Observable List Array of Parts
     * @param selectedPart The Part selected to be removed in the table list
     * @return whether or not the Part was removed
     */
    static boolean deletePart(Part selectedPart) {
        return getAllParts().remove(selectedPart);
    }

    /**
     * The Product being removed from the Observable List Array of Products
     * @param selectedProduct The Product selected to be removed in the table list
     * @return whether or not the Product was removed
     */
    static boolean deleteProduct(Product selectedProduct) {
        return getAllProducts().remove(selectedProduct);
    }

    /**
     * The Observable List of Parts
     * @return Observable List of Parts
     */
    static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * The Observable List of Products
     * @return Observable List of Products
     */
    static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
