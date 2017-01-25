package ru.inrecolan.study.dao.interfaces;

import ru.inrecolan.study.dao.objects.Address;

import java.util.List;

public interface AddressDAO {

    // Методы для обращения к БД, если в объекте Address заполнено 1 поле

    List<Address> getRegList(Address address);

    List<Address> getDistList(Address address);

    List<Address> getTownListLev1(Address address);

    List<Address> getTownListLev2(Address address);

    List<Address> getTownListLev3(Address address);

    List<Address> getLocalList(Address address);


    // Методы для обращения к БД, если в объекте Address заполнено 2 поля
    
    List<Address> getRegAndDistList(Address address);

    List<Address> getRegAndTownListLev2(Address address);

    List<Address> getRegAndTownListLev3(Address address);

    List<Address> getRegAndLocalList(Address address);
    
    List<Address> getDistAndTownList(Address address);

    List<Address> getDistAndLocalList(Address address);

    List<Address> getTownAndStrListLev1(Address address);

    List<Address> getTownAndStrListLev2(Address address);

    List<Address> getTownAndStrListLev3(Address address);

    List<Address> getLocalAndStrList(Address address);


    // Методы для обращения к БД, если в объекте Address заполнено 3 поля

    List<Address> getRegDistAndTownList(Address address);

    List<Address> getRegDistAndLocalList(Address address);
    
    List<Address> getRegTownAndStrListLev2(Address address);

    List<Address> getRegTownAndStrListLev3(Address address);

    List<Address> getRegLocalAndStrList(Address address);

    List<Address> getDistTownAndStrList(Address address);

    List<Address> getDistLocalAndStrList(Address address);


    // Методы для обращения к БД, если в объекте Address заполнено 4 поля
    
    List<Address> getRegDistTownAndStrList(Address address);
    
    List<Address> getRegDistLocalAndStrList(Address address);
    
}