package jpa.practice.entity.surrogate;

import jakarta.persistence.*;
import jpa.practice.entity.Account;
import jpa.practice.entity.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 시나리오 2 — 인조식별자(Surrogate Key) 방식
 * <p>
 * Account와 Customer의 다대다 관계를 풀어낸 중간 엔티티.
 * PK = Auto-increment Long id (인조 식별자)
 * <p>
 * 장점:
 * - 단일 컬럼 PK → 다른 테이블에서 FK로 참조하기 쉬움
 * - 조인 쿼리가 단순 (단일 컬럼 비교)
 * - 키 변경이 비즈니스 로직에 영향을 주지 않음
 * - 확장에 유연 (나중에 FK 구성이 바뀌어도 PK는 유지)
 * <p>
 * 단점:
 * - 동일 Account-Customer 중복 방지를 위해 별도 UniqueConstraint 필요
 * - 의미 없는 숫자가 PK이므로 비즈니스적 의미가 없음
 * - 추가 인덱스(유니크)에 의한 약간의 저장 공간 오버헤드
 */
@Entity
@Table(name = "Account_Customer_Surrogate", uniqueConstraints = @UniqueConstraint(name = "UK_account_customer", columnNames = {
        "Account_id", "Customer_id" }))
@Getter
@Setter
@NoArgsConstructor
public class AccountCustomerSurrogate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Account_Customer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Customer_id", nullable = false)
    private Customer customer;

    @Column(name = "Registered_Date", nullable = false)
    private LocalDate registeredDate;
}
