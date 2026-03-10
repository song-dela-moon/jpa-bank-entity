package jpa.practice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jpa.practice.entity.Account;
import jpa.practice.entity.Branch;
import jpa.practice.entity.Customer;
import jpa.practice.entity.composite.AccountCustomerComposite;
import jpa.practice.entity.composite.AccountCustomerId;
import jpa.practice.entity.surrogate.AccountCustomerSurrogate;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 복합키(@EmbeddedId) vs 인조식별자(Surrogate Key) 비교 테스트
 *
 * ※ 실제 실행하려면 MySQL이 구동 중이어야 합니다.
 * DB 없이 컴파일만 확인할 때는 `mvn compile`을 사용하세요.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompositeVsSurrogateTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        emf = Persistence.createEntityManagerFactory("hello-jpa");
    }

    @AfterAll
    static void tearDown() {
        if (emf != null)
            emf.close();
    }

    // ============================================================
    // 공통 헬퍼: Account, Customer, Branch 저장
    // ============================================================

    private Branch createBranch(EntityManager em) {
        Branch branch = new Branch();
        branch.setBranchName("강남지점");
        branch.setStreetAddress("서울시 강남구 테헤란로 123");
        branch.setCity("서울");
        branch.setState("서울특별시");
        branch.setZipCode("06100");
        em.persist(branch);
        return branch;
    }

    private Account createAccount(EntityManager em, Branch branch) {
        Account account = new Account();
        account.setAccountType(1);
        account.setAccountBalance(1_000_000);
        account.setBranch(branch);
        account.setDateOpened(LocalDate.now());
        em.persist(account);
        return account;
    }

    private Customer createCustomer(EntityManager em) {
        Customer customer = new Customer();
        customer.setFirstName("길동");
        customer.setLastName("홍");
        customer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer.setStreetAddress("서울시 종로구 종로 1");
        customer.setCity("서울");
        customer.setState("서울특별시");
        customer.setZipCode("03154");
        customer.setCountry("KR");
        customer.setEmail("gildong@example.com");
        customer.setSex('M');
        em.persist(customer);
        return customer;
    }

    // ============================================================
    // 시나리오 1: 복합키(@EmbeddedId) 저장 & 조회
    // ============================================================

    @Test
    @Order(1)
    @DisplayName("시나리오1 - 복합키: AccountCustomerComposite 저장 & 조회")
    void testCompositeKey_SaveAndFind() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Branch branch = createBranch(em);
        Account account = createAccount(em, branch);
        Customer customer = createCustomer(em);

        // 복합키 생성 및 엔티티 저장
        AccountCustomerId compositeId = new AccountCustomerId(account.getId(), customer.getId());

        AccountCustomerComposite composite = new AccountCustomerComposite();
        composite.setId(compositeId);
        composite.setAccount(account);
        composite.setCustomer(customer);
        composite.setRegisteredDate(LocalDate.now());
        em.persist(composite);

        em.getTransaction().commit();
        em.close();

        // 조회 — 복합키로 검색
        EntityManager em2 = emf.createEntityManager();
        AccountCustomerComposite found = em2.find(
                AccountCustomerComposite.class, compositeId);

        assertNotNull(found, "복합키로 조회 성공해야 합니다");
        assertEquals(account.getId(), found.getAccount().getId());
        assertEquals(customer.getId(), found.getCustomer().getId());

        System.out.println("=== 시나리오1 결과 ===");
        System.out.println("PK = (" + found.getId().getAccountId()
                + ", " + found.getId().getCustomerId() + ")");
        System.out.println("등록일 = " + found.getRegisteredDate());

        em2.close();
    }

    // ============================================================
    // 시나리오 2: 인조식별자(Surrogate Key) 저장 & 조회
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("시나리오2 - 인조식별자: AccountCustomerSurrogate 저장 & 조회")
    void testSurrogateKey_SaveAndFind() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Branch branch = createBranch(em);
        Account account = createAccount(em, branch);
        Customer customer = createCustomer(em);

        // 인조식별자 — id를 직접 지정하지 않음 (auto-increment)
        AccountCustomerSurrogate surrogate = new AccountCustomerSurrogate();
        surrogate.setAccount(account);
        surrogate.setCustomer(customer);
        surrogate.setRegisteredDate(LocalDate.now());
        em.persist(surrogate);

        em.getTransaction().commit();

        Long generatedId = surrogate.getId();
        assertNotNull(generatedId, "인조식별자(auto-increment)가 생성되어야 합니다");

        em.close();

        // 조회 — 단일 Long PK로 검색
        EntityManager em2 = emf.createEntityManager();
        AccountCustomerSurrogate found = em2.find(
                AccountCustomerSurrogate.class, generatedId);

        assertNotNull(found, "인조식별자로 조회 성공해야 합니다");
        assertEquals(account.getId(), found.getAccount().getId());
        assertEquals(customer.getId(), found.getCustomer().getId());

        System.out.println("=== 시나리오2 결과 ===");
        System.out.println("PK (인조식별자) = " + found.getId());
        System.out.println("Account_id = " + found.getAccount().getId());
        System.out.println("Customer_id = " + found.getCustomer().getId());
        System.out.println("등록일 = " + found.getRegisteredDate());

        em2.close();
    }

    // ============================================================
    // 시나리오 비교: 중복 등록 방지
    // ============================================================

    @Test
    @Order(3)
    @DisplayName("비교 - 복합키는 동일 조합 insert 시 PK 중복 에러 발생")
    void testCompositeKey_DuplicatePrevention() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Branch branch = createBranch(em);
        Account account = createAccount(em, branch);
        Customer customer = createCustomer(em);

        AccountCustomerId compositeId = new AccountCustomerId(account.getId(), customer.getId());

        AccountCustomerComposite first = new AccountCustomerComposite();
        first.setId(compositeId);
        first.setAccount(account);
        first.setCustomer(customer);
        first.setRegisteredDate(LocalDate.now());
        em.persist(first);
        em.getTransaction().commit();

        // 첫 번째 em에서 사용한 엔티티의 ID를 저장
        Long accountId = account.getId();
        Long customerId = customer.getId();
        em.close();

        // 동일 복합키로 다시 등록 시도 → PK 중복으로 예외 발생
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();

        // 새 영속성 컨텍스트에서 엔티티를 다시 조회 (detached 방지)
        Account managedAccount = em2.find(Account.class, accountId);
        Customer managedCustomer = em2.find(Customer.class, customerId);

        AccountCustomerComposite duplicate = new AccountCustomerComposite();
        duplicate.setId(new AccountCustomerId(accountId, customerId));
        duplicate.setAccount(managedAccount);
        duplicate.setCustomer(managedCustomer);
        duplicate.setRegisteredDate(LocalDate.now());

        // 복합키 방식: PK 자체가 중복이므로 commit 시 예외
        assertThrows(Exception.class, () -> {
            em2.persist(duplicate);
            em2.getTransaction().commit();
        }, "복합키 중복으로 예외가 발생해야 합니다");

        em2.close();

        System.out.println("=== 복합키 중복 방지 테스트 통과 ===");
    }

    @Test
    @Order(4)
    @DisplayName("비교 - 인조식별자는 UniqueConstraint로 중복 방지")
    void testSurrogateKey_DuplicatePrevention() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Branch branch = createBranch(em);
        Account account = createAccount(em, branch);
        Customer customer = createCustomer(em);

        AccountCustomerSurrogate first = new AccountCustomerSurrogate();
        first.setAccount(account);
        first.setCustomer(customer);
        first.setRegisteredDate(LocalDate.now());
        em.persist(first);
        em.getTransaction().commit();

        Long accountId = account.getId();
        Long customerId = customer.getId();
        em.close();

        // 동일 Account-Customer 조합으로 다시 등록 시도 → UniqueConstraint 위반
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();

        // 새 영속성 컨텍스트에서 엔티티를 다시 조회 (detached 방지)
        Account managedAccount = em2.find(Account.class, accountId);
        Customer managedCustomer = em2.find(Customer.class, customerId);

        AccountCustomerSurrogate duplicate = new AccountCustomerSurrogate();
        duplicate.setAccount(managedAccount);
        duplicate.setCustomer(managedCustomer);
        duplicate.setRegisteredDate(LocalDate.now());

        // 인조식별자 + IDENTITY 전략: persist() 시점에 즉시 INSERT → UK 위반 예외
        assertThrows(Exception.class, () -> {
            em2.persist(duplicate);
            em2.getTransaction().commit();
        }, "UniqueConstraint 위반으로 예외가 발생해야 합니다");

        em2.close();

        System.out.println("=== 인조식별자 UniqueConstraint 중복 방지 테스트 통과 ===");
    }
}
