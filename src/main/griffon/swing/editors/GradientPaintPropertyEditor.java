/*
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.swing.editors;

import griffon.core.resources.editors.AbstractPropertyEditor;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * @author Andres Almiray
 * @author Alexander Klein
 * @since 1.1.0
 */
public class GradientPaintPropertyEditor extends AbstractPropertyEditor {
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }

    public void setValue(Object value) {
        if (null == value) return;
        if (value instanceof CharSequence) {
            handleAsString(String.valueOf(value));
        } else if (value instanceof List) {
            handleAsList((List) value);
        } else if (value instanceof Map) {
            handleAsMap((Map) value);
        } else if (value instanceof GradientPaint) {
            super.setValue(value);
        } else {
            throw illegalValue(value, GradientPaint.class);
        }
    }

    private void handleAsString(String str) {
        float x1 = 0;
        float y1 = 0;
        float x2 = 0;
        float y2 = 0;
        Color c1 = Color.WHITE;
        Color c2 = Color.BLACK;
        boolean cyclic = false;
        String[] parts = str.split(",");
        switch (parts.length) {
            case 7:
                cyclic = parseBoolean(parts[6]);
            case 6:
                ColorPropertyEditor colorEditor = new ColorPropertyEditor();
                x1 = parseValue(parts[0]);
                y1 = parseValue(parts[1]);
                x2 = parseValue(parts[3]);
                y2 = parseValue(parts[4]);
                try {
                    colorEditor.setAsText(parts[2]);
                    c1 = (Color) colorEditor.getValue();
                } catch (Exception e) {
                    throw illegalValue(parts[2], GradientPaint.class);
                }
                try {
                    colorEditor.setAsText(parts[5]);
                    c2 = (Color) colorEditor.getValue();
                } catch (Exception e) {
                    throw illegalValue(parts[5], GradientPaint.class);
                }
                super.setValue(new GradientPaint(x1, y1, c1, x2, y2, c2, cyclic));
                break;
            default:
                throw illegalValue(str, Insets.class);
        }
    }

    private void handleAsList(List list) {
        float x1 = 0;
        float y1 = 0;
        float x2 = 0;
        float y2 = 0;
        Color c1 = Color.WHITE;
        Color c2 = Color.BLACK;
        boolean cyclic = false;
        switch (list.size()) {
            case 7:
                cyclic = parseBoolean(String.valueOf(list.get(6)));
            case 6:
                ColorPropertyEditor colorEditor = new ColorPropertyEditor();
                x1 = parseValue(list.get(0));
                y1 = parseValue(list.get(1));
                x2 = parseValue(list.get(3));
                y2 = parseValue(list.get(4));
                try {
                    colorEditor.setValue(list.get(2));
                    c1 = (Color) colorEditor.getValue();
                } catch (Exception e) {
                    throw illegalValue(list.get(2), GradientPaint.class);
                }
                try {
                    colorEditor.setValue(list.get(5));
                    c2 = (Color) colorEditor.getValue();
                } catch (Exception e) {
                    throw illegalValue(list.get(5), GradientPaint.class);
                }
                super.setValue(new GradientPaint(x1, y1, c1, x2, y2, c2, cyclic));
                break;
            default:
                throw illegalValue(list, Insets.class);
        }
    }

    private void handleAsMap(Map map) {
        float x1 = (Float) getMapValue(map, "x1", 0);
        float y1 = (Float) getMapValue(map, "y1", 0);
        float x2 = (Float) getMapValue(map, "x2", 0);
        float y2 = (Float) getMapValue(map, "y2", 0);
        Color c1 = Color.WHITE;
        Color c2 = Color.BLACK;
        boolean cyclic = false;

        ColorPropertyEditor colorEditor = new ColorPropertyEditor();
        Object colorValue = map.get("c1");
        try {
            if (null != colorValue) {
                colorEditor.setValue(colorValue);
                c1 = (Color) colorEditor.getValue();
            } else {
                c1 = Color.WHITE;
            }
        } catch (Exception e) {
            throw illegalValue(colorValue, GradientPaint.class);
        }
        colorValue = map.get("c1");
        try {
            if (null != colorValue) {
                colorEditor.setValue(colorValue);
                c2 = (Color) colorEditor.getValue();
            } else {
                c2 = Color.BLACK;
            }
        } catch (Exception e) {
            throw illegalValue(colorValue, GradientPaint.class);
        }
        Object cyclicValue = map.get("cyclic");
        if (null != cyclicValue) {
            cyclic = parseBoolean(String.valueOf(cyclicValue));
        }

        super.setValue(new GradientPaint(x1, y1, c1, x2, y2, c2, cyclic));
    }

    private float parse(String val) {
        try {
            return Float.parseFloat(String.valueOf(val));
        } catch (NumberFormatException e) {
            throw illegalValue(val, GradientPaint.class, e);
        }
    }

    private boolean parseBoolean(String val) {
        try {
            return Boolean.parseBoolean(val);
        } catch (Exception e) {
            throw illegalValue(val, GradientPaint.class, e);
        }
    }

    private float parseValue(Object value) {
        if (value instanceof CharSequence) {
            return parse(String.valueOf(value));
        } else if (value instanceof Number) {
            return parse((Number) value);
        }
        throw illegalValue(value, GradientPaint.class);
    }

    private int parse(Number val) {
        return val.intValue() & 0xFF;
    }

    private Object getMapValue(Map map, String key, Object defaultValue) {
        Object val = map.get(key);
        if (null == val) {
            return defaultValue;
        } else if (val instanceof CharSequence) {
            return parse(String.valueOf(val));
        } else if (val instanceof Number) {
            return parse((Number) val);
        }
        throw illegalValue(map, GradientPaint.class);
    }
}