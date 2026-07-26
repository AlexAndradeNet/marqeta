@clearing
Feature: Mimic clearing a transaction in BF bank

  # It's necessary to mimic clearing a transaction in BF bank
  # because Marqeta take 20 minutes to clear a transaction
  # and we don't want to wait that long for our tests to run

  Scenario: Mimic clearing a previous transaction
    Given I am a user with a transaction that has been cleared
    When I mimic clearing the transaction
    Then the clearing response from BF bank is successful
