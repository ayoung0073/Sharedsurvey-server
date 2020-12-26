package com.doubleslash.sharedsurvey.controller;

import com.doubleslash.sharedsurvey.controller.SurveyController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SurveyController.class)
public class SurveyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test() throws Exception{
        String test = "{\"success\":false}";
        mvc.perform(get("/survey/15"))
                .andExpect(status().isOk())
                .andExpect(content().string(test));
    }
}
