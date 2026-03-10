package jpa.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Customer_id")
    private Long id;

    @Column(name = "First_name", length = 45)
    private String firstName;

    @Column(name = "Last_name", length = 45)
    private String lastName;

    @Column(name = "Date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "Street_address", nullable = false, length = 100)
    private String streetAddress;

    @Column(name = "City", nullable = false, length = 45)
    private String city;

    @Column(name = "State", nullable = false, length = 45)
    private String state;

    @Column(name = "ZipCode", nullable = false, length = 45)
    private String zipCode;

    @Column(name = "Country", length = 45)
    private String country;

    @Column(name = "Email", length = 45)
    private String email;

    @Column(name = "Sex", length = 1)
    private Character sex;

    @ManyToMany(mappedBy = "customers")
    private List<Account> accounts = new ArrayList<>();
}
