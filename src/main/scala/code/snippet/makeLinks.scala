package code.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.{Templates, SHtml}
import net.liftweb.http.js.jquery.JqJsCmds.ModalDialog
import net.liftweb.http.js.JsCmds._
import xml.NodeSeq

/**
 * Created with IntelliJ IDEA.
 * User: andreas
 * Date: 6/22/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */

object MakeLinks {

  def render =
    "#md1 [onclick]" #> SHtml.ajaxInvoke( () => {
      ( for {
        template <- Templates("page" :: Nil)
      } yield ModalDialog(template)) openOr Noop
    }) &
    "#md2 [onclick]" #> SHtml.ajaxInvoke( () => {
      ( for {
        template <- Templates("page" :: "1" :: Nil)
      } yield ModalDialog(template)) openOr Noop
    }) &
    "#md3 [onclick]" #> SHtml.ajaxInvoke( () => {
      ( for {
        template <- Templates("pageview" :: "1" :: Nil)
      } yield ModalDialog(template)) openOr Noop
    })
}
