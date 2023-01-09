package com.example.normalboard.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.DataAmount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테스트 도구 - Form 데이터 인코더")
@Import({FormDataEncoder.class, ObjectMapper.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Void.class)
class FormDataEncoderTest {

    private final FormDataEncoder formDataEncoder;

    public FormDataEncoderTest(@Autowired FormDataEncoder formDataEncoder) {
        this.formDataEncoder = formDataEncoder;
    }

    @DisplayName("객체를 넣으면, url encoding 된 form body data 형식의 문자열을 돌려준다.")
    @Test
    void givenObject_whenEncoding_thenReturnsFormEncodedString() {
        // Given
        TestObject obj = new TestObject(
                "This 'is' \"test\" string.",
                List.of("hello", "my", "friend").toString().replace(" ", ""),
                String.join(",", "hello", "my", "friend"),
                null,
                1234,
                3.14,
                false,
                BigDecimal.TEN,
                TestEnum.THREE
        );

        // When
        String result = formDataEncoder.encode(obj);

        // Then
        assertThat(result).isEqualTo(
                "str=This%20'is'%20%22test%22%20string." +
                        "&listStr1=%5Bhello,my,friend%5D" +
                        "&listStr2=hello,my,friend" +
                        "&nullStr" +
                        "&number=1234" +
                        "&floatingNumber=3.14" +
                        "&bool=false" +
                        "&bigDecimal=10" +
                        "&testEnum=THREE"
        );
    }


    static class TestObject {
        String str;
        String listStr1;
        String listStr2;
        String nullStr;
        Integer number;
        Double floatingNumber;
        Boolean bool;
        BigDecimal bigDecimal;
        TestEnum testEnum;

        public TestObject(String str, String listStr1, String listStr2, String nullStr, Integer number, Double floatingNumber, Boolean bool, BigDecimal bigDecimal, TestEnum testEnum) {
            this.str = str;
            this.listStr1 = listStr1;
            this.listStr2 = listStr2;
            this.nullStr = nullStr;
            this.number = number;
            this.floatingNumber = floatingNumber;
            this.bool = bool;
            this.bigDecimal = bigDecimal;
            this.testEnum = testEnum;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public void setListStr1(String listStr1) {
            this.listStr1 = listStr1;
        }

        public void setListStr2(String listStr2) {
            this.listStr2 = listStr2;
        }

        public void setNullStr(String nullStr) {
            this.nullStr = nullStr;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public void setFloatingNumber(Double floatingNumber) {
            this.floatingNumber = floatingNumber;
        }

        public void setBool(Boolean bool) {
            this.bool = bool;
        }

        public void setBigDecimal(BigDecimal bigDecimal) {
            this.bigDecimal = bigDecimal;
        }

        public void setTestEnum(TestEnum testEnum) {
            this.testEnum = testEnum;
        }

        public String getStr() {
            return str;
        }

        public String getListStr1() {
            return listStr1;
        }

        public String getListStr2() {
            return listStr2;
        }

        public String getNullStr() {
            return nullStr;
        }

        public Integer getNumber() {
            return number;
        }

        public Double getFloatingNumber() {
            return floatingNumber;
        }

        public Boolean getBool() {
            return bool;
        }

        public BigDecimal getBigDecimal() {
            return bigDecimal;
        }

        public TestEnum getTestEnum() {
            return testEnum;
        }
    }

    enum TestEnum {
        ONE, TWO, THREE
    }

}
