package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetMyPageFeedsRes {
    private boolean hasNext;
    private List<String> feedImageList;


}
