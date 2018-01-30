package org.intellij.plugins.export.keymap.generator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import org.jetbrains.annotations.NotNull;

/**
 * @author Denis Zhdanov
 * @since 6/13/12 5:46 PM
 */
public class GenerationConstants {

  public static final BaseColor COLOR_BACKGROUND_AND_LINES = new BaseColor(230, 230, 230);

  public static final float           GAP_BETWEEN_COLUMNS    = 20f;
  public static final float           PADDING_HEADER_BOTTOM  = 5f;
  public static final float           HEADER_FONT_SIZE       = 14f;
  public static final float           ACTION_GROUP_FONT_SIZE = 8f;
  public static final float           DATA_FONT_SIZE         = 7f;
  public static final int             COLUMNS_NUMBER         = 3;

  private static final String NORMAL_FONT_NAME = "font/DejaVuLGCSans.ttf";
  private static final String SHORTCUT_FONT_NAME   = "font/DejaVuLGCSans-Bold.ttf";

  public static final Font HEADER_FONT             = new Font(getFont(NORMAL_FONT_NAME), HEADER_FONT_SIZE);
  public static final Font ACTION_GROUP_FONT       = new Font(getFont(NORMAL_FONT_NAME), ACTION_GROUP_FONT_SIZE);
  public static final Font ACTION_SHORTCUT_FONT    = new Font(getFont(SHORTCUT_FONT_NAME), DATA_FONT_SIZE);
  public static final Font ACTION_DESCRIPTION_FONT = new Font(getFont(NORMAL_FONT_NAME), DATA_FONT_SIZE);


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
