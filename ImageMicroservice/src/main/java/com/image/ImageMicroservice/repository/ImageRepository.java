package com.image.ImageMicroservice.repository;

import com.image.ImageMicroservice.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByJournalEntryId(String journalEntryId);

    Image findByUserId(long userId);

    @Query("SELECT m FROM Image m WHERE m.journalEntryId IN :jids")
    List<Image> findAllImagesForJournalIds(@Param("jids") ArrayList<String> journalIds);

    @Transactional
    @Modifying
    @Query("DELETE FROM Image m WHERE m.journalEntryId = :id ")
    void deleteImagesHavingJournalEntryId(@Param("id") String journalEntryId);

    @Query("SELECT m.publicId FROM Image m WHERE m.journalEntryId=:id")
    List<String> findImagePublicIdForEntryId(@Param("id") String journalEntryId);

}
