package com.journalmicroservice.JournalMicroService.repository;

import com.journalmicroservice.JournalMicroService.entities.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JournalRepository extends JpaRepository<JournalEntry, String> {
    List<JournalEntry> findByUserId(int userId);

    Optional<JournalEntry> findByEntryId(String journalEntryId);
}
