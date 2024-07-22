package unaldi.orderservice.service.order;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import unaldi.orderservice.entity.Order;
import unaldi.orderservice.entity.dto.EmailDetailsDTO;
import unaldi.orderservice.entity.dto.request.OrderSaveRequest;
import unaldi.orderservice.entity.Package;
import unaldi.orderservice.entity.dto.request.OrderUpdateRequest;
import unaldi.orderservice.entity.dto.response.OrderResponse;
import unaldi.orderservice.repository.OrderRepository;
import unaldi.orderservice.repository.PackageRepository;
import unaldi.orderservice.service.EmailService;
import unaldi.orderservice.service.Impl.OrderServiceImpl;
import unaldi.orderservice.utils.client.UserServiceClient;
import unaldi.orderservice.utils.client.dto.response.RestResponse;
import unaldi.orderservice.utils.client.dto.response.UserResponse;
import unaldi.orderservice.utils.exception.OrderNotFoundException;
import unaldi.orderservice.utils.exception.PackageNotFoundException;
import unaldi.orderservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.orderservice.utils.rabbitMQ.dto.OrderDTO;
import unaldi.orderservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.orderservice.utils.rabbitMQ.producer.OrderProducer;
import unaldi.orderservice.utils.result.DataResult;
import unaldi.orderservice.utils.result.Result;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private static Order order;
    private static List<Order> orderList;
    private static OrderSaveRequest orderSaveRequest;
    private static OrderUpdateRequest orderUpdateRequest;
    private static UserResponse userResponse;
    private static Package foundPackage;
    private static RestResponse<UserResponse> userRestResponse;
    private final Long nonExistentOrderId = -1L;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PackageRepository packageRepository;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private EmailService emailService;
    @Mock
    private LogProducer logProducer;
    @Mock
    private OrderProducer orderProducer;
    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeAll
    static void setUp() {
        order = ObjectFactory.getInstance().getOrder();
        orderList = ObjectFactory.getInstance().getOrderList();
        orderSaveRequest = ObjectFactory.getInstance().getOrderSaveRequest();
        orderUpdateRequest = ObjectFactory.getInstance().getOrderUpdateRequest();
        userResponse = ObjectFactory.getInstance().getUserResponse();
        foundPackage = ObjectFactory.getInstance().getPackage();
        userRestResponse = ObjectFactory.getInstance().getRestResponse(userResponse);
    }

    @Test
    void givenOrderSaveRequest_whenSave_thenOrderShouldBeSaved() {
        when(userServiceClient.findById(orderSaveRequest.getUserId())).thenReturn(ResponseEntity.ok(userRestResponse));
        when(packageRepository.findById(anyInt())).thenReturn(Optional.of(foundPackage));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        doNothing().when(emailService).sendEmail(any(EmailDetailsDTO.class));
        doNothing().when(orderProducer).sendToOrder(any(OrderDTO.class));

        DataResult<OrderResponse> result = orderService.save(orderSaveRequest);
        assertTrue(result.getSuccess(), "Order was not saved successfully");
        assertNotNull(result.getData(), "Saved order data should not be null");

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(emailService, times(1)).sendEmail(any(EmailDetailsDTO.class));
        verify(orderProducer, times(1)).sendToOrder(any(OrderDTO.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenOrderUpdateRequest_whenUpdate_thenOrderShouldBeUpdated() {
        when(orderRepository.existsById(orderUpdateRequest.getId())).thenReturn(true);
        when(userServiceClient.findById(orderUpdateRequest.getUserId())).thenReturn(ResponseEntity.ok(userRestResponse));
        when(packageRepository.findById(anyInt())).thenReturn(Optional.of(foundPackage));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        doNothing().when(orderProducer).sendToOrder(any(OrderDTO.class));

        DataResult<OrderResponse> result = orderService.update(orderUpdateRequest);
        assertTrue(result.getSuccess(), "Order was not updated successfully");

        verify(orderRepository, times(1)).existsById(anyLong());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderProducer, times(1)).sendToOrder(any(OrderDTO.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenOrderId_whenDeleteById_thenOrderShouldBeDeleted() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        Result result = orderService.deleteById(order.getId());
        assertTrue(result.getSuccess(), "Order was not deleted successfully");

        verify(orderRepository, times(1)).deleteById(order.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenOrderId_whenFindById_thenOrderShouldBeFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(userServiceClient.findById(order.getUserId())).thenReturn(ResponseEntity.ok(userRestResponse));

        DataResult<OrderResponse> result = orderService.findById(order.getId());
        assertTrue(result.getSuccess(), "Order was not found successfully");

        verify(orderRepository, times(1)).findById(order.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenOrderList_whenFindAll_thenAllOrdersShouldBeReturned() {
        when(orderRepository.findAll()).thenReturn(orderList);
        when(userServiceClient.findById(anyLong())).thenReturn(ResponseEntity.ok(userRestResponse));

        DataResult<List<OrderResponse>> result = orderService.findAll();
        assertTrue(result.getSuccess(), "Orders were not listed successfully");
        assertFalse(result.getData().isEmpty(), "Order list should not be empty");

        verify(orderRepository, times(1)).findAll();
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentOrderUpdateRequest_whenUpdate_thenOrderNotFoundExceptionShouldBeThrown() {
        when(orderRepository.existsById(orderUpdateRequest.getId())).thenReturn(false);

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.update(orderUpdateRequest);
        }, "Expected OrderNotFoundException to be thrown during update");

        verify(orderRepository, times(1)).existsById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentOrderId_whenDeleteById_thenOrderNotFoundExceptionShouldBeThrown() {
        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.deleteById(nonExistentOrderId);
        }, "Expected OrderNotFoundException to be thrown during deletion");

        verify(orderRepository, times(1)).findById(nonExistentOrderId);
        verify(orderRepository, never()).deleteById(anyLong());
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentOrderId_whenFindById_thenOrderNotFoundExceptionShouldBeThrown() {
        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.findById(nonExistentOrderId);
        }, "Expected OrderNotFoundException to be thrown during find by ID");

        verify(orderRepository, times(1)).findById(nonExistentOrderId);
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenPackageNotFound_whenUpdate_thenShouldHandleException() {
        when(orderRepository.existsById(orderUpdateRequest.getId())).thenReturn(true);
        when(userServiceClient.findById(anyLong())).thenReturn(ResponseEntity.ok(userRestResponse));
        when(packageRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(PackageNotFoundException.class, () -> {
            orderService.update(orderUpdateRequest);
        }, "Expected PackageNotFoundException to be thrown during update");

        verify(orderRepository, never()).save(any(Order.class));
        verify(orderProducer, never()).sendToOrder(any(OrderDTO.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void whenFindAllAndNoOrders_thenShouldReturnEmptyList() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        DataResult<List<OrderResponse>> result = orderService.findAll();
        assertTrue(result.getSuccess(), "Orders were not listed successfully");
        assertTrue(result.getData().isEmpty(), "Order list should be empty");

        verify(orderRepository, times(1)).findAll();
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }
}