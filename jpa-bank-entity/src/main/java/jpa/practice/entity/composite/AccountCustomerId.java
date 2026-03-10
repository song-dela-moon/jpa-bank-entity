package jpa.practice.entity.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 복합키(Composite Key) - Account_id + Customer_id
 * <p>
 * 두 FK를 합쳐 PK로 사용하므로 별도의 인조 식별자가 필요 없다.
 * DB 레벨에서 (Account_id, Customer_id) 조합의 유일성이 자동 보장된다.
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AccountCustomerId implements Serializable {

    @Column(name = "Account_id")
    private Long accountId;

    @Column(name = "Customer_id")
    private Long customerId;
}
