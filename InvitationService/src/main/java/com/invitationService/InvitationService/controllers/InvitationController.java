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

    @PostMapping("/decline/{invitationId}")
    public ResponseEntity<Invitation> declineInvite(@PathVariable Long invitationId) {
        return ResponseEntity.ok(invitationService.declineInvite(invitationId));
    }

    @GetMapping("pending/journal/{journalId}")
    public ResponseEntity<List<Invitation>> getPendingInvitationsForJournalEntry(@PathVariable String journalId) {
        return ResponseEntity.ok(invitationService.getPendingInvitationsForJournalEntry(journalId));
    }

    @GetMapping("pending/user/{userId}")
    public ResponseEntity<List<Invitation>> getPendingInvitationsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(invitationService.getPendingInvitationsForUserId(userId));
    }

    @GetMapping("accepted/user/{userId}")
    public ResponseEntity<List<String>> getAcceptedJournalIdsForUser(@PathVariable Long userId) {
        //this is called by the journal-service
        return ResponseEntity.ok(invitationService.getAcceptedJournalIdsForUser(userId));
    }

}
