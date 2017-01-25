package ru.inrecolan.study.dao.objects;

public class Address {

    /*
    * Поля могут добавляться по мере необходимости.
    * Здесь представлены только те свойства, которые необходимы для решения поставленной задачи
    */

    private String region;        // Регион
    private String district;      // Район
    private String town;          // Город
    /*private String intraCityArea; // Внутригородская территория*/
    private String locality;      // Населенный пункт
    private String street;        // Улица

    // Краткие наименования типов объектов
    // Необходимо на стороне клиента для анализа и правильного заполнения отправляемого на сервер объекта
    private String regionShortName;
    private String districtShortName;
    private String townShortName;
    private String intraCityAreaShortName;
    private String localityShortName;
    private String streetShortName;


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    /*public String getIntraCityArea() {
        return intraCityArea;
    }

    public void setIntraCityArea(String intraCityArea) {
        this.intraCityArea = intraCityArea;
    }*/

    public String getRegionShortName() {
        return regionShortName;
    }

    public void setRegionShortName(String regionShortName) {
        this.regionShortName = regionShortName;
    }

    public String getDistrictShortName() {
        return districtShortName;
    }

    public void setDistrictShortName(String districtShortName) {
        this.districtShortName = districtShortName;
    }

    public String getTownShortName() {
        return townShortName;
    }

    public void setTownShortName(String townShortName) {
        this.townShortName = townShortName;
    }

    /*public String getIntraCityAreaShortName() {
        return intraCityAreaShortName;
    }

    public void setIntraCityAreaShortName(String intraCityAreaShortName) {
        this.intraCityAreaShortName = intraCityAreaShortName;
    }*/

    public String getLocalityShortName() {
        return localityShortName;
    }

    public void setLocalityShortName(String localityShortName) {
        this.localityShortName = localityShortName;
    }

    public String getStreetShortName() {
        return streetShortName;
    }

    public void setStreetShortName(String streetShortName) {
        this.streetShortName = streetShortName;
    }

}
