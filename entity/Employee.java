package jpa.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Employee_id")
    private Long id;

    @Column(name = "First_Name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "Last_Name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "Title", length = 45)
    private String title;

    @OneToMany(mappedBy = "employee")
    private List<BranchEmployee> branchEmployees = new ArrayList<>();
}
