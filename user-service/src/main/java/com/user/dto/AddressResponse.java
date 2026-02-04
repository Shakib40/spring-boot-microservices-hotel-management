package com.user.dto;

import com.user.entity.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private String id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private AddressType addressType;
    private String userId;
}
