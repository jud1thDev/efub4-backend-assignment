package efub.assignment.community.post.dto;

import efub.assignment.community.post.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private Long boardId;
    private String writerNickname;
    private String title;
    private String content;
    private Boolean writerOpen;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static PostResponseDto from(Post post, String writerName, Long boardId){
        return new PostResponseDto(
                post.getPostId(),
                boardId,
                writerName,
                post.getTitle(),
                post.getContent(),
                post.getWriterOpen(),
                post.getCreatedDate(),
                post.getModifiedDate()
        );
    }
}
