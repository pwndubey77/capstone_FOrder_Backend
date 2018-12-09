package org.upgrad.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.upgrad.models.Coupon;
import org.upgrad.models.Item;
import org.upgrad.models.Order;
import org.upgrad.repositories.*;
import org.upgrad.requestResponseEntity.ItemQuantity;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    private CouponRepository couponRepository;

    private AddressRepository addressRepository;
    private UserAddressRepository userAddressRepository;

    private OrderRepository orderRepository;
    private ItemRepository itemRepository;

    public OrderServiceImpl(CouponRepository couponRepository, AddressRepository addressRepository,
                            UserAddressRepository userAddressRepository, OrderRepository orderRepository, ItemRepository itemRepository) {
        this.couponRepository = couponRepository;
        this.addressRepository = addressRepository;
        this.userAddressRepository = userAddressRepository;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Coupon getCoupon(String couponName) {
        return couponRepository.findCouponByName(couponName);
    }



    @Override
    public List<Order> getOrdersByUser(Integer userId) {
        return orderRepository.findOrdersByUser(userId);

    }

    @Override
    public Integer addOrderWithPermAddress(Integer addressId, Integer paymentId, Integer userId, ArrayList<ItemQuantity> itemQuantities,
                                           double bill, Integer couponId, double discount) {


        orderRepository.saveOrder(couponId, discount, bill, userId, paymentId,  addressId);
        Integer orderId = orderRepository.findLatestOrderId();

        for (ItemQuantity itemQuantity: itemQuantities) {

            Item item = itemRepository.findItemById (itemQuantity.getItemId());
            double totalPrice = item.getPrice() * itemQuantity.getQuantity();
            orderRepository.saveOrderItem(orderId, itemQuantity.getItemId(), itemQuantity.getQuantity(), totalPrice);
        }

        return orderId;
    }

    @Override
    public Integer addOrder(String flatBuilNumber, String locality, String city, String zipCode, Integer stateId, String type,
                            Integer paymentId, Integer userId, List<ItemQuantity> itemQuantities, double bill, Integer couponId,
                            double discount) {

        addressRepository.addAddress(flatBuilNumber, locality, city, zipCode, stateId);

        Integer lastAddedAddressId = addressRepository.findLastAddedAddress();
        if (type == null || type.equals("")) {
            type = "temp";
        }
        userAddressRepository.addAddressType(type, userId, lastAddedAddressId);
        orderRepository.saveOrder(couponId, discount, bill, userId, paymentId,  lastAddedAddressId);

        Integer latestOrderId = orderRepository.findLatestOrderId();

        for (ItemQuantity itemQuantity: itemQuantities) {

            Item item = itemRepository.findItemById (itemQuantity.getItemId());
            double totalPrice = item.getPrice() * itemQuantity.getQuantity();
            orderRepository.saveOrderItem(latestOrderId, itemQuantity.getItemId(), itemQuantity.getQuantity(), totalPrice);
        }

        return latestOrderId;
    }
}
