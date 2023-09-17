package everymeal.server.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import everymeal.server.global.ControllerTestSupport;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("회원가입을 진행한다.")
    @Test
    void signUp() throws Exception {
        // given
        String deviceId = "123456789";

        // when then
        mockMvc.perform(
                        post("/api/v1/users")
                                .content(deviceId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("로그인을 진행한다.")
    @Test
    void login() throws Exception {
        // given
        String deviceId = "123456789";

        given(userService.login(any()))
                .willReturn(
                        UserLoginRes.builder()
                                .accessToken("accessToken")
                                .refreshToken("refreshToken")
                                .build());

        // when then
        mockMvc.perform(
                        post("/api/v1/users/login")
                                .content(deviceId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(cookie().exists("refresh-token"));
    }
}
