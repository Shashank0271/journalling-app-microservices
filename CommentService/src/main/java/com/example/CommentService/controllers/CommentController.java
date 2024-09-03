package com.example.CommentService.controllers;

import com.example.CommentService.entities.Comment;
import com.example.CommentService.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.createComment(comment));
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<List<Comment>> getAllCommentsForJournalId(@PathVariable String journalId) {
        return ResponseEntity.ok(commentService.getCommentsForJournalID(journalId));
    }

}
