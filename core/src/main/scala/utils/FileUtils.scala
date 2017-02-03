package infrastructure

import java.io._

import scala.reflect.io
import scala.reflect.io.Directory

/**
  * Utility object for File access.
  */
object FileUtils {

  type FileName = String
  type Dir = String

  def withFile[A](fileName: FileName)(func: PrintWriter => A): Unit = {
    val file = new File(fileName)
    val write = new PrintWriter(file)
    try {
      func(write)
    } finally {
      write.close()
    }
  }

  def fromFile(filePath: FileName, encoding: String = "iso-8859-1"): Iterator[String] = scala.io.Source.fromFile(filePath, encoding).getLines

  def readFile(filePath: FileName, encoding: String = "iso-8859-1"): String = fromFile(filePath, encoding).mkString("\n")

  def filesInDir(dir: Dir, fileNameFilter: (FileName => Boolean) = (x => true)): Array[io.File] = {
    Directory(dir).files.toArray.filter(file => fileNameFilter(file.name)).sortBy(x => x.name)
  }

  def withEachFile[A](dir: Dir, fileNameFilter: (FileName => Boolean) = (x => true))(func: (FileName, Iterator[String]) => A): List[A] = {
    filesInDir(dir, fileNameFilter).map(x => (x.name, fromFile(x.path))).map((tuple: (String, Iterator[String])) => func(tuple._1, tuple._2)).toList
  }

  // scalastyle:off regex
  def write(fileName: FileName, iterator: Iterator[String]): Unit = {
    withFile(fileName) { output =>
      iterator.foreach(line => output.println(line))
    }
  }
  // scalastyle:on regex

  def copyFile(srcPath: String, destPath: String): Unit = {
    val src = new File(srcPath)
    val dest = new File(destPath)
    new FileOutputStream(dest).getChannel.transferFrom(
      new FileInputStream(src).getChannel, 0, Long.MaxValue)
  }

  def exist(path: String): Boolean = {
    new java.io.File(path).exists
  }


  def delete(fileName: FileName): Boolean = {
    new File(fileName).delete()
  }

  def fileSize(fileName: FileName): Long = {
    new File(fileName).length()
  }

  def appendLine(fileName: FileName, value: String): Unit = {
    val fileWriter = new FileWriter(fileName, true)
    try {
      fileWriter.write(value)
      fileWriter.write("\n")
    } finally {
      fileWriter.close()
    }
  }
}
