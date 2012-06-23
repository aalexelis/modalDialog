package bootstrap.liftweb

import net.liftweb._
import common.Full
import http._
import http.NotFoundAsTemplate
import http.ParsePath
import sitemap.{**, SiteMap, Menu, Loc}
import util.{ NamedPF }
import net.liftweb._
import mapper.{Schemifier, DB, StandardDBVendor, DefaultConnectionIdentifier}
import util.{Props}
import common.{Full}
import code.model._
import _root_.net.liftweb.sitemap.Loc._
//import code.snippet.PageId
import code.view.PageView


class Boot {
  def boot {
  
        if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = 
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
        			               Props.get("db.url") openOr 
        			               "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
        			               Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User)
  
    // where to search snippet
    LiftRules.addToPackages("code")

    // build sitemap
    val entries = (List(Menu("Home") / "index") :::
                                    // the User management menu items
                  User.sitemap :::
                  List(Menu(Loc("Static", Link(List("static"), true, "/static/index"), "Static Content"))) :::
                  List(Menu("Page") / "page" >> Hidden) :::
                  List(Menu("PageView") / "pageview" / ** >> Hidden) :::
                  /*List(Menu.param[PageId]("Page", "Page",
                    s => Full(PageId(s)),
                    pi => pi.id) / "page" >> Hidden) ::: */
                  Nil)
    
    LiftRules.uriNotFound.prepend(NamedPF("404handler"){
      case (req,failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions","404"),"html",false,false))
    })
    
    LiftRules.setSiteMap(SiteMap(entries:_*))
    
    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
    
        // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.statelessRewrite.append {
      case RewriteRequest(ParsePath("page" :: key :: Nil, "", true, _), _, _) =>
        RewriteResponse("page" :: Nil, Map("id" -> key))
    }

    LiftRules.viewDispatch.append {
      case List("pageview",id) => Left(() => Full(PageView.pageview(id)))
    }

  }
}