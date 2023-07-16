package ru.skypro.homework.model;

import liquibase.repackaged.org.apache.commons.lang3.builder.ToStringExclude;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    //@OneToOne
    //@JoinColumn(name ="image_id")
    //private Image image;

    private int price;

    private String title;

    private String description;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name= "user_id")
    @ToStringExclude
    private List<Comment> adsComments;

    public Ad(User author, int price, String title, String description){
        this.author = author;
        this.price = price;
        this.title = title;
        this.description = description;
    }
}
