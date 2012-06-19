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
  public static final float           HEADER_FONT_SIZE       = 10f;
  public static final float           ACTION_GROUP_FONT_SIZE = 8f;
  public static final float           DATA_FONT_SIZE         = 6f;
  public static final float           FOOTER_FONT_SIZE       = 7f;
  public static final int             COLUMNS_NUMBER         = 3;
  
  public static final String JETBRAINS_LOGO_PATH = "logo_jetbrains.gif";
  public static final String HOME_IMAGE_PATH     = "home.jpg";
  public static final String BLOG_IMAGE_PATH     = "rss.jpg";
  public static final String TWITTER_IMAGE_PATH  = "twitter.png";
  
  public static final String PRODUCT_URL = "www.jetbrains.com/idea";
  public static final String BLOG_URL    = "blogs.jetbrains.com/idea";
  public static final String TWITTER_ID  = "@intellijidea";

  private GenerationConstants() {
  }
}
