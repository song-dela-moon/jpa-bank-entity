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

    //은행 중심의 entity 라서 계좌가 주인이 된다.
    @ManyToMany
    @JoinTable( // 중간테이블 생성 어노테이션
        name = "Account_Customers",
        joinColumns = @JoinColumn(name = "Account_id"), // 내 PK
        inverseJoinColumns = @JoinColumn(name = "Customer_id") // 상대방 PK
    ) // 실무에선 M:N 을 왠만하면 안만들려고하지만, 실제로 만든다고한다면 보통 직접 생성한다.
    // (이 경우에는 'BranchEmployee' 처럼 다른 필드가 생성될 필요가 없어서 @JoinTable 를 사용함.)
    private List<Customer> customers = new ArrayList<>();
}
