package ru.skypro.homework.model;

import liquibase.repackaged.org.apache.commons.lang3.builder.ToStringExclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ads")
@Data
@NoArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @OneToOne
    @JoinColumn(name = "image_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Image image;

    private int price;

    private String title;

    private String description;

    @OneToMany(mappedBy = "ad", fetch = FetchType.EAGER, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToStringExclude
    private List<Comment> adsComments;

    public Ad(User author, int price, String title, String description) {
        this.author = author;
        this.price = price;
        this.title = title;
        this.description = description;
    }
}
