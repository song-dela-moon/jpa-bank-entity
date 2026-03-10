package jpa.practice.entity.composite;

import jakarta.persistence.*;
import jpa.practice.entity.Account;
import jpa.practice.entity.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 시나리오 1 — 복합키(@EmbeddedId) 방식
 * <p>
 * Account와 Customer의 다대다 관계를 풀어낸 중간 엔티티.
 * PK = (Account_id, Customer_id) 복합키
 * <p>
 * 장점:
 * - 별도 PK 컬럼이 없어 저장 공간 절약
 * - DB 레벨에서 동일 Account-Customer 조합의 중복 등록이 원천 차단됨
 * - 비즈니스 키 == PK이므로 의미가 명확
 * <p>
 * 단점:
 * - 복합키를 다른 테이블에서 FK로 참조할 때 컬럼이 2개 필요
 * - 조인 쿼리가 상대적으로 복잡해짐
 * - 키 구성이 변경되면 영향 범위가 큼
 */
@Entity
@Table(name = "Account_Customer_Composite")
@Getter
@Setter
@NoArgsConstructor
public class AccountCustomerComposite {

    @EmbeddedId
    private AccountCustomerId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "Account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    @JoinColumn(name = "Customer_id")
    private Customer customer;

    @Column(name = "Registered_Date", nullable = false)
    private LocalDate registeredDate;
}
