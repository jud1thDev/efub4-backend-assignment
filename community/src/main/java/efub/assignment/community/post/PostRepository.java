package efub.assignment.community.post;

import efub.assignment.community.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByWriterOpen(Boolean writerOpen);

}
