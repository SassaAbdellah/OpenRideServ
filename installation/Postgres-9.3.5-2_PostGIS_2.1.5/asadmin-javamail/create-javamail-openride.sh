

asadmin create-javamail-resource                                           \
  --mailhost="Your mailhost(e.g: <your.mailhost.com)"                      \
  --mailuser="User name for SMTP auth, e.g: <openrideshare@openrideshare.org"> \
  --fromaddress="Addres that from which the mails should appear to be sent, e.g:<interface@openrideshare.org>" \
  --debug="false"                                                         \ 
  --enabled="true"                                                        \
  --description="javamail session for openride"                           \
  --property="mail.smtp.password=Wpq5hCMbwZuE:mail.smtp.auth=true"        \
  jorideMail 
