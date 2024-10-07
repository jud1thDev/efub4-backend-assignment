package efub.assignment.community.account;

import efub.assignment.community.account.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("이메일 중복 확인 - 성공")
    void validateEmail() {
        // given
        Account account = Account.builder()
                .email("test@test.com")
                .password("password123")
                .nickname("fubie")
                .university("이화여자대학교")
                .studentId("1234567")
                .build();
        accountRepository.save(account);

        // when
        Boolean exists = accountRepository.existsByEmail("test@test.com");

        // then
        assertTrue(exists, "이메일이 존재합니다.");
    }

    @Test
    @DisplayName("닉네임으로 계정 조회 - 실패")
    void findByNickname() {
        // given
        Account account = Account.builder()
                .email("test@test.com")
                .password("password123")
                .nickname("fubie")
                .university("이화여자대학교")
                .studentId("1234567")
                .build();
        accountRepository.save(account);
        String searchNickname = "iamfubie";

        // when
        Optional<Account> searchAccount = accountRepository.findByNickname(searchNickname);

        // then
        assertFalse(searchAccount.isPresent(), "존재하지 않는 닉네임입니다.");
    }


}