package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgeCountDto {

    private List<Integer> age;

}
