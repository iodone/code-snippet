val xs = Array.fill(100)(1)
for (i <- 2 until 100; j <- Range(i+i, 100, i)) {
  if (xs(i) == 1) {
    xs(j) = 0
  }
}
