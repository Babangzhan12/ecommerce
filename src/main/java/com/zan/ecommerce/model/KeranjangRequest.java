package com.zan.ecommerce.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class KeranjangRequest implements Serializable {
    
    private String productId;
    private Double quantity;
}
