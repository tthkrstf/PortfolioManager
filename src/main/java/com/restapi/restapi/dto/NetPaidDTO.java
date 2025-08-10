package com.restapi.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class NetPaidDTO {
    public BigDecimal netWorth;
    public BigDecimal paidAmount;
}
