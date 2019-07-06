package garagedoor.alexa;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlexaConfig {
    

    @Bean
    public ServletRegistrationBean registerBean(){

        SpeechletServlet speechletServlet = new SpeechletServlet();
        //speechletServlet.setSpeechlet();

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(speechletServlet, "/alexa");
        return servletRegistrationBean;
    }
}
