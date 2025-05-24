/*
 *    Copyright 2006-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package mbg.domtest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.internal.util.StringUtility;
import org.reflections.Reflections;

public class GenerateTestSourceFiles {

    public static void main(String[] args) {
        if (args.length < 1 || !StringUtility.stringHasValue(args[0])) {
            throw new RuntimeException("This class requres one argument which is the location of the output directory");
        }

        String outputDirectory = args[0];

        GenerateTestSourceFiles app = new GenerateTestSourceFiles();

        try {
            app.run(Path.of(outputDirectory).toFile());
        } catch (Exception e) {
            throw new RuntimeException("Exception creating test classes", e);
        }
    }

    private void gatherGenerators(List<CompilationUnitGenerator> generators) throws ReflectiveOperationException {
        Reflections reflections = new Reflections("mbg.domtest.generators");
        Set<Class<? extends CompilationUnitGenerator>> classes = reflections.getSubTypesOf(CompilationUnitGenerator.class);

        for (Class<? extends CompilationUnitGenerator> clazz : classes) {
            if (clazz.getAnnotation(IgnoreDomTest.class) == null) {
                generators.add(clazz.getDeclaredConstructor().newInstance());
            } else {
                System.out.println("Generator " + clazz.getName() + " ignored");
            }
        }
    }

    private void run(File outputDirectory) throws IOException, ReflectiveOperationException {
        setupOutputDirectry(outputDirectory);

        List<CompilationUnitGenerator> generators = new ArrayList<>();
        gatherGenerators(generators);

        List<CompilationUnit> cus = new ArrayList<>();

        for (CompilationUnitGenerator generator : generators) {
            cus.addAll(generator.generate());
        }

        for (CompilationUnit cu : cus) {
            writeCompilationUnit(outputDirectory, cu);
        }
    }

    private void setupOutputDirectry(File outputDirectory) throws IOException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        if (!outputDirectory.isDirectory()) {
            throw new IOException("can't create output directory");
        }
    }

    private void writeCompilationUnit(File rootDirectory, CompilationUnit cu) throws IOException {
        String _package = cu.getType().getPackageName();

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(_package, ".");
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        Path directory = rootDirectory.toPath().resolve(sb.toString());

        if (!Files.isDirectory(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new IOException("can't create package directory");
            }
        }

        String fileName = cu.getType().getShortName() + ".java";
        Path targetFile = directory.resolve(fileName);

        DefaultJavaFormatter formatter = new DefaultJavaFormatter();
        writeFile(targetFile.toFile(), formatter.getFormattedContent(cu));
    }

    private void writeFile(File file, String content) throws IOException {
        OutputStream fos = Files.newOutputStream(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        OutputStreamWriter osw = new OutputStreamWriter(fos);

        try (BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write(content);
        }
    }
}
