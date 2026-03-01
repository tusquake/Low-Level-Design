class AuthService {

    public boolean validateToken(String token) {
        return "valid-token".equals(token);
    }
}

class UserService {

    public void getUser() {
        System.out.println("User details returned");
    }
}

class OrderService {

    public void getOrder() {
        System.out.println("Order details returned");
    }
}

class ApiGateway {

    private AuthService authService;
    private UserService userService;
    private OrderService orderService;

    public ApiGateway(AuthService authService,
                      UserService userService,
                      OrderService orderService) {
        this.authService = authService;
        this.userService = userService;
        this.orderService = orderService;
    }

    public void handleRequest(String token, String path) {

        System.out.println("Incoming request for: " + path);

        // Authentication
        if (!authService.validateToken(token)) {
            System.out.println("Unauthorized request");
            return;
        }

        // Routing
        switch (path) {
            case "/user":
                userService.getUser();
                break;

            case "/order":
                orderService.getOrder();
                break;

            default:
                System.out.println("404 Not Found");
        }
    }
}

public class Main {

    public static void main(String[] args) {

        AuthService authService = new AuthService();
        UserService userService = new UserService();
        OrderService orderService = new OrderService();

        ApiGateway apiGateway =
                new ApiGateway(authService, userService, orderService);

        // Valid request
        apiGateway.handleRequest("valid-token", "/user");

        System.out.println();

        // Invalid token
        apiGateway.handleRequest("invalid-token", "/order");

        System.out.println();

        // Unknown path
        apiGateway.handleRequest("valid-token", "/payment");
    }
}