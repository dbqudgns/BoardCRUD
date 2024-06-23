package Board_example.Board_crud.PostController;

import Board_example.Board_crud.DTO.PostRegisterDTO;
import Board_example.Board_crud.DTO.PostResponseDTO;
import Board_example.Board_crud.Entity.Post;
import Board_example.Board_crud.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // R : 전체 조회
    @GetMapping
    public List<Post> getAllPost() {
        return postService.getAllPost();
    }

    // R : 아이디로 조회
    @GetMapping("/{id}")
    public PostResponseDTO getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    // C : 게시글 생성
    @PostMapping
    public Post createPost(@RequestBody PostRegisterDTO post) {
        return postService.createPost(post);
    }

    // U : 게시글 수정
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostRegisterDTO updatedPost) {
        return postService.updatePost(id, updatedPost);
    }

    // D : 게시글 삭제
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }


}
