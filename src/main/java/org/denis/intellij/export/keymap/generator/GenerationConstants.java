package org.denis.intellij.export.keymap.generator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

/**
 * @author Denis Zhdanov
 * @since 6/13/12 5:46 PM
 */
public class GenerationConstants {

  public static final BaseColor       TITLE_COLOR            = new BaseColor(35, 83, 142);
  public static final BaseColor       COLOR_BACKGROUND_KEY   = new BaseColor(226, 230, 242);
  public static final BaseColor       COLOR_BORDER_HEADER    = new BaseColor(32, 81, 141);
  public static final BaseColor       COLOR_BORDER_DATA      = new BaseColor(137, 137, 144);
  public static final Font.FontFamily FONT_FAMILY            = Font.FontFamily.TIMES_ROMAN;
  public static final float           PADDING_HEADER_BOTTOM  = 10f;
  public static final float           HEADER_FONT_SIZE       = 8f;
  public static final float           ACTION_GROUP_FONT_SIZE = 6f;
  public static final float           DATA_FONT_SIZE         = 5f;
  public static final int             COLUMNS_NUMBER         = 3;

  private GenerationConstants() {
  }
}
