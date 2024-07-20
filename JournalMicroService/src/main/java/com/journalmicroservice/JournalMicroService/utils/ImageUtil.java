package com.journalmicroservice.JournalMicroService.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtil {

    public static byte[] compressImage(byte[] input) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(input);
        deflater.finish();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(input.length);
        byte[] temp = new byte[4 * 1024]; //4kb array

        while (!deflater.finished()) {
            int compressedBytes = deflater.deflate(temp); //the compressed bytes are being written in temp
            byteArrayOutputStream.write(temp, 0, compressedBytes);
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] unCompressImage(byte[] input) throws DataFormatException {
        Inflater inflater = new Inflater();

        inflater.setInput(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];

        while (!inflater.finished()) {
            int decompressedBytes = inflater.inflate(temp);
            outputStream.write(temp, 0, decompressedBytes);
        }

        return outputStream.toByteArray();
    }

    public static File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}
