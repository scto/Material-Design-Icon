package com.s1243808733.materialicon.common.util;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.github.megatronking.svg.generator.svg.Svg2Vector;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SvgUtils {

    public static String toVector(String data, int width, int height) throws IOException {
        ByteArrayInputStream in =new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String retMsg = Svg2Vector.parseSvgToXml(in, out, width, height);
        final String xml;
        if (retMsg == null) {
            xml = ConvertUtils.outputStream2String(out, "utf-8");
        } else {
            CloseUtils.closeIOQuietly(in, out);
            throw new IOException(retMsg);
        }
        return xml;
    }

}
