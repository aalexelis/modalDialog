package code.view

import net.liftweb.http.{Templates, LiftView}
import xml.NodeSeq
import net.liftweb.common
import common.{Loggable, Empty}

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 6/21/12
 * Time: 2:26 AM
 * To change this template use File | Settings | File Templates.
 */

object PageView extends LiftView with Loggable {
  def dispatch = {
    case _ => () => Empty
  }

  def pageview(id: String): NodeSeq = {
    <html xmlns="http://www.w3.org/1999/xhtml" xmlns:lift="http://liftweb.net/">
      <head>
        <title></title>
      </head>
      <body>
            <div id="page_content">The id passed from the URL is {id}</div>
      </body>
    </html>
  }

}
