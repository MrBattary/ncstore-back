package com.netcracker.ncstore.service.order;

import com.netcracker.ncstore.dto.CartItemDTO;
import com.netcracker.ncstore.dto.OrderGetDTO;
import com.netcracker.ncstore.dto.OrderGetPageDTO;
import com.netcracker.ncstore.dto.create.OrderCreateDTO;
import com.netcracker.ncstore.dto.request.OrderCreateRequest;
import com.netcracker.ncstore.dto.request.OrderGetRequest;
import com.netcracker.ncstore.dto.request.OrderInfoGetRequest;
import com.netcracker.ncstore.dto.response.OrderGetResponse;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.dto.response.OrderItemInfoResponse;
import com.netcracker.ncstore.exception.GeneralBadRequestException;
import com.netcracker.ncstore.exception.GeneralNotFoundException;
import com.netcracker.ncstore.exception.GeneralPermissionDeniedException;
import com.netcracker.ncstore.exception.OrderServiceNotFoundException;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.exception.OrderServicePermissionException;
import com.netcracker.ncstore.model.Order;
import com.netcracker.ncstore.service.order.interfaces.IOrderBusinessService;
import com.netcracker.ncstore.service.order.interfaces.IOrderWebService;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderWebService implements IOrderWebService {
    private final IOrderBusinessService orderBusinessService;
    private final IUserService userService;

    public OrderWebService(final IOrderBusinessService orderBusinessService,
                           final IUserService userService) {
        this.orderBusinessService = orderBusinessService;
        this.userService = userService;
    }


    @Override
    public List<OrderGetResponse> getOrders(OrderGetRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("creationUtcTime").descending());
        OrderGetPageDTO getPageDTO = new OrderGetPageDTO(pageable, request.getEmail());

        Page<Order> ordersPage = orderBusinessService.getOrdersForUserWithPagination(getPageDTO);

        return ordersPage.
                getContent().
                stream().
                map(e -> new OrderGetResponse(
                                e.getId(),
                                e.getOrderStatus(),
                                e.getCreationUtcTime()
                        )
                ).
                collect(Collectors.toList());
    }

    @Override
    public OrderInfoResponse getSpecificOrder(OrderInfoGetRequest request) {
        OrderGetDTO getDTO = new OrderGetDTO(request.getOrderId(), request.getEmail());

        try {
            Order order = orderBusinessService.getSpecificOrderForUser(getDTO);
            return convertOrderToInfoResponse(order);
        } catch (OrderServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        } catch (OrderServicePermissionException permissionException) {
            throw new GeneralPermissionDeniedException(permissionException.getMessage(), permissionException);
        }
    }

    @Override
    public OrderInfoResponse createOrder(OrderCreateRequest request) {
        try {
            Map<UUID, Integer> products = request.
                    getProductsToBuyWithCount().
                    stream().
                    collect(Collectors.toMap(
                                    CartItemDTO::getProductId,
                                    CartItemDTO::getCountOfProduct
                            )
                    );


            OrderCreateDTO orderCreateDTO = new OrderCreateDTO(
                    products,
                    userService.loadUserEntityByEmail(
                            request.getCustomerEmail()
                    ),
                    request.getRegion(),
                    request.isUseBalance(),
                    request.getNonce()
            );

            Order order = orderBusinessService.createNewOrder(orderCreateDTO);

            return convertOrderToInfoResponse(order);
        } catch (OrderServiceOrderCreationException e) {
            throw new GeneralBadRequestException(e.getMessage(), e);
        }
    }

    private OrderInfoResponse convertOrderToInfoResponse(Order order) {
        List<OrderItemInfoResponse> orderItems = order.getProducts().
                stream().
                map(e -> new OrderItemInfoResponse(
                                e.getProduct().getId(),
                                e.getProduct().getName(),
                                e.getProduct().getSupplier().getId(),
                                e.getPrice(),
                                LocaleToCurrencyConverter.getCurrencySymbolByLocale(e.getPriceLocale()),
                                e.getLicenseKey(),
                                e.getItemStatus()
                        )
                ).
                collect(Collectors.toList());

        return new OrderInfoResponse(order.getId(), order.getCreationUtcTime(), order.getOrderStatus(), orderItems);
    }
}
