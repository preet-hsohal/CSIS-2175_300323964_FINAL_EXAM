package com.company;

public class LoanPayment {
    double payment, principal, interest, monthlyPayment, balance;

    public LoanPayment(double payment, double principal, double interest, double monthlyPayment, double balance) {
        this.payment = payment;
        this.principal = principal;
        this.interest = interest;
        this.monthlyPayment = monthlyPayment;
        this.balance = balance;
    }
}
