package com.mars.fw.common.utils;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成类
 *
 * @Author King
 * @create 2020/4/22 16：00
 */
public class QrCodeUtil {

    private static int WIDTH = 200;
    private static int HEIGHT = 200;

    /**
     * 二维码生成到指定目录
     *
     * @param width
     * @param height
     * @param format
     * @param content
     * @param path
     * @throws WriterException
     * @throws IOException
     */
    public static void createQrCodeToPath(Integer width, Integer height, String format, String content, Path path) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix m = writer.encode(content, BarcodeFormat.QR_CODE, height, width);
        MatrixToImageWriter.writeToPath(m, format, path);
    }

    /**
     * 二维码以流的方式返回给C端
     *
     * @param width
     * @param height
     * @param format
     * @param content
     * @param response
     * @throws WriterException
     * @throws IOException
     */
    public static void createQrCodeToPath(Integer width, Integer height, String format, String content, HttpServletResponse response) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix m = writer.encode(content, BarcodeFormat.QR_CODE, height, width);
        ServletOutputStream stream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(m, "png", stream);
    }


    /**
     * 解析二维码
     *
     * @param url
     * @throws WriterException
     * @throws IOException
     */
    public static String dealQrCode(String url) throws IOException, NotFoundException {
        BufferedImage image;
        image = ImageIO.read(downloadNet(url));
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码ßß
        return result.getText();

    }

    /**
     * 下载远程文件 返回文件流InputStream
     * 注意：使用完InputStream 记得关闭流
     *
     * @param urlPath 远程文件下载路径
     * @return
     * @throws IOException
     */
    public static InputStream downloadNet(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        return inputStream;
    }


}
