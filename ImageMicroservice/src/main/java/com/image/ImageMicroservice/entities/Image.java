package com.image.ImageMicroservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(generator = "image_seq_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "image_seq_gen", sequenceName = "image_seq")
    @Column(name = "image_id")
    private long imageId;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_cat")
    private ImageCat imageCategory;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = true, name = "journal_entry_id")
    private String journalEntryId;

    @Column(nullable = false)
    private String publicId;

    @Column(nullable = false)
    private String secureUrl;
}