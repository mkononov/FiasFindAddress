package ru.inrecolan.study.dao.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.inrecolan.study.dao.interfaces.AddressDAO;
import ru.inrecolan.study.dao.objects.Address;
import ru.inrecolan.study.dao.mapper.AddressRowMapper;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
* Spring-компонент mySqlDAO осуществляет запрос к БД, зная параметры, которые пользователь ввел в качестве
* адресных элементов, и возвращает в MainController список с результатами запроса.
* Для каждого набора параметров используется свой, точный запрос.
*/
@Component(value = "mySqlDAO")
public class MySqlDAO implements AddressDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /* Если клиент заполнил 1 поле */

    // Получить список регионов
    public List<Address> getRegList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.shortname !='г' AND t1.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список районов
    public List<Address> getDistList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid AND t1.aolevel=1 AND t2.aolevel=3 " +
                     "WHERE t1.actstatus=1 AND " +
                           "t2.formalname LIKE :district AND t2.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("district", address.getDistrict() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список федеральных городов (1 уровень дерева)
    public List<Address> getTownListLev1(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :town AND t1.shortname='г' AND t1.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("town", address.getTown() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список городов в регионе (2 уровень дерева)
    public List<Address> getTownListLev2(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=4 AND t2.formalname LIKE :town AND t2.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("town", address.getTown() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список городов в районе (3-ий уровень дерева)
    public List<Address> getTownListLev3(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.actstatus=1 AND " +
                           "t3.aolevel=4 AND t3.actstatus=1 AND t3.formalname LIKE :town AND t3.shortname='г' LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("town", address.getTown() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список населенных пунктов
    public List<Address> getLocalList(Address address) {
        String sql = "SELECT * FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.actstatus=1 AND " +
                           "t3.formalname LIKE :locality AND t3.aolevel in (4, 6) AND t3.actstatus=1 LIMIT 3";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("locality", address.getLocality() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    /* Если клиент заполнил 2 поля */

    // Получить список районов, зная регион
    public List<Address> getRegAndDistList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.formalname LIKE :district AND t2.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("district", address.getDistrict() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список городов (2 уровень дерева), зная регион
    public List<Address> getRegAndTownListLev2(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=4 AND t2.formalname LIKE :town AND t2.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("town", address.getTown() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список городов (3 уровень дерева), зная регион
    public List<Address> getRegAndTownListLev3(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.actstatus=1 AND " +
                           "t3.aolevel=4 AND t3.actstatus=1 AND t3.formalname LIKE :town AND t3.shortname='г' LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("town", address.getTown() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список населенных пунктов, зная регион
    public List<Address> getRegAndLocalList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.actstatus=1 AND " +
                           "t3.aolevel in (4, 6) AND t3.actstatus=1 AND t3.formalname LIKE :locality LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("locality", address.getLocality() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список городов, зная район
    public List<Address> getDistAndTownList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.formalname LIKE :district AND t2.actstatus=1 AND " +
                           "t3.aolevel=4 AND t3.formalname LIKE :town AND t3.actstatus= 1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("district", address.getDistrict() + "%");
        param.addValue("town", address.getTown() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список населенных пунктов, зная район
    public List<Address> getDistAndLocalList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.formalname LIKE :district AND t2.actstatus=1 AND " +
                           "t3.aolevel in (4, 6) AND t3.actstatus=1 AND t3.formalname LIKE :locality LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("district", address.getDistrict() + "%");
        param.addValue("locality", address.getLocality() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная город на 1-м уровне дерева
    public List<Address> getTownAndStrListLev1(Address address) {
        String sql = "SELECT * "+
                     "FROM addrobj AS t1 "+
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid "+
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND t1.formalname LIKE :town AND t1.shortname='г' AND "+
                           "t2.aolevel=7 AND t2.formalname LIKE :street AND t2.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("town", address.getTown() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная город на 2-м уровне дерева
    public List<Address> getTownAndStrListLev2(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=4 AND t2.formalname LIKE :town AND t2.actstatus=1 AND " +
                           "t3.aolevel=7 AND t3.actstatus=1 AND t3.formalname LIKE :street LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("town", address.getTown() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная город на 3-м уровень дерева
    public List<Address> getTownAndStrListLev3(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                        "LEFT JOIN addrobj AS t4 ON t3.aoguid = t4.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.actstatus=1 AND " +
                           "t3.aolevel=4 AND t3.actstatus=1 AND t3.formalname LIKE :town AND " +
                           "t4.aolevel=7 AND t4.formalname LIKE :street AND t4.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("town", address.getTown() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная населенный пункт
    public List<Address> getLocalAndStrList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                        "LEFT JOIN addrobj AS t4 ON t3.aoguid = t4.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.actstatus=1 AND " +
                           "t3.aolevel in (4,6) AND t3.actstatus=1 AND t3.formalname LIKE :locality AND " +
                           "t4.aolevel=7 AND t4.formalname LIKE :street AND t4.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("locality", address.getLocality() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    /* Если клиент заполнил 3 поля */

    // Получить список городов, зная регион и район
    public List<Address> getRegDistAndTownList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.formalname LIKE :district AND t2.actstatus=1 AND " +
                           "t3.aolevel=4 AND t3.formalname LIKE :town AND t3.actstatus= 1 AND t3.shortname='г' LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("district", address.getDistrict() + "%");
        param.addValue("town", address.getTown() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список населенных пунктов, зная регион и район
    public List<Address> getRegDistAndLocalList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.formalname LIKE :district AND t2.actstatus=1 AND " +
                           "t3.aolevel in (4, 6) AND t3.formalname LIKE :locality AND t3.actstatus= 1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("district", address.getDistrict() + "%");
        param.addValue("locality", address.getLocality() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная регион и город на 2-м уровне дерева
    public List<Address> getRegTownAndStrListLev2(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=4 AND t2.formalname LIKE :town AND t2.actstatus=1 AND " +
                           "t3.aolevel=7 AND t3.formalname LIKE :street AND t3.actstatus= 1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("town", address.getTown() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная регион и город на 3-м уровне дерева
    public List<Address> getRegTownAndStrListLev3(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                        "LEFT JOIN addrobj AS t4 ON t3.aoguid = t4.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.actstatus=1 AND " +
                           "t3.aolevel=4 AND t3.formalname LIKE :town AND t3.actstatus= 1 AND " +
                           "t4.aolevel=7 AND t4.formalname LIKE :street AND t4.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("town", address.getTown() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная регион и населенный пункт
    public List<Address> getRegLocalAndStrList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                     "LEFT JOIN addrobj AS t4 ON t3.aoguid = t4.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.actstatus=1 AND " +
                           "t3.aolevel in (4, 6) AND t3.formalname LIKE :locality AND t3.actstatus= 1 AND " +
                           "t4.aolevel=7 AND t4.formalname LIKE :street AND t4.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("locality", address.getLocality() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная район и город
    public List<Address> getDistTownAndStrList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                        "LEFT JOIN addrobj AS t4 ON t3.aoguid = t4.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.formalname LIKE :district AND t2.actstatus=1 AND " +
                           "t3.aolevel=4 AND t3.formalname LIKE :town AND t3.actstatus= 1 AND " +
                           "t4.aolevel=7 AND t4.formalname LIKE :street AND t4.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("district", address.getDistrict() + "%");
        param.addValue("town", address.getTown() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная район и населенный пункт
    public List<Address> getDistLocalAndStrList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                        "LEFT JOIN addrobj AS t4 ON t3.aoguid = t4.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.formalname LIKE :district AND t2.actstatus=1 AND " +
                           "t3.aolevel in (4, 6) AND t3.formalname LIKE :locality AND t3.actstatus= 1 AND " +
                           "t4.aolevel=7 AND t4.formalname LIKE :street AND t4.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("district", address.getDistrict() + "%");
        param.addValue("locality", address.getLocality() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    /* Если клиент заполнил 4 поля */

    // Получить список улиц, зная регион, район и город
    public List<Address> getRegDistTownAndStrList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                        "LEFT JOIN addrobj AS t4 ON t3.aoguid = t4.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.formalname LIKE :district AND t2.actstatus=1 AND " +
                           "t3.aolevel=4 AND t3.formalname LIKE :town AND t3.actstatus=1 AND " +
                           "t4.aolevel=7 AND t4.formalname LIKE :street AND t4.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("district", address.getDistrict() + "%");
        param.addValue("town", address.getTown() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

    // Получить список улиц, зная регион, район и населенный пункт
    public List<Address> getRegDistLocalAndStrList(Address address) {
        String sql = "SELECT * " +
                     "FROM addrobj AS t1 " +
                        "LEFT JOIN addrobj AS t2 ON t1.aoguid = t2.parentguid " +
                        "LEFT JOIN addrobj AS t3 ON t2.aoguid = t3.parentguid " +
                        "LEFT JOIN addrobj AS t4 ON t3.aoguid = t4.parentguid " +
                     "WHERE t1.aolevel=1 AND t1.formalname LIKE :region AND t1.actstatus=1 AND " +
                           "t2.aolevel=3 AND t2.formalname LIKE :district AND t2.actstatus=1 AND " +
                           "t3.aolevel in (4, 6) AND t3.formalname LIKE :locality AND t3.actstatus=1 AND " +
                           "t4.aolevel=7 AND t4.formalname LIKE :street AND t4.actstatus=1 LIMIT 5";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("region", address.getRegion() + "%");
        param.addValue("district", address.getDistrict() + "%");
        param.addValue("locality", address.getLocality() + "%");
        param.addValue("street", address.getStreet() + "%");
        return namedParameterJdbcTemplate.query(sql, param, new AddressRowMapper());
    }

}