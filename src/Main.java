import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File file_g = new File("grammar.txt");
        File file_t = new File("terminals.txt");
        File file_v = new File("variables.txt");
        File file_i = new File("input.txt");

        Scanner scanner_g = new Scanner(file_g);
        Scanner scanner_t = new Scanner(file_t);
        Scanner scanner_v = new Scanner(file_v);
        Scanner scanner_i = new Scanner(file_i);

        String tmp2 = scanner_v.nextLine();
        String[] vars;
        vars = tmp2.split(",");

        String string = scanner_i.nextLine();

        int nOfL = 0;
        while (scanner_g.hasNextLine()) {
            scanner_g.nextLine();
            nOfL++;
        }

        scanner_g.close();
        scanner_g = new Scanner(file_g);

        String[] grams = new String[nOfL];

        String[] tmp;
        String[] sGram = new String[nOfL];
        String[] eGram = new String[nOfL];

        int nOfOr = 0;
        int[] orInL = new int[nOfL];

        for (int i = 0; i < nOfL; i++) {
            grams[i] = scanner_g.nextLine();
            for (int j = 0; j < grams[i].length(); j++) {
                if (grams[i].charAt(j) == '|') {
                    orInL[i]++;
                    nOfOr++;
                }
            }

            tmp = grams[i].split("->");
            sGram[i] = tmp[0];
            eGram[i] = tmp[1];
        }


        String[] iGram = new String[nOfL + nOfOr];
        String[] oGram = new String[nOfL + nOfOr];

        for (int i = 0, j = 0; (i < nOfL) && (j < nOfL + nOfOr); i++, j++) {
            if (orInL[i] == 0) {
                iGram[j] = sGram[i];
                oGram[j] = eGram[i].substring(0, eGram[i].length() - 1);
            } else {
                String[] outs;
                outs = eGram[i].substring(0, eGram[i].length() - 1).split("\\|");
                for (int k = 0; k < orInL[i] + 1; k++, j++) {
                    iGram[j] = sGram[i];
                    oGram[j] = outs[k];
                }
                j--;
            }
        }

        createString("S", iGram, oGram, vars, string);

        System.out.println(false);

    }

    private static void createString(String input, String[] iGram, String[] oGram, String[] vars, String string) {
        for (int i = 0; i < iGram.length; i++) {
            if (input.contains(iGram[i])) {
                int fIndex = 0;
                while (true) {
                    fIndex = input.indexOf(iGram[i], fIndex);
                    if (fIndex == -1) {
                        break;
                    } else {
                        StringBuilder str = new StringBuilder(input);
                        str.replace(fIndex, fIndex + iGram[i].length(), oGram[i]);

                        if (str.indexOf("?") != -1) {
                            str.replace(str.indexOf("?"), str.indexOf("?") + 1, "");
                        }

                        if (str.length() < string.length() + 5) {
                            createString(str.toString(), iGram, oGram, vars, string);
                        }

                        boolean isStr = true;
                        for (int j = 0; j < vars.length; j++) {
                            if (str.indexOf(vars[j]) != -1) {
                                isStr = false;
                            }
                        }

                        String string2 = str.toString();
                        if (isStr) {
                            //System.out.println(str);
                            if (string2.equals(string)){
                                System.out.println(true);
                                System.exit(0);
                            }
                        }
                        fIndex++;
                    }
                }
            }
        }
    }
}
