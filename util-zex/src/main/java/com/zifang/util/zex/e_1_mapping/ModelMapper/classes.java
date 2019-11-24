package com.zifang.util.zex.e_1_mapping.ModelMapper;


import org.modelmapper.ModelMapper;

public class classes {

    public static void main(String[] args) {
        ModelMapper modelMapper = new ModelMapper();
        OrderDTO orderDTO = modelMapper.map(new Order(), OrderDTO.class);

//        modelMapper.addMappings(mapper -> {
//            mapper.map(src -> src.getBillingAddress().getStreet(),
//                    Destination::setBillingStreet);
//            mapper.map(src -> src.getBillingAddress().getCity(),
//                    Destination::setBillingCity);
//        });
    }
}


class Order {
    Customer customer;
    Address billingAddress;
}

class Customer {
    Name name;
}

class Name {
    String firstName;
    String lastName;
}

class Address {
    String street;
    String city;
}

class OrderDTO {
    String customerFirstName;
    String customerLastName;
    String billingStreet;
    String billingCity;
}