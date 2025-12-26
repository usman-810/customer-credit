package com.creditcard.customer.mapper;

import com.creditcard.customer.dto.CustomerRequestDTO;
import com.creditcard.customer.dto.CustomerResponseDTO;
import com.creditcard.customer.dto.CustomerUpdateDTO;
import com.creditcard.customer.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CustomerRequestDTO dto);

    @Mapping(target = "fullName", expression = "java(customer.getFullName())")
    CustomerResponseDTO toDTO(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(CustomerUpdateDTO dto, @MappingTarget Customer customer);
}