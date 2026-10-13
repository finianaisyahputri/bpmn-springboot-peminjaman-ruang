package id.ac.uinjkt.bpm.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("helloWorldDelegate")
public class HelloWorldDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info(">>> Hello World dari BPMN + Spring Boot! processInstanceId={}", execution.getProcessInstanceId());
    }
}
