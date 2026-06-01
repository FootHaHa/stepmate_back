package foothaha.stepmate_back.run.controller;

import foothaha.stepmate_back.run.service.RunSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/run")
@RequiredArgsConstructor
public class RunSessionController {

    private final RunSessionService runSessionService;
}