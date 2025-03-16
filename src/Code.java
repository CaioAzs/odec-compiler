import java.util.Scanner;

public class Code {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Digite a quantidade de números a serem printados:");
		int qnt_num = scanner.nextInt();
		int num;
		for (num = 0; num <= qnt_num; num = num + 1) {
			System.out.println(num);
			for (num = 0; num <= qnt_num; num = num + 1) {
				System.out.println("a");
			}
		}

		scanner.close();
	}
}
