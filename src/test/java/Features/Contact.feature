Feature: ContactExample

  @Regression @UNIQUE-PerformContactCreateandDelete
  Scenario: Perform Contact Create and Delete
    Given Create a contact 98765432101
    Then Assert created contact 98765432101
    And Delete created contact 98765432101
    And Assert deleted contact

  @Regression @UNIQUE-PerformContactCreateModifyandDelete
  Scenario: Perform Contact Create, Modify and Delete
    Given Create a contact 98765432104
    Then Assert created contact 98765432104
    When Modify created contact 98765432104
    Then Assert modified contact
    And Delete created contact 98765432104
    And Assert deleted contact

  @Regression @UNIQUE-PerformContactCreationwithemptyphonenumber
  Scenario: Perform Contact Creation with empty phone number
    Given Create an empty phone number contact
    Then Assert contact creation failed

  @Regression @Sanity @UNIQUE-PerformContactCreationtwice
  Scenario: Perform Contact Creation twice
    Given Create a contact 98765432103
    Then Assert created contact 98765432103
    Given Create a contact 98765432103
    Then Assert already created contact
    And Delete created contact 98765432103
    And Assert deleted contact

  @Regression @UNIQUE-PerformContactModifywithemptyphonenumber
  Scenario: Perform Contact Modify with empty phone number
    Given Create a contact 98765432105
    Then Assert created contact 98765432105
    When Modify created contact with empty phone number
    Then Assert contact modification failed
    And Delete created contact 98765432105
    And Assert deleted contact

  @Regression @UNIQUE-PerformContactModificationwithoutcreate
  Scenario: Perform Contact Modification without create
    Given Modify created contact 98765432107
    And Assert modify contact which doesn't exists

  @Regression @UNIQUE-PerformContactDeletewithoutcreate
  Scenario: Perform Contact Delete without create
    Given Delete a contact which was not created 98765432106
    And Assert delete contact which doesn't exists