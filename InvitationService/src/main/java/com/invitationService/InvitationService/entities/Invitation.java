package com.invitationService.InvitationService.entities;

import com.invitationService.InvitationService.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"invited_user_email", "journal_id"})
})
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invitation_generator")
    @SequenceGenerator(name = "invitation_generator", sequenceName = "invitation_gen", initialValue = 80, allocationSize = 27)
    @Column(name = "invitation_id")
    private Long invitationId;
    @Column(name = "invited_user_email")
    private String userEmail;
    @Column(name = "journal_id")
    private String journalId;
    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;
}
