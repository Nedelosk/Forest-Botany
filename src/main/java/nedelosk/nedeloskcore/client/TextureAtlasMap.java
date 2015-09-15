package nedelosk.nedeloskcore.client;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Level;

import nedelosk.nedeloskcore.common.core.Log;

import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class TextureAtlasMap extends TextureAtlasSprite {

    private final int index;
    private final int rows;
    private final int columns;

    public static IIcon[] unstitchIcons(IIconRegister iconRegister, String name, int numIcons) {
        return unstitchIcons(iconRegister, name, numIcons, 1);
    }

    public static IIcon[] unstitchIcons(IIconRegister iconRegister, String name, int columns, int rows) {
        TextureMap textureMap = (TextureMap) iconRegister;
        int numIcons = rows * columns;
        IIcon[] icons = new IIcon[numIcons];
        for (int i = 0; i < numIcons; i++) {
            String texName = name + "." + i;
            TextureAtlasMap texture = new TextureAtlasMap(texName, i, rows, columns);
            textureMap.setTextureEntry(texName, texture);
            icons[i] = texture;
        }
        return icons;
    }

    private TextureAtlasMap(String name, int index, int rows, int columns) {
        super(name);
        this.index = index;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location) {
        location = new ResourceLocation(location.getResourceDomain(), location.getResourcePath().replace("." + index, ""));
        int split = location.getResourcePath().indexOf(':');
        if (split != -1)
            location = new ResourceLocation(location.getResourceDomain(), location.getResourcePath().substring(0, split));
        location = new ResourceLocation(location.getResourceDomain(), "textures/blocks/" + location.getResourcePath() + ".png");

        BufferedImage image;
        IResource resource = null;
        try {
            resource = manager.getResource(location);
            image = ImageIO.read(resource.getInputStream());
        } catch (IOException ex) {
            Log.log(Level.WARN, "Failed to load sub-texture from {0}: {1}", location.getResourcePath(), ex.getLocalizedMessage());
            return true;
        } finally {
            if (resource != null)
                try {
                    resource.getInputStream().close();
                } catch (IOException e) {
                }
        }

        Field mipmapLevel;
        Field anisotropicFiltering;

        try {
            mipmapLevel = TextureMap.class.getDeclaredField("field_147636_j");
            anisotropicFiltering = TextureMap.class.getDeclaredField("field_147637_k");
        } catch (NoSuchFieldException e) {
            try {
                mipmapLevel = TextureMap.class.getDeclaredField("mipmapLevels");
                anisotropicFiltering = TextureMap.class.getDeclaredField("anisotropicFiltering");
            } catch (NoSuchFieldException f) {
                throw new RuntimeException(f);
            }
        }

        mipmapLevel.setAccessible(true);
        anisotropicFiltering.setAccessible(true);
        int mipmapLevels;

        try {
            mipmapLevels = mipmapLevel.getInt(Minecraft.getMinecraft().getTextureMapBlocks());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int size = image.getHeight() / rows;
        int x = index % columns;
        int y = index / columns;

        BufferedImage subImage;
        try {
            subImage = image.getSubimage(x * size, y * size, size, size);
        } catch (RasterFormatException ex) {
        	Log.log(Level.WARN, "Failed to load sub-texture from {0} - {1}x{2}: {3}", location.getResourcePath(), image.getWidth(), image.getHeight(), ex.getLocalizedMessage());
            return true;
        }
        this.height = subImage.getHeight();
        this.width = subImage.getWidth();
        int[] rgbaData = new int[this.height * this.width];
        subImage.getRGB(0, 0, this.width, this.height, rgbaData, 0, this.width);
        int[][] imageData = new int[1 + mipmapLevels][];
        imageData[0] = rgbaData;
        framesTextureData.add(imageData);
        return false;
    }

}
