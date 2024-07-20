package com.journalmicroservice.JournalMicroService.controllers;

import com.journalmicroservice.JournalMicroService.entities.JournalEntry;
import com.journalmicroservice.JournalMicroService.services.JournalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequestMapping("/journal")
@RestController
public class JournalController {
    private static final Logger logger = LoggerFactory.getLogger(JournalService.class);
    @Autowired
    JournalService journalService;

    @PostMapping
    public ResponseEntity<?> createEntry(
            @RequestPart(required = false, name = "images") List<MultipartFile> images,
            @RequestParam(name = "userName") String userName,
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "content") String content
    ) {
        return new ResponseEntity<>(journalService.createEntry(images, userName, userId, content), HttpStatus.CREATED);
    }

    @GetMapping("{journalEntryId}")
    public ResponseEntity<JournalEntry> getEntryByEntryId(@PathVariable String journalEntryId) {
        return new ResponseEntity<>(journalService.getEntry(journalEntryId), HttpStatus.OK);
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<JournalEntry>> getEntriesByUserId(@PathVariable int userId) {
        return new ResponseEntity<>(journalService.getEntriesByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{journalEntryId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable String journalEntryId) {
        journalService.deleteJournalEntryById(journalEntryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
