package com.invitationService.InvitationService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInviteDTO {
    private List<Long> userIds;
    private String journalId;
}
