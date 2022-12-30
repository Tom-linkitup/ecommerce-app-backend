package com.example.springbootecommerce.service;

import com.example.springbootecommerce.dao.CustomerRepository;
import com.example.springbootecommerce.dto.Purchase;
import com.example.springbootecommerce.dto.PurchaseResponse;
import com.example.springbootecommerce.entity.Customer;
import com.example.springbootecommerce.entity.Order;
import com.example.springbootecommerce.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        // retrieve the order from dto
        Order order = purchase.getOrder();

        // generate order tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        // populate address
        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        // populate customer
        Customer customer = purchase.getCustomer();
        // check if duplicate email
        String email = customer.getEmail();
        Customer customerFromDb = customerRepository.findByEmail(email);
        if (Objects.nonNull(customerFromDb)) {
            customer = customerFromDb;
        }
        customer.add(order);

        // save to db
        customerRepository.save(customer);

        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
