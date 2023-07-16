package ru.skypro.homework.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
    private Long createdAt;
    private String text;

    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

}
