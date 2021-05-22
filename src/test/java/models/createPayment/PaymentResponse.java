package models.createPayment;

import lombok.Data;

@Data
public class PaymentResponse {
    private Double amount;
    private Integer paymentId;
    private String toAcctNo;
    private String paymentCreatedDate;
    private CustomerDetails customerDetails;
    private String fromAcctNo;
    private String paymentStatus;
    private String remarks;
}
