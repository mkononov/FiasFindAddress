
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html ng-app="fias">

<head>
    <title>Поиск адреса</title>
    <script src="resources/js/lib/angular.min.js"></script>
    <style>
        input::-webkit-calendar-picker-indicator {
            display: none;
        }
    </style>
</head>

<body ng-controller="fiasCtrl" ng-cloak="">

    <h3>Поиск по БД ФИАС</h3>

    <input placeholder="Регион" ng-model="region" ng-keypress="fetch()" autocomplete="off" list="reg" autofocus/><br><br>
    <input placeholder="Район" ng-model="district" ng-keypress="fetch()" autocomplete="off" list="dist"/><br><br>
    <input placeholder="Город" ng-model="town" ng-keypress="fetch()" autocomplete="off" list="town"/><br><br>
    <input placeholder="Нас. пункт" ng-model="locality" ng-keypress="fetch()" autocomplete="off" list="locality"/><br><br>
    <input placeholder="Улица" ng-model="street" ng-keypress="fetch()" autocomplete="off" list="street"/><br><br>

    <input type="button" value="Очистить" ng-click="clear()">

    <datalist id="reg">
        <option onselect="console.log(s)" ng-repeat="address in message.data">{{address.region}}</option>
    </datalist>
    <datalist id="dist">
        <option ng-repeat="address in message.data">{{address.district}}</option>
    </datalist>
    <datalist id="town">
        <option ng-repeat="address in message.data">{{address.town}}</option>
    </datalist>
    <datalist id="locality">
        <option ng-repeat="address in message.data">{{address.locality}}</option>
    </datalist>
    <datalist id="street">
        <option ng-repeat="address in message.data">{{address.street}}</option>
    </datalist>

    <br><br>

    <div id="output" ng-repeat="fias in message.data" ng-show="showResult">
        {{fias.region + " " + fias.regionShortName + ", " +
        fias.district + " " + fias.districtShortName + ", " +
        fias.town + " " + fias.townShortName + ", " +
        fias.locality + " " + fias.localityShortName + ", " +
        fias.street + " " + fias.streetShortName | fiasFilt}} <br>
    </div>

    <script src="resources/js/main.js"></script>

</body>

</html>