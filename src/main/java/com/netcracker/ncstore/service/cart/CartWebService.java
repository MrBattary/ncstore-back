package com.netcracker.ncstore.service.cart;

import com.netcracker.ncstore.dto.ActualProductPrice;
import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
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
import com.netcracker.ncstore.service.price.interfaces.IPricesBusinessService;
import com.netcracker.ncstore.service.priceconverter.interfaces.IPriceConversionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CartWebService implements ICartWebService {
    private final ICartBusinessService cartBusinessService;
    private final IPriceConversionService priceConversionService;
    private final IPricesBusinessService pricesService;
    private final IOrderWebService orderWebService;

    @Value("${security.anonymous_user_name}")
    private String anonymousUserName;

    public CartWebService(final ICartBusinessService cartBusinessService,
                          final IPriceConversionService priceConversionService,
                          final IPricesBusinessService pricesService,
                          final IOrderWebService orderWebService) {

        this.cartBusinessService = cartBusinessService;
        this.priceConversionService = priceConversionService;
        this.pricesService = pricesService;
        this.orderWebService = orderWebService;
    }


    @Override
    public List<CartItemResponse> getCartItems(final CartGetRequest request) {
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
    public CartItemResponse putCartItem(final CartPutRequest request) {
        checkAndSetCartUserEmailIfAbsent(request.getEmail());

        CartItemDTO put = cartBusinessService.addOrUpdateProductInCart(new CartPutDTO(
                        request.getProductId(),
                        request.getProductCount()
                )
        );

        return convertCartItemDTOToResponse(put, request.getLocale());
    }

    @Override
    public CartItemResponse deleteCartItem(final CartDeleteRequest request) {
        checkAndSetCartUserEmailIfAbsent(request.getEmail());

        CartItemDTO deleted = cartBusinessService.deleteProductFromCart(request.getProductId());
        if (deleted.getCountOfProduct() == 0) {
            return null;
        } else {
            return convertCartItemDTOToResponse(deleted, request.getLocale());
        }
    }

    @Override
    public OrderInfoResponse checkoutCartForUser(final CartCheckoutRequest request) {
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

    private void checkAndSetCartUserEmailIfAbsent(final String email) {
        if (cartBusinessService.getCartUserEmail() == null && !email.equals(anonymousUserName)) {
            cartBusinessService.setCartUserEmail(email);
        }
    }

    private CartItemResponse convertCartItemDTOToResponse(final CartItemDTO cartItemDTO,
                                                          final Locale locale) {
        ActualProductPrice priceForProduct =
                pricesService.getActualPriceForProductInRegion(new ProductLocaleDTO(cartItemDTO.getProductId(), locale));

        ActualProductPriceConvertedForRegionDTO regionalPriceForProduct =
                priceConversionService.convertUCPriceForRealPrice(priceForProduct);

        return new CartItemResponse(
                cartItemDTO.getProductId(),
                cartItemDTO.getCountOfProduct(),
                regionalPriceForProduct.getProductName(),
                regionalPriceForProduct.getNormalConvertedPrice(),
                regionalPriceForProduct.getDiscountConvertedPrice(),
                regionalPriceForProduct.getCurrencySymbol());
    }
}
