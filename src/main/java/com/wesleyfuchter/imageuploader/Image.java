package com.wesleyfuchter.imageuploader;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

}
