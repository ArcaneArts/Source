abstract class NoisePlane {
  double noise1(double x) => noise2(x, 0);
  double noise2(double x, double y) => noise3(x, y, 0);
  double noise3(double x, double y, double z);
  double getMaxOutput() => 1;
  double getMinOutput() => -1;

  void benchmark(String name, double ms) {
    double msb = ms / 3;
    int t = DateTime.now().millisecondsSinceEpoch;
    int d1 = 0;
    int d2 = 0;
    int d3 = 0;
    double n = 0;

    while (DateTime.now().millisecondsSinceEpoch - t < msb) {
      noise1(n++);
      d1++;
    }

    t = DateTime.now().millisecondsSinceEpoch;

    while (DateTime.now().millisecondsSinceEpoch - t < msb) {
      noise2(n++, n++);
      d2++;
    }

    t = DateTime.now().millisecondsSinceEpoch;

    while (DateTime.now().millisecondsSinceEpoch - t < msb) {
      noise3(n++, n++, n++);
      d3++;
    }

    print(
        "$name: 1D: ${(d1 / msb).round()}/ms 2D: ${(d2 / msb).round()}/ms 3D: ${(d3 / msb).round()}/ms");
  }
}
