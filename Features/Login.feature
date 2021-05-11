@POC  @TRA
Feature: TRA Public Website - POC

  @tra @webapp @mobileapp @TRA-128 @smoke
  Scenario: As a Consumer, I am able to navigate to TRA in Numbers section
    Given TRA public website is loaded successfully
    When I as a public user navigates to the TRA in Numbers Section
    And I select Market Information Option
    Then I am able to see the Market Information sub options


  @tra @webapp @mobileapp @TRA-129 @regression
  Scenario: As a Consumer, I am able to validate search Options
    Given TRA public website is loaded successfully
    When I as a public user click on the search icon
    And I am able to view the search options
    And I am able to enter the key in the search text

