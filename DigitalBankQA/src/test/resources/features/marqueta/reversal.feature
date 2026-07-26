@reversal
Feature: Reverse a previous transaction

  Scenario: Reverse a previous transaction
    Given I am a user with an authorized transaction to reverse
    When I reverse the transaction
    Then the response of reversal is CLEARED
