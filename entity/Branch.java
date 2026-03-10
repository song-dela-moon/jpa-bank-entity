package jpa.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Branches")
@Getter
@Setter
@NoArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Branch_id")
    private Long id;

    @Column(name = "Branch_name", nullable = false, length = 45)
    private String branchName;

    @Column(name = "Street_address", nullable = false, length = 100)
    private String streetAddress;

    @Column(name = "City", nullable = false, length = 45)
    private String city;

    @Column(name = "State", nullable = false, length = 45)
    private String state;

    @Column(name = "ZipCode", nullable = false, length = 45)
    private String zipCode;

    @OneToMany(mappedBy = "branch")
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "branch")
    private List<BranchEmployee> branchEmployees = new ArrayList<>();
}
