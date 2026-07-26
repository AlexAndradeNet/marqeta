@authorize
Feature: Authorize transaction

  Scenario: Authorize a transaction
    Given I am a user with a valid credit card
    When I simulate an authorization
    Then the response of transaction is PENDING
