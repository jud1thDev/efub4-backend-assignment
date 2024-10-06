package efub.assignment.community.account.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("Account 생성 - 성공")
    void createAccount() {
        // given
        String email = "test@test.com";
        String password = "password123";
        String nickname = "fubie";
        String university = "이화여자대학교";
        String studentId = "1234567";

        // when
        Account account = Account.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .university(university)
                .studentId(studentId)
                .build();

        // then
        assertAll("Account 필드 값 검증",
                () -> assertEquals(email, account.getEmail()),
                () -> assertEquals(password, account.getPassword()),
                () -> assertEquals(nickname, account.getNickname()),
                () -> assertEquals(university, account.getUniversity()),
                () -> assertEquals(studentId, account.getStudentId()),
                () -> assertEquals(AccountStatus.REGISTERED, account.getStatus())
        );
    }

    @Test
    @DisplayName("비어 있는 닉네임으로 업데이트 - 실패")
    void updateAccount(){
        // given
        Account account = Account.builder()
                .email("test@test.com")
                .password("password123")
                .nickname("fubie")
                .university("이화여자대학교")
                .studentId("1234567")
                .build();

        // when
        String newNickname = "";

        // then
        assertTrue(newNickname.isEmpty(), "닉네임은 비어 있을 수 없습니다.");
    }

}