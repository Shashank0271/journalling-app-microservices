package com.invitationService.InvitationService.services;

import com.invitationService.InvitationService.dtos.CreateInviteDTO;
import com.invitationService.InvitationService.dtos.MessageDto;
import com.invitationService.InvitationService.entities.Invitation;
import com.invitationService.InvitationService.enums.InvitationStatus;
import com.invitationService.InvitationService.messaging.InvitationMessageProducer;
import com.invitationService.InvitationService.repositories.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final InvitationMessageProducer invitationMessageProducer;

    public List<Invitation> createInvite(CreateInviteDTO createInviteDTO) {

        // push task to notification-service
        MessageDto messageDto = MessageDto.builder()
                .emails(createInviteDTO.getUserEmails())
                .journalId(createInviteDTO.getJournalId())
                .subject("Journal Entry invitation")
                .body("You have been invited to view journal with id :" + createInviteDTO.getJournalId())
                .build();

        invitationMessageProducer.sendMessage(messageDto);

        // save and return the entries in the database with invite status sent to 'pending'
        return createInviteDTO.getUserEmails()
                .stream()
                .map((userEmail) -> invitationRepository.save(Invitation.builder()
                        .userEmail(userEmail)
                        .journalId(createInviteDTO.getJournalId())
                        .invitationStatus(InvitationStatus.PENDING)
                        .build()))
                .collect(Collectors.toList());

    }

    public Invitation acceptInvite(Long pendingInvitationId) {
        Invitation pendingInvitation = invitationRepository.findPendingInvitationById(pendingInvitationId);
        if (pendingInvitation == null || !pendingInvitation.getInvitationStatus()
                .equals(InvitationStatus.PENDING)) {
            throw new RuntimeException("No pending invitation for id : " + pendingInvitationId);
        }
        pendingInvitation.setInvitationStatus(InvitationStatus.ACCEPTED);
        return invitationRepository.save(pendingInvitation);
    }

    public Invitation declineInvite(Long invitationId) {
        Invitation pendingInvitation = invitationRepository.findPendingInvitationById(invitationId);
        if (pendingInvitation == null || !pendingInvitation.getInvitationStatus()
                .equals(InvitationStatus.PENDING)) {
            throw new RuntimeException("No pending invitation for id : " + invitationId);
        }
        pendingInvitation.setInvitationStatus(InvitationStatus.DECLINED);
        return invitationRepository.save(pendingInvitation);
    }

    public List<Invitation> getPendingInvitationsForJournalEntry(String journalId) {
        return invitationRepository.findPendingInvitationsByJournalId(journalId);
    }

    public List<Invitation> getPendingInvitationsForUserEmail(String email) {
        return invitationRepository.findPendingInvitationsByUserEmail(email);
    }

    public List<String> getAcceptedJournalIdsForUser(String email) {
        return invitationRepository.getAcceptedJournalIdsForUser(email);
    }

}
