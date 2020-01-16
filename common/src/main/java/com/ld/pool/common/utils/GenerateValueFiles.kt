package com.ld.pool.common.utils

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintWriter


class GenerateValueFiles(private val baseW: Int, private val baseH: Int, supportStr: String) {
    //此处为文件生成路径，改为项目中./res文件下
    private val dirStr = "./res"
    private var supportStr = SUPPORT_DIMESION
    /**
     * @param supportStr
     * w,h_...w,h;
     * @return
     */
    private fun validateInput(supportStr: String): String {
        val sb = StringBuffer()
        val vals = supportStr.split("_").toTypedArray()
        var w = -1
        var h = -1
        var wh: Array<String>
        for (`val` in vals) {
            try {
                if (`val` == null || `val`.trim { it <= ' ' }.length == 0) continue
                wh = `val`.split(",").toTypedArray()
                w = wh[0].toInt()
                h = wh[1].toInt()
            } catch (e: Exception) {
                println("skip invalidate params : w,h = $`val`")
                continue
            }
            sb.append("$w,$h;")
        }
        return sb.toString()
    }

    fun generate() {
        val vals = supportStr.split(";").toTypedArray()
        for (`val` in vals) {
            val wh = `val`.split(",").toTypedArray()
            generateXmlFile(wh[0].toInt(), wh[1].toInt())
        }
    }

    private fun generateXmlFile(w: Int, h: Int) {
        val sbForWidth = StringBuffer()
        sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        sbForWidth.append("<resources>")
        val cellw = w * 1.0f / baseW
        println("width : $w,$baseW,$cellw")
        for (i in 1 until baseW) {
            sbForWidth.append(
                WTemplate.replace(
                    "{0}",
                    i.toString() + ""
                ).replace("{1}", change(cellw * i).toString() + "")
            )
        }
        sbForWidth.append(
            WTemplate.replace(
                "{0}",
                baseW.toString() + ""
            ).replace("{1}", w.toString() + "")
        )
        sbForWidth.append("</resources>")
        val sbForHeight = StringBuffer()
        sbForHeight.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        sbForHeight.append("<resources>")
        val cellh = h * 1.0f / baseH
        println("height : $h,$baseH,$cellh")
        for (i in 1 until baseH) {
            sbForHeight.append(
                HTemplate.replace(
                    "{0}",
                    i.toString() + ""
                ).replace("{1}", change(cellh * i).toString() + "")
            )
        }
        sbForHeight.append(
            HTemplate.replace(
                "{0}",
                baseH.toString() + ""
            ).replace("{1}", h.toString() + "")
        )
        sbForHeight.append("</resources>")
        val fileDir = File(
            dirStr + File.separator
                .toString() + VALUE_TEMPLATE.replace(
                "{0}",
                h.toString() + ""
            ) //
                .replace("{1}", w.toString() + "")
        )
        fileDir.mkdir()
        val layxFile = File(fileDir.getAbsolutePath(), "lay_x.xml")
        val layyFile = File(fileDir.getAbsolutePath(), "lay_y.xml")
        try {
            var pw = PrintWriter(FileOutputStream(layxFile))
            pw.print(sbForWidth.toString())
            pw.close()
            pw = PrintWriter(FileOutputStream(layyFile))
            pw.print(sbForHeight.toString())
            pw.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val WTemplate = "<dimen name=\"x{0}\">{1}px</dimen>\n"
        private const val HTemplate = "<dimen name=\"y{0}\">{1}px</dimen>\n"
        /**
         * {0}-HEIGHT
         */
        private const val VALUE_TEMPLATE = "values-{0}x{1}"
        private const val SUPPORT_DIMESION =
            "320,480;480,800;480,854;540,960;600,1024;720,1184;720,1196;720,1280;768,1024;768,1280;800,1280;1080,1812;1080,1920;1440,2560;"

        fun change(a: Float): Float {
            val temp = (a * 100).toInt()
            return temp / 100f
        }

        //根据型号像素自动生成对应文件如下列像素为：320×480像素
        @JvmStatic
        fun main(args: Array<String>) {
            var baseW = 320
            var baseH = 480
            var addition = ""
            try {
                if (args.size >= 3) {
                    baseW = args[0].toInt()
                    baseH = args[1].toInt()
                    addition = args[2]
                } else if (args.size >= 2) {
                    baseW = args[0].toInt()
                    baseH = args[1].toInt()
                } else if (args.size >= 1) {
                    addition = args[0]
                }
            } catch (e: NumberFormatException) {
                System.err
                    .println("right input params : java -jar xxx.jar width height w,h_w,h_..._w,h;")
                e.printStackTrace()
                System.exit(-1)
            }
            GenerateValueFiles(baseW, baseH, addition).generate()
        }
    }

    init {
        if (!this.supportStr.contains("$baseW,$baseH")) {
            this.supportStr += "$baseW,$baseH;"
        }
        this.supportStr += validateInput(supportStr)
        println(supportStr)
        val dir = File(dirStr)
        if (!dir.exists()) {
            dir.mkdir()
        }
        System.out.println(dir.getAbsoluteFile())
    }
}