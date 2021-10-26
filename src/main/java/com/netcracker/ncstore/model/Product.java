package com.netcracker.ncstore.model;

import com.netcracker.ncstore.model.enumerations.EProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that defines one product in the store.
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private long id;
    private long companyId;
    private long parentProductId;
    private String name;
    private String description;
    private EProductStatus status;
}
