package unaldi.orderservice.service.order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import unaldi.orderservice.entity.Order;
import unaldi.orderservice.entity.Package;
import unaldi.orderservice.entity.dto.request.OrderSaveRequest;
import unaldi.orderservice.entity.dto.request.OrderUpdateRequest;
import unaldi.orderservice.utils.client.dto.response.RestResponse;
import unaldi.orderservice.utils.client.dto.response.UserResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectFactory {
    private static ObjectFactory instance;
    private OrderSaveRequest orderSaveRequest;
    private Order order;
    private OrderUpdateRequest orderUpdateRequest;
    private UserResponse userResponse;
    private Package pckg;
    private List<Order> orderList;
    private RestResponse<UserResponse> userRestResponse;

    public static synchronized ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    public OrderSaveRequest getOrderSaveRequest() {
        if (orderSaveRequest == null) {
            orderSaveRequest = new OrderSaveRequest(
                    1L,
                    5
            );
        }
        return orderSaveRequest;
    }

    public Order getOrder() {
        if (order == null) {
            order = new Order(
                    1L,
                    1L,
                    5,
                    100.0,
                    500.0,
                    LocalDate.now(),
                    LocalDate.now().plusDays(30)
            );
        }
        return order;
    }

    public OrderUpdateRequest getOrderUpdateRequest() {
        if (orderUpdateRequest == null) {
            orderUpdateRequest = new OrderUpdateRequest(
                    1L,
                    1L,
                    5
            );
        }
        return orderUpdateRequest;
    }

    public UserResponse getUserResponse() {
        if (userResponse == null) {
            userResponse = new UserResponse(
                    1L,
                    "John",
                    "Doe",
                    "johnDoe",
                    "john.doe@example.com",
                    "password",
                    "1234567890",
                    List.of("ROLE_USER")
            );
        }
        return userResponse;
    }

    public Package getPackage() {
        if (pckg == null) {
            pckg = new Package(
                    1,
                    10,
                    30L,
                    100.0
            );
        }
        return pckg;
    }

    public List<Order> getOrderList() {
        if (orderList == null) {
            Order order1 = new Order(
                    1L,
                    2L,
                    10,
                    200.0,
                    1000.0,
                    LocalDate.now(),
                    LocalDate.now().plusDays(60));

            Order order2 = new Order(
                    2L,
                    2L,
                    10,
                    200.0,
                    1000.0,
                    LocalDate.now(),
                    LocalDate.now().plusDays(60));

            orderList = List.of(order1, order2);
        }
        return orderList;
    }

    public RestResponse<UserResponse> getRestResponse(UserResponse userResponse) {
        if (userRestResponse == null) {
            userRestResponse = RestResponse.<UserResponse>builder()
                    .success(true)
                    .message("User response fetched successfully")
                    .responseDateTime(LocalDateTime.now().toString())
                    .data(userResponse)
                    .build();
        }
        return userRestResponse;
    }
}
