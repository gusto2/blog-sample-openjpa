            // name, dependencies
            var app = angular.module('personListApp', ['ngResource', 'ngRoute']);

            app.config(function($routeProvider) {
                $routeProvider
                        .when('/', {
                    controller: 'PersonListController',
                    templateUrl: './views/personlist.html'
                })
                        .when('/detail/:personId', {
//                        .when('/orders', {
                    controller: 'PersonDetailController',
                    templateUrl: './views/persondetail.html'
                });
            });

            app.controller('PersonListController', function($scope, personListFactory) {
                function init() {
                    $scope.personName = '';
                    $scope.personBorn = '';
                    $scope.personRRN = '';
                    $scope.errMessage = '';
                    personListFactory.query(
                            function(data) {
                                $scope.plist = data;
                            },
                            function (err) {
                                $scope.errMessage = err.status + ', '+err.data;
                             } );
                }
                init();
                
                $scope.createPerson = function(name, bornDate, rrn) {
                    personListFactory.save({
                        name: name,
                        born: bornDate,
                        rrn: rrn
                    },
                    function () {
                        init();
                    },
                    function (err) {
                        if (!err.data) {
                            $scope.errMessage = err;
                        }
                        else if(!err.data.err) {
                            $scope.errMessage = err.data;
                        }
                        else {
                            $scope.errMessage = err.data.err;
                        }
                    });
                    
                }
            });
            
            app.controller('PersonDetailController', function($scope, $routeParams, $location, personListFactory) {
                $scope.params = $routeParams;
                function init() {
                    $scope.errMessage = '';
                    personListFactory.get(
                         {personId: $scope.params.personId},
                         function(data) {
                            $scope.person = data;
                            },
                         function(err) {
                            $scope.errMessage = err.status + ', '+err.data;
                        });
                }
                init();
                $scope.deletePerson = function(personId) {
                    personListFactory.remove({personId: personId},function() {
                            $location.path('/');
                        },function(err) {
                            $scope.errMessage = err.status + ', '+err.data;
                        });
                        
                }
            });            


//            app.config(['$resourceProvider', function ($resourceProvider) {
//                // Don't strip trailing slashes from calculated URLs
//                $resourceProvider.defaults.stripTrailingSlashes = false;
//            }]);

            app.factory('personListFactory', function($resource) {
                // rrn is mapped automatically as a query parameter
                // if not present in the template
                var factory = $resource('./person/:personId');

                return factory;
            });


