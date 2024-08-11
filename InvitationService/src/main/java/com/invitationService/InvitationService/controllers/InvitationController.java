package com.invitationService.InvitationService.controllers;

import com.invitationService.InvitationService.dtos.CreateInviteDTO;
import com.invitationService.InvitationService.entities.Invitation;
import com.invitationService.InvitationService.services.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("invitation")
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<List<Invitation>> createInvite(@RequestBody CreateInviteDTO createInviteDTO) {
        return new ResponseEntity<>(invitationService.createInvite(createInviteDTO), HttpStatus.CREATED);
    }

    @PostMapping("/accept/{invitationId}")
    public ResponseEntity<Invitation> acceptInvite(@PathVariable Long invitationId) {
        return ResponseEntity.ok(invitationService.acceptInvite(invitationId));
    }

    @GetMapping("pending/journal/{journalId}")
    public ResponseEntity<List<Invitation>> getPendingInvitationsForJournalEntry(@PathVariable String journalId) {
        return new ResponseEntity<>(invitationService.getPendingInvitationsForJournalEntry(journalId), HttpStatus.OK);
    }

    @GetMapping("pending/user/{userId}")
    public ResponseEntity<List<Invitation>> getPendingInvitationsForUser(@PathVariable Long userId) {
        return new ResponseEntity<>(invitationService.getPendingInvitationsForUserId(userId), HttpStatus.OK);
    }


}

/*

a user is supposed to invite others to view a journal entry
other users are supposed to accept that invite
when they accept they can get access to view and comment on that journalEntry

 */