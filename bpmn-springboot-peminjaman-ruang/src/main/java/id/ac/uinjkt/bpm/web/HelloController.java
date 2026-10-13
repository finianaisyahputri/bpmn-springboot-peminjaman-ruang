package id.ac.uinjkt.bpm.web;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final RuntimeService runtimeService;

    public HelloController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    /** Tes instalasi: menjalankan proses BPMN hello-world.bpmn */
    @GetMapping("/hello")
    public String hello() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("helloWorld");
        return "Hello World! Proses BPMN 'helloWorld' dijalankan (instance " + pi.getId() + ")";
    }
}
