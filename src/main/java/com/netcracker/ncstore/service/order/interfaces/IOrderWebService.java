package com.netcracker.ncstore.service.order.interfaces;

import com.netcracker.ncstore.dto.request.OrderCreateRequest;
import com.netcracker.ncstore.dto.request.OrderGetRequest;
import com.netcracker.ncstore.dto.request.OrderInfoGetRequest;
import com.netcracker.ncstore.dto.response.OrderGetResponse;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.exception.general.GeneralNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;

import java.util.List;

/**
 * Interface for all WEB services related to Order.
 */
public interface IOrderWebService {
    /**
     * Returns List of responses for GET request.
     *
     * @param request DTO containing info for request
     * @return response
     */
    List<OrderGetResponse> getOrders(final OrderGetRequest request);

    /**
     * Returns response for GET request for specific order.
     *
     * @param request DTO containing info for request
     * @return response
     * @throws GeneralNotFoundException         when requested order not exists
     * @throws GeneralPermissionDeniedException when user do not have access to view this order
     */
    OrderInfoResponse getSpecificOrder(final OrderInfoGetRequest request) throws GeneralNotFoundException, GeneralPermissionDeniedException;

    /**
     * Creates new order for provided request
     *
     * @param request DTO containing order creation request info
     * @return response containing info about new order
     * @throws GeneralBadRequestException
     */
    OrderInfoResponse createOrder(final OrderCreateRequest request) throws GeneralBadRequestException;
}
