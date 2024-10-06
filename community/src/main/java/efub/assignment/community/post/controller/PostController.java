package efub.assignment.community.post.controller;


import efub.assignment.community.board.service.BoardService;
import efub.assignment.community.comment.dto.HeartRequestDto;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.AllPostsResponseDto;
import efub.assignment.community.post.dto.PostRequestDto;
import efub.assignment.community.post.dto.PostResponseDto;
import efub.assignment.community.post.dto.PostUpdateDto;
import efub.assignment.community.post.service.PostHeartService;
import efub.assignment.community.post.service.PostService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final PostHeartService postHeartService;

    @PostMapping("/posts")
    @ResponseStatus(value = HttpStatus.CREATED)
    public PostResponseDto createNewPost(@RequestBody @Valid final PostRequestDto dto){
        Post savedPost = postService.createNewPost(dto);
        return PostResponseDto.from(savedPost,savedPost.getAccount().getNickname(),savedPost.getBoard().getBoardId());
    }

    @GetMapping("/boards/{board_id}/posts")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPostsResponseDto getAllPost(@PathVariable(name="board_id")Long board_id){

        List<PostResponseDto> list = new ArrayList<>();
        List<Post> posts = postService.findAllPosts();
        posts.stream().forEach(post -> { //posts리스트를 스트림으로 변환 후, 각 글에 대해
            PostResponseDto dto = PostResponseDto.from(post, post.getAccount().getNickname(), post.getBoard().getBoardId());
            list.add(dto);
        });

        //for (Post post : posts){
        //    PostResponseDto dto = PostResponseDto.from(post, post.getAccount().getNickname(), post.getBoard().getBoardId());
        //    list.add(dto);
        //}
        long count = postService.countAllPosts();
        return new AllPostsResponseDto(list,count);
    }

    @GetMapping("/posts/{post_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PostResponseDto getOnePost(@PathVariable(name="post_id")Long post_id){
        Post post = postService.findPostById(post_id);
        return PostResponseDto.from(post, post.getAccount().getNickname(), post.getBoard().getBoardId());
    }

    @PatchMapping("/posts/{post_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PostResponseDto updatePost(@PathVariable(name="post_id")Long post_id,
                                      @RequestBody @Valid PostUpdateDto dto){
        Long postId = postService.updatePost(post_id,dto);
        Post post = postService.findPostById(postId);
        return PostResponseDto.from(post, post.getAccount().getNickname(), post.getBoard().getBoardId());
    }

    @DeleteMapping("/posts/{post_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deletePost(@PathVariable(name="post_id")Long post_id,
                           @RequestParam(name="accountId")Long account_id){
        postService.deletePost(post_id,account_id);

        return "글을 삭제하였습니다.";
    }

    // 게시글 좋아요 생성 api
    @PostMapping("/posts/{postId}/hearts")
    @ResponseStatus(HttpStatus.CREATED)
    public String createPostHeart(@PathVariable(name = "postId") final Long postId, @RequestBody final HeartRequestDto requestDto){
        postHeartService.create(postId, requestDto.getAccountId());
        return "좋아요를 눌렀습니다.";
    }

    // 게시글 좋아요 삭제 api
    @DeleteMapping("/posts/{postId}/hearts")
    @ResponseStatus(HttpStatus.OK)
    public String deletePostHeart(@PathVariable(name ="postId") final Long postId, @RequestParam(name ="accountId") final Long accountId){
        postHeartService.delete(postId, accountId);
        return "좋아요가 취소되었습니다.";
    }




}
