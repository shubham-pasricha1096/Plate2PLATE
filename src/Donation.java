public class Donation {
    int id, userId, quantity;
    String foodType, expiryDate, description;

    public Donation(int id, int userId, String foodType, int quantity, String expiryDate, String description) {
        this.id = id;
        this.userId = userId;
        this.foodType = foodType;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.description = description;
    }

    // Getters...
}
