package garagedoor.Configurations;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlexaConfig {

    @Bean
    public ServletRegistrationBean registerBean() {

        SpeechletServlet speechletServlet = new SpeechletServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(speechletServlet, "/alexa");
        return servletRegistrationBean;
    }
}
