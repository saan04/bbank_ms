package com.bloodbank.donor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "donors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Blood group is required")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Invalid blood group")
    @Column(name = "blood_group", nullable = false)
    private String bloodGroup;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Email(message = "Invalid email address")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;

    @Column(name = "last_donation_date")
    private LocalDate lastDonationDate;

    @Min(value = 0, message = "Number of donations cannot be negative")
    @Column(name = "donation_count")
    private Integer donationCount = 0;

    @Column(name = "eligible")
    private Boolean eligible = true;
}
