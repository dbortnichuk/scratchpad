case class Email(subject: String, text: String, sender: String, recipient: String)

val addMissingSubject = (email: Email) =>
  if (email.subject.isEmpty) email.copy(subject = "No subject")
  else email
val checkSpelling = (email: Email) =>
  email.copy(text = email.text.replaceAll("your", "you're"))
val removeInappropriateLanguage = (email: Email) =>
  email.copy(text = email.text.replaceAll("dynamic typing", "**CENSORED**"))
val addAdvertismentToFooter = (email: Email) =>
  email.copy(text = email.text + "\nThis mail sent via Super Awesome Free Mail")

val pipeline = Function.chain(Seq(
  addMissingSubject,
  checkSpelling,
  removeInappropriateLanguage,
  addAdvertismentToFooter))

val filteredEmail = pipeline(Email("", "your here, no dynamic typing", "some@some.com", "some@some.com"))

val pf: PartialFunction[Int, Boolean] = { case i if i > 0 => i % 2 == 0}
//pf(-2)

val liftedPf = pf.lift
liftedPf(-2)

def times2(i: Int) = i * 2
val f = times2 _  //eta-expansion
times2(2)
f(2)
