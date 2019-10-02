package gl.util;

import gl.model.entity.ImageEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;


@Service
public class CreateImageThumbnail {

    private BufferedImage resizeImage(BufferedImage originalImage, Dimension dimension, int type) {
        BufferedImage resizedImage = new BufferedImage((int) dimension.getHeight(), (int) dimension.getWidth(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, (int) dimension.getHeight(), (int) dimension.getWidth(), null);
        g.dispose();

        return resizedImage;
    }

    private File createFile(ImageEntity image) throws IllegalStateException, IOException {
        UUID uuid = UUID.randomUUID();
        File file = new File(  "./" + uuid +".jpg");
        System.out.println(file.getAbsolutePath());
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(image.getFile());
        fos.close();
        return file;
    }

    private  Dimension _getScaledDimension(Dimension imageSize, Dimension boundary) {

        double widthRatio = boundary.getWidth() / imageSize.getWidth();
        double heightRatio = boundary.getHeight() / imageSize.getHeight();

        double ratio = Math.max(widthRatio, heightRatio);

         return new Dimension((int) (imageSize.width * ratio), (int) (imageSize.height * ratio));
    }

    public byte[] createThumbnail(ImageEntity image) throws IOException {
        File file = createFile(image);
        BufferedImage originalImage = ImageIO.read(file);

        Dimension imgSize = new Dimension(originalImage.getHeight(), originalImage.getWidth());
        Dimension boundary = new Dimension(300, 300);
        Dimension scaledDimension = _getScaledDimension(imgSize, boundary);

        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = resizeImage(originalImage, scaledDimension, type);

        ImageIO.write(resizedImage, "jpg", file);
        byte[] fileContent = Files.readAllBytes(file.toPath());

        file.delete();
        return fileContent;
    }

}
