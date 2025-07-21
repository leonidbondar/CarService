package com.carserviceapp.service;

import com.carserviceapp.interfaces.ReportGenerator;
import com.carserviceapp.interfaces.StringFormatter;
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
            com.carserviceapp.interfaces.StringFormatter<com.carserviceapp.model.Invoice> invoiceFormatter =
                    invoice -> "  " + invoice.getDisplayInfo() + "\n";
            invoices.stream().map(invoiceFormatter::format).forEach(report::append);
        } else {
            report.append("\nNo invoices recorded.\n");
        }

        if (!payments.isEmpty()) {
            report.append("\nPAYMENTS:\n");
            com.carserviceapp.interfaces.StringFormatter<com.carserviceapp.model.Payment> paymentFormatter =
                    payment -> "  " + payment.toString() + "\n";
            payments.stream().map(paymentFormatter::format).forEach(report::append);
        } else {
            report.append("\nNo payments recorded.\n");
        }

        report.append("-------------------------------------------\n");
        return report.toString();
    }

    /**
     * Generates a report with a custom invoice formatter.
     */
    public String generateReportWithInvoiceFormatter(com.carserviceapp.interfaces.StringFormatter<com.carserviceapp.model.Invoice> formatter) {
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
            invoices.stream().map(formatter::format).forEach(report::append);
        } else {
            report.append("\nNo invoices recorded.\n");
        }
        if (!payments.isEmpty()) {
            report.append("\nPAYMENTS:\n");
            com.carserviceapp.interfaces.StringFormatter<com.carserviceapp.model.Payment> paymentFormatter =
                    payment -> "  " + payment.toString() + "\n";
            payments.stream().map(paymentFormatter::format).forEach(report::append);
        } else {
            report.append("\nNo payments recorded.\n");
        }
        report.append("-------------------------------------------\n");
        return report.toString();
    }

    /**
     * Example static method to demonstrate using StringFormatter with a lambda Use StringFormatter to add a custom prefix to each invoice
     */
    public static String demoCustomInvoiceFormatting(FinancialReport report, String prefix) {
        StringFormatter<Invoice> customFormatter =
                invoice -> prefix + invoice.getDisplayInfo() + "\n";
        return report.generateReportWithInvoiceFormatter(customFormatter);
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