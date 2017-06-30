package com.smack.spark
import scala.util.matching.Regex

case class AccessLog(
	ipAddress: String, // IP地址
	clientId: String, // 客户端唯一标识符
	userId: String, // 用户唯一标识符
	serverTime: String, // 服务器时间
	method: String, // 请求类型/方式
	endpoint: String, // 请求的资源
	protocol: String, // 请求的协议名称
	responseCode: Int, // 请求返回值：比如：200、401
	contentSize: Long // 返回的结果数据大小
	)

object AccessLog {
	// Apache日志的正则
	val PARTTERN: Regex = """^(\S+) (\S+) (\S+) \[([\w:/]+\s[+\-]\d{4})\] "(\S+) (\S+) (\S+)" (\d{3}) (\d+)""".r

	/**
	 * 验证一下输入的数据是否符合给定的日志正则，如果符合返回true；否则返回false
	 *
	 * @param line
	 * @return
	 */
	def isValidateLogLine(line: String): Boolean = {
		//    val options = PARTTERN.findFirstMatchIn(line)
		//
		//    if (options.isEmpty) {
		//      false
		//    } else {
		//      true
		//    }
		true
	}

	/**
	 * 解析输入的日志数据
	 *
	 * @param line
	 * @return
	 */
	def parseLogLine(line: String): AccessLog = {
		if (!isValidateLogLine(line)) {
			throw new IllegalArgumentException("参数格式异常")
		}

		// 从line中获取匹配的数据
		val options = PARTTERN.findFirstMatchIn(line)

		// 获取matcher
		val matcher = options.get

		// 构建返回值
		AccessLog(
			matcher.group(1), // 获取匹配字符串中第一个小括号中的值
			matcher.group(2),
			matcher.group(3),
			matcher.group(4),
			matcher.group(5),
			matcher.group(6),
			matcher.group(7),
			matcher.group(8).toInt,
			matcher.group(9).toLong)
	}
}