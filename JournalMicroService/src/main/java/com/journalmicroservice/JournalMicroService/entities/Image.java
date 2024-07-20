package com.journalmicroservice.JournalMicroService.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

enum ImageCat {
    PROFILE_PIC, JOURNAL_ENTRY
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private long imageId;
    private ImageCat imageCategory;
    private Long userId;
    private String journalEntryId;
    private String publicId;
    private String secureUrl;
}