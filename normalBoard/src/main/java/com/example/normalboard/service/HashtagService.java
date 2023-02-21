package com.example.normalboard.service;

import com.example.normalboard.domain.Hashtag;
import com.example.normalboard.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public Set<String> parseHashtagNames(String content) {
        if(content == null) return Set.of();

        Set<String> hashtagNames = new HashSet<>();

        Matcher matcher = extractHashtagNameWithHashtagFrom(content);

        while(matcher.find()) hashtagNames.add(removeHashtagAndReturnHashtagName(matcher));

        return Set.copyOf(hashtagNames);
    }

    private Matcher extractHashtagNameWithHashtagFrom(String content){
        Pattern pattern = Pattern.compile("#[\\w가-힣]+");
        return  pattern.matcher(content.strip());
    }

    private String removeHashtagAndReturnHashtagName(Matcher matcher){
        return matcher.group().substring(1);
    }

    public Set<Hashtag> findHashtagsByNames(Set<String> hashtagNames) {
        return new HashSet<>(hashtagRepository.findByHashtagNameIn(hashtagNames));
    }

    public void deleteHashtagWithoutArticles(Long hashtagId) {
        Hashtag hashtag = hashtagRepository.getReferenceById(hashtagId);
        if(hashtag.getArticles().isEmpty()) hashtagRepository.delete(hashtag);
    }

    public void deleteAllHashtagWithoutArticles(Set<Long> hashtags) {
        for(var element : hashtags) deleteHashtagWithoutArticles(element);
    }

}
