package com.example.normalboard.service;

import com.example.normalboard.domain.Hashtag;
import com.example.normalboard.repository.HashtagRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비지니스 로직 - 해시태그")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @InjectMocks private HashtagService sut;

    @Mock private HashtagRepository hashtagRepository;

    @DisplayName("본문안에 해시태그를 중복 없이 추출")
    @ParameterizedTest(name = "[{index}] \"{0}\" => {1}")
    @MethodSource(value = "givenContent_whenParsing_thenReturnUniqueHashTagNames")
    public void givenContent_whenParsing_thenReturnUniqueHashTagNames(String content,Set<String> expected) throws Exception{
        // given
        // when
        Set<String> actual = sut.parseHashtagNames(content);
        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        then(hashtagRepository).shouldHaveNoInteractions();
    }


    static Stream<Arguments> givenContent_whenParsing_thenReturnUniqueHashTagNames(){
        return Stream.of(
                arguments("#java",Set.of("java")),
                arguments("#java#java",Set.of("java")),
                arguments("#java#spring",Set.of("java","spring")),
                arguments("#  ",Set.of()),
                arguments("  #  ",Set.of()),
                arguments("   #",Set.of()),
                arguments(null,Set.of()),
                arguments("null",Set.of()),
                arguments("javajavasa;lvdsfewfmdsadhk##das",Set.of("das")),
                arguments("#_java_spring_##das",Set.of("_java_spring_","das")),
                arguments("#_java-spring##das",Set.of("_java","das")),
                arguments("#java_spring",Set.of("java_spring"))
        );
    }


    @DisplayName("해시태그 이름들을 입력하면, 저장된 해시태그 중 이름에 매칭하는 것들을 중복 없이 반환한다.")
    @Test
    void givenHashtagNames_whenFindingHashtags_thenReturnsHashtagSet() {
        // Given
        Set<String> hashtagNames = Set.of("java", "spring", "boots");
        given(hashtagRepository.findByHashtagNameIn(hashtagNames)).willReturn(List.of(
                Hashtag.of("spring"),
                Hashtag.of("java")
        ));

        // When
        Set<Hashtag> hashtags = sut.findHashtagsByNames(hashtagNames);

        System.out.println(hashtags);

        // Then
        assertThat(hashtags).hasSize(2);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNames);
    }


}