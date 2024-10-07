package efub.assignment.community.post;

import efub.assignment.community.account.AccountRepository;
import efub.assignment.community.account.domain.Account;
import efub.assignment.community.board.BoardRepository;
import efub.assignment.community.board.domain.Board;
import efub.assignment.community.post.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PostRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Account account;
    private Board board;

    @BeforeEach
    void setup() {
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
    @DisplayName("writerOpen이 true인 Post 조회 - 성공")
    void findPublicPost() {
        // given
        String title1 = "공개 게시글";
        String content1 = "이건 보여야 함";
        Boolean writerOpen1 = true;

        String title2 = "비공개 게시글";
        String content2 = "이건 안 보여야 함";
        Boolean writerOpen2 = false;

        Post publicPost = Post.builder()
                .account(account)
                .board(board)
                .title(title1)
                .content(content1)
                .writerOpen(writerOpen1)
                .build();
        testEntityManager.persist(publicPost);

        Post privatePost = Post.builder()
                .account(account)
                .board(board)
                .title(title2)
                .content(content2)
                .writerOpen(writerOpen2)
                .build();
        testEntityManager.persist(privatePost);
        testEntityManager.flush();

        // when
        List<Post> openPosts = postRepository.findByWriterOpen(true);

        // then
        assertEquals(1, openPosts.size(), "writerOpen이 true인 게시글이 하나여야 합니다.");
        assertEquals(title1, openPosts.get(0).getTitle(), "조회된 게시글의 제목이 일치해야 합니다.");
    }


    @Test
    @DisplayName("Post 조회 실패 - 존재하지 않는 ID")
    void findPostByDeletedId() {
        // given
        Post post = Post.builder()
                .account(account)
                .board(board)
                .title("제목")
                .content("내용")
                .writerOpen(true)
                .build();
        testEntityManager.persist(post);
        testEntityManager.flush();
        Long deletedId = post.getPostId();
        postRepository.delete(post);
        testEntityManager.flush();

        // when
        Optional<Post> searchPost = postRepository.findById(deletedId);

        // then
        assertFalse(searchPost.isPresent(), "존재하지 않는 ID로 Post를 조회할 수 없습니다. ");
    }

}