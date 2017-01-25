
var app = angular.module('fias', []);

app.controller('fiasCtrl', ['$scope', '$http', function ($scope, $http) {

    $scope.showResult = true;

    $scope.fetch = function () {
        $scope.showResult = true;
        var address = {
            region: $scope.region,
            district: $scope.district,
            town: $scope.town,
            locality: $scope.locality,
            street: $scope.street
        }

        var request = {
            method: 'POST',
            url: '/usability',
            data: address
        };

        var res = $http(request).then(function (response) {
            $scope.message = response;
        });
    }

    $scope.clear = function () {
        $scope.region = "";
        $scope.district = "";
        $scope.town = "";
        $scope.locality = "";
        $scope.street = "";
        $scope.showResult = false;
    }

}]);

app.filter("fiasFilt", function () {
    return function (text) {
       var arrInp = text.split(", ");
       var arrOut = [];
       var result = "";
       for (var i = 0; i < arrInp.length; i++) {
           if (arrInp[i].indexOf("null") == -1) {
               arrOut.push(arrInp[i]);
           }
       }
       for (var i = 0; i < arrOut.length; i++) {
           if (i == (arrOut.length - 1)) result = result + arrOut[i];
           else result = result + arrOut[i] + ", ";
       }
       return result;
   }
});