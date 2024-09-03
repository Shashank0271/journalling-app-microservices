package com.example.CommentService.services;

import com.example.CommentService.entities.Comment;
import com.example.CommentService.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createComment(com.example.CommentService.entities.Comment comment) {
        return commentRepository.save(comment);
    }


    public List<Comment> getCommentsForJournalID(String journalId) {
        return commentRepository.findByJournalId(journalId);
    }

}
