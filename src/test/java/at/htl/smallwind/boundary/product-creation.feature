Feature: Product creation endpoint.
  An user of the endpoint is able to create Products

  Background:
    * url baseUrl

  Scenario: Create a Person
    Given path 'products'
    And request { name: "Timmie's Hundefutter", unitPrice: 2.30, unitsInStock: 25, discontinued: "false" }
    When method POST
    Then status 201
    And match header Location contains 'http://localhost:8081/products/78'

#  Scenario: Create a Person and use a json instead of inline
#    Given path 'person'
#    And request read('person-halil.json')
#    When method POST
#    Then status 200
#    And match response == { id: '#number', name: 'halil', age: 18 }
#    * def inlineFunction = function(name) { return 'hello ' + name }
#    * print inlineFunction('halil')
#    * def getRandomRange =
#    """
#    function(max, min) {
#      return Math.random() * (max-min) + min;
#    }
#    """
#    * print getRandomRange(5, 10)
#    * def sumFromJava =
#    """
#    function(a, b) {
#      var Helper = Java.type('at.htl.demo.util.Helper');
#      // Access static method
#      var sum = Helper.add(a, b);
#      // Create Object
#      var Person = Java.type('at.htl.demo.entity.Person');
#      var susi = new Person('susi', 18);
#      return sum;
#    }
#    """
#    * print sumFromJava(15, 13)

#  Scenario: Create a person where the age is 0 or below
#  Scenario: Create a person where the name is too long
#  Scenario: Create a person where the chracters are non ascii
