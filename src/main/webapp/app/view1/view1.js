'use strict';

angular.module('myApp.view1', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
      $routeProvider.when('/view1', {
        templateUrl: 'view1/view1.html',
        controller: 'View1Ctrl'
      });
    }])
    .controller('View1Ctrl', function ($scope, $http) {

      $scope.budgetHistory = [] ;
      $scope.showTable=false;
      $scope.newDateInput = null;
      $scope.newTimeInput = null;
      $scope.newBudgetInput = null;
      $scope.previousDate;
      var obj={},obj1={},obj2={},obj3={},obj4={},obj5={},obj6={},obj7={},obj8={};

      obj.date= new Date(2019,0,1,10 );
      obj.budget=7;
      $scope.budgetHistory.push(obj);

      obj1.date= new Date(2019,0,1,11 );
      obj1.budget=0;
      $scope.budgetHistory.push(obj1);

      obj2.date= new Date(2019,0,1,12 );
      obj2.budget=1;
      $scope.budgetHistory.push(obj2);

      obj3.date= new Date(2019,0,1,23,0 );
      obj3.budget=6;
      $scope.budgetHistory.push(obj3);

      obj4.date= new Date(2019,0,5,10,0 );
      obj4.budget=2;
      $scope.budgetHistory.push(obj4);

      obj5.date= new Date(2019,0,6,0,0 );
      obj5.budget=0;
      $scope.budgetHistory.push(obj5);

      obj6.date= new Date(2019,1,9,13,13);
      obj6.budget=8;
      $scope.budgetHistory.push(obj6);

      obj7.date= new Date(2019,2,1,12,0 );
      obj7.budget=0;
      $scope.budgetHistory.push(obj7);

      obj8.date= new Date(2019,2,1,14,0 );
      obj8.budget=1;
      $scope.budgetHistory.push(obj8);
      console.log("scope budget = "+JSON.stringify($scope.budgetHistory));

      $scope.processData = function() {
        fetch('http://localhost:8080/api/processData', {
          method: 'POST', // or 'PUT'
          headers: {
            'Content-Type': 'application/json',
          },
          body: angular.toJson($scope.budgetHistory),
        })
            .then(response => response.json())
            .then(data => {
              $scope.data=data.costList;
              $scope.bugetMaxList=data.reportModelList;
              $scope.drawTable();
            })
            .catch((error) => {
              console.error('Error:', error);
            });
      };
      $scope.getDate = function( data ) {
        return new Date(data).toLocaleDateString()
      }
      $scope.getTime = function( data ) {
        return new Date(data).toLocaleTimeString()
      }
      $scope.checkDate = function( data ) {
        if(data!=$scope.previousDate){
          $scope.previousDate=data;
          return true;
        }
        else return false;

      }
      $scope.addDate = function () {
        let time, date;

        if ($scope.newDateInput) {
          date = $scope.newDateInput.toString().slice(4,15);
        }

        if ($scope.newTimeInput) {
          time = $scope.newTimeInput.toString().slice(16,21);
        }

        let newDate = new Date(date + ' ' + time);
        let addObj = {};
        addObj.date = newDate;
        addObj.budget = $scope.newBudgetInput;

        $scope.budgetHistory.push(addObj);
        $scope.budgetHistory.sort(function (a,b) {
          return a.date - b.date;
        })

        $scope.newBudgetInput = null;
        $scope.newDateInput = null;
        $scope.newTimeInput = null;
      }

      $scope.deleteInput = function (input) {
        let i;
        for (i = 0; i < $scope.budgetHistory.length; i++ ) {
          if ($scope.budgetHistory[i].date.toString() == input.date.toString() && $scope.budgetHistory[i].budget == input.budget) {
            $scope.budgetHistory.splice(i, 1);
          }
        }
      }

      $scope.drawTable = function() {
        $scope.showTable=true;
        console.log('Success:', $scope.data);
        $scope.$apply();
      }

    });
