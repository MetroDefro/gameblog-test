package sparta.gameblog.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class SnsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sns_info_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    @Builder
    public SnsInfo(SnsType snsType) {
        this.snsType = snsType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum SnsType {
        google,
        naver,
        kakao
    }
}
