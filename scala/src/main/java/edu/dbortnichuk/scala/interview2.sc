val stream = (1 to 100000000).toStream
stream.head
stream.tail

//val list = (1 to 100000000).toList
val stream1 = 0 #:: stream
val stream2 = Stream(-1) #::: stream1