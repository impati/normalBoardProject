package com.example.normalboard.service;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber,int totalPages){
        int startNumber = Math.max(0,currentPageNumber - BAR_LENGTH / 2);
        int endNumber = Math.min(totalPages ,startNumber + BAR_LENGTH );
        return IntStream.range(startNumber,endNumber).boxed().collect(Collectors.toList());
    }

    public int currentBarLength(){
        return BAR_LENGTH;
    }
}
