package everymeal.server.user.controller;


import everymeal.server.global.dto.response.ApplicationResponse;
import everymeal.server.global.util.authresolver.Auth;
import everymeal.server.global.util.authresolver.AuthUser;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import everymeal.server.user.controller.dto.request.UserEmailAuthReq;
import everymeal.server.user.controller.dto.request.UserEmailAuthVerifyReq;
import everymeal.server.user.controller.dto.response.UserEmailAuthRes;
import everymeal.server.user.controller.dto.response.UserLoginRes;
import everymeal.server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "유저 관련 API입니다")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping
    public ApplicationResponse<Boolean> signUp(
            @RequestBody
                    @Schema(
                            description = "유저의 기기 고유 번호",
                            defaultValue = "123456789",
                            example = "1234567890")
                    String userDeviceId) {
        return ApplicationResponse.ok(userService.signUp(userDeviceId));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApplicationResponse<UserLoginRes>> login(
            @RequestBody
                    @Schema(
                            description = "유저의 기기 고유 번호",
                            defaultValue = "123456789",
                            example = "1234567890")
                    String userDeviceId) {
        UserLoginRes response = userService.login(userDeviceId);
        ResponseCookie cookie =
                ResponseCookie.from("refresh-token", response.getRefreshToken())
                        .httpOnly(true)
                        .sameSite("None")
                        .path("/")
                        .maxAge(60 * 60 * 24 * 30L)
                        .secure(true)
                        .build();
        response.setRefreshToken(null);
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(ApplicationResponse.ok(response));
    }

    @Auth(require = true)
    @GetMapping("/auth")
    @Operation(summary = "유저 인증 여부",
    description = "유저가 인증되었는지 여부를 반환합니다. <br> 인증되었다면 true, 아니라면 false를 반환합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ApplicationResponse<Boolean> isAuth(@AuthUser @Parameter(hidden = true
    ) AuthenticatedUser authenticatedUser) {
        return ApplicationResponse.ok(userService.isAuth(authenticatedUser));
    }

    @Auth(require = true)
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/email/auth")
    @Operation(
            summary = "이메일 인증",
            description =
                    "이메일 인증을 진행합니다. <br> Response에 5분 동안 유효한 JWT 토큰이 담기는데 해당 토큰에는 발송 값이 들어있습니다.")
    public ApplicationResponse<UserEmailAuthRes> emailAuth(
            @RequestBody UserEmailAuthReq request,
            @AuthUser @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        return ApplicationResponse.ok(userService.emailAuth(request, authenticatedUser));
    }

    @Auth(require = true)
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/email/auth/verify")
    @Operation(
            summary = "이메일 인증 확인",
            description = "이메일 인증을 확인합니다. <br> Request에는 이메일 인증 시 발송된 값이 담겨야 합니다.")
    public ApplicationResponse<Boolean> verifyEmailAuth(
            @RequestBody
                UserEmailAuthVerifyReq request,
            @AuthUser @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        return ApplicationResponse.ok(userService.verifyEmailAuth(request, authenticatedUser));
    }

    @Auth(require = true)
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/profile")
    @Operation(
            summary = "프로필 생성",
            description = "프로필을 생성합니다. <br> Request에는 수정할 프로필 정보가 담겨야 합니다.")
    public ApplicationResponse<Boolean> createProfile(
            @RequestBody
                    @Schema(
                            description = "프로필 정보",
                            defaultValue = "프로필 정보",
                            example = "프로필 정보")
                    String profile,
            @AuthUser @Parameter(hidden = true) AuthenticatedUser authenticatedUser) {
        return ApplicationResponse.ok(userService.createProfile(profile, authenticatedUser));
    }

}
