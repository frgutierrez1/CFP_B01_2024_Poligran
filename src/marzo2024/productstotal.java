package marzo2024;

public class productstotal {
    static class ProductInfo {
        String productName;
        double cost;
        int totalQuantity;

        ProductInfo(String productName, double cost, int totalQuantity) {
            this.productName = productName;
            this.cost = cost;
            this.totalQuantity = totalQuantity;
        }

        void addQuantity(int quantity) {
            this.totalQuantity += quantity;
        }

        int getTotalQuantity() {
            return totalQuantity;
        }
    }

}