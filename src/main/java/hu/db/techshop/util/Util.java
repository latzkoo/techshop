package hu.db.techshop.util;

import hu.db.techshop.model.Cart;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Util {

    public static Map<String, String> cartToMap(Cart cart) {
        Map<String, String> mappedCart = new HashMap<>();

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("hu", "HU"));
        numberFormat.setGroupingUsed(true);

        mappedCart.put("qty", Integer.toString(cart.count()));
        mappedCart.put("value", numberFormat.format(cart.value()));

        return mappedCart;
    }

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ioe) {
            throw new IOException("A fájl nem menthető: " + fileName, ioe);
        }
    }

    public static void createThumbnail(String inputImagePath, String outputImagePath, int size) throws IOException {
        int scaledWidth, scaledHeight;
        double ratio;

        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        if (inputImage.getHeight() > inputImage.getWidth()) {
            ratio = (double) inputImage.getHeight() / inputImage.getWidth();
            scaledHeight = (int) (size * ratio);
            scaledWidth = size;
        }
        else {
            ratio = (double) inputImage.getWidth() / inputImage.getHeight();
            scaledWidth = (int) (size * ratio);
            scaledHeight = size;
        }

        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);

        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    public static boolean deleteFile(String imagePath) {
        File file = new File(imagePath);
        return file.delete();
    }

}
