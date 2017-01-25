package ru.inrecolan.study.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.inrecolan.study.dao.implementations.MySqlDAO;
import ru.inrecolan.study.dao.objects.Address;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
* Spring интерфейс RowMapper позволяет из таблиц делать выборку данных
* Чтобы правильно заполнить объект необходимо знать какой метод обратился к БД. Для этого используется Java
* Reflection.
*/
public class AddressRowMapper implements RowMapper<Address> {
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {

        MySqlDAO exemplarOfMySqlDao = new MySqlDAO();
        Class classMySqlDao = exemplarOfMySqlDao.getClass();
        Method[] methodsOfMySqlDao = classMySqlDao.getDeclaredMethods(); // Получаем все методы компонента mySqlDAO...

        List<String> methodsList = new ArrayList<>();

        for (Method method: methodsOfMySqlDao) {
            methodsList.add(method.getName());
        }

        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        String methodCalledMapRowMethod = null;

        // ... и если в стеке методов присутствует любой из методов компонента mySqlDAO значит это он обратился к БД
        for (StackTraceElement stTrEl: stackTraceElement) {
            if (methodsList.contains(stTrEl.getMethodName())) {
                methodCalledMapRowMethod = stTrEl.getMethodName();
                break;
            }
        }

        Address address = new Address();

        switch (methodCalledMapRowMethod) {

            /* Если клиент заполнил 1 поле */

            // Получить список регионов
            case "getRegList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                break;

            // Получить список районов
            case "getDistList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                break;

            // Получить список федеральных городов (1 уровень дерева)
            case "getTownListLev1":
                address.setTown(rs.getString("t1.offname"));
                address.setTownShortName(rs.getString("t1.shortname"));
                break;

            // Получить список городов в регионе (2 уровень дерева)
            case "getTownListLev2":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setTown(rs.getString("t2.offname"));
                address.setTownShortName(rs.getString("t2.shortname"));
                break;

            // Получить список городов в районе (3-ий уровень дерева)
            case "getTownListLev3":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setTown(rs.getString("t3.offname"));
                address.setTownShortName(rs.getString("t3.shortname"));
                break;

            // Получить список населенных пунктов
            case "getLocalList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setLocality(rs.getString("t3.offname"));
                address.setLocalityShortName(rs.getString("t3.shortname"));
                break;

            /* Если клиент заполнил 2 поля */

            // Получить список районов, зная регион
            case "getRegAndDistList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                break;

            // Получить список городов (2 уровень дерева), зная регион
            case "getRegAndTownListLev2":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setTown(rs.getString("t2.offname"));
                address.setTownShortName(rs.getString("t2.shortname"));
                break;

            // Получить список городов (3 уровень дерева), зная регион
            case "getRegAndTownListLev3":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setTown(rs.getString("t3.offname"));
                address.setTownShortName(rs.getString("t3.shortname"));
                break;

            // Получить список населенных пунктов, зная регион
            case "getRegAndLocalList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setLocality(rs.getString("t3.offname"));
                address.setLocalityShortName(rs.getString("t3.shortname"));
                break;

            // Получить список городов, зная район
            case "getDistAndTownList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setTown(rs.getString("t3.offname"));
                address.setTownShortName(rs.getString("t3.shortname"));
                break;

            // Получить список населенных пунктов, зная район
            case "getDistAndLocalList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setLocality(rs.getString("t3.offname"));
                address.setLocalityShortName(rs.getString("t3.shortname"));
                break;

            // Получить список улиц, зная город на 1-м уровне дерева
            case "getTownAndStrListLev1":
                address.setTown(rs.getString("t1.offname"));
                address.setTownShortName(rs.getString("t1.shortname"));
                address.setStreet(rs.getString("t2.offname"));
                address.setStreetShortName(rs.getString("t2.shortname"));
                break;

            // Получить список улиц, зная город на 2-м уровне дерева
            case "getTownAndStrListLev2":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setTown(rs.getString("t2.offname"));
                address.setTownShortName(rs.getString("t2.shortname"));
                address.setStreet(rs.getString("t3.offname"));
                address.setStreetShortName(rs.getString("t3.shortname"));
                break;

            // Получить список улиц, зная город на 3-м уровень дерева
            case "getTownAndStrListLev3":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setTown(rs.getString("t3.offname"));
                address.setTownShortName(rs.getString("t3.shortname"));
                address.setStreet(rs.getString("t4.offname"));
                address.setStreetShortName(rs.getString("t4.shortname"));
                break;

            // Получить список улиц, зная населенный пункт
            case "getLocalAndStrList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setLocality(rs.getString("t3.offname"));
                address.setLocalityShortName(rs.getString("t3.shortname"));
                address.setStreet(rs.getString("t4.offname"));
                address.setStreetShortName(rs.getString("t4.shortname"));
                break;

            /* Если клиент заполнил 3 поля */

            // Получить список городов, зная регион и район
            case "getRegDistAndTownList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setTown(rs.getString("t3.offname"));
                address.setTownShortName(rs.getString("t3.shortname"));
                break;

            // Получить список населенных пунктов, зная регион и район
            case "getRegDistAndLocalList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setLocality(rs.getString("t3.offname"));
                address.setLocalityShortName(rs.getString("t3.shortname"));
                break;

            // Получить список улиц, зная регион и город на 2-м уровне дерева
            case "getRegTownAndStrListLev2":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setTown(rs.getString("t2.offname"));
                address.setTownShortName(rs.getString("t2.shortname"));
                address.setStreet(rs.getString("t3.offname"));
                address.setStreetShortName(rs.getString("t3.shortname"));
                break;

            // Получить список улиц, зная регион и город на 3-м уровне дерева
            case "getRegTownAndStrListLev3":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setTown(rs.getString("t3.offname"));
                address.setTownShortName(rs.getString("t3.shortname"));
                address.setStreet(rs.getString("t4.offname"));
                address.setStreetShortName(rs.getString("t4.shortname"));
                break;

            // Получить список улиц, зная регион и населенный пункт
            case "getRegLocalAndStrList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setLocality(rs.getString("t3.offname"));
                address.setLocalityShortName(rs.getString("t3.shortname"));
                address.setStreet(rs.getString("t4.offname"));
                address.setStreetShortName(rs.getString("t4.shortname"));
                break;

            // Получить список улиц, зная район и город
            case "getDistTownAndStrList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setTown(rs.getString("t3.offname"));
                address.setTownShortName(rs.getString("t3.shortname"));
                address.setStreet(rs.getString("t4.offname"));
                address.setStreetShortName(rs.getString("t4.shortname"));
                break;

            // Получить список улиц, зная район и населенный пункт
            case "getDistLocalAndStrList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setLocality(rs.getString("t3.offname"));
                address.setLocalityShortName(rs.getString("t3.shortname"));
                address.setStreet(rs.getString("t4.offname"));
                address.setStreetShortName(rs.getString("t4.shortname"));
                break;

            /* Если клиент заполнил 4 поля */

            // Получить список улиц, зная регион, район и город
            case "getRegDistTownAndStrList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setTown(rs.getString("t3.offname"));
                address.setTownShortName(rs.getString("t3.shortname"));
                address.setStreet(rs.getString("t4.offname"));
                address.setStreetShortName(rs.getString("t4.shortname"));
                break;

            // Получить список улиц, зная регион, район и населенный пункт
            case "getRegDistLocalAndStrList":
                address.setRegion(rs.getString("t1.offname"));
                address.setRegionShortName(rs.getString("t1.shortname"));
                address.setDistrict(rs.getString("t2.offname"));
                address.setDistrictShortName(rs.getString("t2.shortname"));
                address.setLocality(rs.getString("t3.offname"));
                address.setLocalityShortName(rs.getString("t3.shortname"));
                address.setStreet(rs.getString("t4.offname"));
                address.setStreetShortName(rs.getString("t4.shortname"));
                break;

            default: break;
        }

        return address;
    }

}