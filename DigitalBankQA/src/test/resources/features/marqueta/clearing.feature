@clearing
Feature: Clear a previous transaction

  Scenario: Clear a previous transaction
    Given I am a user with an authorized transaction to clear
    When I clear the transaction
    Then the response of clearing is COMPLETION
