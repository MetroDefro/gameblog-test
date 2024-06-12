package sparta.gameblog.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long id;
    private String title;
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private Set<Comment> comments;

    @Builder
    public Post(String title, String contents, User user, Set<Comment> comments) {
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.comments = comments;
    }

    public void update(Post post) {
        this.title = post.title;
        this.contents = post.contents;
    }


    @Transactional
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public boolean canUpdateBy(User user) {
        return this.user.getId() == user.getId() || user.isAdmin();
    }
}
