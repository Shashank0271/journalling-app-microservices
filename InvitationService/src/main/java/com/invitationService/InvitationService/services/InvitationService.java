package com.invitationService.InvitationService.services;

import com.invitationService.InvitationService.dtos.CreateInviteDTO;
import com.invitationService.InvitationService.entities.Invitation;
import com.invitationService.InvitationService.enums.InvitationStatus;
import com.invitationService.InvitationService.repositories.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;

    public List<Invitation> createInvite(CreateInviteDTO createInviteDTO) {
        return createInviteDTO.getUserIds()
                .stream()
                .map((userId) -> invitationRepository.save(Invitation.builder()
                        .userId(userId)
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

    public List<Invitation> getPendingInvitationsForUserId(Long userId) {
        return invitationRepository.findPendingInvitationsByUserId(userId);
    }

    public List<String> getAcceptedJournalIdsForUser(Long userId) {
        return invitationRepository.getAcceptedJournalIdsForUser(userId);
    }

}
