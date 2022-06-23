class FloatCache {
  final List<double> cache;
  final int width;
  final int height;

  FloatCache(this.width, this.height)
      : cache = <double>[..._initCache(width * height)];

  set(int x, int y, double v) {
    cache[((y % height) * width) + (x % width)] = v;
  }

  double get(int x, int y) {
    return cache[((y % height) * width) + (x % width)];
  }

  static Iterable<double> _initCache(int size) sync* {
    for (int i = 0; i < size; i++) {
      yield 0;
    }
  }
}
