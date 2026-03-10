package jpa.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Account_id")
    private Long id;

    @Column(name = "Account_type", nullable = false)
    private Integer accountType;

    @Column(name = "Account_balance", nullable = false)
    private Integer accountBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Branch_id", nullable = false)
    private Branch branch;

    @Column(name = "Date_Opened", nullable = false)
    private LocalDate dateOpened;

    @ManyToMany
    @JoinTable(
        name = "Account_Customers",
        joinColumns = @JoinColumn(name = "Account_id"),
        inverseJoinColumns = @JoinColumn(name = "Customer_id")
    )
    private List<Customer> customers = new ArrayList<>();
}
