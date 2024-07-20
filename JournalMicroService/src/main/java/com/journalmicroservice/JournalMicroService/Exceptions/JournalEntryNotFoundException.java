package com.journalmicroservice.JournalMicroService.Exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JournalEntryNotFoundException extends RuntimeException {
    private String entryId;
    private String message;

    public JournalEntryNotFoundException(String entryId) {
        this.entryId = entryId;
        this.message = "Entry with Id = " + entryId + " not found";
    }
}
