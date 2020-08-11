package com.company;

import java.util.ArrayList;

public class Personal extends LoanP implements Generate {
    double interestRate = 6;
    Personal(String clientNumber, String clientName, String loanType, int noOfYears, double loanAmount) {
        super(clientNumber, clientName, loanType, noOfYears, loanAmount);
    }

    public double calMonthlyPayment() {
        double monthlyRate = this.interestRate/12;
        double termInMonths = noOfYears*12;

        return (loanAmount * monthlyRate/100)/(1 - Math.pow(1 + monthlyRate/100, -termInMonths));
    }

    @Override
    public ArrayList<LoanPayment> generateTable() {
        ArrayList<LoanPayment> loanPayments = new ArrayList<>();
        loanPayments.add(new LoanPayment(0.0, 0.0, 0.0, 0.0, loanAmount));
        double monthlyPayment = calMonthlyPayment();
        for (int i = 1; i <= noOfYears*12; i++) {
            double balance = loanPayments.get(i - 1).balance;
            double interest = monthlyPayment * this.interestRate /(12*100);
            double principal = monthlyPayment - interest;
            loanPayments.add(new LoanPayment(i, principal, interest, monthlyPayment, balance - monthlyPayment));
        }

        return loanPayments;
    }
}
