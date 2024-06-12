package sparta.gameblog.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Builder
    public Comment(Long id, Post post, User user, String comment) {
        this.post = post;
        this.user = user;
        this.comment = comment;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    public void update(Comment comment) {
        if (comment.getComment() != null) {
            this.comment = comment.getComment();
        }
    }
}
