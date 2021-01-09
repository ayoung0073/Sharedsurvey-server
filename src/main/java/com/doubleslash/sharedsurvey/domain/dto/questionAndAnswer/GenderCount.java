package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenderCount {
    private int woman = 0;
    private int man = 0;
}
