package com.online.bookstore.services;

import com.online.bookstore.dto.MediaPost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MediaPostsCacheService {
    @Value("${media.base.url}")
    private String mediaEndPoint;

    Map<String, Set<MediaPost>> map = new HashMap<>();

    @Scheduled(fixedDelay = 10000)
    public void getPostsInMedia() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MediaPost[]> responseEntity = restTemplate.getForEntity(mediaEndPoint, MediaPost[].class);
        List<MediaPost> mediaPostList = Arrays.asList(responseEntity.getBody());
        mediaPostList.forEach(
                mediaPost -> {
                    String title = mediaPost.getTitle();
                    String description = mediaPost.getContent();
                    String[] titleArray = title.split(" ");

                    for(int i = 0 ; i < titleArray.length ; i++) {
                        Set<MediaPost> set = map.getOrDefault(titleArray[i], new HashSet<>());
                        set.add(mediaPost);
                        map.put(titleArray[i],set);
                    }

                    String[] descriptionArray = description.split(" ");
                    for(int i = 0 ; i < descriptionArray.length ; i++) {
                        Set<MediaPost> set = map.getOrDefault(titleArray[i], new HashSet<>());
                        set.add(mediaPost);
                        map.put(titleArray[i],set);
                    }
                }
        );
    }

    public Set<MediaPost> getMediaPosts(String inputText) {
        String words[] = inputText.split(" ");
        Set<MediaPost> mediaPosts = new HashSet<>();
        for (String word : words) {
            if (map.containsKey(word)) {
                mediaPosts.addAll(map.get(word));
            }
        }
        return mediaPosts;
    }


}
