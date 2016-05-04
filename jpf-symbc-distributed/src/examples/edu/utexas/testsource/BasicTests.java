package edu.utexas.testsource;

import java.lang.reflect.InvocationTargetException;

import edu.utexas.util.RunUtil;

public class BasicTests {

	public void testLoop0(int x) {
		while (true) {
			if (x <= 0) {
				if (x > 1) {
					assert false;
				}
				break;
			}
			x = x - 1;
		}
	}

	public int testLoop1(int x) {
		int c = 0, p = 0;
		while (true) {
			if (x <= 0) {
				break;
			}
			if (c == 50) {
				System.out.println("abort1");
				return 0;
			}
			c = c + 1;
			p = p + c;
			x = x - 1;
		}
		if (c == 30) {
			System.out.println("abort2");
			return 1;
		}
		return 2;
	}

	public void doubleMethod(double x, double y) {
		if (x > 0) {
			System.out.println("Place1");
			if (x < y) {
				System.out.println("Place2");
			} else {
				System.out.println("Place3");
			}
		} else {
			if (y < 0) {
				System.out.println("Place4");
			} else {
				System.out.println("Place5");
			}
		}
	}

	public int example(int x, int y, int z) {
		if (z > 0) {
			if (x <= y) {
				if (x > y) {
					return 1;
				} else {
					return 2;
				}
			} else {
				if (x + 1 == 0) {
					if (x != 3) {
						return 3;
					}
					return 4;
				} else {
					return 5;
				}
			}
		}
		return 6;
	}

	public void unsatMethod1(int a, int b) {
		if (a > 0) {
			if (b < 0) {
				if (a < 0) {
					System.out.println("Cannot be here!");
				}
				System.out.println("Here!");
			}
		}
	}

	public void unsatMethod2(int x) {
		if (x > 5) {
			if (x < 2) {
				System.out.println("Cannot be here!");
			}
			System.out.println("Here!");
		}
	}

	public void loop1(int a) {
		int i = 3;
		while (i > 0) {
			if (a > 0) {
				System.out.println("Path 1 in loop1");
			} else {
				System.out.println("Path 2 in loop1");
			}
			i--;
		}
	}

	public void nothing(int a, int b) {
		if (b > 0) {
			if (a > 0) {
				System.out.println("Path 1");
			} else {
				System.out.println("Path 3");
			}
		} else {
			System.out.println("Path 2");
		}
	}

	public void nothingMultipleTimes(int a, int b) {
		nothing(a, b);
		nothing(a, b);
		nothing(a, b);
		nothing(a, b);
		nothing(a, b);
	}

	public int triangPartial(int x, int y, int z) {
		int tri_out;
		if (x <= 0 || y <= 0 || z <= 0) {
			tri_out = 4;
			return tri_out;
		}
		tri_out = 0;
		if (x == y)
			tri_out = tri_out + 1;
		if (x == z)
			tri_out = tri_out + 2;
		if (y == z)
			tri_out = tri_out + 3;
		if (tri_out == 0) {
			if (x + y <= z || y + z <= x || x + z <= y)
				tri_out = 4;
			else
				tri_out = 1;
			return tri_out;
		}
		return tri_out;
	}

	public int p(int x, int y) {
		if (x > y) {
			System.out.println("Place 1");
			x--;
		} else {
			System.out.println("Place 2");
			y++;
		}
		if (x == y) {
			System.out.println("Place 3");
			return x;
		} else {
			System.out.println("Place 4");
			return y;
		}
	}

	public int pMore(int x, int y) {
		if (x > y) {
			x--;
			System.out.println("Place 1");
		} else {
			y++;
			System.out.println("Place 2");
		}
		if (x == y) {
			if (x > 1) {
				System.out.println("Place 3");
				return x;
			}
			System.out.println("Place 4");
			return y;
		} else {
			if (x < 1) {
				System.out.println("Place 5");
				return x;
			}
			System.out.println("Place 6");
			return y;
		}
	}

	public int ppp(int x) {
		if (x > 0) {
			System.out.println("Place 0");
			return 0;
		} else {
			System.out.println("Place 1");
			return 1;
		}
	}

	public int pp(int x) {
		if (x > 0) {
			System.out.println("Place 0");
			if (x < 5) {
				if (x > 3) {
					System.out.println("Place 5");
				} else {
					System.out.println("Place 6");
				}

				return 1;
			} else {
				System.out.println("Place 2");
				return 0;
			}
		} else {
			System.out.println("Place 3");
			return 2;
		}
	}

	public void p1(int x, int y) {
		if (x < y) {
			x--;
			System.out.println("p1");
		} else {
			System.out.println("p2");
			y--;
		}
		if (x == y) {
			System.out.println("p3");
		} else {
			System.out.println("p4");
		}
	}

	public void m1(int x, int y) {
		if (x > 0) {
			System.out.println("x > 0");
		} else {
			System.out.println("x <= 0");
		}
		if (y > 0) {
			System.out.println("y > 0");
		} else {
			System.out.println("y <= 0");
		}
	}

	public static void main(String[] args) {
		BasicTests o = new BasicTests();
		try {
			if (args.length == 1) {
				RunUtil.runMethod(o, args[0]);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

}
