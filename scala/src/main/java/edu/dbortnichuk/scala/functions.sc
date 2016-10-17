case class Email(subject: String, text: String, sender: String, recipient: String)

def complement[A](predicate: A => Boolean) = (a: A) => !predicate(a)

type EmailFilter = Email => Boolean

def newMailsForUser(mails: Seq[Email], f: EmailFilter): Seq[Email] = mails.filter(f)

val sentByOneOf: Set[String] => EmailFilter =
  senders => email => senders.contains(email.sender)

//val notSentByAnyOf: Set[String] => EmailFilter =
//  senders => email => !senders.contains(email.sender)

val notSentByAnyOf = sentByOneOf andThen(complement(_))
//val minimumSize: Int => EmailFilter = n => email => email.text.length >= n

//val maximumSize: Int => EmailFilter = n => email => email.text.length <= n

val emailFilter: EmailFilter = notSentByAnyOf(Set("johndoe@example.com"))
val mails = Email(
  subject = "It's me again, your stalker friend!",
  text = "Hello my friend! How are you?",
  sender = "johndoe@example.com",
  recipient = "me@example.com") :: Nil

newMailsForUser(mails, emailFilter)

type SizeChecker = Int => Boolean
val sizeConstraint: SizeChecker => EmailFilter = f => email => f(email.text.size)

val minimumSize: Int => EmailFilter = n => sizeConstraint(_ >= n)
val maximumSize: Int => EmailFilter = n => sizeConstraint(_ <= n)

def any[A](predicates: (A => Boolean)*): A => Boolean =
  a => predicates.exists(pred => pred(a))
def none[A](predicates: (A => Boolean)*) = complement(any(predicates: _*))
def every[A](predicates: (A => Boolean)*) = none(predicates.view.map(complement(_)): _*)

val filter: EmailFilter = every(
  notSentByAnyOf(Set("johndoe@example.com")),
  minimumSize(100),
  maximumSize(10000)
)


