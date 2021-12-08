package com.netcracker.ncstore.service.cart;

import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
import com.netcracker.ncstore.dto.ActualProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.CartItemDTO;
import com.netcracker.ncstore.dto.CartPutDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.request.CartCheckoutRequest;
import com.netcracker.ncstore.dto.request.CartDeleteRequest;
import com.netcracker.ncstore.dto.request.CartGetRequest;
import com.netcracker.ncstore.dto.request.CartPutRequest;
import com.netcracker.ncstore.dto.request.OrderCreateRequest;
import com.netcracker.ncstore.dto.response.CartItemResponse;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.service.cart.interfaces.ICartBusinessService;
import com.netcracker.ncstore.service.cart.interfaces.ICartWebService;
import com.netcracker.ncstore.service.order.interfaces.IOrderWebService;
import com.netcracker.ncstore.service.price.IPricesService;
import com.netcracker.ncstore.service.priceconverter.IPriceConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CartWebService implements ICartWebService {
    private final ICartBusinessService cartBusinessService;
    private final IPriceConversionService priceConversionService;
    private final IPricesService pricesService;
    private final IOrderWebService orderWebService;

    public CartWebService(final ICartBusinessService cartBusinessService,
                          final IPriceConversionService priceConversionService,
                          final IPricesService pricesService,
                          final IOrderWebService orderWebService) {

        this.cartBusinessService = cartBusinessService;
        this.priceConversionService = priceConversionService;
        this.pricesService = pricesService;
        this.orderWebService = orderWebService;
    }


    @Override
    public List<CartItemResponse> getCartItems(CartGetRequest request) {
        checkAndSetCartUserEmailIfAbsent(request.getEmail());

        List<CartItemDTO> cartItemDTOList = cartBusinessService.getCartItems();

        return cartItemDTOList.
                stream().
                map(element -> convertCartItemDTOToResponse(
                                element,
                                request.getLocale()
                        )
                ).
                collect(Collectors.toList());
    }

    @Override
    public CartItemResponse putCartItem(CartPutRequest request) {
        checkAndSetCartUserEmailIfAbsent(request.getEmail());

        CartItemDTO put = cartBusinessService.addOrUpdateProductInCart(new CartPutDTO(
                        request.getProductId(),
                        request.getProductCount()
                )
        );

        return convertCartItemDTOToResponse(put, request.getLocale());
    }

    @Override
    public CartItemResponse deleteCartItem(CartDeleteRequest request) {
        checkAndSetCartUserEmailIfAbsent(request.getEmail());

        CartItemDTO deleted = cartBusinessService.deleteProductFromCart(request.getProductId());
        if (deleted.getCountOfProduct() == 0) {
            return null;
        } else {
            return convertCartItemDTOToResponse(deleted, request.getLocale());
        }
    }

    @Override
    public OrderInfoResponse checkoutCartForUser(CartCheckoutRequest request) {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                cartBusinessService.getCartItems(),
                request.getEmail(),
                request.getLocale(),
                request.isUseBalance(),
                request.getNonce()
        );

        OrderInfoResponse orderInfoResponse = orderWebService.createOrder(orderCreateRequest);

        cartBusinessService.clearCart();

        return orderInfoResponse;
    }

    private void checkAndSetCartUserEmailIfAbsent(String email) {
        if (cartBusinessService.getCartUserEmail() == null) {
            cartBusinessService.setCartUserEmail(email);
        }
    }

    private CartItemResponse convertCartItemDTOToResponse(CartItemDTO cartItemDTO, Locale locale) {
        ActualProductPriceInRegionDTO priceForProduct =
                pricesService.getActualPriceForProductInRegion(new ProductLocaleDTO(cartItemDTO.getProductId(), locale));

        ActualProductPriceConvertedForRegionDTO regionalPriceForProduct =
                priceConversionService.convertActualUCPriceForRealPrice(priceForProduct);

        return new CartItemResponse(
                cartItemDTO.getProductId(),
                cartItemDTO.getCountOfProduct(),
                regionalPriceForProduct.getProductName(),
                regionalPriceForProduct.getNormalConvertedPrice(),
                regionalPriceForProduct.getDiscountConvertedPrice(),
                regionalPriceForProduct.getCurrencySymbol());
    }
}
