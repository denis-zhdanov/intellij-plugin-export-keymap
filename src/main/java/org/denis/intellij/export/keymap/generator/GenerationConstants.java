package org.denis.intellij.export.keymap.generator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import org.jetbrains.annotations.NotNull;

/**
 * @author Denis Zhdanov
 * @since 6/13/12 5:46 PM
 */
public class GenerationConstants {

  public static final BaseColor TITLE_COLOR          = new BaseColor(35, 83, 142);
  public static final BaseColor COLOR_BACKGROUND_KEY = new BaseColor(226, 230, 242);
  public static final BaseColor COLOR_BORDER_HEADER  = new BaseColor(32, 81, 141);
  public static final BaseColor COLOR_BORDER_DATA    = new BaseColor(137, 137, 144);

  public static final float GAP_BETWEEN_COLUMNS    = 20f;
  public static final float           PADDING_HEADER_BOTTOM  = 10f;
  public static final float           HEADER_FONT_SIZE       = 9;
  public static final float           ACTION_GROUP_FONT_SIZE = 8f;
  public static final float           DATA_FONT_SIZE         = 5.5f;
  public static final float           FOOTER_FONT_SIZE       = 6f;
  public static final int             COLUMNS_NUMBER         = 3;
  
  private static final String BOLD_FONT_NAME   = "font/Lucida Grande Bold.ttf";
  private static final String NORMAL_FONT_NAME = "font/Lucida Grande.ttf";
  public static final Font HEADER_FONT             = new Font(getFont(BOLD_FONT_NAME), HEADER_FONT_SIZE, Font.NORMAL, TITLE_COLOR);
  public static final Font ACTION_GROUP_FONT       = new Font(getFont(BOLD_FONT_NAME), ACTION_GROUP_FONT_SIZE, Font.NORMAL, TITLE_COLOR);
  public static final Font ACTION_SHORTCUT_FONT    = new Font(getFont(BOLD_FONT_NAME), DATA_FONT_SIZE);
  public static final Font ACTION_DESCRIPTION_FONT = new Font(getFont(NORMAL_FONT_NAME), DATA_FONT_SIZE);
  public static final Font FOOTER_FONT             = new Font(getFont(BOLD_FONT_NAME), FOOTER_FONT_SIZE);
  
  public static final String JETBRAINS_LOGO_PATH      = "image/logo_jetbrains.gif";
  public static final String HOME_IMAGE_PATH          = "image/home.jpg";
  public static final String BLOG_IMAGE_PATH          = "image/rss.jpg";
  public static final String TWITTER_IMAGE_PATH       = "image/twitter.png";
  public static final String GO_TO_ACTION_IMAGE_PATH  = "image/go-to-action.png";
  
  public static final String PRODUCT_URL = "www.jetbrains.com/idea";
  public static final String BLOG_URL    = "blogs.jetbrains.com/idea";
  public static final String TWITTER_ID  = "@intellijidea";
  
  public static final String GO_TO_ACTION_TEXT =
    "To find any action inside the IDE\n" +
    "use Find Action (%s)";

  private GenerationConstants() {
  }

  /**
   * Allows to retrieve target font object by its classpath location.
   * 
   * @param fontPath  classpath font location
   * @return          target font object for the given classpath location
   */
  @NotNull
  private static BaseFont getFont(@NotNull String fontPath) {
    final BaseFont baseFont;
    final ClassLoader classLoaderToRestore = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(GenerationConstants.class.getClassLoader());
      return BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    finally {
      Thread.currentThread().setContextClassLoader(classLoaderToRestore);
    }
  }
}
