package pojo;

public class CheckOut {
    private final String customerId;
    private final double subtotal;
    private final double finalTotal;
    private final double discount;

    public CheckOut(String customerId, double subtotal, double finalTotal) {
        this.customerId = customerId;
        this.subtotal = subtotal;
        this.finalTotal = finalTotal;
        this.discount = subtotal - finalTotal;
    }

    public double getFinalTotal() {
        return finalTotal;
    }
}
