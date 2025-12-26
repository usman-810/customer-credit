package com.creditcard.customer.repository;


import com.creditcard.customer.entity.Customer;
import com.creditcard.customer.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<Customer> findByStatus(CustomerStatus status);

    Page<Customer> findByStatus(CustomerStatus status, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "c.phone LIKE CONCAT('%', :keyword, '%')")
    Page<Customer> searchCustomers(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.city = :city AND c.status = :status")
    List<Customer> findByCityAndStatus(@Param("city") String city, 
                                       @Param("status") CustomerStatus status);

    long countByStatus(CustomerStatus status);
}