package jpa.practice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BranchEmployeeId implements Serializable {

    @Column(name = "Branch_id")
    private Long branchId;

    @Column(name = "Employee_id")
    private Long employeeId;
}
