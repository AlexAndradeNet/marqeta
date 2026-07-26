@reversal
Feature: Mimic reversal a transaction in BF bank

  # It's necessary to mimic clearing a transaction in BF bank
  # because Marqeta take 20 minutes to clear a transaction
  # and we don't want to wait that long for our tests to run

  Scenario: Mimic reversal a previous transaction
    Given I am a user with a transaction that has been reversed
    When I mimic reversal the transaction
    Then the reversal response from BF bank is successful
