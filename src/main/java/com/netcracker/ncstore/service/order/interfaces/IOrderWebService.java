package com.netcracker.ncstore.service.order.interfaces;

import com.netcracker.ncstore.dto.CheckoutDetails;
import com.netcracker.ncstore.dto.request.OrderCreateRequest;
import com.netcracker.ncstore.dto.request.OrderGetRequest;
import com.netcracker.ncstore.dto.request.OrderInfoGetRequest;
import com.netcracker.ncstore.dto.response.OrderGetResponse;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.exception.GeneralNotFoundException;
import com.netcracker.ncstore.exception.GeneralPermissionDeniedException;

import java.util.List;

public interface IOrderWebService {
    /**
     * Returns List of responses for GET request.
     *
     * @param request DTO containing info for request
     * @return response
     */
    List<OrderGetResponse> getOrders(OrderGetRequest request);

    /**
     * Returns response for GET request for specific order.
     *
     * @param request DTO containing info for request
     * @return response
     * @throws GeneralNotFoundException when requested order not exists
     * @throws GeneralPermissionDeniedException when user do not have access to view this order
     */
    OrderInfoResponse getSpecificOrder(OrderInfoGetRequest request) throws GeneralNotFoundException, GeneralPermissionDeniedException;

    OrderInfoResponse createOrder(OrderCreateRequest request);
}
