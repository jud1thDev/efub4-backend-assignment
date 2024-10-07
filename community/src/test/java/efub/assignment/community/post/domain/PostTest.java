package efub.assignment.community.post.domain;

import efub.assignment.community.account.domain.Account;
import efub.assignment.community.board.domain.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PostTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private Account account;
    private Board board;

    @BeforeEach
    void setup(){
        account = Account.builder()
                .email("test@test.com")
                .password("password123")
                .nickname("fubie")
                .university("이화여자대학교")
                .studentId("1234567")
                .build();
        testEntityManager.persist(account);
        board = Board.builder()
                .account(account)
                .boardName("테스트 게시판")
                .boardDescription("테스트 설명")
                .boardNotice("테스트 공지사항")
                .build();
        testEntityManager.persist(board);
        testEntityManager.flush();
    }

    @Test
    @DisplayName("Post 생성 - 성공")
    void createPost() {
        // given
        String title = "제목제목";
        String content = "내용내용";
        Boolean writerOpen = true;

        // when
        Post post = Post.builder()
                .account(account)
                .board(board)
                .title(title)
                .content(content)
                .writerOpen(writerOpen)
                .build();
        testEntityManager.persist(post);
        testEntityManager.flush();

        // then
        assertAll("Post 필드 값 검증",
                () -> assertNotNull(post.getPostId()),
                () -> assertEquals(title, post.getTitle()),
                () -> assertEquals(content, post.getContent()),
                () -> assertEquals(writerOpen, post.getWriterOpen()),
                () -> assertEquals(account, post.getAccount()),
                () -> assertEquals(board, post.getBoard())
        );
    }

    @Test
    @DisplayName("Post 생성 실패 - 제목 글자 수 초과")
    void createPostWithLongTitle() {
        // given
        String longTitle = "제목".repeat(30);
        String content = "내용내용";
        Boolean writerOpen = true;

        // when & then
        assertThrows(Exception.class, () -> {
            Post post = Post.builder()
                    .account(account)
                    .board(board)
                    .title(longTitle)
                    .content(content)
                    .writerOpen(writerOpen)
                    .build();
            testEntityManager.persist(post);
            testEntityManager.flush();
        });
    }



}