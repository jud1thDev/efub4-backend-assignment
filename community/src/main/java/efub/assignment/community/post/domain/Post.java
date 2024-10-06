package efub.assignment.community.post.domain;


import efub.assignment.community.account.domain.Account;
import efub.assignment.community.board.domain.Board;
import efub.assignment.community.comment.domain.Comment;
import efub.assignment.community.global.entity.BaseTimeEntity;
import efub.assignment.community.post.dto.PostRequestDto;
import efub.assignment.community.post.dto.PostUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id",updatable = false)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "account_id",updatable = false)  //외래키 매핑
    private Account account;

    @ManyToOne
    @JoinColumn(name = "board_id",updatable = false)  //외래키 매핑
    private Board board;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Boolean writerOpen;

    /* mappedBy : 연관관계의 주인 */
    /* cascade : 엔티티 삭제 시 연관된 엔티티의 처리 방식 */
    /* orphanRemoval : 고아 객체의 처리 방식 */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    /* @OneToMany: 한 개의 게시글에 여러 개의 좋아요가 존재할 수 있음 */
    /* mappedBy = 'post': 'Heart가 'post_id' 값을 참조 */
    /* 외래 키의 주인이 Post이므로, 연관관계의 주인을 Post로 명시 */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHeart> postHeartList = new ArrayList<>();

    @Builder
    public Post(Account account, Board board, String title, String content, Boolean writerOpen){
        this.account = account;
        this.board = board;
        this.title = title;
        this.content = content;
        this.writerOpen = writerOpen;
    }

    public void update(PostUpdateDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.writerOpen = dto.getWriterOpen();
    }
}
