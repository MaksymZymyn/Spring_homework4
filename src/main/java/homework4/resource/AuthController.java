package homework4.resource;

import homework4.security.JwtRequest;
import homework4.security.JwtResponse;
import homework4.security.RefreshJwtRequest;
import homework4.service.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/revoke")
    public ResponseEntity<?> revokeToken(@RequestParam String accessToken) {
        boolean isRevokeSuccess = authService.revokeToken(accessToken);
        if (isRevokeSuccess) {
            return ResponseEntity.ok("Token was revoked successfully");
        }
        return ResponseEntity.badRequest().body("Token was not revoked");
    }
}
