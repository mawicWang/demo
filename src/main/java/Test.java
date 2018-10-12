import com.diogonunes.jcdp.bw.Printer.*;
import com.diogonunes.jcdp.bw.Printer;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.*;

import java.util.Random;

public class Test {

    public static void main(String[] args) {

        OpenSimplexNoise osn = new OpenSimplexNoise(19900502);


        int[][] map = new int[50][50];
        ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();

        int[] you = new int[2];
        you[0] = 35;
        you[1] = 40;

        for (int x = 0; x < 50; ++x) {
            for (int y = 0; y < 50; ++y) {
                double d = 5.0;
                double dx = x / d;
                double dy = y / d;
                double dv = osn.eval(dx, dy);

                map[x][y] = (int) (dv * d);
            }
        }

        render(map, you, cp);
        while (true) {
            Random random = new Random();
            int rand = random.nextInt(4);
            switch (rand) {
                case 0:
                    you[0] = you[0] - 1;
                    break;
                case 1:
                    you[0] = you[0] + 1;
                    break;
                case 3:
                    you[1] = you[1] - 1;
                    break;
                case 4:
                    you[1] = you[1] + 1;
                    break;
            }
            if (you[0] < 0) {
                you[0] = 0;
            }
            if (you[0] > 49) {
                you[0] = 49;
            }
            if (you[1] < 0) {
                you[1] = 0;
            }
            if (you[1] > 49) {
                you[1] = 49;
            }

            render(map, you, cp);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void render(int[][] map, int[] you, ColoredPrinter cp) {
        for (int x = 0; x < 50; ++x) {
            for (int y = 0; y < 50; ++y) {
                if (x == you[0] && y == you[1]) {
                    cp.print("  ", Attribute.NONE, FColor.BLACK, BColor.RED);
                    continue;
                }

                if (map[x][y] < 0) {
                    cp.print("  ", Attribute.NONE, FColor.BLACK, BColor.WHITE);
                } else {
                    cp.print("  ", Attribute.NONE, FColor.BLACK, BColor.GREEN);
                }
                cp.clear();
            }
            System.out.println();
        }
    }
}
