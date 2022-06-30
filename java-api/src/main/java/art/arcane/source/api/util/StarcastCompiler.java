package art.arcane.source.api.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class StarcastCompiler {

    public static void main(String[] a) throws FileNotFoundException {
        writeOptimizedSrc(1, 128, true, new File("src/main/java/art/arcane/source/api/util/generated/CompiledStarcast.java"));
    }

    public static void writeOptimizedSrc(int from, int to, boolean arrays, File file) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        StringBuilder va = new StringBuilder();
        StringBuilder co = new StringBuilder();
        List<Float> computed = new ArrayList<>();
        int cx = 0;

        for(int i = from; i <= to; i++)
        {
            generateOptimizedStarcast(i, co, computed);
        }

        for(Float i : computed)
        {
            va.append(cx % 4 == 0 && cx > 0 ? "\n" : "").append(cx == 0? "" : ",").append(i).append("F");
            cx++;
        }

        sb.append("package art.arcane.source.api.util.generated;\n\n");
        sb.append("import art.arcane.source.api.NoisePlane;\n\n");
        sb.append("public class CompiledStarcast {\n");

        sb.append("private static final float[] MAGIC = {");
        sb.append(va);
        sb.append("};\n\n");
        sb.append("public static float getStarcast(float x, float z, float r, float checks, NoisePlane n){\n");
        sb.append("    if(checks >= ").append(from).append(" && checks <= ").append(to).append("){\n");

        for(int i = from; i < to; i++)
        {
            sb.append("        if(checks < ").append(i + 1).append("){")
                .append("return ").append("sc").append(i).append("(x,z,r,n);").append("}\n");
        }

        sb.append("        return ").append("sc").append(to).append("(x,z,r,n);").append("\n");

        sb.append("    }\n\n");



        sb.append("""
            float m = 360F / checks;
                float v = 0;
                        
                for(int i = 0; i < 360; i += m) {
                    float sin = (float) Math.sin(Math.toRadians(i));
                    float cos = (float) Math.cos(Math.toRadians(i));
                    float cx = x + ((r * cos) - (r * sin));
                    float cz = z + ((r * sin) + (r * cos));
                    v += n.noise(cx, cz);
                }
                        
                return v / checks;   
            """);
        sb.append("}\n");
        sb.append(co);
        sb.append("\n}");

        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file);
        PrintWriter pw = new PrintWriter(fos);
        pw.print(sb);
        // For a fun time, use .replaceAll("\\Q\n\\E", "").replaceAll("\\Q  \\E", " ").replaceAll("\\Q  \\E", " ").replaceAll("\\Q  \\E", " ");
        pw.close();
    }

    public static void generateOptimizedStarcast(double checks, StringBuilder code, List<Float> computed) {
        double m = (360 / checks);
        int ig = 0;
        float div = 1f/(float)checks;

        List<Float> compiledNumbers = new ArrayList<>();

        code.append("private static float sc").append((int) checks).append("(float x, float z, float r, NoisePlane n) {\n    return (");
        for(int i = 0; i < 360; i += m) {
            float sin = (float) Math.sin(Math.toRadians(i));
            float cos = (float) Math.cos(Math.toRadians(i));
            int sinIndex;
            int cosIndex;

            if(!computed.contains(sin))
            {
                computed.add(sin);
                sinIndex = computed.size() - 1;
            }

            else {
                sinIndex = computed.indexOf(sin);
            }

            if(!computed.contains(cos))
            {
                computed.add(cos);
                cosIndex = computed.size() - 1;
            }

            else {
                cosIndex = computed.indexOf(cos);
            }

            code.append(ig > 0 ? "\n    +" : "").append("((float)n.noise(x + ((r * ")
                .append("MAGIC[").append(cosIndex).append("]").append(") - (r * ")
                .append("MAGIC[").append(sinIndex).append("]").append(")), z + ((r * ")
                .append("MAGIC[").append(sinIndex).append("]").append(") + (r * ")
                .append("MAGIC[").append(cosIndex).append("]").append("))))");
            ig++;
        }

        code.append(")*").append(div).append("F;\n}\n\n");
    }

}
