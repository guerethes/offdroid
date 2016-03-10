package br.com.guerethes.acra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import android.util.Log;

public class MailSender implements ReportSender{

	private Session session = null;
	static Random gerador = new Random();
	static String email = "";
	
	public MailSender(String email) {
		super();
		this.email = email;
		initSession();
	}
	
	public MailSender() {
		super();
		this.email = "mobile@info.ufrn.br";
		initSession();
	}

	@Override
	public void send(CrashReportData report) throws ReportSenderException {
		String reportBody = createCrashReport(report);
		try {
			int i = new Random().nextInt(2);
			if ( i == 0 ) {
				sendMail("CRASH REPORT", reportBody, "mobile@info.ufrn.br", email);	
				checkHasFile();
			} else {
				createFile(reportBody);
				Log.d("Status da Conexão", "Arquivo Criado com sucesso");
			}
		} catch (Exception e) {
			Log.d("Error Sending email", e.toString());
			e.printStackTrace();
		}
	}

	private void checkHasFile() {
		String path = DeviceInformationHandler.getExceptionsDir();
		File file = new File(path);
		File[] list = file.listFiles();
		for (File f: list){
			String name = f.getName();
			String mailSender =  name.substring(0, name.indexOf("-"));
			Log.i("Status da Conexão", name);
			
			StringBuilder text = new StringBuilder();
			
			try {
				BufferedReader br =  new BufferedReader(new FileReader(f));
				String line;

			    while ((line = br.readLine()) != null) {
			        text.append(line);
			        text.append('\n');
			    }
			    br.close();
			    sendMail("CRASH REPORT", text.toString(), "mobile@info.ufrn.br", mailSender);
			    f.delete();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void createFile(String reportBody) throws IOException {
		String filename = Long.toString(new Date().getTime());

		// Cria o arquivo para guardar a exceção
		BufferedWriter bw = new BufferedWriter(new FileWriter(DeviceInformationHandler.getExceptionsDir() + "/" + email + "-" + filename + ".stacktrace"));

		bw.write(reportBody);
		bw.flush();
		bw.close();
	}

	public void initSession(){
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.info.ufrn.br");
		session = Session.getInstance(props);
	}

	/** Extract the required data out of the crash report.*/
	private String createCrashReport(CrashReportData report) {

		// I've extracted only basic information.
		// U can add loads more data using the enum ReportField. See below.
		StringBuilder body = new StringBuilder();
		body
		.append("Device : " + report.getProperty(ReportField.BRAND) + "-" + report.getProperty(ReportField.PHONE_MODEL))
		.append("\n")
		.append("Android Version :" + report.getProperty(ReportField.ANDROID_VERSION))
		.append("\n")
		.append("App Version : " + report.getProperty(ReportField.APP_VERSION_CODE))
		.append("\n")
		.append("STACK TRACE : \n" + report.getProperty(ReportField.STACK_TRACE))
		.append("\n")
		.append("LOGCAT : \n" + report.getProperty(ReportField.LOGCAT))
		.append("\n")
		.append("CRASH DATE : \n" + report.getProperty(ReportField.USER_CRASH_DATE))
		.append("\n")
		.append("USER EMAIL : \n" + report.getProperty(ReportField.USER_EMAIL));
		
		return body.toString();
	}

	public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {   
		try{
			MimeMessage message = new MimeMessage(session);   
			DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));   
			message.setSender(new InternetAddress(sender));   
			message.setSubject(subject);   
			message.setDataHandler(handler);   
			if (recipients.indexOf(',') > 0)   
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));   
			else 
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));   
			Transport.send(message);   
		}catch(Exception e){
			Log.d("Error Sending Email", e.toString());
		}
	}  

}