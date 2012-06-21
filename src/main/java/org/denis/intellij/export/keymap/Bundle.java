package org.denis.intellij.export.keymap;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author Denis Zhdanov
 * @since 6/5/12 4:00 PM
 */
public class Bundle extends AbstractBundle {
  
  public static final String PATH_TO_BUNDLE = "i18n/bundle";
  
  private static final Bundle BUNDLE = new Bundle();
  
  public Bundle() {
    super(PATH_TO_BUNDLE);
  }

  public static String message(@PropertyKey(resourceBundle = PATH_TO_BUNDLE) String key, Object... params) {
    return BUNDLE.getMessage(key, params);
  }
}
