package com.journalmicroservice.JournalMicroService.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.journalmicroservice.JournalMicroService.Exceptions.JournalEntryNotFoundException;
import com.journalmicroservice.JournalMicroService.entities.Image;
import com.journalmicroservice.JournalMicroService.entities.JournalEntry;
import com.journalmicroservice.JournalMicroService.repository.JournalRepository;
import com.journalmicroservice.JournalMicroService.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.journalmicroservice.JournalMicroService.utils.ImageUtil.convertMultipartFileToFile;

@Service
@RequiredArgsConstructor
public class JournalService {
    final static Logger logger = LoggerFactory.getLogger(JournalService.class);

    private final JournalRepository journalRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    //works
    public JournalEntry createEntry(List<MultipartFile> multipartFiles, String userName, Long userId, String content) {
        String journalEntryId = UUID.randomUUID()
                .toString();
        
        List<File> imageFiles = multipartFiles != null ? multipartFiles.stream()
                .map(imageFile -> {
                    try {
                        return convertMultipartFileToFile(imageFile);
                    } catch (
                            IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList() : new ArrayList<>();


        JournalEntry journalEntry = JournalEntry.builder().
                entryId(journalEntryId).
                userName(userName)
                .userId(userId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();


        if (!imageFiles.isEmpty()) {
            String url = "http://localhost:9000/image/journal";
//            String url = "http://IMAGEMICROSERVICE/image/journal";
            //forming the body
            MultiValueMap<String, Object> body
                    = new LinkedMultiValueMap<>();

            body.add("journalId", journalEntryId);
            body.add("userId", userId);

            imageFiles.forEach(imageFile -> {
                FileSystemResource fileSystemResource = new FileSystemResource(imageFile);
                body.add("files", fileSystemResource);
            });

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);

            List response = restTemplate.postForObject(url, requestEntity, List.class);
            journalEntry.setImages(response);
        } else {
            journalEntry.setImages(new ArrayList<>());
        }
        journalRepository.save(journalEntry);
        imageFiles.forEach(ImageUtil::deleteFile);
        return journalEntry;
    }

    //works
    public JournalEntry getEntry(String journalEntryId) {
        Optional<JournalEntry> journalEntry = journalRepository.findByEntryId(journalEntryId);
        if (journalEntry.isEmpty()) {
            throw new JournalEntryNotFoundException(journalEntryId);
        }
        String url = "http://localhost:9000/image/journal/" + journalEntryId;
        List images = restTemplate.getForObject(url, List.class);
        journalEntry.get()
                .setImages(images);
        return journalEntry.get();
    }

    //works
    public List<JournalEntry> getEntriesByUserId(Long userId) {
        List<JournalEntry> journalEntries = journalRepository.findByUserId(userId);
        addImagesToJournalEntries(journalEntries);
        return journalEntries;
    }

    public List<JournalEntry> getSharedEntriesByUserId(Long userId) {
        List<String> journalIds =
                objectMapper.convertValue(restTemplate.getForObject("http://localhost:8086/invitation/accepted/user/" + userId, List.class)
                        , new TypeReference<List<String>>() {
                        });

        List<JournalEntry> journalEntries = journalRepository.findAllEntriesHavingIdIn(journalIds);
        addImagesToJournalEntries(journalEntries);
        return journalEntries;
    }

    //works
    public void deleteJournalEntryById(String journalEntryId) {
        journalRepository.deleteById(journalEntryId);
        restTemplate.delete("http://localhost:9000/image/journal/" + journalEntryId);
    }


    private void addImagesToJournalEntries(List<JournalEntry> journalEntries) {
        /* the images won't be coming along with the entries
                HOW TO SOLVE THIS: BATCH PROCESSING
                1> fetch all the journal entries from the database for the required users
                2> add all the journal ids into a HashSet
                3> pass the list of ids to the ImageService
                4> fetch all the required images from the image service
                5> when we get the response here we can map the images to the respective journalEntry images list
         */
        Set<String> journalEntryIds = new HashSet<>();
        journalEntries.forEach(journalEntry -> {
            journalEntryIds.add(journalEntry.getEntryId());
        });

        String url = "http://localhost:9000/image/journal-entries";
        HttpEntity<Set<String>> httpEntity = new HttpEntity<>(journalEntryIds);
        ResponseEntity<List> response = restTemplate.postForEntity(url, httpEntity, List.class);
        List<Image> images = objectMapper.convertValue(response.getBody(), new TypeReference<List<Image>>() {
        });

        assert images != null;

        Map<String, List<Image>> entries = new HashMap<>(); //response

        journalEntryIds.forEach((entryId) -> entries.put(entryId, new ArrayList<>()));

        for (Image image : images) { //aggregate all the images for one journal entry id
            String journalId = image.getJournalEntryId();
            entries.get(journalId)
                    .add(image);
        }

        //entries is a map of journal id to its list of images
        //we use this to attach the list of images to its respective journalEntry
        journalEntries.forEach(journalEntry -> {
            if (entries.get(journalEntry.getEntryId()) != null)
                journalEntry.setImages(entries.get(journalEntry.getEntryId()));
        });

    }

}
