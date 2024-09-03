package com.invitationService.InvitationService.repositories;

import com.invitationService.InvitationService.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    @Query("SELECT i FROM Invitation i WHERE i.invitationStatus='PENDING' AND i.journalId = :journalId")
    List<Invitation> findPendingInvitationsByJournalId(@Param("journalId") String journalId);

    @Query("SELECT i FROM Invitation i WHERE i.invitationStatus='PENDING' AND i.userEmail = :userEmail")
    List<Invitation> findPendingInvitationsByUserEmail(@Param("userEmail") String userEmail);

    @Query("SELECT i FROM Invitation i WHERE i.invitationStatus='PENDING' AND i.id = :id")
    Invitation findPendingInvitationById(@Param("id") Long invitationId);

    @Query("SELECT i.journalId FROM Invitation i WHERE i.invitationStatus='ACCEPTED' AND i.userEmail = :userEmail")
    List<String> getAcceptedJournalIdsForUser(@Param("userEmail") String userEmail);
}
