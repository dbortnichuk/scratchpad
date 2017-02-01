val seq = Seq(1, 2, 4, 8)
seq.foldLeft((0, 0))((accum, currEl) => (accum._1 + currEl, accum._2 + 1))
seq.reduceLeft((x, y) => x + (y / 2))