package unaldi.orderservice.service.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import unaldi.orderservice.entity.dto.EmailDetailsDTO;
import unaldi.orderservice.service.Impl.EmailServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void givenValidEmailDetails_whenSendEmail_thenEmailShouldBeSentSuccessfully() {
        EmailDetailsDTO emailDetailsDTO = new EmailDetailsDTO("test@example.com", "Test Subject", "Test Body");

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendEmail(emailDetailsDTO);

        verify(javaMailSender, times(1))
                .send(any(SimpleMailMessage.class));
    }

    @Test
    void givenInvalidEmailDetails_whenSendEmail_thenMailExceptionShouldBeThrown() {
        EmailDetailsDTO emailDetailsDTO = new EmailDetailsDTO("", "", "");

        doThrow(new MailSendException("Failed to send email"))
                .when(javaMailSender).send(any(SimpleMailMessage.class));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> emailService.sendEmail(emailDetailsDTO));

        assertThrows(MailException.class, () -> emailService.sendEmail(emailDetailsDTO));
        assertEquals("Failed to send email", thrown.getMessage(), "Exception message does not match");
    }
}
