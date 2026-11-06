package edu.course.lab05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SampleFileLoaderTest {

    private static final String HEADER = "id;label;source;featureCount;status";

    @TempDir
    Path tempDir;

    private final SampleFileLoader loader = new SampleFileLoader();

    private Path writeFile(String... lines) throws IOException {
        Path file = tempDir.resolve("samples.txt");
        Files.write(file, List.of(lines), StandardCharsets.UTF_8);
        return file;
    }

    @Test
    void loadsValidFile() throws IOException {
        Path file = writeFile(HEADER,
                "S-101;cat;camera-1;128;READY",
                "S-102;dog;camera-2;64;NEW");

        LoadResult result = loader.load(file);

        assertEquals(2, result.getSamples().size());
        assertEquals(0, result.getErrors().size());
        DataSample first = result.getSamples().get(0);
        assertEquals("S-101", first.getId());
        assertEquals("cat", first.getLabel());
        assertEquals("camera-1", first.getSource());
        assertEquals(128, first.getFeatureCount());
        assertEquals(SampleStatus.READY, first.getStatus());
    }

    @Test
    void loadsCyrillicText() throws IOException {
        Path file = writeFile(HEADER, "S-1;кошка;камера-1;16;NEW");

        LoadResult result = loader.load(file);

        assertEquals(1, result.getSamples().size());
        assertEquals("кошка", result.getSamples().get(0).getLabel());
        assertEquals("камера-1", result.getSamples().get(0).getSource());
    }

    @Test
    void wrongHeaderIsReported() throws IOException {
        Path file = writeFile("id;name;source;featureCount;status", "S-1;cat;camera-1;1;NEW");

        LoadResult result = loader.load(file);

        assertEquals(0, result.getSamples().size());
        assertEquals(1, result.getErrors().size());
        assertEquals(1, result.getErrors().get(0).getLineNumber());
        assertEquals("header", result.getErrors().get(0).getField());
    }

    @Test
    void emptyFileReportsMissingHeader() throws IOException {
        Path file = writeFile();

        LoadResult result = loader.load(file);

        assertEquals(0, result.getSamples().size());
        assertEquals("header", result.getErrors().get(0).getField());
    }

    @Test
    void blankLinesAreSkipped() throws IOException {
        Path file = writeFile(HEADER, "", "S-1;cat;camera-1;1;NEW", "   ");

        LoadResult result = loader.load(file);

        assertEquals(1, result.getSamples().size());
        assertEquals(0, result.getErrors().size());
    }

    @Test
    void byteOrderMarkInHeaderIsIgnored() throws IOException {
        Path file = writeFile("\uFEFF" + HEADER, "S-1;cat;camera-1;1;NEW");

        LoadResult result = loader.load(file);

        assertEquals(1, result.getSamples().size());
        assertEquals(0, result.getErrors().size());
    }

    @Test
    void spacesAroundFieldsAreTrimmed() throws IOException {
        Path file = writeFile(HEADER, " S-1 ; cat ; camera-1 ; 5 ; READY ");

        LoadResult result = loader.load(file);

        assertEquals(1, result.getSamples().size());
        assertEquals("S-1", result.getSamples().get(0).getId());
        assertEquals("cat", result.getSamples().get(0).getLabel());
        assertEquals(SampleStatus.READY, result.getSamples().get(0).getStatus());
    }

    @Test
    void emptyFieldIsReported() throws IOException {
        Path file = writeFile(HEADER, "S-1;;camera-1;128;READY");

        LoadResult result = loader.load(file);

        assertEquals(0, result.getSamples().size());
        assertEquals(1, result.getErrors().size());
        assertEquals(2, result.getErrors().get(0).getLineNumber());
        assertEquals("label", result.getErrors().get(0).getField());
    }

    @Test
    void invalidNumberIsReported() throws IOException {
        Path file = writeFile(HEADER, "S-1;cat;camera-1;abc;READY");

        LoadResult result = loader.load(file);

        assertEquals(0, result.getSamples().size());
        assertEquals("featureCount", result.getErrors().get(0).getField());
    }

    @Test
    void nonPositiveNumberIsReported() throws IOException {
        Path file = writeFile(HEADER, "S-1;cat;camera-1;0;READY");

        LoadResult result = loader.load(file);

        assertEquals(0, result.getSamples().size());
        assertEquals("featureCount", result.getErrors().get(0).getField());
    }

    @Test
    void unknownStatusIsReported() throws IOException {
        Path file = writeFile(HEADER, "S-1;cat;camera-1;128;DONE");

        LoadResult result = loader.load(file);

        assertEquals(0, result.getSamples().size());
        assertEquals("status", result.getErrors().get(0).getField());
    }

    @Test
    void tooFewColumnsAreReported() throws IOException {
        Path file = writeFile(HEADER, "S-1;cat;camera-1;128");

        LoadResult result = loader.load(file);

        assertEquals(0, result.getSamples().size());
        assertEquals("columns", result.getErrors().get(0).getField());
    }

    @Test
    void tooManyColumnsAreReported() throws IOException {
        Path file = writeFile(HEADER, "S-1;cat;camera-1;128;READY;extra");

        LoadResult result = loader.load(file);

        assertEquals(0, result.getSamples().size());
        assertEquals("columns", result.getErrors().get(0).getField());
    }

    @Test
    void validAndInvalidRowsAreProcessedTogether() throws IOException {
        Path file = writeFile(HEADER,
                "S-1;cat;camera-1;10;READY",
                "S-2;dog;camera-2;abc;NEW",
                "S-3;bird;camera-3;30;LABELED",
                "S-4;fish");

        LoadResult result = loader.load(file);

        assertEquals(2, result.getSamples().size());
        assertEquals("S-1", result.getSamples().get(0).getId());
        assertEquals("S-3", result.getSamples().get(1).getId());
        assertEquals(2, result.getErrors().size());
        assertEquals(3, result.getErrors().get(0).getLineNumber());
        assertEquals(5, result.getErrors().get(1).getLineNumber());
    }

    @Test
    void missingFileThrowsExceptionWithCause() {
        Path missing = tempDir.resolve("no-such-file.txt");

        DataLoadingException exception =
                assertThrows(DataLoadingException.class, () -> loader.load(missing));

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    void invalidUtf8ThrowsExceptionWithCause() throws IOException {
        Path file = tempDir.resolve("broken.txt");
        Files.write(file, new byte[]{(byte) 0xC3, (byte) 0x28});

        DataLoadingException exception =
                assertThrows(DataLoadingException.class, () -> loader.load(file));

        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    void nullPathIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> loader.load(null));
    }
}