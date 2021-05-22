package models.createPayment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class CustomerDetails {
    private Long ecn;
    private Long phoneNumber;
    private String firstName;
    private String custId;
    private String lastName;
    private Long ssn;
}
