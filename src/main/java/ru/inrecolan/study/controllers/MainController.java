package ru.inrecolan.study.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.inrecolan.study.dao.interfaces.AddressDAO;
import ru.inrecolan.study.dao.objects.Address;

import java.lang.reflect.Field;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showMainPage() {
        return "index";
    }

    /*
    *  В методе используется механизм Java Reflection.
    *  Это сделано для того, чтобы определить имена и количество полей, которые клиент зполнил у объекта.
    *  Для каждого набора полей вызывается метод, который сделает запрос к БД, используя введенную пользователем
    *  информацию.
    */
    @RequestMapping(value = "/usability", method = RequestMethod.POST)
    @ResponseBody
    public List<Address> usability(@RequestBody Address address) throws IllegalAccessException {

        Class addressclass = address.getClass();
        Field[] fields = addressclass.getDeclaredFields();

        Map<String, String> fieldsMap = new HashMap<String, String>();

        for (Field field: fields) {
            field.setAccessible(true);
            String fieldValue =  (String) field.get(address);
            if (!(fieldValue == null) && (!(fieldValue == null) && !"".equals(fieldValue))) {
                fieldsMap.put(field.getName() ,fieldValue);
            }
        }

        AddressDAO mySqlDAO = (AddressDAO) applicationContext.getBean("mySqlDAO");
        List<Address> list = null;

        switch (fieldsMap.size()) {
            case 0: break;

            case 1:
                // Регион
                if (fieldsMap.containsKey("region")) {
                    list = mySqlDAO.getRegList(address);
                    
                // Район
                } else if (fieldsMap.containsKey("district")) {
                    list = mySqlDAO.getDistList(address);

                // Город
                } else if (fieldsMap.containsKey("town")) {
                    list = new ArrayList<>();
                    List<Address> listLev1 = mySqlDAO.getTownListLev1(address);
                    List<Address> listLev2 = mySqlDAO.getTownListLev2(address);
                    List<Address> listLev3 = mySqlDAO.getTownListLev3(address);
                    list.addAll(listLev1);
                    list.addAll(listLev2);
                    list.addAll(listLev3);

                // Населенный пункт
                } else if (fieldsMap.containsKey("locality")) {
                    list = mySqlDAO.getLocalList(address);
                }
                break;

            case 2:
                // Регион - район
                if (fieldsMap.containsKey("region") && fieldsMap.containsKey("district")) {
                    list = mySqlDAO.getRegAndDistList(address);

                // Регион - город
                } else if (fieldsMap.containsKey("region") && fieldsMap.containsKey("town")) { 
                    list = new ArrayList<>();
                    List<Address> listLev2 = mySqlDAO.getRegAndTownListLev2(address);
                    List<Address> listLev3 = mySqlDAO.getRegAndTownListLev3(address);
                    list.addAll(listLev2);
                    list.addAll(listLev3);
                    
                // Регион - населенный пункт
                } else if (fieldsMap.containsKey("region") && fieldsMap.containsKey("locality")) {
                    list = mySqlDAO.getRegAndLocalList(address);

                // Район - город
                } else if (fieldsMap.containsKey("district") && fieldsMap.containsKey("town")) {
                    list = mySqlDAO.getDistAndTownList(address);

                // Район - населенный пункт
                } else if (fieldsMap.containsKey("district") && fieldsMap.containsKey("locality")) {
                    list = mySqlDAO.getDistAndLocalList(address);

                // Город - улица
                } else if (fieldsMap.containsKey("town") && fieldsMap.containsKey("street")) {
                    list = new ArrayList<>();
                    List<Address> listLev1 = mySqlDAO.getTownAndStrListLev1(address);
                    List<Address> listLev2 = mySqlDAO.getTownAndStrListLev2(address);
                    List<Address> listLev3 = mySqlDAO.getTownAndStrListLev3(address);
                    list.addAll(listLev1);
                    list.addAll(listLev2);
                    list.addAll(listLev3);

                // Населенный пункт - улица
                } else if (fieldsMap.containsKey("locality") && fieldsMap.containsKey("street")) {
                    list = mySqlDAO.getLocalAndStrList(address);
                }
                break;

            case 3:
                // Регион - район - город
                if (fieldsMap.containsKey("region") && fieldsMap.containsKey("district") 
                                                    && fieldsMap.containsKey("town")) {
                    list = mySqlDAO.getRegDistAndTownList(address);

                // Регион - район - населенный пункт
                } else if (fieldsMap.containsKey("region") && fieldsMap.containsKey("district") 
                                                           && fieldsMap.containsKey("locality")) {
                    list = mySqlDAO.getRegDistAndLocalList(address);
                    
                // Регион - город - улица
                } else if (fieldsMap.containsKey("region") && fieldsMap.containsKey("town") 
                                                           && fieldsMap.containsKey("street")) {
                    list = new ArrayList<>();
                    List<Address> listLev2 = mySqlDAO.getRegTownAndStrListLev2(address);
                    List<Address> listLev3 = mySqlDAO.getRegTownAndStrListLev3(address);
                    list.addAll(listLev2);
                    list.addAll(listLev3);

                // Регион - населенный пункт - улица
                } else if (fieldsMap.containsKey("region") && fieldsMap.containsKey("locality") 
                                                           && fieldsMap.containsKey("street")) {
                    list = mySqlDAO.getRegLocalAndStrList(address);

                // Район - город - улица
                } else if (fieldsMap.containsKey("district") && fieldsMap.containsKey("town")
                                                           && fieldsMap.containsKey("street")) {
                    list = mySqlDAO.getDistTownAndStrList(address);

                // Район - населенный пункт - улица
                } else if (fieldsMap.containsKey("district") && fieldsMap.containsKey("locality")
                                                           && fieldsMap.containsKey("street")) {
                    list = mySqlDAO.getDistLocalAndStrList(address);
                }
                break;

            case 4:
                // Регион - район - город - улица
                if (fieldsMap.containsKey("region") && fieldsMap.containsKey("district")
                    && fieldsMap.containsKey("town") && fieldsMap.containsKey("street")) {
                    list = mySqlDAO.getRegDistTownAndStrList(address);

                // Регион - район - населенный пункт - улица
                } else if (fieldsMap.containsKey("region") && fieldsMap.containsKey("district")
                        && fieldsMap.containsKey("locality") && fieldsMap.containsKey("street")) {
                    list = mySqlDAO.getRegDistLocalAndStrList(address);
                }
                break;

            default: break;
        }
        
        return list;
    }
}