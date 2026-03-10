package jpa.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Branch_Employees")
@Getter
@Setter
@NoArgsConstructor
public class BranchEmployee {

    @EmbeddedId
    private BranchEmployeeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("branchId")
    @JoinColumn(name = "Branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "Employee_id")
    private Employee employee;

    @Column(name = "Start_Date", nullable = false)
    private LocalDate startDate;

    @Column(name = "End_Date")
    private LocalDate endDate;
}
