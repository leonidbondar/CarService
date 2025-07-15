package com.carserviceapp.service;

import com.carserviceapp.interfaces.ReportGenerator;
import com.carserviceapp.model.Invoice;
import com.carserviceapp.model.Payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Generates a financial report based on invoices and payments.
 */
public class FinancialReport implements ReportGenerator {
    private List<Invoice> invoices;
    private List<Payment> payments;
    private LocalDate reportDate;

    public FinancialReport(List<Invoice> invoices, List<Payment> payments) {
        this.invoices = invoices;
        this.payments = payments;
        this.reportDate = LocalDate.now();
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    @Override
    public String generateReport() {
        double totalInvoicedAmount = invoices.stream().mapToDouble(Invoice::getAmount).sum();
        double totalPaymentsReceived = payments.stream().mapToDouble(Payment::getAmount).sum();
        double outstandingBalance = totalInvoicedAmount - totalPaymentsReceived;

        StringBuilder report = new StringBuilder();
        report.append("\n--- FINANCIAL REPORT (").append(reportDate).append(") ---\n");
        report.append("Total Invoiced Amount: $").append(String.format("%.2f", totalInvoicedAmount)).append("\n");
        report.append("Total Payments Received: $").append(String.format("%.2f", totalPaymentsReceived)).append("\n");
        report.append("Outstanding Balance:     $").append(String.format("%.2f", outstandingBalance)).append("\n");
        report.append("\n--- Details ---\n");

        if (!invoices.isEmpty()) {
            report.append("\nINVOICES:\n");
            invoices.forEach(invoice -> report.append("  ").append(invoice.getDisplayInfo()).append("\n"));
        } else {
            report.append("\nNo invoices recorded.\n");
        }

        if (!payments.isEmpty()) {
            report.append("\nPAYMENTS:\n");
            payments.forEach(payment -> report.append("  ").append(payment.toString()).append("\n"));
        } else {
            report.append("\nNo payments recorded.\n");
        }

        report.append("-------------------------------------------\n");
        return report.toString();
    }

    @Override
    public String toString() {
        return "Financial Report generated on " + reportDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialReport that = (FinancialReport) o;
        return Objects.equals(invoices, that.invoices) &&
                Objects.equals(payments, that.payments) &&
                Objects.equals(reportDate, that.reportDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoices, payments, reportDate);
    }
}