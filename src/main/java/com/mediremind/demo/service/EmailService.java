package com.mediremind.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    // Spring auto-configures this using our application.properties mail settings

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendMedicineReminder(String toEmail,
                                     String patientName,
                                     String medicineName,
                                     String dosage) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
           

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("💊 MediRemind — Time to take " + medicineName);

            String htmlContent = buildReminderEmailHTML(patientName,
                    medicineName,
                    dosage);
            helper.setText(htmlContent, true);
     

            mailSender.send(message);

            System.out.println("✅ Reminder email sent to: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to " + toEmail
                    + ": " + e.getMessage());

        }
    }

    public void sendWelcomeEmail(String toEmail, String patientName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("🏥 Welcome to MediRemind!");

            String htmlContent = buildWelcomeEmailHTML(patientName);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("✅ Welcome email sent to: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send welcome email: "
                    + e.getMessage());
        }
    }

    // 🎨 Build HTML for reminder email
    private String buildReminderEmailHTML(String patientName,
                                          String medicineName,
                                          String dosage) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="font-family: Arial, sans-serif;
                             background-color: #f4f4f4;
                             padding: 20px;">
                    <div style="max-width: 500px;
                                margin: auto;
                                background: white;
                                border-radius: 10px;
                                padding: 30px;
                                box-shadow: 0 2px 10px rgba(0,0,0,0.1);">

                        <h2 style="color: #2c7be5;">💊 Medicine Reminder</h2>
                        <hr style="border: 1px solid #eee;">

                        <p style="font-size: 16px;">Hi <strong>%s</strong>,</p>

                        <p style="font-size: 16px;">
                            It's time to take your medicine!
                        </p>

                        <div style="background: #f0f7ff;
                                    border-left: 4px solid #2c7be5;
                                    padding: 15px;
                                    border-radius: 5px;
                                    margin: 20px 0;">
                            <p style="margin:0; font-size: 18px;">
                                🏷️ <strong>Medicine:</strong> %s
                            </p>
                            <p style="margin:5px 0 0; font-size: 18px;">
                                💉 <strong>Dosage:</strong> %s
                            </p>
                        </div>

                        <p style="color: #666; font-size: 14px;">
                            Please take your medicine as prescribed.
                            Consistency is key to recovery! 💪
                        </p>

                        <hr style="border: 1px solid #eee;">
                        <p style="color: #999; font-size: 12px; text-align:center;">
                            MediRemind — Your Personal Medicine Reminder
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(patientName, medicineName, dosage);
        // .formatted() replaces %s placeholders with actual values
    }

    // 🎨 Build HTML for welcome email
    private String buildWelcomeEmailHTML(String patientName) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="font-family: Arial, sans-serif;
                             background-color: #f4f4f4;
                             padding: 20px;">
                    <div style="max-width: 500px;
                                margin: auto;
                                background: white;
                                border-radius: 10px;
                                padding: 30px;
                                box-shadow: 0 2px 10px rgba(0,0,0,0.1);">

                        <h2 style="color: #28a745;">🏥 Welcome to MediRemind!</h2>
                        <hr style="border: 1px solid #eee;">

                        <p style="font-size: 16px;">Hi <strong>%s</strong>,</p>

                        <p style="font-size: 16px;">
                            You have successfully registered on MediRemind!
                            We will help you never miss a dose again. 💊
                        </p>

                        <p style="color: #666;">
                            Start by adding your medicines and
                            we'll send you timely reminders.
                        </p>

                        <hr style="border: 1px solid #eee;">
                        <p style="color: #999; font-size: 12px; text-align:center;">
                            MediRemind — Your Personal Medicine Reminder
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(patientName);
    }
}
