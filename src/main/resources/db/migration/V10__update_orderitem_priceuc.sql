UPDATE order_item
SET price_uc = order_item.localized_price * pcrt.universal_price_value
FROM order_item oit
LEFT JOIN price_conversion_rate pcrt
ON oit.price_locale = pcrt.region;
