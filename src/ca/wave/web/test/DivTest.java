package ca.wave.web.test;

public class DivTest {
	public static void main(String[] args) {
		long now = System.currentTimeMillis();
		long future = now + 103567L;
		System.out.println((future - now) >> 3);
		System.out.println((future - now) % 1000);
		System.out.println((future - now) / 1000);
	}
}
