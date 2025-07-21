package com.carserviceapp.service;

import com.carserviceapp.interfaces.ReportGenerator;
import com.carserviceapp.interfaces.StringFormatter;
import com.carserviceapp.model.ServiceRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Generates a report on service performance (e.g., completed requests, average time).
 */
public class ServicePerformanceReport implements ReportGenerator {
    private List<ServiceRequest> completedServiceRequests;
    private LocalDate reportDate;

    public ServicePerformanceReport(List<ServiceRequest> completedServiceRequests) {
        this.completedServiceRequests = completedServiceRequests;
        this.reportDate = LocalDate.now();
    }

    public List<ServiceRequest> getCompletedServiceRequests() {
        return completedServiceRequests;
    }

    public void setCompletedServiceRequests(List<ServiceRequest> completedServiceRequests) {
        this.completedServiceRequests = completedServiceRequests;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    @Override
    public String generateReport() {
        int totalCompletedRequests = completedServiceRequests.size();
        double totalEstimatedTime = completedServiceRequests.stream()
                .mapToDouble(ServiceRequest::estimateTime)
                .sum();
        double averageEstimatedTime = totalCompletedRequests > 0 ? totalEstimatedTime / totalCompletedRequests : 0;

        StringBuilder report = new StringBuilder();
        report.append("\n--- SERVICE PERFORMANCE REPORT (").append(reportDate).append(") ---\n");
        report.append("Total Completed Service Requests: ").append(totalCompletedRequests).append("\n");
        report.append("Total Estimated Work Time: ").append(String.format("%.1f", totalEstimatedTime)).append(" hours\n");
        report.append("Average Estimated Time per Request: ").append(String.format("%.1f", averageEstimatedTime)).append(" hours\n");

        report.append("\n--- Completed Request Details ---\n");
        if (!completedServiceRequests.isEmpty()) {
            StringFormatter<ServiceRequest> formatter =
                    req -> "  Request ID: " + req.getId()
                            + ", Vehicle: " + req.getVehicle().getDisplayInfo()
                            + ", Cost: $" + String.format("%.2f", req.calculateCost())
                            + ", Time: " + String.format("%.1f", req.estimateTime()) + " hrs\n";
            completedServiceRequests.stream().map(formatter::format).forEach(report::append);
        } else {
            report.append("No completed service requests to report.\n");
        }

        report.append("-------------------------------------------\n");
        return report.toString();
    }

    @Override
    public String toString() {
        return "Service Performance Report generated on " + reportDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServicePerformanceReport that = (ServicePerformanceReport) o;
        return Objects.equals(completedServiceRequests, that.completedServiceRequests) &&
                Objects.equals(reportDate, that.reportDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completedServiceRequests, reportDate);
    }
}