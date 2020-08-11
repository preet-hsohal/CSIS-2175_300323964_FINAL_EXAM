package com.company;

public class LoanP {
    String clientNumber, clientName, loanType;
    int noOfYears;
    double loanAmount;

    LoanP(String clientNumber, String clientName, String loanType, int noOfYears, double loanAmount) {
        this.clientNumber = clientNumber;
        this.clientName = clientName;
        this.loanType = loanType;
        this.noOfYears = noOfYears;
        this.loanAmount = loanAmount;
    }
}
