package everymeal.server.review.entity;


import everymeal.server.global.entity.BaseEntity;
import everymeal.server.meal.entity.Meal;
import everymeal.server.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table
@Entity(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 300)
    private String content;

    @Column(nullable = false, length = 1)
    private int grade;

    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_idx")
    private Meal meal;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private Set<ReviewMark> reviewMarks = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_idx")
    private List<Image> images = new ArrayList<>();

    @Builder
    public Review(String content, int grade, List<Image> images, User user, Meal meal) {
        this.content = content;
        this.grade = grade;
        this.images = images;
        this.user = user;
        this.meal = meal;
        this.isDeleted = Boolean.FALSE;
    }

    public void updateEntity(String content, int grade, List<Image> images) {
        this.content = content;
        this.grade = grade;
        this.images = images;
    }

    public void deleteEntity() {
        this.isDeleted = Boolean.TRUE;
    }
}