package com.journalmicroservice.JournalMicroService.repository;

import com.journalmicroservice.JournalMicroService.entities.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JournalRepository extends JpaRepository<JournalEntry, String> {
    List<JournalEntry> findByUserId(Long userId);

    Optional<JournalEntry> findByEntryId(String journalEntryId);

    @Query("SELECT j FROM JournalEntry j WHERE j.id IN :journalIds")
    List<JournalEntry> findAllEntriesHavingIdIn(@Param("journalIds") List<String> journalIds);

}
