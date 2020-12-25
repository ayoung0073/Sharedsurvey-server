package com.doubleslash.sharedsurvey;

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
public class SurveyControllerTest2 {
    @Autowired
    private MockMvc mvc;

    @Test
    public void test() throws Exception{
        mvc.perform(get("/surveys"))
                .andExpect(status().isOk());
    }
}
