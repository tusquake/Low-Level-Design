class OrderServiceViolation {

    public void placeOrder(String orderId) {

        System.out.println("Placing order: " + orderId);

        System.out.println("Sending email for order: " + orderId);

        System.out.println("Writing log to file for order: " + orderId);
    }
}

class lchcViolation{
    public static void main(String[] args) {
        OrderServiceViolation orderServiceViolation = new OrderServiceViolation();
        orderServiceViolation.placeOrder("#452");
    }
}


