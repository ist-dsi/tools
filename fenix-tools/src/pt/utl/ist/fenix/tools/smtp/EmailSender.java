package pt.utl.ist.fenix.tools.smtp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    public static Collection<String> send(
    		final String server, final String fromName, final String fromAddress,
    		final Collection<String> toAddresses, final Collection<String> ccAddresses, final Collection<String> bccAddresses,
    		final String subject, final String body) {

        final Collection<String> unsentAddresses = new ArrayList<String>(0);

        final MimeMessage mimeMessage = createNewMessage(server);
        try {
        	final String from = constructFromString(fromName, fromAddress);
            mimeMessage.setFrom(new InternetAddress(from));
            addRecipients(mimeMessage, Message.RecipientType.TO, toAddresses, unsentAddresses);
            addRecipients(mimeMessage, Message.RecipientType.CC, ccAddresses, unsentAddresses);
            addRecipients(mimeMessage, Message.RecipientType.BCC, bccAddresses, unsentAddresses);
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);
            Transport.send(mimeMessage);
        } catch (SendFailedException e) {
        	registerInvalidAddresses(unsentAddresses, e, toAddresses, ccAddresses, bccAddresses);
        } catch (MessagingException e) {
        	e.printStackTrace();
        }
        return unsentAddresses;
    }

	private static void registerInvalidAddresses(final Collection<String> unsentAddresses,
			final SendFailedException e, final Collection<String> toAddresses,
			final Collection<String> ccAddresses, final Collection<String> bccAddresses) {
        if (e.getValidUnsentAddresses() != null) {
            for (int i = 0; i < e.getValidUnsentAddresses().length; i++) {
            	unsentAddresses.add(e.getValidUnsentAddresses()[i].toString());
            }
        } else {
            if (e.getValidSentAddresses() == null || e.getValidSentAddresses().length == 0) {
            	unsentAddresses.addAll(toAddresses);
            	unsentAddresses.addAll(ccAddresses);
            	unsentAddresses.addAll(bccAddresses);
            }
        }
	}

	private static String constructFromString(final String fromName, String fromAddress) {
		return (fromName == null || fromName.length() == 0) ?
				"\"" + fromName + "\"" + " <" + fromAddress + ">" : fromAddress;
	}

	private static MimeMessage createNewMessage(final String server) {
        final Properties properties = new Properties();
        properties.put("mail.smtp.host", server);
        final Session session = Session.getDefaultInstance(properties, null);
        return new MimeMessage(session);
	}

	private static void addRecipients(final MimeMessage mensagem, final RecipientType recipientType,
			final Collection<String> emailAddresses, Collection<String> unsentMails)
			throws MessagingException {
		if (emailAddresses != null) {
			for (final String emailAddress : emailAddresses) {
				try {
					if (emailAddressFormatIsValid(emailAddress)) {
						mensagem.addRecipient(recipientType, new InternetAddress(emailAddress));
					}
				} catch (AddressException e) {
					unsentMails.add(emailAddress);
				}
			}
		}
	}

	public static boolean emailAddressFormatIsValid(String emailAddress) {
		if((emailAddress == null) || (emailAddress.length() == 0))
			return false;
						
		if(emailAddress.indexOf(' ') > 0)
			return false;
		
		String[] atSplit = emailAddress.split("@");
		if(atSplit.length != 2)
			return false;

		else if((atSplit[0].length() == 0) || (atSplit[1].length() == 0))
			return false;
		
		String domain = new String(atSplit[1]);

		if(domain.lastIndexOf('.') == (domain.length() - 1))
			return false;
		
		if(domain.indexOf('.') <= 0)
			return false;
				
		return true;
		
	}
}