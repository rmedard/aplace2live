package be.aplacetolive.service;

/**
 * Created by medard on 03.07.17.
 */
public interface EmailService {
    void sendSimpleMessage(String[] to, String subject, String text);
}
